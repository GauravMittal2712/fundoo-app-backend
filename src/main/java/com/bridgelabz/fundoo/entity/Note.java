package com.bridgelabz.fundoo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import java.time.LocalDateTime;


@Entity
@Table(
        name = "notes",
        indexes = {
                @Index(name = "idx_note_user", columnList = "owner_id"),
                @Index(name = "idx_note_title", columnList = "title")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 500)
    private String title;

    @Column(length = 5000)
    private String description;

    @Column(length = 30)
    private String color;

    private boolean pinned;

    private boolean archived;

    private boolean trashed;

    private boolean deleted;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "owner_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_note_user")
    )
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "note_labels",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private Set<Label> labels = new HashSet<>();

    @OneToMany(
            mappedBy = "note",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Reminder> reminders = new ArrayList<>();

    @OneToMany(
            mappedBy = "note",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Collaborator> collaborators = new ArrayList<>();
}
