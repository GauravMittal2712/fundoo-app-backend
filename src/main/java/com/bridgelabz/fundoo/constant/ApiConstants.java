package com.bridgelabz.fundoo.constant;

public final class ApiConstants {

    private ApiConstants() {}

    public static final String BASE_URL = "/api";
    public static final String VERSION = "/v1";
    public static final String API_PREFIX = BASE_URL + VERSION;

    public static final String USERS                = API_PREFIX + "/users";
    public static final String USER_REGISTER        = USERS + "/register";
    public static final String USER_LOGIN           = USERS + "/login";
    public static final String USER_BY_ID           = USERS + "/{userId}";
    public static final String USER_FORGOT_PASSWORD = USERS + "/forgot-password";
    public static final String USER_RESET_PASSWORD  = USERS + "/reset-password";

    public static final String NOTES            = API_PREFIX + "/notes";
    public static final String NOTE_BY_ID       = NOTES + "/{noteId}";
    public static final String NOTE_SEARCH      = NOTES + "/search";
    public static final String NOTE_PIN         = NOTE_BY_ID + "/pin";
    public static final String NOTE_ARCHIVE     = NOTE_BY_ID + "/archive";
    public static final String NOTE_TRASH       = NOTE_BY_ID + "/trash";
    public static final String NOTE_COLOR       = NOTE_BY_ID + "/color";

    public static final String LABELS               = API_PREFIX + "/labels";
    public static final String LABEL_BY_ID          = LABELS + "/{labelId}";
    public static final String LABEL_NOTES          = LABEL_BY_ID + "/notes";
    public static final String NOTE_LABEL           = NOTE_BY_ID + "/labels/{labelId}";

    public static final String NOTE_COLLABORATORS   = NOTE_BY_ID + "/collaborators";
    public static final String COLLABORATOR_BY_ID   = API_PREFIX + "/collaborators/{collaboratorId}";
    public static final String NOTE_COLLABORATOR_BY_ID = NOTE_COLLABORATORS + "/{collaboratorId}";

    public static final String NOTE_REMINDERS       = NOTE_BY_ID + "/reminders";
    public static final String REMINDER_BY_ID       = API_PREFIX + "/reminders/{reminderId}";
    public static final String REMINDER_SNOOZE      = REMINDER_BY_ID + "/snooze";

}
