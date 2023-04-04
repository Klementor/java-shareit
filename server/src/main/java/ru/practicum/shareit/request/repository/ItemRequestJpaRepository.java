package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface ItemRequestJpaRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long requesterId, Sort sort);

    @Query("SELECT ir FROM Request  ir where ir.requester.id <> :requesterId")
    List<Request> findOtherUserItems(@Param("requesterId") Long requesterId, Pageable pageable);
}
