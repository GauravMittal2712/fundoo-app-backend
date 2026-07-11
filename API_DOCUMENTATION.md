# Fundoo Notes - REST API Integration Guide

This document provides the complete API specification for the **Fundoo Notes Backend**. It is designed specifically for the frontend team to integrate client applications (Web/Mobile) with the backend services.

---

## 1. Global Specifications & Authentication

### Protocol & Formats
* **Protocol:** HTTP/1.1 or HTTP/2 over HTTPS
* **Default Port:** `8080` (configurable via `server.port`)
* **Content-Type:** `application/json` (UTF-8)
* **Timestamp Format:** ISO 8601 (`YYYY-MM-DDThh:mm:ss.sss`)

### Authentication Mechanism
All secured endpoints require authentication via a **JSON Web Token (JWT)** passed in the HTTP Authorization header.
* **Header Name:** `Authorization`
* **Format:** `Bearer <JWT_TOKEN>`
* **Example:** `Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

> [!IMPORTANT]
> Non-authenticated requests to secured endpoints will return a `401 Unauthorized` response. Keep the token securely stored in client memory or secure HTTPS-only cookies.

---

## 2. Standard Response Schemas

All API responses follow a unified structure for predictability.

### A. Success Response (`APIResponse<T>`)
```json
{
  "statusCode": 200,
  "message": "Operation completed successfully.",
  "data": { ... },
  "timestamp": "2026-07-01T10:15:30.123456"
}
```
* **`statusCode`**: Match standard HTTP status codes (200, 201, 204).
* **`message`**: Readable operation success message.
* **`data`**: Payload object, array, or `null` for void actions.
* **`timestamp`**: Time when the response was processed on the server.

### B. Error Response (`ErrorResponse`)
```json
{
  "statusCode": 400,
  "error": "Validation Failed",
  "message": "{email=Please provide a valid email address.}",
  "path": "/api/v1/users/register",
  "timestamp": "2026-07-01T10:16:45.987654"
}
```
* **`statusCode`**: Standard HTTP status error code (400, 401, 403, 404, 409, 500).
* **`error`**: Error classification (e.g., `Not Found`, `Conflict`, `Validation Failed`).
* **`message`**: Details of the error. For validation failures, it returns a stringified key-value mapping of invalid request fields.
* **`path`**: Request URI path that triggered the error.

---

## 3. Input Validation Rules
Refer to the following bounds when designing form validations on the client side:

| Field | Validation Type | Constraint / Rule | Validation Error Message |
| :--- | :--- | :--- | :--- |
| **First/Last Name** | Regex / Size | `^[A-Za-z]{2,50}$`, Min: 3, Max: 50 | *Name must be 2–50 alphabetic characters.* |
| **Email** | Regex | `^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$` | *Please provide a valid email address.* |
| **Password** | Regex / Size | At least 1 uppercase letter, 1 lowercase, 1 digit, 1 special character (`@#$%^&+=!`), Min: 8, Max: 64 | *Password must be 8+ chars with uppercase, lowercase, digit and special character.* |
| **Phone Number** | Regex | `^[6-9]\d{9}$` (Indian 10-digit format) or empty | *Please provide a valid 10-digit Indian mobile number.* |
| **Note Title** | Size | Max: 200 characters | *Title must be less than 200 characters* |
| **Note Description**| Size | Max: 5000 characters | *Description must be less than 5000 characters* |
| **Note Color** | Size | Max: 20 characters (Hex value recommended, e.g., `#ffffff`) | *Color must be less than 20 characters* |
| **Label Name** | Size | Max: 50 characters | *Label name must be less than 50 characters* |

---

## 4. REST API Endpoint Index

