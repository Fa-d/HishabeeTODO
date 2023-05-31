package com.fahad.hishabeetodo.db

import android.content.Context
import androidx.room.Room
import com.fahad.hishabeetodo.interfaces.TodoDao
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
    fun buildDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(context, TodoDatabase::class.java, "hishabeetodo").build()
    }

    @Provides
    @Singleton
    fun getAppDao(appDatabase: TodoDatabase): TodoDao {
        return appDatabase.getTodoDao()
    }

}