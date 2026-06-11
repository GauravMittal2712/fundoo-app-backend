package com.bridgelabz.fundoo.entity;

import com.bridgelabz.fundoo.entity.enums.CollaboratorRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "collaborators",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_note_user",
                        columnNames = {"note_id","user_id"}
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collaborator extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "note_id",
            nullable = false
    )
    private Note note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Enumerated(EnumType.STRING)
    private CollaboratorRole role;
}