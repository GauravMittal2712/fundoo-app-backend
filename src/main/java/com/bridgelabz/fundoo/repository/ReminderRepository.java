package com.bridgelabz.fundoo.repository;
import com.bridgelabz.fundoo.entity.Note;
import com.bridgelabz.fundoo.entity.Reminder;
import com.bridgelabz.fundoo.entity.enums.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder,Long> {
    List<Reminder> findAllByNote(Note note);
    List<Reminder> findAllByStatusAndRemindAtBeforeAndNotifiedFalse(ReminderStatus status, LocalDateTime dateTime);
}
