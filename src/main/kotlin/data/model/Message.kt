package data.model

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table


data class Message(
    val id: Long,
    val role: String, //user||system||assistant
    val content: String,
    val historyId: Long,
)

object MessageTable : Table(name = "messages") {
    val id = long("id").autoIncrement()
    val role = varchar("role", 32)
    var content = varchar("content", 12000)
    var historyId = reference("history_id", ChatHistoryTable.id)

    override val primaryKey = PrimaryKey(id)

    fun toMessage(row: ResultRow) = Message(
        id = row[id],
        role = row[role],
        content = row[content],
        historyId = row[historyId],
    )
}
