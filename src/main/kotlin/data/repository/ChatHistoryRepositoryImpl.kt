package data.repository

import data.DatabaseSingleton.dbQuery
import data.model.ChatHistory
import data.model.ChatHistoryTable
import data.model.ChatHistoryTable.toHistory
import data.model.Message
import data.model.MessageTable
import data.model.MessageTable.toMessage
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class ChatHistoryRepositoryImpl : ChatHistoryRepository {
    override suspend fun getHistoryList(): List<ChatHistory> = dbQuery {
        ChatHistoryTable.selectAll().map(::toHistory)
    }

    override suspend fun getHistoryById(id: Long): Pair<ChatHistory?, List<Message>?> = dbQuery {
        val history = ChatHistoryTable
            .select { ChatHistoryTable.id eq id}
            .map(::toHistory)
            .singleOrNull()

        val messages = MessageTable
            .select { MessageTable.historyId eq id }
            .map(::toMessage)

        Pair(history, messages)
    }

    override suspend fun createNewChat(modelName: String): Long = dbQuery {
        val chatHistory = ChatHistory(modelName)

        val insertStatement = ChatHistoryTable.insert {
            it[this.modelName] = chatHistory.modelName
            it[this.createdAt] = chatHistory.createdAt
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::toHistory)?.id ?: -1
    }

    override suspend fun addChatMessage(chatHistoryId: Long, role: String, content: String): Long = dbQuery {
        val insertStatement = MessageTable.insert {
            it[this.role] = role
            it[this.content] = content
            it[this.historyId] = chatHistoryId
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::toMessage)?.id ?: -1
    }
}
