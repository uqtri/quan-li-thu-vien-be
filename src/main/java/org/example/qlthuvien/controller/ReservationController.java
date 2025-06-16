package org.example.qlthuvien.controller;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.entity.Reservation;
import org.example.qlthuvien.repository.ReservationRepository;
import org.example.qlthuvien.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getAllReservations(@RequestHeader("Authorization") String authHeader) {
        if (!hasRole(authHeader, "ADMIN")) {
            return ResponseEntity.status(403).body("Access denied");
        }
        return ResponseEntity.ok(reservationRepository.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyReservations(@RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        if (email == null) return ResponseEntity.status(401).body("Invalid token");

        return ResponseEntity.ok(
                reservationRepository.findAll()
                        .stream()
                        .filter(res -> res.getUser() != null && email.equals(res.getUser().getEmail()))
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        String role = getRoleFromToken(authHeader);
        if (email == null) return ResponseEntity.status(401).body("Invalid token");

        Optional<Reservation> opt = reservationRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(404).body("Reservation not found");

        Reservation res = opt.get();
        if (!email.equals(res.getUser().getEmail()) && !"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return ResponseEntity.ok(res);
    }


    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation, @RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        if (email == null || reservation.getBookItem() == null) {
            return ResponseEntity.badRequest().body("Invalid data");
        }

        if (reservation.getReservationDate() == null) {
            reservation.setReservationDate(new Date());
        }

        return ResponseEntity.status(201).body(reservationRepository.save(reservation));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<?> markAsReturned(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        if (!hasRole(authHeader, "ADMIN")) {
            return ResponseEntity.status(403).body("Access denied");
        }

        Optional<Reservation> opt = reservationRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(404).body("Reservation not found");

        Reservation res = opt.get();
        res.setReturned(true);
        return ResponseEntity.ok(reservationRepository.save(res));
    }

    private String getEmailFromToken(String authHeader) {
        String token = extractToken(authHeader);
        return token != null && jwtUtil.validateToken(token) ? jwtUtil.extractEmail(token) : null;
    }

    private String getRoleFromToken(String authHeader) {
        String token = extractToken(authHeader);
        return token != null && jwtUtil.validateToken(token) ? jwtUtil.extractRole(token) : null;
    }

    private boolean hasRole(String authHeader, String requiredRole) {
        String token = extractToken(authHeader);
        return token != null && jwtUtil.validateToken(token) && requiredRole.equals(jwtUtil.extractRole(token));
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.substring(7);
    }
}
