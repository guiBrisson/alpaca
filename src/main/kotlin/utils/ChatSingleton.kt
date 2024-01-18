package utils

import androidx.compose.runtime.mutableStateOf
import data.model.ChatHistory

object ChatSingleton {
    var selectedChat = mutableStateOf<ChatHistory?>(null)
}