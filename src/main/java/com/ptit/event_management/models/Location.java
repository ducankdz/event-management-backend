package com.ptit.event_management.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "locations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "available = true")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;              // Tên địa điểm
    String address;           // Địa chỉ đầy đủ
    Double latitude;          // Vĩ độ (cho bản đồ)
    Double longitude;         // Kinh độ (cho bản đồ)
    Long pricePerHour;    // Giá
    Integer capacity;         // Sức chứa
    String image;
    boolean available = true;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
