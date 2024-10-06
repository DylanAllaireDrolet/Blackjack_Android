package com.example.blackjack.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.blackjack.dao.CardStatisticsDao
import com.example.blackjack.model.CardStatistic

@Database(entities = [CardStatistic::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val cardStatisticsDao: CardStatisticsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "blackjack_statistics_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
