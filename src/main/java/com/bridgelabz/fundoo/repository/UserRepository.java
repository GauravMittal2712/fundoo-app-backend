package com.bridgelabz.fundoo.repository;

import com.bridgelabz.fundoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
