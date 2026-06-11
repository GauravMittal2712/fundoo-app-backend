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
        name = "verification_tokens",
        indexes = {
                @Index(
                        name = "idx_verification_token",
                        columnList = "token"
                )
        }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class VerificationToken extends AbstractToken {
}