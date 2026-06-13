package com.biteplate.dto;

import com.biteplate.domain.reservation.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationStatusResponse(
        Long reservationId,
        String customerName,
        String phone,
        int tableNumber,
        LocalDateTime reservationTime,
        ReservationStatus status,
        String customerMessage
) {
}
