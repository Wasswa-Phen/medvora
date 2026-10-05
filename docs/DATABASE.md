# 🗄 Medvora — Database Schema Reference

> **Status:** 🔜 *The database schema is planned. This document defines the target data model.*

This document describes the MySQL relational database schema that underpins Medvora's inventory and replenishment system.

---

## Table of Contents

- [Entity-Relationship Diagram](#entity-relationship-diagram)
- [Tables](#tables)
  - [users](#users)
  - [roles](#roles)
  - [facilities](#facilities)
  - [stores](#stores)
  - [medicines](#medicines)
  - [medicine_batches](#medicine_batches)
  - [suppliers](#suppliers)
  - [purchase_orders](#purchase_orders)
  - [purchase_order_items](#purchase_order_items)
  - [stock_movements](#stock_movements)
  - [alerts](#alerts)
  - [audit_log](#audit_log)
- [Design Principles](#design-principles)
- [Indexing Strategy](#indexing-strategy)

---

## Entity-Relationship Diagram

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│    roles     │       │    users     │       │  facilities  │
│──────────────│       │──────────────│       │──────────────│
│ id (PK)      │◄──┐   │ id (PK)      │   ┌──▶│ id (PK)      │
│ name         │   └───│ role_id (FK) │   │   │ name         │
│ description  │       │ facility_id──│───┘   │ location     │
│              │       │ username     │       │ type         │
└──────────────┘       │ password_hash│       └──────┬───────┘
                       │ full_name    │              │
                       │ is_active    │              │
                       └──────────────┘              │
                                                     │
                       ┌──────────────┐              │
                       │    stores    │              │
                       │──────────────│              │
                       │ id (PK)      │◄─────────────┘
                       │ facility_id  │
                       │ name         │
                       │ type         │
                       └──────┬───────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌──────────────┐     ┌────────────────┐    ┌───────────────┐
│  medicines   │     │medicine_batches│    │stock_movements│
│──────────────│     │────────────────│    │───────────────│
│ id (PK)      │◄────│ medicine_id(FK)│    │ id (PK)       │
│ name         │     │ id (PK)        │◄───│ batch_id (FK) │
│ generic_name │     │ batch_number   │    │ store_id (FK) │
│ category     │     │ quantity       │    │ user_id (FK)  │
│ unit         │     │ current_qty    │    │ type          │
│ reorder_level│     │ expiry_date    │    │ quantity      │
└──────────────┘     │ status         │    │ timestamp     │
                     │ store_id (FK)  │    │ notes         │
                     │ supplier_id(FK)│    └───────────────┘
                     └────────────────┘
                              ▲
                              │
                     ┌────────┴───────┐
                     │   suppliers    │
                     │────────────────│
                     │ id (PK)        │
                     │ name           │
                     │ contact_person │
                     │ phone          │
                     │ email          │
                     │ address        │
                     └────────────────┘

┌──────────────────┐          ┌──────────────┐
│ purchase_orders  │          │  audit_log   │
│──────────────────│          │──────────────│
│ id (PK)          │          │ id (PK)      │
│ supplier_id (FK) │          │ user_id (FK) │
│ created_by (FK)  │          │ entity_type  │
│ status           │          │ entity_id    │
│ order_date       │          │ action       │
│ expected_date    │          │ old_value    │
│ total_amount     │          │ new_value    │
└────────┬─────────┘          │ timestamp    │
         │                    └──────────────┘
         ▼
┌──────────────────────┐      ┌──────────────┐
│ purchase_order_items │      │    alerts     │
│──────────────────────│      │──────────────│
│ id (PK)              │      │ id (PK)      │
│ order_id (FK)        │      │ type         │
│ medicine_id (FK)     │      │ batch_id(FK) │
│ quantity_ordered     │      │ medicine_id  │
│ quantity_received    │      │ message      │
│ unit_price           │      │ is_dismissed │
└──────────────────────┘      │ created_at   │
                              └──────────────┘
```

---

## Tables

### `users`

Stores all system users with their authentication credentials and role assignments.

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Unique user identifier |
| `username` | `VARCHAR(50)` | `UNIQUE, NOT NULL` | Login username |
| `password_hash` | `VARCHAR(255)` | `NOT NULL` | BCrypt hashed password |
| `full_name` | `VARCHAR(100)` | `NOT NULL` | Display name |
| `email` | `VARCHAR(100)` | `UNIQUE` | Contact email |
| `role_id` | `BIGINT` | `FK → roles.id` | Assigned role |
| `facility_id` | `BIGINT` | `FK → facilities.id` | Assigned facility |
| `is_active` | `BOOLEAN` | `DEFAULT TRUE` | Account status |
| `created_at` | `TIMESTAMP` | `DEFAULT CURRENT_TIMESTAMP` | Account creation time |
| `updated_at` | `TIMESTAMP` | `ON UPDATE CURRENT_TIMESTAMP` | Last modification |

---

### `roles`

Defines the system roles for RBAC.

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Role identifier |
| `name` | `VARCHAR(30)` | `UNIQUE, NOT NULL` | Role code (e.g., `ADMIN`) |
| `description` | `VARCHAR(255)` | | Human-readable description |

**Seed Data:**

| id | name | description |
|:---|:-----|:------------|
| 1 | `ADMIN` | Full system administration |
| 2 | `PHARMACIST` | Dispensing and medicine management |
| 3 | `STORE_OFFICER` | Receiving, transfers, stock counts |
| 4 | `VIEWER` | Read-only reporting access |

---

### `facilities`

Represents healthcare facilities (hospitals, clinics, pharmacies).

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Facility identifier |
| `name` | `VARCHAR(100)` | `NOT NULL` | Facility name |
| `location` | `VARCHAR(255)` | | Physical address |
| `type` | `ENUM('HOSPITAL','CLINIC','PHARMACY','WAREHOUSE')` | `NOT NULL` | Facility type |

---

### `stores`

Storage locations within a facility (e.g., main pharmacy, ward store, cold room).

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Store identifier |
| `facility_id` | `BIGINT` | `FK → facilities.id` | Parent facility |
| `name` | `VARCHAR(100)` | `NOT NULL` | Store name |
| `type` | `ENUM('MAIN_PHARMACY','WARD_STORE','COLD_ROOM','WAREHOUSE')` | | Store category |

---

### `medicines`

Master catalogue of medicines managed in the system.

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Medicine identifier |
| `name` | `VARCHAR(150)` | `NOT NULL` | Brand/trade name |
| `generic_name` | `VARCHAR(150)` | `NOT NULL` | Generic/INN name |
| `category` | `VARCHAR(50)` | `NOT NULL` | Therapeutic category |
| `unit` | `VARCHAR(30)` | `NOT NULL` | Unit of measure (tablets, ml, etc.) |
| `reorder_level` | `INT` | `DEFAULT 0` | Minimum stock threshold |
| `description` | `TEXT` | | Additional details |

---

### `medicine_batches`

Tracks individual batches with expiry dates for FEFO allocation.

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Batch identifier |
| `medicine_id` | `BIGINT` | `FK → medicines.id` | Parent medicine |
| `batch_number` | `VARCHAR(50)` | `UNIQUE, NOT NULL` | Manufacturer batch code |
| `quantity_received` | `INT` | `NOT NULL` | Original quantity received |
| `current_quantity` | `INT` | `NOT NULL` | Remaining usable quantity |
| `manufacture_date` | `DATE` | | Manufacturing date |
| `expiry_date` | `DATE` | `NOT NULL` | Expiration date |
| `status` | `ENUM('AVAILABLE','QUARANTINED','EXPIRED','DISPOSED')` | `DEFAULT 'AVAILABLE'` | Stock status |
| `store_id` | `BIGINT` | `FK → stores.id` | Current storage location |
| `supplier_id` | `BIGINT` | `FK → suppliers.id` | Source supplier |
| `received_at` | `TIMESTAMP` | `DEFAULT CURRENT_TIMESTAMP` | Date received into inventory |

> **FEFO Rule:** When dispensing, batches are selected by ascending `expiry_date` (earliest first) with status `AVAILABLE`.

---

### `suppliers`

External medicine suppliers.

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Supplier identifier |
| `name` | `VARCHAR(100)` | `NOT NULL` | Company name |
| `contact_person` | `VARCHAR(100)` | | Primary contact |
| `phone` | `VARCHAR(20)` | | Phone number |
| `email` | `VARCHAR(100)` | | Email address |
| `address` | `TEXT` | | Physical address |
| `is_active` | `BOOLEAN` | `DEFAULT TRUE` | Whether supplier is active |

---

### `purchase_orders`

Tracks the lifecycle of medicine purchase orders.

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Order identifier |
| `supplier_id` | `BIGINT` | `FK → suppliers.id` | Supplier being ordered from |
| `created_by` | `BIGINT` | `FK → users.id` | User who created the order |
| `status` | `ENUM('DRAFT','SUBMITTED','APPROVED','SHIPPED','RECEIVED','CANCELLED')` | `DEFAULT 'DRAFT'` | Order lifecycle status |
| `order_date` | `DATE` | | Date order was placed |
| `expected_date` | `DATE` | | Expected delivery date |
| `total_amount` | `DECIMAL(12,2)` | | Total order value |
| `notes` | `TEXT` | | Additional notes |
| `created_at` | `TIMESTAMP` | `DEFAULT CURRENT_TIMESTAMP` | Creation timestamp |

---

### `purchase_order_items`

Line items for each purchase order.

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Item identifier |
| `order_id` | `BIGINT` | `FK → purchase_orders.id` | Parent order |
| `medicine_id` | `BIGINT` | `FK → medicines.id` | Medicine being ordered |
| `quantity_ordered` | `INT` | `NOT NULL` | Quantity requested |
| `quantity_received` | `INT` | `DEFAULT 0` | Quantity actually received |
| `unit_price` | `DECIMAL(10,2)` | | Price per unit |

---

### `stock_movements`

Immutable log of every stock movement for audit traceability.

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Movement identifier |
| `batch_id` | `BIGINT` | `FK → medicine_batches.id` | Affected batch |
| `store_id` | `BIGINT` | `FK → stores.id` | Store where movement occurred |
| `user_id` | `BIGINT` | `FK → users.id` | User who performed the action |
| `type` | `ENUM('RECEIVE','DISPENSE','TRANSFER','ADJUST','DISPOSE')` | `NOT NULL` | Movement type |
| `quantity` | `INT` | `NOT NULL` | Quantity moved (positive = in, negative = out) |
| `reference` | `VARCHAR(100)` | | Reference code (e.g., ward name, PO number) |
| `notes` | `TEXT` | | Additional context |
| `created_at` | `TIMESTAMP` | `DEFAULT CURRENT_TIMESTAMP` | Movement timestamp |

> ⚠️ **This table is append-only.** Rows must never be updated or deleted to preserve audit integrity.

---

### `alerts`

System-generated notifications for expiry and stock threshold events.

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Alert identifier |
| `type` | `ENUM('EXPIRY_WARNING','LOW_STOCK','EXPIRED','STOCKOUT')` | `NOT NULL` | Alert category |
| `medicine_id` | `BIGINT` | `FK → medicines.id` | Related medicine |
| `batch_id` | `BIGINT` | `FK → medicine_batches.id, NULLABLE` | Related batch (for expiry alerts) |
| `message` | `VARCHAR(500)` | `NOT NULL` | Human-readable alert message |
| `is_dismissed` | `BOOLEAN` | `DEFAULT FALSE` | Whether alert has been acknowledged |
| `created_at` | `TIMESTAMP` | `DEFAULT CURRENT_TIMESTAMP` | Alert creation time |

---

### `audit_log`

General-purpose audit trail for tracking all data changes.

| Column | Type | Constraints | Description |
|:-------|:-----|:------------|:------------|
| `id` | `BIGINT` | `PK, AUTO_INCREMENT` | Log entry identifier |
| `user_id` | `BIGINT` | `FK → users.id` | User who made the change |
| `entity_type` | `VARCHAR(50)` | `NOT NULL` | Table name that was changed |
| `entity_id` | `BIGINT` | `NOT NULL` | ID of the changed record |
| `action` | `ENUM('CREATE','UPDATE','DELETE')` | `NOT NULL` | Type of change |
| `old_value` | `JSON` | | Previous state (JSON snapshot) |
| `new_value` | `JSON` | | New state (JSON snapshot) |
| `created_at` | `TIMESTAMP` | `DEFAULT CURRENT_TIMESTAMP` | Change timestamp |

> ⚠️ **This table is append-only.** Rows must never be updated or deleted.

---

## Design Principles

1. **Referential Integrity** — All foreign keys are enforced. No orphaned records.
2. **Soft Deletes** — Users and suppliers use `is_active` flags rather than hard deletes.
3. **Append-Only Audit** — `stock_movements` and `audit_log` are immutable append-only tables.
4. **FEFO by Default** — Batch queries default to `ORDER BY expiry_date ASC`.
5. **Timestamps Everywhere** — Every table has `created_at`; mutable tables also have `updated_at`.
6. **UTF-8** — Database uses `utf8mb4` charset for full Unicode support.

---

## Indexing Strategy

| Table | Index | Columns | Purpose |
|:------|:------|:--------|:--------|
| `medicine_batches` | `idx_batch_fefo` | `medicine_id, expiry_date, status` | FEFO allocation queries |
| `medicine_batches` | `idx_batch_expiry` | `expiry_date` | Expiry alert scanning |
| `stock_movements` | `idx_movement_batch` | `batch_id, created_at` | Movement history lookups |
| `stock_movements` | `idx_movement_date` | `created_at` | Date-range reporting |
| `users` | `idx_user_username` | `username` | Login lookup |
| `audit_log` | `idx_audit_entity` | `entity_type, entity_id` | Entity change history |
| `alerts` | `idx_alert_active` | `is_dismissed, type` | Active alert dashboard |

---

<div align="center">

*Schema migrations will be managed using Flyway or Liquibase once the application service is implemented.*

</div>
