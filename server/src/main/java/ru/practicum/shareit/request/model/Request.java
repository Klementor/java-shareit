package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime dateTimeOfCreate = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "requester_id")
    private User requester;
}
