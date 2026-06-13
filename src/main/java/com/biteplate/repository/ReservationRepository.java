package com.biteplate.repository;

import com.biteplate.domain.reservation.Reservation;
import com.biteplate.domain.reservation.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatusOrderByReservationTimeAsc(ReservationStatus status);
    List<Reservation> findByPhoneOrderByReservationTimeDesc(String phone);
}