### A. Authentication & User Management (`/api/v1/users`)
| Method | Endpoint | Auth | Description |
| :--- | :--- | :---: | :--- |
| `POST` | `/api/v1/users/register` | Public | Register a new user account |
| `POST` | `/api/v1/users/login` | Public | Log in and receive JWT token |
| `GET` | `/api/v1/users/{userId}` | Admin | Get details of a user by ID |
| `GET` | `/api/v1/users` | Admin | Get list of all users |
| `PUT` | `/api/v1/users/{userId}` | Admin | Update user registration details |
| `DELETE`| `/api/v1/users/{userId}` | Admin | Mark user as deleted |
| `POST` | `/api/v1/users/forgot-password` | Public | Send password reset token to email |
| `POST` | `/api/v1/users/reset-password` | Public | Reset password using token |

### B. Notes Management (`/api/v1/notes`)
| Method | Endpoint | Auth | Description |
| :--- | :--- | :---: | :--- |
| `POST` | `/api/v1/notes` | User | Create a new note |
| `GET` | `/api/v1/notes` | User | Get all notes belonging to the authenticated user |
| `GET` | `/api/v1/notes/{noteId}` | User | Fetch note by ID |
| `PUT` | `/api/v1/notes/{noteId}` | User | Update title, description, color, states of a note |
| `DELETE`| `/api/v1/notes/{noteId}` | User | Permanent delete or soft delete depending on trash state |
| `GET` | `/api/v1/notes/search` | User | Search note title/description by matching query |
| `PATCH`| `/api/v1/notes/{noteId}/pin` | User | Toggle pinned status (true/false) |
| `PATCH`| `/api/v1/notes/{noteId}/archive`| User | Toggle archived status (true/false) |
| `PATCH`| `/api/v1/notes/{noteId}/trash` | User | Toggle trashed status (true/false) |
| `PATCH`| `/api/v1/notes/{noteId}/color` | User | Update background hex color of the note |

### C. Labels Management (`/api/v1/labels`)
| Method | Endpoint | Auth | Description |
| :--- | :--- | :---: | :--- |
| `POST` | `/api/v1/labels` | User | Create a new custom label |
| `GET` | `/api/v1/labels` | User | Get all labels of the authenticated user |
| `GET` | `/api/v1/labels/{labelId}` | User | Get label details by ID |
| `PUT` | `/api/v1/labels/{labelId}` | User | Update label name |
| `DELETE`| `/api/v1/labels/{labelId}` | User | Delete a label and unlink it from all notes |
| `POST` | `/api/v1/labels/notes/{noteId}/labels/{labelId}` | User | Link a label to a note |
| `DELETE`| `/api/v1/labels/notes/{noteId}/labels/{labelId}` | User | Unlink a label from a note |
| `GET` | `/api/v1/labels/{labelId}/notes` | User | Fetch all notes linked to a specific label |

### D. Collaborators Management (`/api/v1/notes/{noteId}/collaborators` & `/api/v1/collaborators`)
| Method | Endpoint | Auth | Description |
| :--- | :--- | :---: | :--- |
| `POST` | `/api/v1/notes/{noteId}/collaborators` | User | Add collaborator to a note |
| `GET` | `/api/v1/notes/{noteId}/collaborators` | User | Fetch all collaborators on a note |
| `GET` | `/api/v1/collaborators/{collaboratorId}` | User | Fetch individual collaborator entry |
| `DELETE`| `/api/v1/notes/{noteId}/collaborators/{collaboratorId}` | User | Remove collaborator from a note |

### E. Reminders Management (`/api/v1/notes/{noteId}/reminders` & `/api/v1/reminders`)
| Method | Endpoint | Auth | Description |
| :--- | :--- | :---: | :--- |
| `POST` | `/api/v1/notes/{noteId}/reminders` | User | Add a reminder schedule to a note |
| `GET` | `/api/v1/notes/{noteId}/reminders` | User | Get all reminders configured on a note |
| `GET` | `/api/v1/reminders/{reminderId}` | User | Fetch individual reminder info by ID |
| `PUT` | `/api/v1/reminders/{reminderId}` | User | Reschedule a reminder |
| `DELETE`| `/api/v1/reminders/{reminderId}` | User | Delete/remove a reminder |
| `PATCH`| `/api/v1/reminders/{reminderId}/snooze` | User | Snooze reminder by a specified duration |

