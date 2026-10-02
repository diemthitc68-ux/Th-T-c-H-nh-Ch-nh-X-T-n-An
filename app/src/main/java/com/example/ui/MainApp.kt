package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppHeader
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.DossierTrackingScreen
import com.example.ui.screens.FeedbackScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OnlineServicesScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.PersonalDossiersScreen
import com.example.ui.screens.ProceduresCatalogScreen
import com.example.ui.screens.RatingScreen
import com.example.ui.screens.SubmitDossierFormScreen
import com.example.ui.screens.UserGuideScreen
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.OrangePrimary

@Composable
fun MainApp(
    viewModel: MainViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val activeScreen by viewModel.activeScreen.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val allDossiers by viewModel.allDossiers.collectAsStateWithLifecycle()
    val unpaidDossiers by viewModel.unpaidDossiers.collectAsStateWithLifecycle()
    val allFeedbacks by viewModel.allFeedbacks.collectAsStateWithLifecycle()
    val allNotifications by viewModel.allNotifications.collectAsStateWithLifecycle()
    val unreadCount by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
    val citizenProfile by viewModel.citizenProfile.collectAsStateWithLifecycle()
    val searchedDossier by viewModel.searchedDossier.collectAsStateWithLifecycle()
    val lookupNotFound by viewModel.lookupNotFound.collectAsStateWithLifecycle()
    val selectedService = viewModel.selectedService.collectAsStateWithLifecycle().value
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        bottomBar = {
            if (activeScreen == ActiveScreen.HOME) {
                AppBottomNav(
                    currentTab = currentTab,
                    unreadNotificationCount = unreadCount,
                    onTabSelected = { tab -> viewModel.onTabSelected(tab) },
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 60.dp)
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = BackgroundLight,
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_app_scaffold")
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (activeScreen) {
                ActiveScreen.HOME -> {
                    when (currentTab) {
                        BottomTab.HOME -> {
                            Column(modifier = Modifier.fillMaxSize()) {
                                // Status bar inset padding for full-bleed gradient header
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(OrangePrimary)
                                        .windowInsetsPadding(WindowInsets.statusBars)
                                )
                                AppHeader(
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                                    onSearchAction = {
                                        if (searchQuery.isNotBlank()) {
                                            viewModel.navigateTo(ActiveScreen.ONLINE_SERVICES)
                                        }
                                    }
                                )
                                HomeScreen(
                                    dossiers = allDossiers,
                                    onMenuSelect = { screen, label ->
                                        viewModel.navigateTo(screen)
                                    },
                                    onTrackDossier = { code ->
                                        viewModel.performLookup(code)
                                        viewModel.navigateTo(ActiveScreen.DOSSIER_TRACKING)
                                    },
                                    onSelectHighlight = {
                                        // Highlight is: "Cấp Phiếu lý lịch tư pháp"
                                        val ltpService = viewModel.servicesCatalog.firstOrNull { it.id == "LTP-01" }
                                        if (ltpService != null) {
                                            viewModel.selectService(ltpService)
                                        } else {
                                            viewModel.navigateTo(ActiveScreen.ONLINE_SERVICES)
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        BottomTab.NOTIFICATIONS -> {
                            NotificationsScreen(
                                notifications = allNotifications,
                                onNotificationClick = { id -> viewModel.markNotificationRead(id) },
                                onMarkAllRead = { viewModel.markAllNotificationsRead() },
                                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                            )
                        }

                        BottomTab.HISTORY -> {
                            HistoryScreen(
                                dossiers = allDossiers,
                                onSelectDossier = { code ->
                                    viewModel.performLookup(code)
                                    viewModel.navigateTo(ActiveScreen.DOSSIER_TRACKING)
                                },
                                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                            )
                        }

                        BottomTab.ACCOUNT -> {
                            AccountScreen(
                                profile = citizenProfile,
                                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                            )
                        }
                    }
                }

                ActiveScreen.PERSONAL_DOSSIERS -> {
                    PersonalDossiersScreen(
                        dossiers = allDossiers,
                        onBack = { viewModel.navigateBack() },
                        onSelectDossier = { code ->
                            viewModel.performLookup(code)
                            viewModel.navigateTo(ActiveScreen.DOSSIER_TRACKING)
                        },
                        onNewDossier = { viewModel.navigateTo(ActiveScreen.ONLINE_SERVICES) },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    )
                }

                ActiveScreen.ONLINE_SERVICES -> {
                    OnlineServicesScreen(
                        services = viewModel.servicesCatalog,
                        onSelectService = { service -> viewModel.selectService(service) },
                        onBack = { viewModel.navigateBack() },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    )
                }

                ActiveScreen.PROCEDURES_CATALOG -> {
                    ProceduresCatalogScreen(
                        services = viewModel.servicesCatalog,
                        onSelectService = { service -> viewModel.selectService(service) },
                        onBack = { viewModel.navigateBack() },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    )
                }

                ActiveScreen.DOSSIER_TRACKING -> {
                    DossierTrackingScreen(
                        currentCode = viewModel.lookupCodeInput.value,
                        searchedDossier = searchedDossier,
                        isNotFound = lookupNotFound,
                        onSearch = { code -> viewModel.performLookup(code) },
                        onPayFee = { id -> viewModel.payDossierFee(id) },
                        onBack = { viewModel.navigateBack() },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    )
                }

                ActiveScreen.PAYMENT -> {
                    PaymentScreen(
                        unpaidDossiers = unpaidDossiers,
                        allDossiers = allDossiers,
                        onPay = { id -> viewModel.payDossierFee(id) },
                        onBack = { viewModel.navigateBack() },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    )
                }

                ActiveScreen.FEEDBACK -> {
                    FeedbackScreen(
                        feedbacks = allFeedbacks,
                        onSubmitFeedback = { title, content, location, phone, category ->
                            viewModel.submitCitizenFeedback(title, content, location, phone, category)
                        },
                        onBack = { viewModel.navigateBack() },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    )
                }

                ActiveScreen.RATING -> {
                    RatingScreen(
                        onSubmitRating = { srv, speed, attitude, facility, comment ->
                            viewModel.submitSatisfactionSurvey(srv, speed, attitude, facility, comment)
                        },
                        onBack = { viewModel.navigateBack() },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    )
                }

                ActiveScreen.USER_GUIDE -> {
                    UserGuideScreen(
                        onBack = { viewModel.navigateBack() },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    )
                }

                ActiveScreen.SUBMIT_DOSSIER_FORM -> {
                    val service = selectedService ?: viewModel.servicesCatalog.first()
                    SubmitDossierFormScreen(
                        service = service,
                        citizenProfile = citizenProfile,
                        onSubmit = { title, cat, dept, fee, notes ->
                            viewModel.submitNewDossier(title, cat, dept, fee, notes)
                        },
                        onBack = { viewModel.navigateBack() },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    )
                }
            }
        }
    }
}
