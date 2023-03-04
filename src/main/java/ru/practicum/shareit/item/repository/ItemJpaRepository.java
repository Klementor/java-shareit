package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemJpaRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long ownerId);

    @Query(value = "SELECT items " +
            "FROM Item AS items " +
            "WHERE (Item.available = TRUE) " +
            "AND ((CAST((:text) AS string) IS NULL) OR (LOWER(Item.name) LIKE LOWER(CONCAT('%', :text, '%')))" +
            "OR (CAST((:text) AS string) IS NULL) OR (LOWER(Item.description) LIKE LOWER(CONCAT('%', :text, '%'))))"
    )
    List<Item> findAllByText(@Param("text") String text);
}
