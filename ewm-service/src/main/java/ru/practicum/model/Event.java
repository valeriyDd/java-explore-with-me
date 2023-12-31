package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.EventState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private String title;
    @Column
    private String annotation;//Краткое описание
    @Column
    private String description;//Полное описание события
    @Column
    private Boolean paid;//Нужно ли оплачивать участие

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    //Ограничение на количество участников.
    // Значение 0 - означает отсутствие ограничения
    @Column(name = "participant_limit")
    private Integer participantLimit;

    //Количество одобренных заявок на участие в данном событии
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    //Дата и время создания события (в формате &quot;yyyy-MM-dd HH:mm:ss
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    //Дата и время публикации события (в формате &quot;yyyy-MM-dd HH:mm:ss)
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    // Нужна ли пре-модерация заявок на участие
    @Column(name = "request_moderation")
    private Boolean requestModeration;

    //Список состояний жизненного цикла события
    @Enumerated(EnumType.STRING)
    private EventState state;
}
