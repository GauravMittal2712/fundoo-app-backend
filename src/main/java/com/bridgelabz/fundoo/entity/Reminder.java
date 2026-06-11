package com.bridgelabz.fundoo.entity;

import com.bridgelabz.fundoo.entity.enums.ReminderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reminder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime remindAt;

    @Enumerated(EnumType.STRING)
    private ReminderStatus status;

    private boolean notified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "note_id",
            nullable = false
    )
    private Note note;
}