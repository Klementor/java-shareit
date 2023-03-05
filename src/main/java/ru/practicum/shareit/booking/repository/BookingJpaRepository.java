package ru.practicum.shareit.booking.repository;

import net.bytebuddy.TypeCache;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT bookings " +
            "FROM Booking AS bookings " +
            "WHERE (bookings.booker.id = (:bookerId))")
    List<Booking> findAllByBookerId(@Param("bookerId") Long bookerId, Sort sort);

    @Query(value = "SELECT bookings " +
            "FROM Booking AS bookings " +
            "WHERE (bookings.booker.id = (:bookerId)) " +
            "AND (bookings.start < (:time)) AND (bookings.end > (:time))")
    List<Booking> findCurrentBookingByBookerId(@Param("bookerId") Long userId, @Param("time") LocalDateTime now);

    @Query(value = "SELECT bookings " +
            "FROM Booking AS bookings " +
            "WHERE (bookings.booker.id = (:bookerId)) " +
            "AND (bookings.start < (:time)) AND (bookings.end < (:time))")
    List<Booking> findPastBookingByBookerId(@Param("bookerId") Long userId, @Param("time") LocalDateTime now);

    @Query(value = "SELECT bookings " +
            "FROM Booking AS bookings " +
            "WHERE (bookings.booker.id = (:bookerId)) " +
            "AND (bookings.start > (:time))")
    List<Booking> findFutureBookingByBookerId(@Param("bookerId") Long userId, @Param("time") LocalDateTime now, Sort sort);

    @Query(value = "SELECT bookings " +
            "FROM Booking AS bookings " +
            "WHERE (bookings.booker.id = (:bookerId)) AND (bookings.status = (:status))")
    List<Booking> findBookingByBookerIdAndStatus(@Param("bookerId") Long userId, @Param("status") Booking.Status status);
}