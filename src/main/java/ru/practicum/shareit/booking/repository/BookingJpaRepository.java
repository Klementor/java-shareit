package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    Iterable<Booking> findAllByItemOwnerId(Long ownerId, Sort sort);

    @Query(value = "SELECT bookings " +
            "FROM Booking AS bookings " +
            "WHERE (bookings.item.owner.id = (:ownerId)) " +
            "AND (bookings.start < (:time)) " +
            "AND (bookings.end > (:time))")
    Iterable<Booking> findCurrentBookingByOwnerId(@Param("ownerId") Long ownerId, @Param("time") LocalDateTime time);

    @Query(value = "SELECT bookings " +
            "FROM Booking AS bookings " +
            "WHERE (bookings.item.owner.id = (:ownerId)) " +
            "AND (bookings.end < (:time))")
    Iterable<Booking> findPastBookingByOwnerId(@Param("ownerId") Long ownerId, @Param("time") LocalDateTime time, Sort sort);

@Query(value = "SELECT bookings " +
        "FROM Booking AS bookings " +
        "WHERE(bookings.item.owner.id = (:ownerId) " +
        "AND (bookings.start > (:time)))")
    Iterable<Booking> findFutureBookingByOwnerId(Long ownerId, LocalDateTime time, Sort sort);


    Iterable<Booking> findBookingByItemOwnerIdAndStatusEquals(Long ownerId, Booking.Status status);

    Optional<Booking> findFirstByItemIdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime time);

    Optional<Booking> findFirstByItemIdAndStartIsAfterOrderByStart(Long itemId, LocalDateTime time);

    boolean existsByBookerIdAndItemIdAndEndIsBefore(Long userId, Long itemId, LocalDateTime time);
}