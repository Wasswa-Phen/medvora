# 📝 Medvora — Architecture Decision Records (ADR)

This document captures key technical decisions made during the development of Medvora, along with the context and rationale behind each choice.

---

## Table of Contents

- [ADR-001: Three-Tier Architecture](#adr-001-three-tier-architecture)
- [ADR-002: JavaFX for Desktop Client](#adr-002-javafx-for-desktop-client)
- [ADR-003: Java Platform Module System (JPMS)](#adr-003-java-platform-module-system-jpms)
- [ADR-004: Launcher Class Pattern](#adr-004-launcher-class-pattern)
- [ADR-005: Scene Navigator Centralised Routing](#adr-005-scene-navigator-centralised-routing)
- [ADR-006: CSS Design Token System](#adr-006-css-design-token-system)
- [ADR-007: Spring Boot for Application Service](#adr-007-spring-boot-for-application-service)
- [ADR-008: MySQL as Primary Database](#adr-008-mysql-as-primary-database)
- [ADR-009: FEFO Inventory Strategy](#adr-009-fefo-inventory-strategy)
- [ADR-010: Append-Only Audit Logs](#adr-010-append-only-audit-logs)
- [Template for New ADRs](#template-for-new-adrs)

---

## ADR-001: Three-Tier Architecture

**Date:** 2026 | **Status:** Accepted

### Context
We needed an architecture that separates concerns, supports multiple future clients (mobile, web), and provides clear security boundaries for healthcare data.

### Decision
Adopt a **three-tier architecture**: JavaFX Desktop Client → Spring Boot REST API → MySQL Database.

### Rationale
- **Security:** The desktop client never touches the database directly. All data access goes through authenticated API calls.
- **Scalability:** The backend can serve multiple clients without modification.
- **Testability:** Each tier can be tested independently.
- **Team parallelism:** Frontend and backend teams can work simultaneously against a shared API contract.

### Consequences
- Network latency is introduced between client and data.
- Requires API versioning discipline.
- More complex deployment than a monolithic fat-client approach.

---

## ADR-002: JavaFX for Desktop Client

**Date:** 2026 | **Status:** Accepted

### Context
We needed a desktop UI framework for a cross-platform inventory management application targeting healthcare facilities.

### Decision
Use **JavaFX 21** with FXML for declarative layouts and CSS for styling.

### Alternatives Considered
| Option | Reason Rejected |
|:-------|:----------------|
| Swing | Legacy, no modern styling, poor DPI support |
| Electron | Heavy memory footprint, requires JavaScript expertise |
| .NET MAUI | Not cross-platform enough for our Linux deployments |

### Rationale
- JavaFX 21 is actively maintained and has strong LTS support.
- FXML enables designer-developer collaboration (Scene Builder).
- Java ecosystem aligns with our Spring Boot backend.
- Cross-platform: runs on Windows, macOS, and Linux.

---

## ADR-003: Java Platform Module System (JPMS)

**Date:** 2026 | **Status:** Accepted

### Context
JavaFX 21 strongly encourages JPMS module usage for proper encapsulation and runtime image creation.

### Decision
Define a `module-info.java` for the desktop client:

```java
module com.medvora.desktopclient {
    requires javafx.controls;
    requires javafx.fxml;
    opens com.medvora.desktopclient.controller to javafx.fxml;
    exports com.medvora.desktopclient;
}
```

### Consequences
- All new controller packages must be `opens`-ed to `javafx.fxml` for FXML reflection.
- Third-party libraries must be modular or placed on the classpath.
- Enables `jlink` for creating custom runtime images.

---

## ADR-004: Launcher Class Pattern

**Date:** 2026 | **Status:** Accepted

### Context
When using JPMS, the `main()` method cannot reside in a class that extends `javafx.application.Application` if launched from certain IDEs or build tools.

### Decision
Create a separate `Launcher.java` class that delegates to `Application.launch(MedvoraApplication.class, args)`.

### Rationale
This is a well-documented workaround for the JPMS + JavaFX main class restriction. It ensures the application can be launched from:
- IntelliJ IDEA (Run Configuration)
- Maven (`javafx:run`)
- Command line (`java -jar`)

---

## ADR-005: Scene Navigator Centralised Routing

**Date:** 2026 | **Status:** Accepted

### Context
The application has multiple views (Startup, Sign-In, Access Help, and future inventory screens). We needed a consistent way to handle scene transitions.

### Decision
Create a `SceneNavigator` utility class that:
- Defines all view paths as `public static final String` constants
- Provides a single `navigate(Stage, fxmlPath)` method
- Reuses the existing `Scene` object (replacing `root`) after initial creation

### Rationale
- **Single source of truth** for all view paths — no scattered string literals.
- **Consistent behaviour** — all navigation goes through one method.
- **Memory efficient** — reuses the `Scene` instance rather than creating new ones.

### Consequences
- All new views must be registered in `SceneNavigator.java`.
- Complex navigation (back stacks, modal dialogs) will need enhancements to this class.

---

## ADR-006: CSS Design Token System

**Date:** 2026 | **Status:** Accepted

### Context
We needed consistent visual styling across all views without duplicating colour values and sizing in every FXML file.

### Decision
Define a **design token system** in `styles.css` using JavaFX CSS variables:

```css
* {
    -fx-primary-action: #175CD3;
    -fx-page-bg: #F5F7FA;
    -fx-text-main: #182230;
    /* ... */
}
```

### Rationale
- Changing a brand colour updates the entire app from one file.
- Developers use semantic class names (`.btn-primary`, `.auth-card`) rather than raw values.
- Mirrors modern web design systems (Material, Shadcn).

### Consequences
- Requires discipline: no inline colour values in FXML.
- JavaFX CSS is a subset of web CSS — some web patterns may not apply.

---

## ADR-007: Spring Boot for Application Service

**Date:** 2026 | **Status:** Accepted (Not Yet Implemented)

### Context
The backend needs to expose RESTful APIs, manage database transactions, enforce business rules (FEFO, RBAC), and handle concurrency.

### Decision
Use **Spring Boot 3.x** with:
- Spring Web (REST controllers)
- Spring Security (JWT authentication, RBAC)
- Spring Data JPA (database access)

### Rationale
- Industry standard for enterprise Java applications.
- Extensive ecosystem for security, data access, and testing.
- Auto-configuration reduces boilerplate.
- Team members already have Spring experience from coursework.

---

## ADR-008: MySQL as Primary Database

**Date:** 2026 | **Status:** Accepted (Not Yet Implemented)

### Context
We needed a reliable, free relational database suitable for healthcare facility deployments.

### Decision
Use **MySQL 8.x** Community Edition.

### Alternatives Considered
| Option | Reason Rejected |
|:-------|:----------------|
| PostgreSQL | Excellent, but less familiarity on the team |
| SQLite | Insufficient for multi-user concurrent access |
| MongoDB | Relational integrity critical for financial/medical data |

### Rationale
- Free and open source.
- Strong relational integrity for inventory and financial data.
- Widely deployed in healthcare environments.
- Good tooling (MySQL Workbench, DBeaver).

---

## ADR-009: FEFO Inventory Strategy

**Date:** 2026 | **Status:** Accepted

### Context
Medicine dispensing must prioritise items expiring soonest to minimise waste and comply with pharmaceutical best practices.

### Decision
Implement **First-Expiry-First-Out (FEFO)** as the default dispensing algorithm:
1. When dispensing, query batches `WHERE status = 'AVAILABLE' ORDER BY expiry_date ASC`
2. Deduct from the earliest-expiring batch first
3. If the batch doesn't have enough, spill over to the next batch

### Rationale
- FEFO is the WHO-recommended standard for medicine management.
- Reduces expired stock waste.
- Provides audit evidence of proper pharmaceutical practice.

---

## ADR-010: Append-Only Audit Logs

**Date:** 2026 | **Status:** Accepted

### Context
Healthcare inventory systems require verifiable, tamper-resistant records of all stock movements for regulatory compliance.

### Decision
Make `stock_movements` and `audit_log` tables **append-only** — no `UPDATE` or `DELETE` operations are allowed.

### Rationale
- Provides a complete, immutable chain of custody for all medicines.
- Supports regulatory audits and investigations.
- Corrections are recorded as new adjustment entries, not by modifying history.

### Consequences
- Storage grows continuously — will need archival strategy for long-running deployments.
- "Undo" operations must create compensating entries rather than deleting originals.

---

## Template for New ADRs

When making a significant technical decision, copy this template:

```markdown
## ADR-XXX: [Title]

**Date:** YYYY-MM | **Status:** Proposed | Accepted | Deprecated | Superseded

### Context
[What is the problem or situation that requires a decision?]

### Decision
[What did we decide to do?]

### Alternatives Considered
[What other options were evaluated?]

### Rationale
[Why this option over the alternatives?]

### Consequences
[What are the trade-offs and implications?]
```

---

<div align="center">

*ADRs are living documents. When a decision is superseded, mark it as such and link to the new ADR.*

</div>
