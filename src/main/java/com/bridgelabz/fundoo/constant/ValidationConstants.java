package com.bridgelabz.fundoo.constant;

public final class ValidationConstants {

    private ValidationConstants() {}

    // Regex patterns
    public static final String EMAIL_REGEX    = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
    public static final String PHONE_REGEX    = "^[6-9]\\d{9}$";
    public static final String NAME_REGEX     = "^[A-Za-z]{2,50}$";

    //Field size limits
    public static final int NAME_MIN         = 3;
    public static final int NAME_MAX         = 50;
    public static final int PASSWORD_MIN     = 8;
    public static final int PASSWORD_MAX     = 64;
    public static final int NOTE_TITLE_MAX   = 200;
    public static final int NOTE_DESC_MAX    = 5000;
    public static final int LABEL_NAME_MAX   = 50;
    public static final int COLOR_MAX        = 20;

    // Validation messages (used in @Pattern, @Size annotations)
    public static final String EMAIL_INVALID    = "Please provide a valid email address.";
    public static final String PASSWORD_INVALID = "Password must be 8+ chars with uppercase, lowercase, digit and special character.";
    public static final String PHONE_INVALID    = "Please provide a valid 10-digit Indian mobile number.";
    public static final String NAME_INVALID     = "Name must be 2–50 alphabetic characters.";
    public static final String FIELD_REQUIRED   = "This field is required.";
}