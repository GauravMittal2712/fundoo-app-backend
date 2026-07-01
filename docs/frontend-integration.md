# Fundoo Frontend Integration Guide

This guide is designed for the frontend development team to integrate with the Fundoo Backend REST APIs.

---

## 🔌 Connection Reference

* **Base URL**: `http://localhost:8080`
* **API Version Path**: `/api/v1`
* **Full API Entry Point**: `http://localhost:8080/api/v1`

---

## 🔒 CORS Settings
The backend has been configured to accept requests from standard frontend development ports:
- **Allowed HTTP Origins**: `http://localhost:3000`, `http://localhost:5173`, `http://localhost:8080`, `http://localhost:8081`
- **Allowed HTTP Methods**: `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`
- **Allowed Headers**: `Authorization`, `Content-Type`, `Cache-Control`, `Accept`
- **Exposed Headers**: `Authorization`
- **Credentials Support**: `true` (enables cookies and auth headers)

---

## 🔑 Authentication Strategy (JWT)

Fundoo uses **stateless JWT-based security**. 

### 1. User Sign-up
- **Endpoint**: `POST /api/v1/users/register`
- **Payload**:
  ```json
  {
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "Password@123",
    "phoneNumber": "9876543210"
  }
  ```

### 2. Login & Token Retrieval
- **Endpoint**: `POST /api/v1/users/login`
- **Payload**:
  ```json
  {
    "email": "john.doe@example.com",
    "password": "Password@123"
  }
  ```
- **Response**:
  ```json
  {
    "statusCode": 200,
    "message": "User login successful",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "email": "john.doe@example.com"
    },
    "timestamp": "2026-07-01T10:00:00"
  }
  ```

### 3. Adding Authorization Header to Requests
For all secured endpoints (Notes, Labels, Collaborators, Reminders), you **must** attach the retrieved token in the `Authorization` request header:
```http
Authorization: Bearer <your_jwt_token_here>
```

---

## 📬 Standard API Response Envelopes

### Success Envelope (`APIResponse<T>`)
Every successful request returns a structured response matching this format:
```json
{
  "statusCode": 201,
  "message": "Note created successfully",
  "data": {
    "id": 1,
    "title": "My Note",
    "description": "This is a note description.",
    "color": "#ffffff",
    "pinned": false,
    "archived": false,
    "trashed": false,
    "createdAt": "2026-07-01T10:00:00",
    "updatedAt": "2026-07-01T10:00:00"
  },
  "timestamp": "2026-07-01T10:00:00"
}
```

### Error Envelope (`ErrorResponse`)
Requests that fail due to validation errors, invalid parameters, or security access exceptions return this format:
```json
{
  "statusCode": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/users/register",
  "timestamp": "2026-07-01T10:00:00"
}
```

---

## 📋 Comprehensive API Endpoints Reference

### 1. User & Profile Management (`/api/v1/users`)
*Public endpoints do not require authorization headers.*

