package com.bridgelabz.fundoo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "password_reset_tokens",
        indexes = {
                @Index(
                        name = "idx_password_reset_token",
                        columnList = "token"
                )
        }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PasswordResetToken extends AbstractToken {
}