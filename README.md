# Medvora

<div align="center">

**Ready for care.**

An enterprise-grade Java desktop medicine inventory and replenishment management system.

[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-2E7D32?logo=java&logoColor=white)](https://openjfx.io/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

[Architecture](#system-architecture) • [Quickstart](#getting-started) • [Roadmap](#implementation-roadmap) • [Team](#team-roster) • [Documentation](docs/)

</div>

---

## Executive Summary

Medvora is an institutional software system built to prevent critical medicine stockouts across healthcare facilities. Developed by Software Engineering students at Victoria University Kampala, the platform provides end-to-end stock governance:

* **FEFO Inventory Allocation:** Enforces First-Expiry-First-Out dispensing protocols.
* **Balance Differentiation:** Strictly distinguishes usable floor stock from quarantined or expired items[cite: 7].
* **Replenishment Pipeline:** Tracks multi-step purchase orders, supplier fulfillment, and inventory adjustments.
* **Audit-Proof Traceability:** Immutable transaction history across all stock movements[cite: 7, 8].

---

## System Architecture

Medvora implements a decoupled, secure three-tier architectural model:

```text
┌──────────────────────────────────────┐
│       JavaFX 21 Desktop Client       │
│  - FXML View Layer + CSS Tokens      │
│  - Client-Side Form Validation       │
│  - Scene Navigation Engine           │
└──────────────────┬───────────────────┘
                   │
                   │ Authenticated REST / JSON
                   ▼
┌──────────────────────────────────────┐
│    Spring Boot Application Service   │
│  - Domain Business Rules & RBAC      │
│  - Transaction & Concurrency Control │
│  - Expiry & Stock Threshold Alerts   │
└──────────────────┬───────────────────┘
                   │
                   │ JDBC Connection Pool
                   ▼
┌──────────────────────────────────────┐
│       MySQL Relational Database      │
│  - Batches, Movement Logs, Audits    │
│  - Facility Stores & Warehouses      │
└──────────────────────────────────────┘
