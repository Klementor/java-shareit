package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingByBookerId(@Param("bookerId") Long bookerId, Sort sort);

    List<Booking> findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(@Param("bookerId") Long userId,
                                                                     @Param("time") LocalDateTime time1,
                                                                     @Param("time") LocalDateTime time2);

    List<Booking> findBookingByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(@Param("bookerId") Long userId,
                                                                                    @Param("time") LocalDateTime time1,
                                                                                    @Param("time") LocalDateTime time2);

    List<Booking> findBookingByBookerIdAndStartIsAfter(@Param("bookerId") Long userId, @Param("time") LocalDateTime now, Sort sort);

    List<Booking> findBookingByBookerIdAndStatusEquals(@Param("bookerId") Long userId, @Param("status") Booking.Status status);

    Iterable<Booking> findAllByItemOwnerId(Long ownerId, Sort sort);

    Iterable<Booking> findBookingByItemOwnerIdAndStartIsBeforeAndEndIsAfter(@Param("ownerId") Long ownerId,
                                                                            @Param("time1") LocalDateTime time1,
                                                                            @Param("time2") LocalDateTime time2);

    Iterable<Booking> findBookingByItemOwnerIdAndEndIsBefore(@Param("ownerId") Long ownerId, @Param("time") LocalDateTime time, Sort sort);

    Iterable<Booking> findBookingByItemOwnerIdAndStartIsAfter(Long ownerId, LocalDateTime time, Sort sort);

    Iterable<Booking> findBookingByItemOwnerIdAndStatusEquals(Long ownerId, Booking.Status status);

    Optional<Booking> findFirstByItemIdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime time);

    Optional<Booking> findFirstByItemIdAndStartIsAfterOrderByStart(Long itemId, LocalDateTime time);

    boolean existsByBookerIdAndItemIdAndEndIsBefore(Long userId, Long itemId, LocalDateTime time);

    List<Booking> findFirstByItemIdInAndEndIsBeforeOrderByEndDesc(List<Long> itemIds, LocalDateTime now);

    List<Booking> findFirstByItemIdInAndStartIsAfterOrderByStart(List<Long> itemIds, LocalDateTime now);
}