---

## 5. Detailed Endpoint Schemas

### A. Authentication & User Management

#### 1. Register User
* **Endpoint:** `POST /api/v1/users/register`
* **Security:** Public (No authorization header)
* **Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "johndoe@example.com",
  "password": "Password@123",
  "phoneNumber": "9876543210"
}
```
* **Response Body (`201 Created`):**
```json
{
  "statusCode": 201,
  "message": "User registered successfully.",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "johndoe@example.com",
    "phoneNumber": "9876543210",
    "role": "ROLE_USER",
    "active": true,
    "verified": false,
    "createdAt": "2026-07-01T10:00:00.123",
    "updatedAt": "2026-07-01T10:00:00.123"
  },
  "timestamp": "2026-07-01T10:00:00.123"
}
```
* **Error Response Sample (`409 Conflict` - Email Exists):**
```json
{
  "statusCode": 409,
  "error": "Conflict",
  "message": "An account with this email already exists.",
  "path": "/api/v1/users/register",
  "timestamp": "2026-07-01T10:00:05.123"
}
```

#### 2. Log In User
* **Endpoint:** `POST /api/v1/users/login`
* **Security:** Public
* **Request Body:**
```json
{
  "email": "johndoe@example.com",
  "password": "Password@123"
}
```
* **Response Body (`200 OK`):**
```json
{
  "statusCode": 200,
  "message": "Login successful.",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huZG9lQGV4YW1wbGUuY29tIiwiaWF0IjoxNz...[JWT token truncated]",
    "email": "johndoe@example.com",
    "firstName": "John",
    "lastName": "Doe"
  },
  "timestamp": "2026-07-01T10:01:00.123"
}
```

#### 3. Forgot Password
* **Endpoint:** `POST /api/v1/users/forgot-password`
* **Security:** Public
* **Request Body:**
```json
{
  "email": "johndoe@example.com"
}
```
* **Response Body (`200 OK`):**
```json
{
  "statusCode": 200,
  "message": "Password reset link sent to your email.",
  "timestamp": "2026-07-01T10:02:00.123"
}
```

#### 4. Reset Password
* **Endpoint:** `POST /api/v1/users/reset-password`
* **Security:** Public
* **Request Body:**
```json
{
  "token": "b911c479-7a56-42d7-b8db-000000000000",
  "newPassword": "NewPassword@999"
}
```
* **Response Body (`200 OK`):**
```json
{
  "statusCode": 200,
  "message": "Password has been reset successfully.",
  "timestamp": "2026-07-01T10:03:00.123"
}
```

---

### B. Notes Management

#### 1. Create Note
* **Endpoint:** `POST /api/v1/notes`
* **Security:** Authenticated
* **Request Body:**
```json
{
  "title": "Project Meeting",
  "description": "Discuss architecture and UI design components with the frontend team.",
  "color": "#ff9999",
  "pinned": true,
  "archived": false
}
```
* **Response Body (`201 Created`):**
```json
{
  "statusCode": 201,
  "message": "Note created successfully.",
  "data": {
    "id": 12,
    "title": "Project Meeting",
    "description": "Discuss architecture and UI design components with the frontend team.",
    "color": "#ff9999",
    "pinned": true,
    "archived": false,
    "trashed": false,
    "ownerId": 1,
    "ownerEmail": "johndoe@example.com",
    "labels": [],
    "reminders": [],
    "collaborators": [],
    "createdAt": "2026-07-01T10:05:00.123",
    "updatedAt": "2026-07-01T10:05:00.123"
  },
  "timestamp": "2026-07-01T10:05:00.123"
}
```

#### 2. Get All Notes for User
* **Endpoint:** `GET /api/v1/notes`
* **Security:** Authenticated
* **Response Body (`200 OK`):**
```json
{
  "statusCode": 200,
  "message": "All notes fetched successfully.",
  "data": [
    {
      "id": 12,
      "title": "Project Meeting",
      "description": "Discuss architecture and UI design components with the frontend team.",
      "color": "#ff9999",
      "pinned": true,
      "archived": false,
      "trashed": false,
      "ownerId": 1,
      "ownerEmail": "johndoe@example.com",
      "labels": [
        {
          "id": 3,
          "name": "Work",
          "userId": 1,
          "createdAt": "2026-07-01T09:00:00.000",
          "updatedAt": "2026-07-01T09:00:00.000"
        }
      ],
      "reminders": [
        {
          "id": 5,
          "noteId": 12,
          "remindAt": "2026-07-01T15:00:00.000",
          "status": "PENDING",
          "notified": false,
          "createdAt": "2026-07-01T10:05:10.000",
          "updatedAt": "2026-07-01T10:05:10.000"
        }
      ],
      "collaborators": [
        {
          "id": 2,
          "noteId": 12,
          "userId": 4,
          "userEmail": "collaborator@example.com",
          "userFirstName": "Alice",
          "userLastName": "Smith",
          "role": "EDITOR",
          "createdAt": "2026-07-01T10:06:00.000",
          "updatedAt": "2026-07-01T10:06:00.000"
        }
      ],
      "createdAt": "2026-07-01T10:05:00.123",
      "updatedAt": "2026-07-01T10:06:00.123"
    }
  ],
  "timestamp": "2026-07-01T10:10:00.123"
}
```

#### 3. Update Note
* **Endpoint:** `PUT /api/v1/notes/{noteId}`
* **Security:** Authenticated
* **Request Body:**
```json
{
  "title": "Meeting (Rescheduled)",
  "description": "Moved meeting to discuss UI assets.",
  "color": "#ffffff",
  "pinned": false,
  "archived": false,
  "trashed": false
}
```
* **Response Body (`200 OK`):**
```json
{
  "statusCode": 200,
  "message": "Note updated successfully.",
  "data": {
    "id": 12,
    "title": "Meeting (Rescheduled)",
    "description": "Moved meeting to discuss UI assets.",
    "color": "#ffffff",
    "pinned": false,
    "archived": false,
    "trashed": false,
    "ownerId": 1,
    "ownerEmail": "johndoe@example.com",
    "labels": [],
    "reminders": [],
    "collaborators": [],
    "createdAt": "2026-07-01T10:05:00.123",
    "updatedAt": "2026-07-01T10:12:00.123"
  },
  "timestamp": "2026-07-01T10:12:00.123"
}
```

#### 4. Search Notes
* **Endpoint:** `GET /api/v1/notes/search?query=meeting`
* **Security:** Authenticated
* **Response Body (`200 OK`):**
*Matches title or description text (case-insensitive).*
```json
{
  "statusCode": 200,
  "message": "Search results fetched successfully.",
  "data": [ ... ],
  "timestamp": "2026-07-01T10:13:00.123"
}
```

#### 5. Patch Toggles (Pin / Archive / Trash / Color)
* **Pin Toggle:** `PATCH /api/v1/notes/{noteId}/pin`
* **Archive Toggle:** `PATCH /api/v1/notes/{noteId}/archive`
* **Trash Toggle:** `PATCH /api/v1/notes/{noteId}/trash`
* **Update Color:** `PATCH /api/v1/notes/{noteId}/color?color=%23ff00ff`
* **Security:** Authenticated
* **Response Body (`200 OK`):**
```json
{
  "statusCode": 200,
  "message": "Note trash status updated.",
  "data": {
    "id": 12,
    "title": "Project Meeting",
    "pinned": false,
    "archived": false,
    "trashed": true,
    ...
  },
  "timestamp": "2026-07-01T10:14:00.000"
}
```

---

### C. Labels Management

#### 1. Create Label
* **Endpoint:** `POST /api/v1/labels`
* **Security:** Authenticated
* **Request Body:**
```json
{
  "name": "Personal"
}
```
* **Response Body (`201 Created`):**
```json
{
  "statusCode": 201,
  "message": "Label created successfully.",
  "data": {
    "id": 4,
    "name": "Personal",
    "userId": 1,
    "createdAt": "2026-07-01T10:20:00.000",
    "updatedAt": "2026-07-01T10:20:00.000"
  },
  "timestamp": "2026-07-01T10:20:00.000"
}
```

#### 2. Link Label to Note
* **Endpoint:** `POST /api/v1/labels/notes/{noteId}/labels/{labelId}`
* **Security:** Authenticated
* **Response Body (`200 OK`):**
```json
{
  "statusCode": 200,
  "message": "Label added to note successfully.",
  "timestamp": "2026-07-01T10:21:00.000"
}
```

#### 3. Unlink Label from Note
* **Endpoint:** `DELETE /api/v1/labels/notes/{noteId}/labels/{labelId}`
* **Security:** Authenticated
* **Response Body (`200 OK`):**
```json
{
  "statusCode": 200,
  "message": "Label removed from note successfully.",
  "timestamp": "2026-07-01T10:22:00.000"
}
```

---

### D. Collaborators Management

#### 1. Add Collaborator to Note
* **Endpoint:** `POST /api/v1/notes/{noteId}/collaborators`
* **Security:** Authenticated
* **Request Body:**
```json
{
  "email": "friend@example.com",
  "role": "EDITOR" 
}
```
*Values for `role`: `OWNER`, `EDITOR`, `VIEWER`, `ADMIN`*
* **Response Body (`201 Created`):**
```json
{
  "statusCode": 201,
  "message": "Collaborator added successfully.",
  "data": {
    "id": 8,
    "noteId": 12,
    "userId": 5,
    "userEmail": "friend@example.com",
    "userFirstName": "Bob",
    "userLastName": "Johnson",
    "role": "EDITOR",
    "createdAt": "2026-07-01T10:25:00.000",
    "updatedAt": "2026-07-01T10:25:00.000"
  },
  "timestamp": "2026-07-01T10:25:00.000"
}
```

---

### E. Reminders Management

#### 1. Add Reminder to Note
* **Endpoint:** `POST /api/v1/notes/{noteId}/reminders`
* **Security:** Authenticated
* **Request Body:**
```json
{
  "remindAt": "2026-07-01T18:30:00"
}
```
* **Response Body (`201 Created`):**
```json
{
  "statusCode": 201,
  "message": "Reminder added successfully.",
  "data": {
    "id": 14,
    "noteId": 12,
    "remindAt": "2026-07-01T18:30:00",
    "status": "PENDING",
    "notified": false,
    "createdAt": "2026-07-01T10:30:00.000",
    "updatedAt": "2026-07-01T10:30:00.000"
  },
  "timestamp": "2026-07-01T10:30:00.000"
}
```

#### 2. Snooze Reminder
* **Endpoint:** `PATCH /api/v1/reminders/{reminderId}/snooze?minutes=15`
* **Security:** Authenticated
* **Response Body (`200 OK`):**
```json
{
  "statusCode": 200,
  "message": "Reminder snoozed successfully.",
  "data": {
    "id": 14,
    "noteId": 12,
    "remindAt": "2026-07-01T18:45:00",
    "status": "SNOOZED",
    "notified": false,
    "createdAt": "2026-07-01T10:30:00.000",
    "updatedAt": "2026-07-01T10:35:00.000"
  },
  "timestamp": "2026-07-01T10:35:00.000"
}
```

---

## 6. Real-time Notifications & Web Sockets (Future Scope)
Currently, reminders are fired asynchronously on the backend server every minute. A notification payload is produced to Kafka under the topic `reminder-alerts`. To surface these notifications in the client:
1. Integrate a Server-Sent Events (SSE) or WebSocket endpoint (Planned for v2).
2. For now, client polling or service workers can retrieve active reminders via `GET /api/v1/notes` or check pending alarms.
