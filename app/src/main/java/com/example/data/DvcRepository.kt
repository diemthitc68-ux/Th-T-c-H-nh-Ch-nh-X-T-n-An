package com.example.data

import com.example.data.dao.DossierDao
import com.example.data.dao.FeedbackDao
import com.example.data.dao.NotificationDao
import com.example.data.dao.RatingDao
import com.example.data.model.Dossier
import com.example.data.model.Feedback
import com.example.data.model.NotificationItem
import com.example.data.model.PublicServiceDef
import com.example.data.model.SatisfactionRating
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class DvcRepository(
    private val dossierDao: DossierDao,
    private val feedbackDao: FeedbackDao,
    private val ratingDao: RatingDao,
    private val notificationDao: NotificationDao
) {
    val allDossiers: Flow<List<Dossier>> = dossierDao.getAllDossiers()
    val unpaidDossiers: Flow<List<Dossier>> = dossierDao.getUnpaidDossiers()
    val allFeedbacks: Flow<List<Feedback>> = feedbackDao.getAllFeedbacks()
    val allRatings: Flow<List<SatisfactionRating>> = ratingDao.getAllRatings()
    val allNotifications: Flow<List<NotificationItem>> = notificationDao.getAllNotifications()
    val unreadNotificationsCount: Flow<Int> = notificationDao.getUnreadCount()

    suspend fun checkAndPrepopulate() {
        if (dossierDao.getCount() == 0) {
            val sampleDossiers = listOf(
                Dossier(
                    dossierCode = "TA-2026-LTP-0812",
                    serviceName = "Cấp Phiếu lý lịch tư pháp cho người dân",
                    category = "Tư pháp",
                    applicantName = "Nguyễn Văn An",
                    applicantId = "080098001234",
                    submissionDate = "01/10/2026 09:15",
                    estimatedCompletionDate = "05/10/2026",
                    status = "Phê duyệt & Ký số",
                    statusStep = 4,
                    department = "Sở Tư pháp tỉnh Long An",
                    fee = 200000L,
                    isPaid = true,
                    notes = "Đã thẩm định thông tin trích lục tư pháp hợp lệ. Đang chờ chữ ký số Giám đốc Sở."
                ),
                Dossier(
                    dossierCode = "TA-2026-GPLX-0193",
                    serviceName = "Cấp đổi Giấy phép lái xe do ngành GTVT cấp",
                    category = "Giao thông vận tải",
                    applicantName = "Nguyễn Văn An",
                    applicantId = "080098001234",
                    submissionDate = "26/09/2026 14:20",
                    estimatedCompletionDate = "30/09/2026",
                    status = "Có kết quả",
                    statusStep = 5,
                    department = "Sở Giao thông Vận tải",
                    fee = 135000L,
                    isPaid = true,
                    notes = "Đã in GPLX thẻ PET. Đang chuyển phát qua bưu chính công ích VNPost đến tận nhà."
                ),
                Dossier(
                    dossierCode = "TA-2026-DKKH-0411",
                    serviceName = "Đăng ký kết hôn trực tuyến",
                    category = "Hộ tịch",
                    applicantName = "Nguyễn Văn An",
                    applicantId = "080098001234",
                    submissionDate = "02/10/2026 08:10",
                    estimatedCompletionDate = "06/10/2026",
                    status = "Tiếp nhận hồ sơ",
                    statusStep = 2,
                    department = "UBND Phường 1, TP Tân An",
                    fee = 0L,
                    isPaid = true,
                    notes = "Cán bộ Tư pháp - Hộ tịch đang đối soát dữ liệu cơ sở dữ liệu dân cư quốc gia."
                ),
                Dossier(
                    dossierCode = "TA-2026-XDDD-0922",
                    serviceName = "Cấp giấy phép xây dựng nhà ở riêng lẻ đô thị",
                    category = "Xây dựng",
                    applicantName = "Nguyễn Văn An",
                    applicantId = "080098001234",
                    submissionDate = "28/09/2026 10:45",
                    estimatedCompletionDate = "08/10/2026",
                    status = "Thẩm định hồ sơ (Chờ nộp phí)",
                    statusStep = 3,
                    department = "Phòng Quản lý Đô thị TP Tân An",
                    fee = 150000L,
                    isPaid = false,
                    notes = "Bản vẽ thiết kế đạt yêu cầu chỉ giới đường đỏ. Vui lòng thanh toán lệ phí để phê duyệt giấy phép."
                )
            )
            dossierDao.insertAll(sampleDossiers)
        }

        if (feedbackDao.getCount() == 0) {
            val sampleFeedbacks = listOf(
                Feedback(
                    feedbackCode = "PA-2026-TA-102",
                    title = "Đèn chiếu sáng công cộng đường Nguyễn Đình Chiểu, Phường 1 bị hỏng",
                    content = "Đoạn đường từ hẻm 45 đến hẻm 67 đường Nguyễn Đình Chiểu bị mất đèn chiếu sáng công cộng 2 đêm qua, gây nguy hiểm cho người tham gia giao thông vào ban đêm.",
                    location = "Phường 1, TP Tân An",
                    citizenName = "Nguyễn Văn An",
                    phoneNumber = "0912345678",
                    category = "Hạ tầng đô thị",
                    status = "Đã xử lý",
                    officialResponse = "Công ty Cổ phần Công trình Đô thị Tân An đã cử tổ kỹ thuật kiểm tra và tiến hành thay bóng đèn Led 150W lúc 16h30 ngày 01/10/2026. Hiện hệ thống đã hoạt động bình thường.",
                    createdAt = "01/10/2026 10:20"
                ),
                Feedback(
                    feedbackCode = "PA-2026-TA-098",
                    title = "Khen ngợi cán bộ một cửa giải quyết hồ sơ hộ tịch rất tận tình",
                    content = "Sáng nay tôi đến nộp hồ sơ trích lục khai sinh bản sao tại Trung tâm hành chính công Tân An, cán bộ hướng dẫn thao tác trên VNeID rất nhiệt tình và chu đáo.",
                    location = "Bộ phận Tiếp nhận và Trả kết quả TP Tân An",
                    citizenName = "Trần Thị Mai",
                    phoneNumber = "0988776655",
                    category = "Dịch vụ công",
                    status = "Tiếp nhận",
                    officialResponse = "Ủy ban nhân dân thành phố Tân An ghi nhận và trân trọng cảm ơn ý kiến khen ngợi của công dân đối với đội ngũ cán bộ công chức.",
                    createdAt = "30/09/2026 14:15"
                ),
                Feedback(
                    feedbackCode = "PA-2026-TA-089",
                    title = "Đề nghị kiểm tra nắp cống thoát nước ngã tư Quốc lộ 1 - Châu Thị Kim",
                    content = "Nắp cống thoát nước có dấu hiệu bị vênh và phát ra tiếng ồn lớn khi xe tải chạy qua.",
                    location = "Phường 3, TP Tân An",
                    citizenName = "Lê Hoàng Phúc",
                    phoneNumber = "0903112233",
                    category = "Giao thông đô thị",
                    status = "Đang xử lý",
                    officialResponse = "Phòng Quản lý Đô thị đã bàn giao công trình cho đơn vị thi công kiểm tra gia cố bulong đệm cao su trong ngày hôm nay.",
                    createdAt = "29/09/2026 08:45"
                )
            )
            feedbackDao.insertAll(sampleFeedbacks)
        }

        if (notificationDao.getCount() == 0) {
            val sampleNotifications = listOf(
                NotificationItem(
                    title = "Hồ sơ TA-2026-LTP-0812 đã được phê duyệt",
                    message = "Phiếu lý lịch tư pháp số điện tử đã được ký số hợp lệ và chuyển về kho dữ liệu cá nhân của bạn.",
                    category = "HỒ SƠ",
                    timestamp = "Hôm nay, 08:30",
                    isRead = false
                ),
                NotificationItem(
                    title = "Nhắc nộp phí hồ sơ TA-2026-XDDD-0922",
                    message = "Lệ phí cấp Giấy phép xây dựng: 150.000đ. Quý công dân vui lòng thanh toán trực tuyến qua VNeID/VNPay để nhận kết quả.",
                    category = "THANH TOÁN",
                    timestamp = "Hôm qua, 15:40",
                    isRead = false
                ),
                NotificationItem(
                    title = "Tân An: 100% dịch vụ công trực tuyến toàn trình",
                    message = "Triển khai cung cấp 125 dịch vụ công trực tuyến toàn trình mới trên Cổng Dịch vụ công tỉnh Long An từ tháng 10/2026.",
                    category = "CHÍNH SÁCH",
                    timestamp = "28/09/2026",
                    isRead = true
                ),
                NotificationItem(
                    title = "Đánh giá sự hài lòng phục vụ hành chính công",
                    message = "Hãy dành 1 phút để đánh giá chất lượng phục vụ của cán bộ tiếp nhận tại Bộ phận Một cửa TP Tân An.",
                    category = "THÔNG BÁO",
                    timestamp = "26/09/2026",
                    isRead = true
                )
            )
            notificationDao.insertAll(sampleNotifications)
        }
    }

    suspend fun findDossierByCode(code: String): Dossier? {
        return dossierDao.findByCode(code.trim())
    }

    suspend fun submitDossier(
        serviceName: String,
        category: String,
        applicantName: String,
        applicantId: String,
        department: String,
        fee: Long,
        notes: String
    ): Dossier {
        val randomSuffix = Random.nextInt(1000, 9999)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateEstFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val now = Date()
        val estDate = Date(now.time + 3 * 24 * 60 * 60 * 1000L)

        val codePrefix = when {
            category.contains("Tư pháp", true) -> "LTP"
            category.contains("Hộ tịch", true) -> "HT"
            category.contains("Xây dựng", true) -> "XD"
            category.contains("Giao thông", true) -> "GT"
            category.contains("Đất đai", true) -> "DD"
            else -> "DVC"
        }
        val code = "TA-2026-$codePrefix-$randomSuffix"

        val dossier = Dossier(
            dossierCode = code,
            serviceName = serviceName,
            category = category,
            applicantName = applicantName.ifBlank { "Nguyễn Văn An" },
            applicantId = applicantId.ifBlank { "080098001234" },
            submissionDate = dateFormat.format(now),
            estimatedCompletionDate = dateEstFormat.format(estDate),
            status = "Đã nộp trực tuyến",
            statusStep = 1,
            department = department,
            fee = fee,
            isPaid = fee == 0L,
            notes = notes
        )
        val id = dossierDao.insertDossier(dossier)
        val newDossier = dossier.copy(id = id.toInt())

        // Also add a notification
        notificationDao.insertNotification(
            NotificationItem(
                title = "Nộp hồ sơ thành công ($code)",
                message = "Hồ sơ '$serviceName' đã được gửi thành công đến $department.",
                category = "HỒ SƠ",
                timestamp = "Vừa xong",
                isRead = false
            )
        )

        return newDossier
    }

    suspend fun markDossierPaid(id: Int) {
        dossierDao.markAsPaid(id)
    }

    suspend fun submitFeedback(
        title: String,
        content: String,
        location: String,
        citizenName: String,
        phone: String,
        category: String
    ): Feedback {
        val randomNum = Random.nextInt(100, 999)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val feedback = Feedback(
            feedbackCode = "PA-2026-TA-$randomNum",
            title = title,
            content = content,
            location = location,
            citizenName = citizenName.ifBlank { "Nguyễn Văn An" },
            phoneNumber = phone.ifBlank { "0912345678" },
            category = category,
            status = "Tiếp nhận",
            officialResponse = "UBND TP Tân An đã tiếp nhận phản ánh và chuyển đến phòng chuyên môn xử lý trong vòng 24 giờ làm việc.",
            createdAt = dateFormat.format(Date())
        )
        feedbackDao.insertFeedback(feedback)
        return feedback
    }

    suspend fun submitRating(
        serviceName: String,
        speed: Int,
        attitude: Int,
        facility: Int,
        comment: String
    ) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val rating = SatisfactionRating(
            serviceName = serviceName,
            officerName = "Bộ phận Một cửa TP Tân An",
            processingSpeedRating = speed,
            officerAttitudeRating = attitude,
            facilityRating = facility,
            comment = comment,
            submittedAt = dateFormat.format(Date())
        )
        ratingDao.insertRating(rating)
    }

    suspend fun markNotificationAsRead(id: Int) {
        notificationDao.markAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        notificationDao.markAllAsRead()
    }

    // Static list of administrative public services in TP Tân An
    fun getPublicServiceCatalog(): List<PublicServiceDef> {
        return listOf(
            PublicServiceDef(
                id = "LTP-01",
                title = "Cấp Phiếu lý lịch tư pháp cho người dân",
                category = "Tư pháp",
                department = "Sở Tư pháp tỉnh Long An",
                processingDays = 3,
                fee = 200000L,
                description = "Cung cấp Phiếu lý lịch tư pháp số 1 hoặc số 2 phục vụ xin việc, xuất cảnh hoặc thủ tục hành chính.",
                requiredDocuments = listOf(
                    "Tờ khai yêu cầu cấp Phiếu lý lịch tư pháp (Điền trực tuyến)",
                    "Bản quét/chụp CCCD gắn chip hoặc Định danh VNeID mức 2",
                    "Giấy tờ chứng minh đối tượng miễn/giảm phí (nếu có)"
                )
            ),
            PublicServiceDef(
                id = "GPLX-02",
                title = "Cấp đổi Giấy phép lái xe do ngành GTVT cấp",
                category = "Giao thông vận tải",
                department = "Sở Giao thông Vận tải tỉnh Long An",
                processingDays = 5,
                fee = 135000L,
                description = "Đổi GPLX hạng A1, A2, B1, B2 khi hết hạn hoặc bị hư hỏng trực tuyến toàn trình.",
                requiredDocuments = listOf(
                    "Đơn đề nghị đổi Giấy phép lái xe",
                    "Giấy khám sức khỏe lái xe điện tử từ cơ sở y tế",
                    "Ảnh chụp 3x4 nền trắng hoặc dùng ảnh CCCD",
                    "Bản quét GPLX hiện tại"
                )
            ),
            PublicServiceDef(
                id = "DKKH-03",
                title = "Đăng ký kết hôn trực tuyến",
                category = "Hộ tịch",
                department = "UBND các Xã/Phường thuộc TP Tân An",
                processingDays = 1,
                fee = 0L,
                description = "Đăng ký kết hôn giữa công dân Việt Nam cư trú trên địa bàn thành phố Tân An.",
                requiredDocuments = listOf(
                    "Tờ khai đăng ký kết hôn theo mẫu",
                    "Giấy xác nhận tình trạng hôn nhân (nếu khác nơi cư trú)",
                    "CCCD gắn chip của hai bên nam nữ"
                )
            ),
            PublicServiceDef(
                id = "KS-04",
                title = "Đăng ký khai sinh kết hợp cấp thẻ BHYT và đăng ký thường trú",
                category = "Hộ tịch - Liên thông",
                department = "UBND TP Tân An - BHXH TP Tân An",
                processingDays = 3,
                fee = 0L,
                description = "Thủ tục liên thông 3 trong 1: Khai sinh, cấp mã số định danh cá nhân và cấp thẻ BHYT cho trẻ dưới 6 tuổi.",
                requiredDocuments = listOf(
                    "Tờ khai đăng ký khai sinh điện tử",
                    "Giấy chứng sinh do bệnh viện/trung tâm y tế cấp",
                    "Định danh điện tử VNeID của cha hoặc mẹ"
                )
            ),
            PublicServiceDef(
                id = "XD-05",
                title = "Cấp giấy phép xây dựng nhà ở riêng lẻ đô thị",
                category = "Xây dựng",
                department = "Phòng Quản lý Đô thị TP Tân An",
                processingDays = 10,
                fee = 150000L,
                description = "Cấp phép xây mới hoặc cải tạo công trình nhà ở riêng lẻ trong phạm vi đô thị thành phố Tân An.",
                requiredDocuments = listOf(
                    "Đơn đề nghị cấp giấy phép xây dựng",
                    "Bản sao Giấy chứng nhận quyền sử dụng đất (Sổ hồng/đỏ)",
                    "Bộ bản vẽ thiết kế xây dựng công trình kèm trích lục sơ đồ"
                )
            ),
            PublicServiceDef(
                id = "DD-06",
                title = "Đăng ký biến động quyền sử dụng đất, quyền sở hữu tài sản",
                category = "Đất đai - Tài nguyên",
                department = "Chi nhánh Văn phòng Đăng ký Đất đai TP Tân An",
                processingDays = 7,
                fee = 50000L,
                description = "Chuyển nhượng, tặng cho, thừa kế, cấp đổi sổ quyền sử dụng đất trên địa bàn thành phố.",
                requiredDocuments = listOf(
                    "Hợp đồng chuyển nhượng/tặng cho có công chứng",
                    "Bản gốc Giấy chứng nhận quyền sử dụng đất",
                    "Tờ khai lệ phí trước bạ và thuế thu nhập cá nhân"
                )
            ),
            PublicServiceDef(
                id = "HK-07",
                title = "Đăng ký tạm trú / Thông báo lưu trú trực tuyến",
                category = "An ninh trật tự",
                department = "Công an các Phường/Xã thuộc TP Tân An",
                processingDays = 1,
                fee = 15000L,
                description = "Đăng ký tạm trú cho công dân đến sinh sống, làm việc tại thành phố Tân An.",
                requiredDocuments = listOf(
                    "Tờ khai thay đổi thông tin cư trú (Mẫu CT01)",
                    "Giấy tờ chứng minh chỗ ở hợp pháp (Hợp đồng thuê nhà/sổ hồng chủ trọ)"
                )
            ),
            PublicServiceDef(
                id = "KD-08",
                title = "Đăng ký thành lập hộ kinh doanh cá thể",
                category = "Kinh doanh",
                department = "Phòng Tài chính - Kế hoạch TP Tân An",
                processingDays = 3,
                fee = 100000L,
                description = "Cấp Giấy chứng nhận đăng ký hộ kinh doanh cá thể trên địa bàn các phường, xã Tân An.",
                requiredDocuments = listOf(
                    "Giấy đề nghị đăng ký hộ kinh doanh",
                    "Bản sao CCCD của chủ hộ kinh doanh",
                    "Hợp đồng thuê mặt bằng kinh doanh (nếu có)"
                )
            )
        )
    }
}
