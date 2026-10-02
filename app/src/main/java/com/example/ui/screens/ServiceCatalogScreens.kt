package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PublicServiceDef
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CivicBlue
import com.example.ui.theme.CivicBlueLight
import com.example.ui.theme.CivicGreen
import com.example.ui.theme.CivicGreenLight
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangeLight
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineServicesScreen(
    services: List<PublicServiceDef>,
    onSelectService: (PublicServiceDef) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tất cả") }

    val categories = listOf("Tất cả", "Tư pháp", "Hộ tịch", "Giao thông vận tải", "Xây dựng", "Đất đai")

    val filteredServices = services.filter { service ->
        val matchesCategory = selectedCategory == "Tất cả" || service.category.contains(selectedCategory, ignoreCase = true)
        val matchesQuery = searchQuery.isBlank() ||
                service.title.contains(searchQuery, ignoreCase = true) ||
                service.department.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dịch vụ công trực tuyến", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundLight,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input
            Surface(
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Tìm kiếm thủ tục dịch vụ công...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("services_search_input")
                )
            }

            // Categories
            LazyRow(
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OrangeLight,
                            selectedLabelColor = OrangeDark
                        )
                    )
                }
            }

            // List
            LazyColumn(
                contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredServices, key = { it.id }) { service ->
                    ServiceItemCard(
                        service = service,
                        onApply = { onSelectService(service) }
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceItemCard(
    service: PublicServiceDef,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CivicBlueLight)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = service.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = CivicBlue
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CivicGreenLight)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Toàn trình",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = service.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = service.description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Thời hạn: ${service.processingDays} ngày",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    val feeStr = if (service.fee > 0) {
                        val fmt = NumberFormat.getInstance(Locale("vi", "VN"))
                        "${fmt.format(service.fee)} đ"
                    } else "Miễn phí"
                    Text(
                        text = "Phí: $feeStr",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (service.fee > 0) TextPrimary else CivicGreen
                    )
                }

                Button(
                    onClick = onApply,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_apply_${service.id}")
                ) {
                    Text("Nộp hồ sơ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProceduresCatalogScreen(
    services: List<PublicServiceDef>,
    onSelectService: (PublicServiceDef) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh mục Thủ tục hành chính", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundLight,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = OrangeLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Bộ thủ tục chuẩn hóa cấp Thành phố & Cấp Xã/Phường",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OrangeDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tất cả thủ tục đều được công khai minh bạch về thành phần hồ sơ, quy trình luân chuyển, thời gian và biểu mẫu hợp lệ theo Quyết định của UBND tỉnh Long An.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            items(services, key = { it.id }) { service ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectService(service) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CivicBlueLight)
                        ) {
                            Icon(Icons.Default.Assignment, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(20.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = service.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${service.department} • ${service.processingDays} ngày",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserGuideScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    val guideSteps = listOf(
        Pair("1. Đăng nhập VNeID Mức 2", "Sử dụng tài khoản định danh điện tử VNeID do Bộ Công an cấp để tự động điền thông tin cá nhân và định danh hợp pháp."),
        Pair("2. Chọn dịch vụ & Nộp hồ sơ", "Tìm kiếm thủ tục hành chính cần nộp, đính kèm ảnh chụp các giấy tờ theo yêu cầu và bấm 'Nộp hồ sơ trực tuyến'."),
        Pair("3. Thanh toán lệ phí trực tuyến", "Sau khi cán bộ tiếp nhận thẩm định hợp lệ, bạn thanh toán phí/lệ phí qua cổng thanh toán quốc gia hoặc VNPay/VietQR."),
        Pair("4. Nhận kết quả tại nhà hoặc bản số", "Kết quả điện tử được cấp có giá trị pháp lý tương đương bản giấy; bản giấy được bưu chính công ích chuyển phát tận nhà.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hướng dẫn sử dụng", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundLight,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CivicGreenLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = CivicGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Chuyển đổi số hành chính công TP Tân An",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tiết kiệm thời gian, chi phí đi lại. Mọi thủ tục đều được giám sát công khai trên Hệ thống thông tin giải quyết TTHC.",
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "4 Bước thực hiện thủ tục trực tuyến",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        guideSteps.forEachIndexed { index, (stepTitle, stepDesc) ->
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(OrangePrimary)
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stepTitle,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = stepDesc,
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        lineHeight = 16.sp
                                    )
                                    if (index < guideSteps.size - 1) {
                                        Spacer(modifier = Modifier.height(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // FAQs
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Câu hỏi thường gặp (FAQ)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Q: Tôi chưa có tài khoản VNeID Mức 2 thì nộp hồ sơ được không?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OrangeDark
                        )
                        Text(
                            text = "A: Bạn có thể đến Công an Phường/Xã gần nhất để đăng ký định danh mức 2 miễn phí trong 5 phút.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Q: Làm sao để biết hồ sơ của tôi đã được giải quyết xong?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OrangeDark
                        )
                        Text(
                            text = "A: Ứng dụng sẽ gửi thông báo tin nhắn và cập nhật trạng thái trong mục 'Tra cứu hồ sơ' và tab 'Thông báo'.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}
