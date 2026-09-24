# Medvora

**Ready for care.**

A Java desktop medicine inventory and replenishment management system.

## Project overview

Medvora is a real-life project by Professional Certificate in Software
Engineering students at Victoria University Kampala, Uganda.

The project addresses medicine stockouts by helping healthcare staff
monitor available stock, identify shortages early, track expiry dates
and coordinate replenishment.

## Current status

Design and development setup.

The capabilities below are planned and will be implemented in stages.
Requirements and stakeholder decisions are tracked separately.

## Planned capabilities

- Medicine records, batches, expiry dates and storage locations.
- Opening stock, receipts, issues, transfers and returns.
- Stock counts, reconciliation and approved adjustments.
- Current usable-stock balances and minimum-stock levels.
- Low-stock, stockout and expiry alerts.
- Suppliers, replenishment requests, approvals and orders.
- User accounts, roles and access permissions.
- Stock reports, movement history and audit records.
- Backup, restoration and user support.

Patient records, diagnosis, prescriptions, billing and supplier payments
are outside the current project scope.

## Technology

| Area | Technology |
|---|---|
| Programming language | Java |
| Desktop interface | JavaFX |
| Screen layouts | FXML and Scene Builder |
| Interface styling | JavaFX CSS |
| Development environment | IntelliJ IDEA |
| Application service | Spring Boot |
| Database | MySQL |
| Database access | JDBC and MySQL Connector/J |
| Build system | Maven with Maven Wrapper |
| Automated testing | JUnit |
| Collaboration | Git and GitHub |
| Task tracking | GitHub Issues and Projects |

Compatible software versions and installation instructions will be
recorded when the starting application is configured.

## Application structure

The JavaFX desktop application communicates with the Spring Boot service.
The service applies permissions and stock rules, and connects to MySQL.

Only the service connects directly to the database.

## Team members

1. Wasswa Stephen — Team Leader
2. Tendo Blair
3. Muhanguzi Eugene
4. Alexander Okello
5. Ssewakiryanga Simon
6. Anna Blessed
7. Cathy N
8. Maria Assumpta
9. Samuel
10. Elizabeth N

## Working together

1. Create or select a GitHub issue before starting a development task.
2. Record the relevant requirement IDs and completion conditions.
3. Assign one task owner and another member as reviewer.
4. Update your local project before creating a feature branch.
5. Make small changes and test them.
6. Commit with a clear message and push your branch.
7. Open a pull request and address review comments.
8. Merge reviewed work and update the issue.

Keep the main branch runnable once the starting application is added.
Use fictional demonstration data and keep passwords out of the repository.

Every member should participate in implementation, testing and review.
Members should be able to explain the changes they contribute.

## First working milestone

Create a medicine, receive a batch, display the saved stock balance,
restart the application and verify that the records remain.

Then implement issuing stock, insufficient-stock rejection and
movement history.

## Getting started

Setup and run instructions will be added with the first runnable build.
Use GitHub Issues to record tasks, questions and defects.
