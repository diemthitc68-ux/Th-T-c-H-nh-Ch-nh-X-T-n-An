package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.DossierDao
import com.example.data.dao.FeedbackDao
import com.example.data.dao.NotificationDao
import com.example.data.dao.RatingDao
import com.example.data.model.Dossier
import com.example.data.model.Feedback
import com.example.data.model.NotificationItem
import com.example.data.model.SatisfactionRating

@Database(
    entities = [
        Dossier::class,
        Feedback::class,
        SatisfactionRating::class,
        NotificationItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dossierDao(): DossierDao
    abstract fun feedbackDao(): FeedbackDao
    abstract fun ratingDao(): RatingDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dvc_tan_an.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
