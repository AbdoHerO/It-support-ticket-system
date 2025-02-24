# IT Support Ticket System API

**Base URL**: `http://localhost:8080/api`

All requests require **Basic Auth**.  
Roles:  
- `ROLE_EMPLOYEE` (EMPLOYEE)  
- `ROLE_IT_SUPPORT` (IT_SUPPORT)  

## Authentication
Use **Basic Auth** with username and password. Example:

### `GET /api/user`
Returns the authenticated user (no password).  
**Response**:
```json
{
  "id": null,
  "username": "employee1",
  "password": null,
  "role": "EMPLOYEE"
}

## Tickets

### POST /api/tickets/create
**Role:** EMPLOYEE  
Creates a new ticket.

**Request Body** (JSON):

```json
{
  "title": "Printer is jammed",
  "description": "Cannot print any documents",
  "priority": "HIGH",
  "category": "HARDWARE"
}
```

**Response** (200 OK if success):

```json
{
  "id": 10,
  "title": "Printer is jammed",
  "description": "Cannot print any documents",
  "priority": "HIGH",
  "category": "HARDWARE",
  "creationDate": "...",
  "status": "NEW",
  "createdBy": {
    "username": "employee1",
    "role": "EMPLOYEE"
  },
  "comments": [],
  "auditLogs": []
}
```

### GET /api/tickets/my
**Role:** EMPLOYEE  
Lists all tickets created by the current authenticated user. Returns an array of `Ticket` objects.

### GET /api/tickets/all
**Role:** IT_SUPPORT  
Lists all tickets in the system (newest first).

### GET /api/tickets/{id}
**Role:** EMPLOYEE or IT_SUPPORT  
Fetch details for a single ticket.

**Response:**

```json
{
  "id": 1,
  "title": "test",
  "description": "some issue",
  "priority": "MEDIUM",
  "category": "SOFTWARE",
  "creationDate": "...",
  "status": "NEW",
  "createdBy": {...},
  "comments": [...],
  "auditLogs": [...]
}
```

### PUT /api/tickets/{id}/status?status=IN_PROGRESS
**Role:** IT_SUPPORT  
Changes the status of a ticket. Also creates an audit log entry.

### GET /api/tickets/search?id=1&status=NEW
Search by ticket ID and/or status. The result is an array of matching `Ticket` objects.

### GET /api/tickets/{id}/audit
**Role:** IT_SUPPORT  
Returns the audit logs for a specific ticket.

## Comments

### POST /api/tickets/{ticketId}/comments
**Role:** IT_SUPPORT  
Adds a comment to a ticket.

**Request Body** (plain string in JSON quotes, e.g. `"My comment"`)

### GET /api/tickets/{ticketId}/comments
**Role:** EMPLOYEE or IT_SUPPORT  
Fetch all comments for a ticket.
