package com.bridgelabz.fundoo.repository;

import com.bridgelabz.fundoo.entity.Note;
import com.bridgelabz.fundoo.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note,Long> {

    @EntityGraph(attributePaths = {"owner"})
    @Query("SELECT DISTINCT n FROM Note n WHERE n.owner = :owner AND n.deleted = false")
    List<Note> findAllByOwnerAndDeletedFalse(@Param("owner") User owner);

    @EntityGraph(attributePaths = {"owner"})
    @Query("SELECT n FROM Note n WHERE n.id = :id AND n.deleted = false")
    Optional<Note> findByIdAndDeletedFalse(@Param("id") Long id);

    @EntityGraph(attributePaths = {"owner"})
    @Query("SELECT n FROM Note n WHERE n.owner.id = :ownerId AND n.deleted = false AND (LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(n.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Note> searchNotes(@Param("ownerId") Long ownerId, @Param("query") String query);
}
