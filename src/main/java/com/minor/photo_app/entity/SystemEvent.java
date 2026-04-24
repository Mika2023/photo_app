package com.minor.photo_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "system_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_id_gen")
    @SequenceGenerator(name = "events_id_gen", sequenceName = "system_events_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private Instant createdDate;

    @Column(name = "event_name", nullable = false)
    private String eventName;
}
