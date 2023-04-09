package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingByBookerId(@Param("bookerId") Long bookerId, Pageable pageable);

    List<Booking> findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(@Param("bookerId") Long userId,
                                                                     @Param("time") LocalDateTime time1,
                                                                     @Param("time") LocalDateTime time2, Pageable pageable);

    List<Booking> findBookingByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(@Param("bookerId") Long userId,
                                                                                    @Param("time") LocalDateTime time1,
                                                                                    @Param("time") LocalDateTime time2, Pageable pageable);

    List<Booking> findBookingByBookerIdAndStartIsAfter(@Param("bookerId") Long userId, @Param("time") LocalDateTime now, Pageable pageable);

    List<Booking> findBookingByBookerIdAndStatusEquals(@Param("bookerId") Long userId, @Param("status") Booking.Status status, Pageable pageable);

    List<Booking> findAllByItemOwnerId(Long ownerId, Pageable pageable);

    List<Booking> findBookingByItemOwnerIdAndStartIsBeforeAndEndIsAfter(@Param("ownerId") Long ownerId,
                                                                        @Param("time1") LocalDateTime time1,
                                                                        @Param("time2") LocalDateTime time2, Pageable pageable);

    List<Booking> findBookingByItemOwnerIdAndEndIsBefore(@Param("ownerId") Long ownerId, @Param("time") LocalDateTime time, Pageable pageable);

    List<Booking> findBookingByItemOwnerIdAndStartIsAfter(Long ownerId, LocalDateTime time, Pageable pageable);

    List<Booking> findBookingByItemOwnerIdAndStatusEquals(Long ownerId, Booking.Status status, Pageable pageable);

    Optional<Booking> findFirstByItemIdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime time);

    Optional<Booking> findFirstByItemIdAndStartIsAfterOrderByStart(Long itemId, LocalDateTime time);

    boolean existsByBookerIdAndItemIdAndEndIsBefore(Long userId, Long itemId, LocalDateTime time);

    List<Booking> findFirstByItemIdInAndEndIsBeforeOrderByEndDesc(List<Long> itemIds, LocalDateTime now);

    List<Booking> findFirstByItemIdInAndStartIsAfterOrderByStart(List<Long> itemIds, LocalDateTime now);

    @Query(" select b " +
            "from Booking b " +
            "where b.item in ?1 " +
            "and b.status = 'APPROVED'")
    List<Booking> findApprovedForItems(Collection<Item> items, Sort sort);
}