package com.bridgelabz.fundoo.repository;

import com.bridgelabz.fundoo.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note,Long> {
}
