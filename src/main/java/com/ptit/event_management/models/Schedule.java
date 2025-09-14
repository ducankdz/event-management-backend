package com.ptit.event_management.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "schedules")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "is_deleted = false")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Sự kiện mà schedule này thuộc về
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore
    Event event;

    String title;        // tiêu đề (ví dụ: "Khai mạc", "Workshop AI")
    String description;  // mô tả chi tiết

    Timestamp startedAt; // thời gian bắt đầu
    Timestamp endedAt;   // thời gian kết thúc

    @CreationTimestamp
    Timestamp createdAt;

    @UpdateTimestamp
    Timestamp updatedAt;

    boolean isDeleted = false;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ScheduleImage> images;  // danh sách ảnh cho schedule
}
