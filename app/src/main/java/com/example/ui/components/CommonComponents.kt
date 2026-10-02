package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ActiveScreen
import com.example.ui.BottomTab
import com.example.ui.theme.CivicBlue
import com.example.ui.theme.CivicBlueLight
import com.example.ui.theme.CivicGreen
import com.example.ui.theme.CivicGreenLight
import com.example.ui.theme.CivicRed
import com.example.ui.theme.CivicRedLight
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangeLight
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.OrangeSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AppHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(OrangePrimary, OrangeSecondary)
                )
            )
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // App Title matching HTML: "Dịch vụ công tỉnh" with subtitle
            Text(
                text = "Cổng dịch vụ công xã Tân An",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .testTag("app_title")
            )
            Text(
                text = "UBND XÃ TÂN AN • HỆ THỐNG MỘT CỬA ĐIỆN TỬ",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Search Box matching HTML: background #fff, rounded 20px, icon + input
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Tìm kiếm",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = {
                            Text(
                                text = "Tìm kiếm dịch vụ công...",
                                fontSize = 14.sp,
                                color = TextMuted
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { onSearchAction() }),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("search_input")
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Xóa",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherCard(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("weather_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Ngày",
                    tint = OrangeDark,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Hôm nay: Thứ Sáu",
                    fontSize = 13.sp,
                    color = Color(0xFF333333),
                    fontWeight = FontWeight.Normal
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = "Thời tiết",
                    tint = Color(0xFFEAA000),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "29°C (Mây rải rác)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
            }
        }
    }
}

@Composable
fun HighlightCard(
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("highlight_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
        ) {
            // Orange accent bar on the left (4px) matching HTML
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(68.dp)
                    .background(OrangePrimary)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Dịch vụ công nổi bật",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OrangePrimary
                    )
                    Text(
                        text = "Nộp ngay →",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeDark
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cấp Phiếu lý lịch tư pháp cho người dân trực tuyến nhanh chóng, thuận tiện.",
                    fontSize = 12.sp,
                    color = Color(0xFF555555),
                    lineHeight = 17.sp
                )
            }
        }
    }
}

data class GridMenuItemData(
    val label: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconTint: Color,
    val targetScreen: ActiveScreen
)

@Composable
fun GridMenu(
    onMenuItemClick: (ActiveScreen, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItems = listOf(
        GridMenuItemData(
            label = "Hồ sơ cá nhân",
            icon = Icons.Default.FolderOpen,
            iconBgColor = OrangeLight,
            iconTint = OrangePrimary,
            targetScreen = ActiveScreen.PERSONAL_DOSSIERS
        ),
        GridMenuItemData(
            label = "DVC trực tuyến",
            icon = Icons.Default.Laptop,
            iconBgColor = CivicRedLight,
            iconTint = CivicRed,
            targetScreen = ActiveScreen.ONLINE_SERVICES
        ),
        GridMenuItemData(
            label = "Danh mục TTHC",
            icon = Icons.Default.Description,
            iconBgColor = CivicBlueLight,
            iconTint = CivicBlue,
            targetScreen = ActiveScreen.PROCEDURES_CATALOG
        ),
        GridMenuItemData(
            label = "Tra cứu hồ sơ",
            icon = Icons.Default.Search,
            iconBgColor = CivicGreenLight,
            iconTint = CivicGreen,
            targetScreen = ActiveScreen.DOSSIER_TRACKING
        ),
        GridMenuItemData(
            label = "Thanh toán",
            icon = Icons.Default.CreditCard,
            iconBgColor = CivicBlueLight,
            iconTint = CivicBlue,
            targetScreen = ActiveScreen.PAYMENT
        ),
        GridMenuItemData(
            label = "Đánh giá",
            icon = Icons.Default.Star,
            iconBgColor = OrangeLight,
            iconTint = OrangePrimary,
            targetScreen = ActiveScreen.RATING
        ),
        GridMenuItemData(
            label = "Phản ánh",
            icon = Icons.Default.ReportProblem,
            iconBgColor = CivicRedLight,
            iconTint = CivicRed,
            targetScreen = ActiveScreen.FEEDBACK
        ),
        GridMenuItemData(
            label = "Hướng dẫn",
            icon = Icons.Default.MenuBook,
            iconBgColor = CivicGreenLight,
            iconTint = CivicGreen,
            targetScreen = ActiveScreen.USER_GUIDE
        )
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("grid_menu")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp)
        ) {
            // Row 1 (4 items)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 0 until 4) {
                    val item = menuItems[i]
                    GridMenuItem(
                        item = item,
                        onClick = { onMenuItemClick(item.targetScreen, item.label) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Row 2 (4 items)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 4 until 8) {
                    val item = menuItems[i]
                    GridMenuItem(
                        item = item,
                        onClick = { onMenuItemClick(item.targetScreen, item.label) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun GridMenuItem(
    item: GridMenuItemData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp)
            .testTag("menu_item_${item.label}")
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(item.iconBgColor)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = item.iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun AppBottomNav(
    currentTab: BottomTab,
    unreadNotificationCount: Int,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("bottom_nav")
    ) {
        // Trang chủ
        NavigationBarItem(
            selected = currentTab == BottomTab.HOME,
            onClick = { onTabSelected(BottomTab.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Trang chủ"
                )
            },
            label = { Text("Trang chủ", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CivicBlue,
                selectedTextColor = CivicBlue,
                indicatorColor = CivicBlueLight,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_home")
        )

        // Thông báo
        NavigationBarItem(
            selected = currentTab == BottomTab.NOTIFICATIONS,
            onClick = { onTabSelected(BottomTab.NOTIFICATIONS) },
            icon = {
                if (unreadNotificationCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = CivicRed,
                                contentColor = Color.White
                            ) {
                                Text("$unreadNotificationCount")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Thông báo"
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Thông báo"
                    )
                }
            },
            label = { Text("Thông báo", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CivicBlue,
                selectedTextColor = CivicBlue,
                indicatorColor = CivicBlueLight,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_notifications")
        )

        // Lịch sử
        NavigationBarItem(
            selected = currentTab == BottomTab.HISTORY,
            onClick = { onTabSelected(BottomTab.HISTORY) },
            icon = {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Lịch sử"
                )
            },
            label = { Text("Lịch sử", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CivicBlue,
                selectedTextColor = CivicBlue,
                indicatorColor = CivicBlueLight,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_history")
        )

        // Tài khoản
        NavigationBarItem(
            selected = currentTab == BottomTab.ACCOUNT,
            onClick = { onTabSelected(BottomTab.ACCOUNT) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Tài khoản"
                )
            },
            label = { Text("Tài khoản", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CivicBlue,
                selectedTextColor = CivicBlue,
                indicatorColor = CivicBlueLight,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("tab_account")
        )
    }
}
