package com.bridgelabz.fundoo.repository;

import com.bridgelabz.fundoo.entity.Label;
import com.bridgelabz.fundoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label,Long> {
    List<Label> findAllByUser(User user);
    Optional<Label> findByIdAndUser(Long id, User user);
    Optional<Label> findByNameAndUser(String name, User user);
}
