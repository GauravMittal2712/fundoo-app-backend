package com.bridgelabz.fundoo.repository;
import com.bridgelabz.fundoo.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder,Long> {
}
