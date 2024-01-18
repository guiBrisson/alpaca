package ollama.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ollama.utils.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class Message(
    val role: Role,
    var content: String,
    val images: List<String>? = null
) {
    companion object {
        fun fromDataObject(message: data.model.Message) = Message(
            role = Role.fromString(message.role),
            content = message.content,
        )
    }
}

@Serializable
enum class Role {
    @SerialName("user") USER,
    @SerialName("system") SYSTEM,
    @SerialName("assistant") ASSISTANT;

    companion object {
        fun fromString(value: String): Role {
            return when {
                value.contains("user") -> USER
                value.contains("assistant") -> ASSISTANT
                else -> SYSTEM
            }
        }
    }
}


@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<Message>
)

@Serializable
data class ChatResponse(
    val model: String,
    @SerialName("created_at")
    @Serializable(LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val message: Message,
    val done: Boolean
)
