package com.raye.codeorbit_todo_list_app.di

import android.content.Context
import androidx.room.Room
import com.raye.codeorbit_todo_list_app.data.local.TodoDatabase
import com.raye.codeorbit_todo_list_app.data.local.dao.TodoDao
import com.raye.codeorbit_todo_list_app.data.repository.TodoRepositoryImpl
import com.raye.codeorbit_todo_list_app.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(todoDao: TodoDao): TodoRepository {
        return TodoRepositoryImpl(todoDao = todoDao)
    }
}