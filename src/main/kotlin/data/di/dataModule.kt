package data.di

import data.repository.ChatHistoryRepository
import data.repository.ChatHistoryRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<ChatHistoryRepository> { ChatHistoryRepositoryImpl() }
}
