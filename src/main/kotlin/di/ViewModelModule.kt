package di

import org.koin.dsl.module
import ui.screens.chat.ChatViewModel

val viewModelModule = module {
    single { ChatViewModel() }
}
