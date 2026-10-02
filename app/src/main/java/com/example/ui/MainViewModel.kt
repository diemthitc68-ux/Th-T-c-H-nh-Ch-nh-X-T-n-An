package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DvcRepository
import com.example.data.model.Dossier
import com.example.data.model.Feedback
import com.example.data.model.NotificationItem
import com.example.data.model.PublicServiceDef
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class BottomTab {
    HOME,
    NOTIFICATIONS,
    HISTORY,
    ACCOUNT
}

enum class ActiveScreen {
    HOME,
    DOSSIER_TRACKING,
    ONLINE_SERVICES,
    PROCEDURES_CATALOG,
    PERSONAL_DOSSIERS,
    PAYMENT,
    FEEDBACK,
    RATING,
    USER_GUIDE,
    SUBMIT_DOSSIER_FORM
}

data class CitizenProfile(
    val fullName: String = "Nguyễn Văn An",
    val idNumber: String = "080098001234",
    val birthDate: String = "15/08/1990",
    val gender: String = "Nam",
    val address: String = "Số 124, Đường Hùng Vương, Phường 2, TP Tân An, Long An",
    val phone: String = "0912 345 678",
    val email: String = "nguyenvanan.tanan@gmail.com",
    val vneidStatus: String = "Đã định danh điện tử Mức 2 (Bộ Công an)"
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DvcRepository

    val currentTab = MutableStateFlow(BottomTab.HOME)
    val activeScreen = MutableStateFlow(ActiveScreen.HOME)

    // Search query from header
    val searchQuery = MutableStateFlow("")

    val citizenProfile = MutableStateFlow(CitizenProfile())

    // Currently selected public service for detail or submission
    val selectedService = MutableStateFlow<PublicServiceDef?>(null)

    // Dossier lookup state
    val lookupCodeInput = MutableStateFlow("")
    val searchedDossier = MutableStateFlow<Dossier?>(null)
    val lookupNotFound = MutableStateFlow(false)
    val isSearching = MutableStateFlow(false)

    // Snackbar / alert message
    val userMessage = MutableStateFlow<String?>(null)

    val allDossiers: StateFlow<List<Dossier>>
    val unpaidDossiers: StateFlow<List<Dossier>>
    val allFeedbacks: StateFlow<List<Feedback>>
    val allNotifications: StateFlow<List<NotificationItem>>
    val unreadNotificationsCount: StateFlow<Int>

    val servicesCatalog: List<PublicServiceDef>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = DvcRepository(
            db.dossierDao(),
            db.feedbackDao(),
            db.ratingDao(),
            db.notificationDao()
        )
        servicesCatalog = repository.getPublicServiceCatalog()

        allDossiers = repository.allDossiers.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        unpaidDossiers = repository.unpaidDossiers.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allFeedbacks = repository.allFeedbacks.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allNotifications = repository.allNotifications.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        unreadNotificationsCount = repository.unreadNotificationsCount.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        viewModelScope.launch {
            repository.checkAndPrepopulate()
        }
    }

    // Filtered services based on search query
    val filteredServices: StateFlow<List<PublicServiceDef>> =
        searchQuery.combine(MutableStateFlow(servicesCatalog)) { query, list ->
            if (query.isBlank()) {
                list
            } else {
                list.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.category.contains(query, ignoreCase = true) ||
                            it.department.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), servicesCatalog)

    fun onTabSelected(tab: BottomTab) {
        currentTab.value = tab
        if (tab == BottomTab.HOME) {
            activeScreen.value = ActiveScreen.HOME
        }
    }

    fun navigateTo(screen: ActiveScreen) {
        activeScreen.value = screen
    }

    fun navigateBack() {
        if (activeScreen.value != ActiveScreen.HOME) {
            activeScreen.value = ActiveScreen.HOME
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun selectService(service: PublicServiceDef) {
        selectedService.value = service
        activeScreen.value = ActiveScreen.SUBMIT_DOSSIER_FORM
    }

    fun performLookup(code: String) {
        if (code.isBlank()) return
        isSearching.value = true
        lookupNotFound.value = false
        searchedDossier.value = null

        viewModelScope.launch {
            val result = repository.findDossierByCode(code.trim())
            if (result != null) {
                searchedDossier.value = result
                lookupNotFound.value = false
            } else {
                // If not found in DB directly, check if matching any sample or show not found
                val all = allDossiers.value
                val matched = all.firstOrNull {
                    it.dossierCode.equals(code.trim(), ignoreCase = true) ||
                            it.applicantId.contains(code.trim())
                }
                if (matched != null) {
                    searchedDossier.value = matched
                    lookupNotFound.value = false
                } else {
                    lookupNotFound.value = true
                }
            }
            isSearching.value = false
        }
    }

    fun submitNewDossier(
        serviceName: String,
        category: String,
        department: String,
        fee: Long,
        notes: String
    ) {
        viewModelScope.launch {
            val profile = citizenProfile.value
            val newDossier = repository.submitDossier(
                serviceName = serviceName,
                category = category,
                applicantName = profile.fullName,
                applicantId = profile.idNumber,
                department = department,
                fee = fee,
                notes = notes
            )
            searchedDossier.value = newDossier
            userMessage.value = "Nộp hồ sơ thành công! Mã hồ sơ: ${newDossier.dossierCode}"
            activeScreen.value = ActiveScreen.DOSSIER_TRACKING
        }
    }

    fun payDossierFee(dossierId: Int) {
        viewModelScope.launch {
            repository.markDossierPaid(dossierId)
            userMessage.value = "Thanh toán lệ phí trực tuyến thành công!"
            // update searched dossier if matched
            if (searchedDossier.value?.id == dossierId) {
                searchedDossier.value = searchedDossier.value?.copy(isPaid = true)
            }
        }
    }

    fun submitCitizenFeedback(
        title: String,
        content: String,
        location: String,
        phone: String,
        category: String
    ) {
        viewModelScope.launch {
            val profile = citizenProfile.value
            repository.submitFeedback(
                title = title,
                content = content,
                location = location,
                citizenName = profile.fullName,
                phone = phone.ifBlank { profile.phone },
                category = category
            )
            userMessage.value = "Phản ánh đã được gửi thành công đến UBND TP Tân An."
        }
    }

    fun submitSatisfactionSurvey(
        serviceName: String,
        speed: Int,
        attitude: Int,
        facility: Int,
        comment: String
    ) {
        viewModelScope.launch {
            repository.submitRating(
                serviceName = serviceName,
                speed = speed,
                attitude = attitude,
                facility = facility,
                comment = comment
            )
            userMessage.value = "Cảm ơn bạn đã gửi đánh giá phục vụ hành chính công!"
            activeScreen.value = ActiveScreen.HOME
        }
    }

    fun markNotificationRead(id: Int) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
            userMessage.value = "Đã đánh dấu tất cả thông báo là đã đọc."
        }
    }

    fun clearUserMessage() {
        userMessage.value = null
    }
}
