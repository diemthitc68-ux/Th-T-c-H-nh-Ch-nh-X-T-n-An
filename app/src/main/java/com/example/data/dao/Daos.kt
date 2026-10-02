package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Dossier
import com.example.data.model.Feedback
import com.example.data.model.NotificationItem
import com.example.data.model.SatisfactionRating
import kotlinx.coroutines.flow.Flow

@Dao
interface DossierDao {
    @Query("SELECT * FROM dossiers ORDER BY id DESC")
    fun getAllDossiers(): Flow<List<Dossier>>

    @Query("SELECT * FROM dossiers WHERE dossierCode = :code OR applicantId = :code LIMIT 1")
    suspend fun findByCode(code: String): Dossier?

    @Query("SELECT * FROM dossiers WHERE isPaid = 0")
    fun getUnpaidDossiers(): Flow<List<Dossier>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDossier(dossier: Dossier): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dossiers: List<Dossier>)

    @Update
    suspend fun updateDossier(dossier: Dossier)

    @Query("UPDATE dossiers SET isPaid = 1 WHERE id = :id")
    suspend fun markAsPaid(id: Int)

    @Query("SELECT COUNT(*) FROM dossiers")
    suspend fun getCount(): Int
}

@Dao
interface FeedbackDao {
    @Query("SELECT * FROM feedbacks ORDER BY id DESC")
    fun getAllFeedbacks(): Flow<List<Feedback>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: Feedback): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(feedbacks: List<Feedback>)

    @Query("SELECT COUNT(*) FROM feedbacks")
    suspend fun getCount(): Int
}

@Dao
interface RatingDao {
    @Query("SELECT * FROM ratings ORDER BY id DESC")
    fun getAllRatings(): Flow<List<SatisfactionRating>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRating(rating: SatisfactionRating): Long
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<NotificationItem>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun getCount(): Int
}
