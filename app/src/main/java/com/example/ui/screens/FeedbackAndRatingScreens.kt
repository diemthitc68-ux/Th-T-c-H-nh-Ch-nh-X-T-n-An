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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.Dossier
import com.example.data.model.Feedback
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
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    feedbacks: List<Feedback>,
    onSubmitFeedback: (String, String, String, String, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var showSubmitDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Phản ánh kiến nghị công dân", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
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
                onClick = { showSubmitDialog = true },
                containerColor = CivicRed,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_submit_feedback")
            ) {
                Row(modifier = Modifier.padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = "Gửi phản ánh")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Gửi phản ánh", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        },
        containerColor = BackgroundLight,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CivicRedLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ReportProblem, contentDescription = null, tint = CivicRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Kênh tiếp nhận ý kiến công dân TP Tân An",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicRed
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Người dân phản ánh về hạ tầng đô thị, thái độ phục vụ của cán bộ, thủ tục hành chính để được chính quyền giải quyết trong 24-48 giờ.",
                            fontSize = 11.sp,
                            color = TextPrimary
                        )
                    }
                }
            }

            items(feedbacks, key = { it.id }) { feedback ->
                FeedbackCardItem(feedback = feedback)
            }
        }
    }

    if (showSubmitDialog) {
        SubmitFeedbackDialog(
            onDismiss = { showSubmitDialog = false },
            onSubmit = { title, content, location, phone, category ->
                onSubmitFeedback(title, content, location, phone, category)
                showSubmitDialog = false
            }
        )
    }
}

