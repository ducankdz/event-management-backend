package com.ptit.event_management.exceptions;

public enum ErrorMessage {
    EVENT_NOT_FOUND("Không tìm thấy sự kiện"),
    BOOKING_NOT_FOUND("Không tìm thấy chỗ đặt"),
    STAGE_NOT_FOUND("Không tìm thấy giai đoạn"),
    TASK_NOT_FOUND("Không tìm thấy công việc"),
    SUPPLIER_NOT_FOUND("Không tìm thấy nhà cung cấp"),
    ONLY_ONE_FEED_BACK_IN_EVENT("Chỉ được tạo 1 phản hồi"),
    PERMISSION_DENIED("Không có quyền"),
    USER_INVITED("Người dùng đã được mời."),
    INVITATION_NOT_FOUND("Không tìm thấy lời mời nào."),
    INVITATION_DENIED("Không có quyền chấp nhận lời mời này"),
    LOCATION_NOT_FOUND("Không tìm thấy địa điểm này."),
    INVITATION_ACCEPTED("Lời mời đã được chấp nhận."),
    INVITATION_DECLINED("Lời mời đã bị từ chối."),
    LOCATION_NOT_AVAILABLE("Địa điểm không khả dụng."),
    TIME_NOT_VALID("Thời gian bắt đầu phải trước thời gian kết thúc"),
    SCHEDULE_NOT_FOUND("Không tìm thấy lịch trình");

    private String message;

    private ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}