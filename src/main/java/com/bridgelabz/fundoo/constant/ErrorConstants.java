package com.bridgelabz.fundoo.constant;

public final class ErrorConstants {

    private ErrorConstants() {}


    public static final String ERR_NOT_FOUND       = "Not Found";
    public static final String ERR_USER_NOT_FOUND  = "User Not Found";
    public static final String ERR_NOTE_NOT_FOUND  = "Note Not Found";
    public static final String ERR_LABEL_NOT_FOUND = "Label Not Found";
    public static final String ERR_COLLAB_NOT_FOUND= "Collaborator Not Found";
    public static final String ERR_REMINDER_NOT_FOUND = "Reminder Not Found";
    public static final String ERR_BAD_REQUEST     = "Bad Request";
    public static final String ERR_VALIDATION      = "Validation Failed";
    public static final String ERR_CONFLICT        = "Conflict";
    public static final String ERR_UNAUTHORIZED    = "Unauthorized";
    public static final String ERR_FORBIDDEN       = "Forbidden";
    public static final String ERR_INTERNAL        = "Internal Server Error";

    public static final String USER_NOT_FOUND         = "User not found with the given ID.";
    public static final String NOTE_NOT_FOUND         = "Note not found with the given ID.";
    public static final String LABEL_NOT_FOUND        = "Label not found with the given ID.";
    public static final String COLLABORATOR_NOT_FOUND = "Collaborator not found.";
    public static final String REMINDER_NOT_FOUND     = "Reminder not found with the given ID.";
    public static final String EMAIL_ALREADY_EXISTS   = "An account with this email already exists.";
    public static final String INVALID_INPUT          = "Invalid input. Please check the request body.";
    public static final String UNAUTHORIZED_ACCESS    = "Unauthorized. Please log in to continue.";
    public static final String ACCESS_DENIED          = "You do not have permission to perform this action.";
    public static final String INTERNAL_SERVER_ERROR  = "An unexpected error occurred. Please try again.";
}