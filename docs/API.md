# 📡 Medvora — API Reference

> **Status:** 🔜 *The application service is under development. This document defines the planned API contract.*

This document describes the REST API endpoints that the Spring Boot application service will expose for the desktop client.

---

## Table of Contents

- [Overview](#overview)
- [Base URL](#base-url)
- [Authentication](#authentication)
- [Endpoints](#endpoints)
  - [Auth](#auth)
  - [Users](#users)
  - [Medicines](#medicines)
  - [Batches](#batches)
  - [Stock Movements](#stock-movements)
  - [Purchase Orders](#purchase-orders)
  - [Suppliers](#suppliers)
  - [Alerts](#alerts)
- [Error Responses](#error-responses)
- [Data Models](#data-models)

---

## Overview

| Property | Value |
|:---------|:------|
| **Protocol** | HTTPS (HTTP for local dev) |
| **Format** | JSON (`application/json`) |
| **Auth** | Bearer Token (JWT) |
| **API Version** | v1 |

---

## Base URL

```
# Local development
http://localhost:8080/api/v1

# Production (TBD)
https://api.medvora.example.com/api/v1
```

---

## Authentication

All endpoints except `/auth/login` require a valid JWT Bearer token.

**Header format:**
```
Authorization: Bearer <jwt-token>
```

---

## Endpoints

### Auth

| Method | Endpoint | Description | Auth Required |
|:-------|:---------|:------------|:--------------|
| `POST` | `/auth/login` | Authenticate user, receive JWT | ❌ |
| `POST` | `/auth/logout` | Invalidate current session | ✅ |
| `GET` | `/auth/me` | Get current user profile | ✅ |

#### `POST /auth/login`

**Request:**
```json
{
  "username": "pharmacist_01",
  "password": "secure_password"
}
```

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "username": "pharmacist_01",
    "fullName": "Jane Nalubega",
    "role": "PHARMACIST",
    "facilityId": 101
  }
}
```

---

### Users

| Method | Endpoint | Description | Roles |
|:-------|:---------|:------------|:------|
| `GET` | `/users` | List all users | ADMIN |
| `POST` | `/users` | Create a new user | ADMIN |
| `GET` | `/users/{id}` | Get user by ID | ADMIN |
| `PUT` | `/users/{id}` | Update user details | ADMIN |
| `DELETE` | `/users/{id}` | Deactivate user | ADMIN |

---

### Medicines

| Method | Endpoint | Description | Roles |
|:-------|:---------|:------------|:------|
| `GET` | `/medicines` | List all medicines | ALL |
| `POST` | `/medicines` | Add new medicine | ADMIN, PHARMACIST |
| `GET` | `/medicines/{id}` | Get medicine details | ALL |
| `PUT` | `/medicines/{id}` | Update medicine | ADMIN, PHARMACIST |

#### `GET /medicines`

**Query Parameters:**
| Param | Type | Description |
|:------|:-----|:------------|
| `search` | string | Filter by name or generic name |
| `category` | string | Filter by category |
| `page` | int | Page number (default: 0) |
| `size` | int | Page size (default: 20) |

**Response `200 OK`:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Amoxicillin 500mg",
      "genericName": "Amoxicillin",
      "category": "ANTIBIOTIC",
      "unit": "CAPSULES",
      "reorderLevel": 100,
      "currentStock": 450
    }
  ],
  "totalElements": 87,
  "totalPages": 5,
  "currentPage": 0
}
```

---

### Batches

| Method | Endpoint | Description | Roles |
|:-------|:---------|:------------|:------|
| `GET` | `/batches` | List batches (FEFO ordered) | ALL |
| `POST` | `/batches` | Register a new batch | ADMIN, STORE_OFFICER |
| `GET` | `/batches/{id}` | Get batch details | ALL |
| `PUT` | `/batches/{id}/quarantine` | Quarantine a batch | ADMIN, PHARMACIST |

#### `POST /batches`

**Request:**
```json
{
  "medicineId": 1,
  "batchNumber": "BATCH-2026-0042",
  "quantity": 500,
  "manufactureDate": "2026-03-15",
  "expiryDate": "2028-03-15",
  "supplierId": 5,
  "purchaseOrderId": 12,
  "storeLocation": "MAIN_PHARMACY"
}
```

---

### Stock Movements

| Method | Endpoint | Description | Roles |
|:-------|:---------|:------------|:------|
| `GET` | `/movements` | List stock movements | ALL |
| `POST` | `/movements/dispense` | Record a dispense (FEFO) | PHARMACIST |
| `POST` | `/movements/transfer` | Transfer between stores | STORE_OFFICER |
| `POST` | `/movements/adjust` | Record stock adjustment | ADMIN |

#### `POST /movements/dispense`

**Request:**
```json
{
  "medicineId": 1,
  "quantity": 30,
  "dispensedTo": "Ward B - Paediatrics",
  "notes": "Monthly ward replenishment"
}
```

> The system automatically selects batches in **First-Expiry-First-Out** order.

---

### Purchase Orders

| Method | Endpoint | Description | Roles |
|:-------|:---------|:------------|:------|
| `GET` | `/orders` | List purchase orders | ALL |
| `POST` | `/orders` | Create purchase order | ADMIN, STORE_OFFICER |
| `PUT` | `/orders/{id}/approve` | Approve order | ADMIN |
| `PUT` | `/orders/{id}/receive` | Mark as received | STORE_OFFICER |
| `PUT` | `/orders/{id}/cancel` | Cancel order | ADMIN |

---

### Suppliers

| Method | Endpoint | Description | Roles |
|:-------|:---------|:------------|:------|
| `GET` | `/suppliers` | List all suppliers | ALL |
| `POST` | `/suppliers` | Add new supplier | ADMIN |
| `PUT` | `/suppliers/{id}` | Update supplier | ADMIN |

---

### Alerts

| Method | Endpoint | Description | Roles |
|:-------|:---------|:------------|:------|
| `GET` | `/alerts` | List active alerts | ALL |
| `GET` | `/alerts/expiry` | Get expiry warnings | ALL |
| `GET` | `/alerts/low-stock` | Get low-stock warnings | ALL |
| `PUT` | `/alerts/{id}/dismiss` | Dismiss an alert | ALL |

---

## Error Responses

All errors follow a consistent format:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Username is required",
  "timestamp": "2026-10-05T16:00:00Z",
  "path": "/api/v1/auth/login"
}
```

### HTTP Status Codes

| Code | Meaning |
|:-----|:--------|
| `200` | Success |
| `201` | Resource created |
| `400` | Bad request (validation error) |
| `401` | Unauthorized (invalid/missing token) |
| `403` | Forbidden (insufficient role) |
| `404` | Resource not found |
| `409` | Conflict (duplicate entry) |
| `500` | Internal server error |

---

## Data Models

### User Roles

| Role | Permissions |
|:-----|:------------|
| `ADMIN` | Full system access, user management |
| `PHARMACIST` | Dispensing, medicine management, stock views |
| `STORE_OFFICER` | Receiving, transfers, purchase orders |
| `VIEWER` | Read-only access to reports |

### Stock Status

| Status | Description |
|:-------|:------------|
| `AVAILABLE` | Usable floor stock, not expired |
| `QUARANTINED` | Under review, not available for dispensing |
| `EXPIRED` | Past expiry date, awaiting disposal |
| `DISPOSED` | Removed from inventory |

### Movement Types

| Type | Description |
|:-----|:------------|
| `RECEIVE` | Goods received from supplier |
| `DISPENSE` | Dispensed to ward or patient |
| `TRANSFER` | Moved between storage locations |
| `ADJUST` | Stock count adjustment |
| `DISPOSE` | Expired/damaged stock removed |

---

<div align="center">

*This API specification is a living document and will be updated as endpoints are implemented.*

</div>
