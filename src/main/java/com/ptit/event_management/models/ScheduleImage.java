package com.ptit.event_management.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Entity
@Data
@Table(name = "schedule_images")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String url; // đường dẫn ảnh

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    @JsonIgnore
    Schedule schedule;
}
