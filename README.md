<div align="center">

# 🏥 Medvora

**Ready for care.**

An enterprise-grade Java desktop medicine inventory and replenishment management system built to prevent critical medicine stockouts across healthcare facilities.

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-2E7D32?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](LICENSE)

[Architecture](#-system-architecture) · [Quick Start](#-quick-start) · [Project Structure](#-project-structure) · [Contributing](#-contributing) · [Documentation](docs/)

</div>

---

## 📋 Table of Contents

- [Executive Summary](#-executive-summary)
- [Key Features](#-key-features)
- [System Architecture](#-system-architecture)
- [Project Structure](#-project-structure)
- [Quick Start](#-quick-start)
- [Development Workflow](#-development-workflow)
- [Tech Stack](#-tech-stack)
- [UI Design System](#-ui-design-system)
- [Contributing](#-contributing)
- [Documentation](#-documentation)
- [Roadmap](#-roadmap)
- [Team](#-team)
- [License](#-license)

---

## 📖 Executive Summary

Medvora is an institutional software system built to prevent critical medicine stockouts across healthcare facilities. Developed by Software Engineering students at **Victoria University Kampala**, the platform provides end-to-end stock governance for pharmacies, hospitals, and clinics.

> **Problem:** Healthcare facilities in resource-constrained environments lose medicines to expiry, theft, and poor tracking — putting patient lives at risk.
>
> **Solution:** Medvora provides a disciplined, auditable medicine lifecycle system with real-time visibility into stock levels, expiry timelines, and replenishment needs.

---

## ✨ Key Features

| Feature | Description |
|:--------|:------------|
| **FEFO Inventory Allocation** | Enforces First-Expiry-First-Out dispensing protocols to minimise waste |
| **Balance Differentiation** | Strictly distinguishes usable floor stock from quarantined or expired items |
| **Replenishment Pipeline** | Tracks multi-step purchase orders, supplier fulfillment, and inventory adjustments |
| **Audit-Proof Traceability** | Immutable transaction history across all stock movements |
| **Role-Based Access Control** | Granular permissions for administrators, pharmacists, and store officers |
| **Threshold Alerts** | Automatic notifications when stock falls below configured safety levels |
| **Expiry Management** | Proactive alerts for upcoming medicine expirations |

---

## 🏗 System Architecture

Medvora implements a **decoupled three-tier architectural model** designed for security, scalability, and maintainability.

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                  PRESENTATION TIER                          │
│                                                             │
│   ┌───────────────────────────────────────────────────┐     │
│   │           JavaFX 21 Desktop Client                │     │
│   │                                                   │     │
│   │   ┌─────────┐  ┌──────────┐  ┌──────────────┐    │     │
│   │   │  FXML   │  │   CSS    │  │  Controllers │    │     │
│   │   │  Views  │  │  Tokens  │  │  & Handlers  │    │     │
│   │   └─────────┘  └──────────┘  └──────────────┘    │     │
│   │                                                   │     │
│   │   ┌─────────────────┐  ┌────────────────────┐    │     │
│   │   │ Scene Navigator │  │ Client Validation  │    │     │
│   │   └─────────────────┘  └────────────────────┘    │     │
│   └───────────────────────────────────────────────────┘     │
│                           │                                  │
└───────────────────────────┼──────────────────────────────────┘
                            │  Authenticated REST / JSON
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                  APPLICATION TIER                           │
│                                                             │
│   ┌───────────────────────────────────────────────────┐     │
│   │        Spring Boot Application Service            │     │
│   │                                                   │     │
│   │   ┌──────────┐  ┌───────────┐  ┌─────────────┐   │     │
│   │   │ REST API │  │ Business  │  │    RBAC     │   │     │
│   │   │ Endpts   │  │  Rules    │  │   Engine    │   │     │
│   │   └──────────┘  └───────────┘  └─────────────┘   │     │
│   │                                                   │     │
│   │   ┌──────────────────┐  ┌─────────────────────┐  │     │
│   │   │   Transaction &  │  │  Expiry & Threshold │  │     │
│   │   │   Concurrency    │  │      Alerting       │  │     │
│   │   └──────────────────┘  └─────────────────────┘  │     │
│   └───────────────────────────────────────────────────┘     │
│                           │                                  │
└───────────────────────────┼──────────────────────────────────┘
                            │  JDBC Connection Pool
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                     DATA TIER                               │
│                                                             │
│   ┌───────────────────────────────────────────────────┐     │
│   │          MySQL 8.x Relational Database            │     │
│   │                                                   │     │
│   │   ┌──────────┐  ┌───────────┐  ┌─────────────┐   │     │
│   │   │ Medicine │  │ Movement  │  │   Audit     │   │     │
│   │   │ Batches  │  │   Logs    │  │   Trails    │   │     │
│   │   └──────────┘  └───────────┘  └─────────────┘   │     │
│   │                                                   │     │
│   │   ┌──────────────────┐  ┌─────────────────────┐  │     │
│   │   │  Facility Stores │  │ Users & Permissions │  │     │
│   │   │  & Warehouses    │  │                     │  │     │
│   │   └──────────────────┘  └─────────────────────┘  │     │
│   └───────────────────────────────────────────────────┘     │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Navigation Flow

```
┌──────────────────┐      2.5s auto      ┌──────────────────┐
│   Startup View   │ ──────────────────▶  │  Sign-In View    │
│   (Splash/Load)  │                      │  (Authentication)│
└──────────────────┘                      └────────┬─────────┘
                                                   │
                                          "Access Help" link
                                                   │
                                                   ▼
                                          ┌──────────────────┐
                                          │ Access Help View  │
                                          │ (Troubleshooting) │
                                          └──────────────────┘
                                                   │
                                          "Back to Sign In"
                                                   │
                                                   ▼
                                          ┌──────────────────┐
                                          │  Sign-In View    │
                                          └──────────────────┘
```

---

## 📁 Project Structure

```
medvora/
├── 📂 desktop-client/                    # JavaFX desktop application
│   ├── pom.xml                           # Maven build config (Java 21, JavaFX 21)
│   ├── mvnw / mvnw.cmd                  # Maven wrapper (no global install needed)
│   └── src/main/
│       ├── java/
│       │   ├── module-info.java          # JPMS module descriptor
│       │   └── com/medvora/desktopclient/
│       │       ├── MedvoraApplication.java   # JavaFX Application entry point
│       │       ├── Launcher.java             # Non-modular launch workaround
│       │       ├── controller/               # FXML view controllers
│       │       │   ├── StartupController.java    # Splash screen logic
│       │       │   ├── SignInController.java     # Authentication form handler
│       │       │   └── AccessHelpController.java # Help/troubleshooting handler
│       │       └── util/
│       │           └── SceneNavigator.java   # Centralised scene routing engine
│       └── resources/com/medvora/desktopclient/
│           ├── css/
│           │   └── styles.css            # Design system tokens & component styles
│           ├── images/
│           │   ├── app-icon.png          # Window/taskbar icon
│           │   └── logo.png              # Medvora brand logo
│           └── view/
│               ├── startup-view.fxml     # Splash/loading screen
│               ├── sign-in-view.fxml     # Authentication form
│               └── access-help-view.fxml # Login help & diagnostics
│
├── 📂 application-service/              # Spring Boot REST API (planned)
│   └── .gitkeep
│
├── 📂 database/                         # Database schemas & migrations (planned)
│   └── .gitkeep
│
├── 📂 docs/                             # Project documentation
│   ├── DESIGN AND DEVELOPMENT GUIDE.docx
│   ├── Interface.docx
│   └── SYSTEM REQUIREMENTS SPECIFICATION.docx
│
├── .gitignore
└── README.md                            # ← You are here
```

---

## 🚀 Quick Start

### Prerequisites

| Tool | Version | Purpose |
|:-----|:--------|:--------|
| **JDK** | 21+ | Core runtime (LTS recommended) |
| **Maven** | 3.9+ | Build automation (or use included `mvnw`) |
| **MySQL** | 8.x | Database engine (required for full stack) |
| **IDE** | IntelliJ IDEA recommended | Development environment |

### 1. Clone the Repository

```bash
git clone https://github.com/Wasswa-Phen/medvora.git
cd medvora
```

### 2. Run the Desktop Client

Using the Maven Wrapper (no Maven installation required):

```bash
cd desktop-client

# On Windows
.\mvnw.cmd javafx:run

# On macOS / Linux
./mvnw javafx:run
```

Or with a global Maven installation:

```bash
cd desktop-client
mvn javafx:run
```

### 3. Verify Launch

You should see:
1. ✅ A **splash screen** with the Medvora logo and a loading progress bar
2. ✅ After ~2.5 seconds, the **Sign-In** screen appears
3. ✅ The "Access Help" link navigates to the troubleshooting page

---

## 🔄 Development Workflow

### Branch Strategy

```
main                    # Production-ready code
├── develop             # Integration branch for next release
│   ├── feature/*       # New features (e.g., feature/inventory-dashboard)
│   ├── bugfix/*        # Bug fixes (e.g., bugfix/login-validation)
│   └── hotfix/*        # Urgent production patches
```

### Daily Workflow

```bash
# 1. Pull latest from develop
git checkout develop
git pull origin develop

# 2. Create your feature branch
git checkout -b feature/your-feature-name

# 3. Make changes, commit frequently
git add .
git commit -m "feat: describe your change"

# 4. Push and open a pull request against develop
git push origin feature/your-feature-name
```

### Commit Message Convention

We follow [Conventional Commits](https://www.conventionalcommits.org/):

| Prefix | Use for |
|:-------|:--------|
| `feat:` | New features |
| `fix:` | Bug fixes |
| `docs:` | Documentation changes |
| `style:` | Formatting, CSS (no logic change) |
| `refactor:` | Code restructuring |
| `test:` | Adding or updating tests |
| `chore:` | Build config, dependencies |

**Examples:**
```
feat: add medicine batch creation form
fix: correct FEFO sorting in inventory list
docs: update API endpoint documentation
```

---

## 🛠 Tech Stack

### Desktop Client

| Technology | Version | Purpose |
|:-----------|:--------|:--------|
| Java | 21 (LTS) | Core language, JPMS modules |
| JavaFX | 21.0.6 | Rich desktop UI framework |
| FXML | — | Declarative view layout |
| CSS (JavaFX) | — | Styling & theming |
| Maven | 3.9+ | Build & dependency management |
| JUnit 5 | 5.12.1 | Unit & integration testing |

### Application Service *(Planned)*

| Technology | Version | Purpose |
|:-----------|:--------|:--------|
| Spring Boot | 3.x | REST API framework |
| Spring Security | — | Authentication & RBAC |
| Spring Data JPA | — | Data access layer |

### Database *(Planned)*

| Technology | Version | Purpose |
|:-----------|:--------|:--------|
| MySQL | 8.x | Relational data storage |

---

## 🎨 UI Design System

Medvora uses a custom design token system defined in [`styles.css`](desktop-client/src/main/resources/com/medvora/desktopclient/css/styles.css). All UI components reference these tokens for visual consistency.

### Brand Colours

| Token | Hex | Usage |
|:------|:----|:------|
| `--fx-primary-action` | `#175CD3` | Primary buttons, links, focus rings |
| `--fx-primary-hover` | `#154FB7` | Button hover states |
| `--fx-brand-teal` | `#0E9384` | Accent highlights, progress bars |

### Neutral Palette

| Token | Hex | Usage |
|:------|:----|:------|
| `--fx-page-bg` | `#F5F7FA` | Page background canvas |
| `--fx-surface` | `#FFFFFF` | Cards, inputs, elevated surfaces |
| `--fx-text-main` | `#182230` | Primary text, headings |
| `--fx-text-secondary` | `#475467` | Subtitles, helper text |
| `--fx-divider` | `#D0D5DD` | Borders, separators |

### Component Style Classes

| Class | Description |
|:------|:------------|
| `.root-container` | Full-page background canvas |
| `.auth-card` | Elevated white card with rounded corners and shadow |
| `.brand-title` | 26px bold brand name heading |
| `.page-title` | 22px bold page heading |
| `.secondary-text` | 13px muted helper text |
| `.field-label` | 13px bold form field labels |
| `.text-input-field` | Standard 40px height text input with focus ring |
| `.btn-primary` | Filled blue action button |
| `.btn-secondary` | Outlined secondary action button |
| `.link-action` | Interactive hyperlink with hover underline |
| `.tile-card` | Bordered info tile (used in Access Help) |

### Adding a New View

1. **Create the FXML** file in `resources/com/medvora/desktopclient/view/`
2. **Create a Controller** in `controller/` package
3. **Register the route** as a constant in [`SceneNavigator.java`](desktop-client/src/main/java/com/medvora/desktopclient/util/SceneNavigator.java)
4. **Reference the stylesheet** in your FXML: `<URL value="@../css/styles.css" />`
5. **Use design tokens** — never hard-code colours or sizes

---

## 🤝 Contributing

### Getting Started

1. **Read the documentation** in the [`docs/`](docs/) folder — especially the *Design and Development Guide*
2. **Set up your environment** using the [Quick Start](#-quick-start) guide
3. **Check the [Roadmap](#-roadmap)** for current priorities
4. **Pick a task** or create an issue for the feature you want to work on

### Code Standards

- **Java 21** — Use modern language features (records, sealed classes, pattern matching) where appropriate
- **FXML for layouts** — Keep UI markup separated from logic
- **Design tokens** — Always reference CSS tokens from `styles.css` rather than inline values
- **Controller naming** — `<ViewName>Controller.java` matches `<view-name>-view.fxml`
- **Scene navigation** — Always use `SceneNavigator.navigate()` for view transitions

### Pull Request Checklist

- [ ] Code compiles: `mvn compile` passes
- [ ] Tests pass: `mvn test` passes
- [ ] New views use existing design system tokens
- [ ] Controller is registered in the module descriptor if it needs FXML reflection
- [ ] Commit messages follow conventional commit format
- [ ] No hard-coded strings (use constants or resource bundles)

---

## 📚 Documentation

Detailed project documentation is available in the [`docs/`](docs/) folder:

| Document | Description |
|:---------|:------------|
| [Design and Development Guide](docs/DESIGN%20AND%20DEVELOPMENT%20GUIDE.docx) | Architecture decisions, coding standards, and design patterns |
| [System Requirements Specification](docs/SYSTEM%20REQUIREMENTS%20SPECIFICATION.docx) | Functional/non-functional requirements and use cases |
| [Interface Design](docs/Interface.docx) | UI wireframes and interaction specifications |

### Additional Resources

| Resource | Location |
|:---------|:---------|
| [Project Setup Guide](docs/SETUP.md) | Detailed environment setup instructions |
| [API Reference](docs/API.md) | REST API endpoint documentation |
| [Database Schema](docs/DATABASE.md) | Entity-relationship diagrams & schema definitions |
| [Architecture Decision Records](docs/ADR.md) | Key technical decisions and rationale |

---

## 🗺 Roadmap

### Phase 1: Foundation ✅ *(Current)*
- [x] Project repository & structure
- [x] JavaFX desktop client scaffolding
- [x] FXML view layer with CSS design system
- [x] Scene navigation engine
- [x] Splash screen → Sign-In → Access Help flow
- [x] Design tokens & component style library

### Phase 2: Authentication & Backend 🔄 *(In Progress)*
- [ ] Spring Boot application service setup
- [ ] MySQL database schema & migrations
- [ ] User authentication (login/logout)
- [ ] Role-based access control (RBAC)
- [ ] REST API client integration in desktop app

### Phase 3: Core Inventory
- [ ] Medicine master data management
- [ ] Batch tracking with expiry dates
- [ ] FEFO stock allocation engine
- [ ] Stock movement logging
- [ ] Balance differentiation (usable vs. quarantined)

### Phase 4: Replenishment & Alerts
- [ ] Purchase order lifecycle management
- [ ] Supplier management
- [ ] Reorder point & safety stock thresholds
- [ ] Expiry alert notifications
- [ ] Low-stock warnings

### Phase 5: Reporting & Audit
- [ ] Stock-on-hand reports
- [ ] Movement history reports
- [ ] Audit trail viewer
- [ ] Data export (PDF, CSV)

---

## 👥 Team

Developed by Software Engineering students at **Victoria University Kampala**.

| Role | Member |
|:-----|:-------|
| **Lead Coordinator** | Wasswa Makubuya Stephen |

> 💡 *Team members: add your name and role via a pull request!*

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**Medvora** — *Ready for care.*

Built with ❤️ at Victoria University Kampala

</div>
