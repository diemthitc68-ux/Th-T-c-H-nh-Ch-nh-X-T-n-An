package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dossiers")
data class Dossier(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dossierCode: String,
    val serviceName: String,
    val category: String,
    val applicantName: String,
    val applicantId: String,
    val submissionDate: String,
    val estimatedCompletionDate: String,
    val status: String, // "Đang xử lý", "Đã tiếp nhận", "Cần bổ sung hồ sơ", "Có kết quả", "Đã trả kết quả"
    val statusStep: Int, // 1: Nộp trực tuyến, 2: Tiếp nhận hồ sơ, 3: Thẩm định hồ sơ, 4: Phê duyệt & Ký số, 5: Trả kết quả
    val department: String,
    val fee: Long,
    val isPaid: Boolean,
    val notes: String = ""
)

@Entity(tableName = "feedbacks")
data class Feedback(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val feedbackCode: String,
    val title: String,
    val content: String,
    val location: String,
    val citizenName: String,
    val phoneNumber: String,
    val category: String,
    val status: String, // "Đã xử lý", "Đang xử lý", "Tiếp nhận"
    val officialResponse: String,
    val createdAt: String
)

@Entity(tableName = "ratings")
data class SatisfactionRating(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serviceName: String,
    val officerName: String = "Bộ phận Một cửa TP Tân An",
    val processingSpeedRating: Int,
    val officerAttitudeRating: Int,
    val facilityRating: Int,
    val comment: String,
    val submittedAt: String
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val message: String,
    val category: String, // "HỒ SƠ", "CHÍNH SÁCH", "THANH TOÁN", "THÔNG BÁO"
    val timestamp: String,
    val isRead: Boolean = false
)

data class PublicServiceDef(
    val id: String,
    val title: String,
    val category: String,
    val department: String,
    val processingDays: Int,
    val fee: Long,
    val isOnlineLevel4: Boolean = true,
    val description: String,
    val requiredDocuments: List<String>
)
