package ollama.models

import kotlinx.serialization.Serializable

@Serializable
data class EmbeddingRequest(
    val model: String,
    val prompt: String,
)

@Serializable
data class EmbeddingResponse(
    val embedding: List<Double>
)