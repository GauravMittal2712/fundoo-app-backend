package com.bridgelabz.fundoo.repository;

import com.bridgelabz.fundoo.entity.Collaborator;
import com.bridgelabz.fundoo.entity.Note;
import com.bridgelabz.fundoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollaboratorRepository extends JpaRepository<Collaborator,Long> {
    List<Collaborator> findAllByNote(Note note);
    Optional<Collaborator> findByNoteAndUser(Note note, User user);
}
