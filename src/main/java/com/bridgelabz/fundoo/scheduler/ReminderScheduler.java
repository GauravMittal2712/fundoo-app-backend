package com.bridgelabz.fundoo.scheduler;

import com.bridgelabz.fundoo.entity.Reminder;
import com.bridgelabz.fundoo.entity.enums.ReminderStatus;
import com.bridgelabz.fundoo.messaging.UserEventProducer;
import com.bridgelabz.fundoo.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {

    private final ReminderRepository reminderRepository;
    private final UserEventProducer eventProducer;

    @Scheduled(cron = "0 * * * * *") // Runs every minute at the 00th second
    @Transactional
    public void processPendingReminders() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Running scheduled check for due reminders at {}", now);

        List<Reminder> dueReminders = reminderRepository.findAllByStatusAndRemindAtBeforeAndNotifiedFalse(
                ReminderStatus.PENDING, now
        );

        if (dueReminders.isEmpty()) {
            log.debug("No due reminders found");
            return;
        }

        log.info("Found {} due reminders to process", dueReminders.size());

        for (Reminder reminder : dueReminders) {
            try {
                String ownerEmail = reminder.getNote().getOwner().getEmail();
                String noteTitle = reminder.getNote().getTitle();
                
                String alertMessage = String.format(
                        "{\"reminderId\": %d, \"noteId\": %d, \"title\": \"%s\", \"ownerEmail\": \"%s\", \"remindAt\": \"%s\"}",
                        reminder.getId(), reminder.getNote().getId(), noteTitle, ownerEmail, reminder.getRemindAt()
                );

                // Publish to Kafka reminder-alerts topic
                eventProducer.sendEvent("reminder-alerts", ownerEmail, alertMessage);

                // Update status to prevent duplicated notifications
                reminder.setNotified(true);
                reminder.setStatus(ReminderStatus.COMPLETED);
                reminderRepository.save(reminder);

                log.info("Successfully triggered alert for reminder ID {}", reminder.getId());
            } catch (Exception e) {
                log.error("Error processing reminder ID {}: {}", reminder.getId(), e.getMessage());
            }
        }
    }
}
