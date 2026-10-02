package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Dossier
import com.example.ui.ActiveScreen
import com.example.ui.components.GridMenu
import com.example.ui.components.HighlightCard
import com.example.ui.components.WeatherCard
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CivicBlue
import com.example.ui.theme.CivicBlueLight
import com.example.ui.theme.CivicGreen
import com.example.ui.theme.CivicGreenLight
import com.example.ui.theme.CivicRed
import com.example.ui.theme.CivicRedLight
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangeLight
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    dossiers: List<Dossier>,
    onMenuSelect: (ActiveScreen, String) -> Unit,
    onTrackDossier: (String) -> Unit,
    onSelectHighlight: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("home_screen_content")
    ) {
        // 1. Weather / Date widget
        item {
            WeatherCard()
        }

        // 2. Highlight card
        item {
            HighlightCard(onCardClick = onSelectHighlight)
        }

        // 3. Grid Menu Title & Grid Menu
        item {
            Column {
                Text(
                    text = "Danh sách dịch vụ công",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF444444),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                GridMenu(onMenuItemClick = onMenuSelect)
            }
        }

        // 4. Dossier Status Summary Card (Recent dossiers in progress)
        if (dossiers.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(OrangePrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Hồ sơ của bạn gần đây",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                            Text(
                                text = "Xem tất cả",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = CivicBlue,
                                modifier = Modifier
                                    .clickable { onMenuSelect(ActiveScreen.PERSONAL_DOSSIERS, "Hồ sơ cá nhân") }
                                    .testTag("view_all_dossiers")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Show up to 2 most recent dossiers
                        val recentItems = dossiers.take(2)
                        recentItems.forEachIndexed { index, dossier ->
                            RecentDossierRow(
                                dossier = dossier,
                                onClick = { onTrackDossier(dossier.dossierCode) }
                            )
                            if (index < recentItems.size - 1) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }

        // 5. Citizen Transparency & Service Metrics
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = CivicGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Chỉ số phục vụ Xã Tân An năm 2026",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricItem(
                            number = "99.2%",
                            label = "Hồ sơ đúng hẹn",
                            color = CivicGreen
                        )
                        MetricItem(
                            number = "100%",
                            label = "DVC toàn trình",
                            color = CivicBlue
                        )
                        MetricItem(
                            number = "98.7%",
                            label = "Đánh giá hài lòng",
                            color = OrangePrimary
                        )
                    }
                }
            }
        }

        // 6. Direct Support & Hotline
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = OrangeLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(OrangePrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Hotline",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tổng đài Hỗ trợ DVC Xã Tân An: 1022",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OrangeDark
                        )
                        Text(
                            text = "Hỗ trợ nộp hồ sơ, giải đáp thủ tục 24/7",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentDossierRow(
    dossier: Dossier,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = BackgroundLight,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dossier.serviceName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dossier.dossierCode,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${dossier.submissionDate}",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            val statusColor = when (dossier.statusStep) {
                5 -> CivicGreen
                3, 4 -> CivicBlue
                else -> OrangeDark
            }
            val statusBg = when (dossier.statusStep) {
                5 -> CivicGreenLight
                3, 4 -> CivicBlueLight
                else -> OrangeLight
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(statusBg)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = dossier.status,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = statusColor
                )
            }
        }
    }
}

@Composable
fun MetricItem(
    number: String,
    label: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}
