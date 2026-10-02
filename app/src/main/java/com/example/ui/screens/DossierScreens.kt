package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Dossier
import com.example.data.model.PublicServiceDef
import com.example.ui.CitizenProfile
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
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
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDossiersScreen(
    dossiers: List<Dossier>,
    onBack: () -> Unit,
    onSelectDossier: (String) -> Unit,
    onNewDossier: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var selectedFilter by remember { mutableStateOf("Tất cả") }
    val filterOptions = listOf("Tất cả", "Đang xử lý", "Có kết quả", "Chưa thanh toán")

    val filteredList = when (selectedFilter) {
        "Đang xử lý" -> dossiers.filter { it.statusStep in 1..4 }
        "Có kết quả" -> dossiers.filter { it.statusStep == 5 }
        "Chưa thanh toán" -> dossiers.filter { !it.isPaid && it.fee > 0 }
        else -> dossiers
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hồ sơ cá nhân", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewDossier,
                containerColor = OrangePrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_new_dossier")
            ) {
                Row(modifier = Modifier.padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = "Nộp hồ sơ")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Nộp hồ sơ", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        },
        containerColor = BackgroundLight,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OrangeLight,
                            selectedLabelColor = OrangeDark
                        )
                    )
                }
            }

            if (filteredList.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Chưa có hồ sơ nào trong mục này",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList, key = { it.id }) { dossier ->
                        DossierCardItem(
                            dossier = dossier,
                            onClick = { onSelectDossier(dossier.dossierCode) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DossierCardItem(
    dossier: Dossier,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("dossier_card_${dossier.dossierCode}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dossier.dossierCode,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicBlue
                )
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
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = dossier.serviceName,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Cơ quan: ${dossier.department}",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Step Indicator progress bar (1 to 5)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (step in 1..5) {
                    val isDone = step <= dossier.statusStep
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isDone) CivicGreen else Color(0xFFE2E8F0))
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ngày hẹn trả: ${dossier.estimatedCompletionDate}",
                    fontSize = 11.sp,
                    color = TextMuted
                )
                if (dossier.fee > 0L) {
                    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
                    val feeStr = "${formatter.format(dossier.fee)} đ"
                    Text(
                        text = if (dossier.isPaid) "Đã nộp phí: $feeStr" else "Chưa nộp phí: $feeStr",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (dossier.isPaid) CivicGreen else CivicRed
                    )
                } else {
                    Text(
                        text = "Miễn phí",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = CivicGreen
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DossierTrackingScreen(
    currentCode: String,
    searchedDossier: Dossier?,
    isNotFound: Boolean,
    onSearch: (String) -> Unit,
    onPayFee: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var inputCode by remember { mutableStateOf(currentCode) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tra cứu tiến độ hồ sơ", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
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
            // Search Input Box
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Nhập mã số hồ sơ hoặc số CCCD",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = inputCode,
                                onValueChange = { inputCode = it },
                                placeholder = { Text("Ví dụ: TA-2026-LTP-0812", fontSize = 13.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = { onSearch(inputCode) }),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("lookup_input_field")
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { onSearch(inputCode) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                                modifier = Modifier.testTag("btn_lookup_search")
                            ) {
                                Icon(Icons.Default.Search, contentDescription = "Tra cứu")
                            }
                        }

                        // Sample quick fill suggestions
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Gợi ý:", fontSize = 11.sp, color = TextMuted)
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = BackgroundLight,
                                modifier = Modifier.clickable {
                                    inputCode = "TA-2026-LTP-0812"
                                    onSearch("TA-2026-LTP-0812")
                                }
                            ) {
                                Text(
                                    "Lý lịch tư pháp",
                                    fontSize = 11.sp,
                                    color = CivicBlue,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = BackgroundLight,
                                modifier = Modifier.clickable {
                                    inputCode = "TA-2026-XDDD-0922"
                                    onSearch("TA-2026-XDDD-0922")
                                }
                            ) {
                                Text(
                                    "Giấy phép xây dựng",
                                    fontSize = 11.sp,
                                    color = CivicBlue,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Results Section
            if (isNotFound) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CivicRedLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Không tìm thấy hồ sơ phù hợp",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicRed
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Vui lòng kiểm tra lại chính xác mã biên nhận hoặc số CCCD người nộp.",
                                fontSize = 12.sp,
                                color = TextPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            if (searchedDossier != null) {
                val dossier = searchedDossier
                item {
                    DossierDetailCard(
                        dossier = dossier,
                        onPayFee = { onPayFee(dossier.id) }
                    )
                }

                // 5-Step Process Timeline
                item {
                    DossierProcessTimeline(currentStep = dossier.statusStep)
                }
            }
        }
    }
}

@Composable
fun DossierDetailCard(
    dossier: Dossier,
    onPayFee: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dossier.dossierCode,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicBlue
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(OrangeLight)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = dossier.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OrangeDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = dossier.serviceName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            DetailRow("Người nộp", dossier.applicantName)
            DetailRow("Số định danh/CCCD", dossier.applicantId)
            DetailRow("Cơ quan tiếp nhận", dossier.department)
            DetailRow("Thời điểm nộp", dossier.submissionDate)
            DetailRow("Hạn trả kết quả", dossier.estimatedCompletionDate)

            if (dossier.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(BackgroundLight)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Ý kiến cán bộ: ${dossier.notes}",
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fee section
            if (dossier.fee > 0L) {
                val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
                val feeStr = "${formatter.format(dossier.fee)} VNĐ"

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (dossier.isPaid) CivicGreenLight else CivicRedLight)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Lệ phí dịch vụ",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = feeStr,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (dossier.isPaid) CivicGreen else CivicRed
                        )
                    }

                    if (dossier.isPaid) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CivicGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Đã thanh toán", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CivicGreen)
                        }
                    } else {
                        Button(
                            onClick = onPayFee,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CivicRed),
                            modifier = Modifier.testTag("btn_pay_dossier_fee")
                        ) {
                            Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Thanh toán ngay", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
    }
}

@Composable
fun DossierProcessTimeline(currentStep: Int) {
    val steps = listOf(
        "Nộp hồ sơ trực tuyến",
        "Tiếp nhận hồ sơ",
        "Thẩm định hồ sơ",
        "Phê duyệt & Ký số",
        "Trả kết quả công dân"
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Quy trình giải quyết hồ sơ",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))

            steps.forEachIndexed { index, stepName ->
                val stepNumber = index + 1
                val isCompleted = stepNumber < currentStep
                val isCurrent = stepNumber == currentStep

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCompleted -> CivicGreen
                                        isCurrent -> OrangePrimary
                                        else -> Color(0xFFE2E8F0)
                                    }
                                )
                        ) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text(
                                    text = "$stepNumber",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) Color.White else TextMuted
                                )
                            }
                        }

                        if (index < steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(26.dp)
                                    .background(if (stepNumber < currentStep) CivicGreen else Color(0xFFE2E8F0))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = stepName,
                            fontSize = 13.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                            color = when {
                                isCurrent -> OrangeDark
                                isCompleted -> CivicGreen
                                else -> TextMuted
                            }
                        )
                        if (isCurrent) {
                            Text(
                                text = "Đang tiến hành tại cơ quan chuyên môn",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitDossierFormScreen(
    service: PublicServiceDef,
    citizenProfile: CitizenProfile,
    onSubmit: (String, String, String, Long, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var applicantName by remember { mutableStateOf(citizenProfile.fullName) }
    var idNumber by remember { mutableStateOf(citizenProfile.idNumber) }
    var phone by remember { mutableStateOf(citizenProfile.phone) }
    var address by remember { mutableStateOf(citizenProfile.address) }
    var notes by remember { mutableStateOf("") }
    var attachCount by remember { mutableStateOf(service.requiredDocuments.size) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nộp hồ sơ trực tuyến", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
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
            // Service Info Header
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(OrangeLight)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = service.category,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OrangeDark
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = service.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Cơ quan thụ lý: ${service.department}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Thời hạn giải quyết: ${service.processingDays} ngày làm việc",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = CivicGreen
                        )
                    }
                }
            }

            // Citizen Info from VNeID
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CivicGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Thông tin công dân (Tự động từ VNeID)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = applicantName,
                            onValueChange = { applicantName = it },
                            label = { Text("Họ và tên") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = idNumber,
                            onValueChange = { idNumber = it },
                            label = { Text("Số CCCD") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Số điện thoại liên hệ") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Địa chỉ thường trú") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Required Documents checklist
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Thành phần hồ sơ kèm theo",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        service.requiredDocuments.forEach { doc ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AttachFile, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = doc,
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "Đã đính kèm",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CivicGreen
                                )
                            }
                        }
                    }
                }
            }

            // Notes
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Ghi chú thêm cho cán bộ thụ lý",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            placeholder = { Text("Ghi chú bổ sung (nếu có)...", fontSize = 12.sp) },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Submission action button
            item {
                Button(
                    onClick = {
                        onSubmit(
                            service.title,
                            service.category,
                            service.department,
                            service.fee,
                            notes
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_confirm_submit_dossier")
                ) {
                    Text(
                        text = "Xác nhận nộp hồ sơ trực tuyến",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
