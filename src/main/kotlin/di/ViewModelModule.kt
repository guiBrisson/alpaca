package di

import org.koin.dsl.module
import ui.screens.chat.ChatViewModel
import ui.screens.chat_history.ChatHistoryViewModel

val viewModelModule = module {
    single { ChatViewModel(get()) }
    single { ChatHistoryViewModel(get()) }
}
