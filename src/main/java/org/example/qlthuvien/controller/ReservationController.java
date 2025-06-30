package org.example.qlthuvien.controller;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.dto.reservation.CreateReservationRequest;
import org.example.qlthuvien.dto.reservation.ReservationResponse;
import org.example.qlthuvien.dto.reservation.UpdateReservationRequest;
import org.example.qlthuvien.entity.Book;
import org.example.qlthuvien.entity.Reservation;
import org.example.qlthuvien.mapper.ReservationMapper;
import org.example.qlthuvien.repository.BookItemRepository;
import org.example.qlthuvien.repository.BookRepository;
import org.example.qlthuvien.repository.ReservationRepository;
import org.example.qlthuvien.services.EmailService;
import org.example.qlthuvien.services.NotificationService;
import org.example.qlthuvien.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.qlthuvien.entity.BookItem;
import org.example.qlthuvien.entity.User;
import java.util.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final EmailService emailService;
    private final ReservationRepository reservationRepository;
    private final JwtUtil jwtUtil;
    private final ReservationMapper reservationMapper;
    private final EntityManager entityManager;
    private final BookRepository bookRepository;
    private final BookItemRepository bookItemRepository;
    private final NotificationService notificationService;
    @GetMapping
    public ResponseEntity<?> getAllReservations(@CookieValue(name = "jwt", required = false) String token) {
        if (!hasRole(token, "ADMIN")) {
            return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Access denied"
            ));
        }

        List<ReservationResponse> reservations = reservationRepository.findAll()
                .stream()
                .map(reservationMapper::toResponse)
                .toList();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", reservations
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyReservations(@CookieValue(name = "jwt", required = false) String token) {
        String email = getEmailFromToken(token);
        if (email == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid token"
            ));
        }

        List<ReservationResponse> myReservations = reservationRepository.findAll()
                .stream()
                .filter(res -> res.getUser() != null && email.equals(res.getUser().getEmail()))
                .map(reservationMapper::toResponse)
                .toList();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", myReservations
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Long id,
                                                @CookieValue(name = "jwt", required = false) String token) {
        String email = getEmailFromToken(token);
        String role = getRoleFromToken(token);
        if (email == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid token"
            ));
        }

        Optional<Reservation> opt = reservationRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Reservation not found"
            ));
        }

        Reservation res = opt.get();
        if (!email.equals(res.getUser().getEmail()) && !"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Access denied"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", reservationMapper.toResponse(res)
        ));
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody CreateReservationRequest data,
                                               @CookieValue(name = "jwt", required = false) String token) {
        if (getEmailFromToken(token) == null ||  data.getBook_id() == null || data.getUser_id() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid data or token"
            ));
        }

        User user = entityManager.find(User.class, data.getUser_id());
        Book book = entityManager.find(Book.class, data.getBook_id());

        if (user == null || book == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "User or Book not found"
            ));
        }

        boolean alreadyExists = reservationRepository.existsByUserAndBook(user, book);
        if (alreadyExists) {
            return ResponseEntity.status(409).body(Map.of(
                    "success", false,
                    "message", "Reservation already exists for this user and book"
            ));
        }

        long availableCount = bookItemRepository.countAvailableByBookId(book.getId());

        Reservation reservation = new Reservation();
        reservation.setReturned(availableCount > 0);
        reservation.setUser(user);
        reservation.setBook(book);

        Reservation saved = reservationRepository.save(reservation);

        String email = user.getEmail();
        if (availableCount > 0) {
            String htmlContent = emailService.loadEmailTemplate("emailTemplate.html").replace("{bookTitle}", book.getTitle());
            emailService.sendHtmlEmail(email, "Thông báo đặt sách thành công", htmlContent);
        }

        return ResponseEntity.status(201).body(Map.of(
                "success", true,
                "message", "Reservation created successfully",
                "data", reservationMapper.toResponse(saved)
        ));
    }


    @PutMapping("/{id}/return")
    public ResponseEntity<?> markAsReturned(@PathVariable Long id,
                                            @CookieValue(name = "jwt", required = false) String token,
                                            @RequestBody UpdateReservationRequest request) {

        if (!hasRole(token, "ADMIN")) {
            return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Access denied"
            ));
        }

        Optional<Reservation> opt = reservationRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Reservation not found"
            ));
        }

        Reservation res = opt.get();
        res.setReturned(request.isReturned());
        Reservation saved = reservationRepository.save(res);
        String email = res.getUser().getEmail();

        String bookTitle = res.getBook().getTitle();

        String htmlContent = emailService.loadEmailTemplate("emailTemplate.html").replace("{bookTitle}", bookTitle);
        notificationService.sendNotification(
                res.getUser(),
                "Đơn đặt trước cho \"" + bookTitle + "\" đã được xác nhận. Bạn có thể đến mượn sách."
        );
        System.out.println(htmlContent);
        emailService.sendHtmlEmail(email, "Thông báo đặt sách thành công", htmlContent);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Return status updated",
                "data", reservationMapper.toResponse(saved)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservationById(@PathVariable Long id,
                                                @CookieValue(name = "jwt", required = false) String token) {
        String email = getEmailFromToken(token);

        if (email == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid token"
            ));
        }

        Optional<Reservation> opt = reservationRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Reservation not found"
            ));
        }

        Reservation res = opt.get();
        if (!email.equals(res.getUser().getEmail()) && !hasRole(token, "ADMIN")) {
            return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Access denied"
            ));
        }

        reservationRepository.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Reservation deleted successfully"
        ));
    }
    private String getEmailFromToken(String token) {
        return token != null && jwtUtil.validateToken(token) ? jwtUtil.extractEmail(token) : null;
    }

    private String getRoleFromToken(String token) {
        return token != null && jwtUtil.validateToken(token) ? jwtUtil.extractRole(token) : null;
    }

    private boolean hasRole(String token, String requiredRole) {
        return token != null && jwtUtil.validateToken(token) && requiredRole.equals(jwtUtil.extractRole(token));
    }
}
