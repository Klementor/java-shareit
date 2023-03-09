package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemJpaRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long ownerId);

    @Query(value = "SELECT items " +
            "FROM Item AS items " +
            "WHERE " +
            "  ((items.available IS NULL) OR (items.available IS TRUE)) " +
            "AND (((:text) IS NULL) OR (LOWER(items.name) LIKE LOWER(CONCAT('%', :text, '%')))" +
            "OR (((:text) IS NULL) OR (LOWER(items.description) LIKE LOWER(CONCAT('%', :text, '%')))))")
    List<Item> findAllByText(@Param("text") String text);
}
