package com.biteplate.controller;

import com.biteplate.domain.reservation.Reservation;
import com.biteplate.dto.*;
import com.biteplate.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> all() {
        return reservationService.all();
    }

    @GetMapping("/pending")
    public List<Reservation> pending() {
        return reservationService.pending();
    }

    @GetMapping("/customer-status")
    public List<ReservationStatusResponse> customerStatus(@RequestParam String phone) {
        return reservationService.byPhone(phone).stream()
                .map(this::toStatusResponse)
                .toList();
    }

    @PostMapping
    public Reservation createByStaff(@Valid @RequestBody CreateReservationRequest request) {
        return reservationService.createByStaff(request);
    }

    @PostMapping("/customer-request")
    public ReservationStatusResponse createByCustomer(@Valid @RequestBody CustomerReservationRequest request) {
        return toStatusResponse(reservationService.createByCustomer(request));
    }

    @PostMapping("/{id}/confirm")
    public ReservationStatusResponse confirm(@PathVariable Long id, @Valid @RequestBody ConfirmReservationRequest request) {
        return toStatusResponse(reservationService.confirm(id, request.staffId()));
    }

    @PostMapping("/{id}/reject")
    public ReservationStatusResponse reject(@PathVariable Long id, @Valid @RequestBody ConfirmReservationRequest request) {
        return toStatusResponse(reservationService.reject(id, request.staffId()));
    }

    @PostMapping("/{id}/reminder")
    public ApiMessage reminder(@PathVariable Long id) {
        return new ApiMessage(reservationService.sendReminder(id));
    }

    private ReservationStatusResponse toStatusResponse(Reservation reservation) {
        return new ReservationStatusResponse(
                reservation.getId(),
                reservation.getCustomerName(),
                reservation.getPhone(),
                reservation.getTableNumber(),
                reservation.getReservationTime(),
                reservation.getStatus(),
                reservation.getCustomerMessage()
        );
    }
}
