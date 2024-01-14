package ollama.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ollama.utils.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class Models(
    val models: List<Model>,
)

@Serializable
data class Model(
    val name: String,
    @SerialName("modified_at")
    @Serializable(LocalDateTimeSerializer::class)
    val modifiedAt: LocalDateTime,
    val size: Long,
    val digest: String,
    val details: ModelDetails,
)

@Serializable
data class ShowModel(
    val modelfile: String,
    val parameters: String,
    val template: String,
    val details: ModelDetails,
)

@Serializable
data class ModelDetails(
    val format: String,
    val family: String,
    val families: List<String>?,
    @SerialName("parameter_size")
    val parameterSize: String,
    @SerialName("quantization_level")
    val quantizationLevel: String,
)