@Composable
fun FeedbackCardItem(
    feedback: Feedback,
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
                Text(
                    text = feedback.feedbackCode,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CivicBlue
                )
                val (bg, color) = when (feedback.status) {
                    "Đã xử lý" -> Pair(CivicGreenLight, CivicGreen)
                    "Đang xử lý" -> Pair(CivicBlueLight, CivicBlue)
                    else -> Pair(OrangeLight, OrangeDark)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(bg)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = feedback.status, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = color)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = feedback.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = feedback.content,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = feedback.location, fontSize = 11.sp, color = TextMuted)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = feedback.createdAt, fontSize = 11.sp, color = TextMuted)
            }

            if (feedback.officialResponse.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(BackgroundLight)
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Ủy ban nhân dân TP Tân An phản hồi:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = feedback.officialResponse,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubmitFeedbackDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Phường 1, TP Tân An") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Hạ tầng đô thị") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gửi phản ánh kiến nghị", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tiêu đề phản ánh") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Địa điểm xảy ra sự việc (Tân An)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Nội dung chi tiết") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onSubmit(title, content, location, "0912345678", category)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CivicRed),
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Gửi phản ánh")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingScreen(
    onSubmitRating: (String, Int, Int, Int, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var speedRating by remember { mutableIntStateOf(5) }
    var attitudeRating by remember { mutableIntStateOf(5) }
    var facilityRating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đánh giá mức độ hài lòng", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
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
                    colors = CardDefaults.cardColors(containerColor = OrangeLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Khảo sát ý kiến công dân đối với bộ máy hành chính",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Đánh giá của bạn góp phần nâng cao chất lượng phục vụ của cán bộ, công chức Trung tâm Phục vụ Hành chính công TP Tân An.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Criterion 1: Speed
            item {
                RatingCriterionCard(
                    title = "1. Thời gian tiếp nhận & trả kết quả hồ sơ",
                    subtitle = "Hồ sơ được giải quyết đúng hẹn, nhanh chóng, minh bạch",
                    rating = speedRating,
                    onRatingChange = { speedRating = it }
                )
            }

            // Criterion 2: Officer Attitude
            item {
                RatingCriterionCard(
                    title = "2. Tinh thần, thái độ phục vụ của cán bộ",
                    subtitle = "Lịch sự, nhiệt tình, giải thích rõ ràng các quy định",
                    rating = attitudeRating,
                    onRatingChange = { attitudeRating = it }
                )
            }

            // Criterion 3: Facility
            item {
                RatingCriterionCard(
                    title = "3. Cơ sở vật chất & trải nghiệm dịch vụ trực tuyến",
                    subtitle = "Ứng dụng trực tuyến thuận tiện, thiết bị một cửa hiện đại",
                    rating = facilityRating,
                    onRatingChange = { facilityRating = it }
                )
            }

            // Comment input
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Ý kiến đóng góp khác (nếu có)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            placeholder = { Text("Nhập góp ý xây dựng chính quyền phục vụ...", fontSize = 12.sp) },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = {
                        onSubmitRating(
                            "Bộ phận Một cửa TP Tân An",
                            speedRating,
                            attitudeRating,
                            facilityRating,
                            comment
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_submit_satisfaction_rating")
                ) {
                    Text(
                        text = "Gửi phiếu đánh giá",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun RatingCriterionCard(
    title: String,
    subtitle: String,
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 11.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (star in 1..5) {
                    IconButton(
                        onClick = { onRatingChange(star) },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = if (star <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Sao $star",
                            tint = if (star <= rating) OrangePrimary else TextMuted,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    unpaidDossiers: List<Dossier>,
    allDossiers: List<Dossier>,
    onPay: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var selectedForPay by remember { mutableStateOf<Dossier?>(null) }
    var paymentSuccessDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thanh toán trực tuyến", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
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
                    colors = CardDefaults.cardColors(containerColor = CivicBlueLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CreditCard, contentDescription = null, tint = CivicBlue, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Cổng Thanh toán Dịch vụ công Quốc gia",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CivicBlue
                            )
                            Text(
                                text = "Hỗ trợ thanh toán qua VietQR, VNPay, Napas247, ví điện tử miễn phí giao dịch.",
                                fontSize = 11.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Lệ phí hồ sơ cần thanh toán (${unpaidDossiers.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }

            if (unpaidDossiers.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CivicGreen, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Không có khoản phí nào cần thanh toán",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                        }
                    }
                }
            } else {
                items(unpaidDossiers, key = { it.id }) { dossier ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = dossier.dossierCode,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CivicBlue
                                )
                                val fmt = NumberFormat.getInstance(Locale("vi", "VN"))
                                Text(
                                    text = "${fmt.format(dossier.fee)} đ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CivicRed
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = dossier.serviceName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Cơ quan: ${dossier.department}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { selectedForPay = dossier },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_pay_now_${dossier.id}")
                            ) {
                                Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Thanh toán ngay bằng VietQR / VNPay")
                            }
                        }
                    }
                }
            }
        }
    }

    if (selectedForPay != null) {
        val dossier = selectedForPay!!
        val fmt = NumberFormat.getInstance(Locale("vi", "VN"))
        AlertDialog(
            onDismissRequest = { selectedForPay = null },
            title = { Text("Xác nhận thanh toán trực tuyến", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Mã hồ sơ: ${dossier.dossierCode}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicBlue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Số tiền: ${fmt.format(dossier.fee)} VNĐ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicRed
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // QR Code visual placeholder
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.size(140.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "VietQR",
                                tint = TextPrimary,
                                modifier = Modifier.size(90.dp)
                            )
                            Text(text = "VietQR DVC QG", fontSize = 10.sp, color = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Hệ thống tự động kích hoạt trạng thái 'Đã nộp phí' ngay khi xác nhận.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onPay(dossier.id)
                        selectedForPay = null
                        paymentSuccessDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CivicGreen)
                ) {
                    Text("Xác nhận thanh toán")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedForPay = null }) {
                    Text("Đóng")
                }
            }
        )
    }

    if (paymentSuccessDialog) {
        AlertDialog(
            onDismissRequest = { paymentSuccessDialog = false },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CivicGreen, modifier = Modifier.size(40.dp)) },
            title = { Text("Thanh toán thành công!") },
            text = { Text("Biên lai điện tử đã được ghi nhận vào kho hồ sơ công dân. Cán bộ thụ lý sẽ tiếp tục xử lý hồ sơ của bạn.") },
            confirmButton = {
                Button(onClick = { paymentSuccessDialog = false }) {
                    Text("Đồng ý")
                }
            }
        )
    }
}
