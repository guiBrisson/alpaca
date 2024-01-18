package data.model

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


data class ChatHistory(
    val id: Long?,
    val modelName: String,
    val createdAt: LocalDateTime,
    val title: String?,
) {
    constructor(modelName: String): this(
        id = null,
        modelName = modelName,
        createdAt = LocalDateTime.now(),
        title = null,
    )
}

object ChatHistoryTable : Table(name = "chat_history") {
    val id = long("id").autoIncrement()
    val modelName = varchar("model_name", 124)
    val createdAt = datetime("created_at")
    val title = varchar("title", 248).nullable()

    override val primaryKey = PrimaryKey(id)

    fun toHistory(row: ResultRow) = ChatHistory(
        id = row[id],
        modelName = row[modelName],
        createdAt = row[createdAt],
        title = row[title],
    )
}
