package data.repository

import data.model.ChatHistory
import data.model.Message

interface ChatHistoryRepository {
    suspend fun getHistoryList(): List<ChatHistory>
    suspend fun getHistoryById(id: Long): Pair<ChatHistory?, List<Message>?>
    suspend fun createNewChat(modelName: String): Long
    suspend fun addChatMessage(chatHistoryId: Long, role: String, content: String): Long
}