| Method | Endpoint | Auth | Description | Payload Schema |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/register` | Public | Register new user | [RegisterRequestDto](#registerrequestdto) |
| **POST** | `/login` | Public | Log in to get token | [LoginRequestDto](#loginrequestdto) |
| **POST** | `/forgot-password` | Public | Trigger forgot password mail/token | [ForgotPasswordDto](#forgotpassworddto) |
| **POST** | `/reset-password` | Public | Reset password with token | [ResetPasswordDto](#resetpassworddto) |
| **GET** | `/{id}` | Secured | Get profile details | None |
| **PUT** | `/{id}` | Secured | Update profile details | [RegisterRequestDto](#registerrequestdto) |
| **DELETE** | `/{id}` | Secured | Delete user account | None |

---

### 2. Notes Management (`/api/v1/notes`)
*All endpoints below require a valid JWT header (`Authorization: Bearer <token>`).*

| Method | Endpoint | Query Parameters | Description | Payload Schema |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/` | None | Create a new note | [NoteRequestDto](#noterequestdto) |
| **GET** | `/` | None | Retrieve user's notes | None |
| **GET** | `/{noteId}` | None | Get note by ID | None |
| **PUT** | `/{noteId}` | None | Update note title / details | [NoteRequestDto](#noterequestdto) |
| **DELETE** | `/{noteId}` | None | Permanently delete note (if trashed) | None |
| **GET** | `/search` | `query` (string) | Search notes by keyword | None |
| **PATCH** | `/{noteId}/pin` | None | Toggle pin/unpin status | None |
| **PATCH** | `/{noteId}/archive` | None | Toggle archive/unarchive status | None |
| **PATCH** | `/{noteId}/trash` | None | Move to trash / restore | None |
| **PATCH** | `/{noteId}/color` | `color` (string) | Update note background color | None |

---

### 3. Labels Management (`/api/v1/labels`)
*All endpoints below require a valid JWT header.*

| Method | Endpoint | Description | Payload Schema |
| :--- | :--- | :--- | :--- |
| **POST** | `/` | Create a new label | [LabelRequestDto](#labelrequestdto) |
| **GET** | `/` | Retrieve all user labels | None |
| **GET** | `/{labelId}` | Get label details | None |
| **PUT** | `/{labelId}` | Rename label | [LabelRequestDto](#labelrequestdto) |
| **DELETE** | `/{labelId}` | Delete label | None |
| **POST** | `/notes/{noteId}/labels/{labelId}` | Assign label to note | None |
| **DELETE** | `/notes/{noteId}/labels/{labelId}` | Remove label from note | None |
| **GET** | `/{labelId}/notes` | Get all notes mapped to label | None |

---

### 4. Collaborator Management
*All endpoints below require a valid JWT header.*

| Method | Endpoint | Description | Payload Schema |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/v1/notes/{noteId}/collaborators` | Add collaborator to note | [CollaboratorRequestDto](#collaboratorrequestdto) |
| **GET** | `/api/v1/notes/{noteId}/collaborators` | List note collaborators | None |
| **GET** | `/api/v1/collaborators/{collaboratorId}` | Fetch collaborator details | None |
| **DELETE** | `/api/v1/notes/{noteId}/collaborators/{collaboratorId}` | Remove collaborator from note | None |

---

### 5. Reminder Management
*All endpoints below require a valid JWT header.*

| Method | Endpoint | Query Parameters | Description | Payload Schema |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/api/v1/notes/{noteId}/reminders` | None | Set reminder schedule | [ReminderRequestDto](#reminderrequestdto) |
| **GET** | `/api/v1/notes/{noteId}/reminders` | None | Get all note reminders | None |
| **GET** | `/api/v1/reminders/{reminderId}` | None | Get specific reminder details | None |
| **PUT** | `/api/v1/reminders/{reminderId}` | None | Update reminder schedule | [ReminderRequestDto](#reminderrequestdto) |
| **DELETE** | `/api/v1/reminders/{reminderId}` | None | Remove reminder schedule | None |
| **PATCH** | `/api/v1/reminders/{reminderId}/snooze` | `minutes` (int, default 10) | Snooze active reminder alert | None |

---

## 📦 Request Payload Schemas (DTOs)

### `RegisterRequestDto`
* **`firstName`** (String, Required): 3–50 alphabetic characters (Regex: `^[A-Za-z]{3,50}$`).
* **`lastName`** (String, Optional): 3–50 alphabetic characters.
* **`email`** (String, Required): Well-formed email address.
* **`password`** (String, Required): 8+ characters containing at least one uppercase, one lowercase, one digit, and one special character (`@#$%^&+=!`).
* **`phoneNumber`** (String, Optional): Valid 10-digit Indian mobile number (e.g. `9876543210`).

### `LoginRequestDto`
* **`email`** (String, Required): Registered email address.
* **`password`** (String, Required): Password.

### `ForgotPasswordDto`
* **`email`** (String, Required): Registered email address.

### `ResetPasswordDto`
* **`token`** (String, Required): The password reset token.
* **`newPassword`** (String, Required): 8+ characters strength-validated password.

### `NoteRequestDto`
* **`title`** (String, Required): Title of the note (Max 200 chars).
* **`description`** (String, Optional): Body of the note (Max 5000 chars).
* **`color`** (String, Optional): Hex or name string (Max 20 chars).
* **`pinned`** (Boolean, Optional): Note pin state.
* **`archived`** (Boolean, Optional): Note archive state.
* **`trashed`** (Boolean, Optional): Note soft-deleted state.

### `LabelRequestDto`
* **`name`** (String, Required): Label name (Max 50 chars).

### `CollaboratorRequestDto`
* **`email`** (String, Required): Collaborator's email.
* **`role`** (Enum, Optional): `READER` or `WRITER` (defaults to WRITER).

### `ReminderRequestDto`
* **`remindAt`** (LocalDateTime, Required): ISO Date-time string (e.g., `2026-07-01T18:00:00`).
