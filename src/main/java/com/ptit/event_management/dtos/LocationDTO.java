package com.ptit.event_management.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDTO {
    String name;
    String address;
    Double latitude;
    Double longitude;
    Long pricePerHour;
    Integer capacity;
    String image;
    Boolean available;
}
