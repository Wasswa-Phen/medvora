# Contributing to Medvora

Thank you for your interest in contributing to Medvora! This guide will help you get up and running quickly.

---

## 🚀 Quick Start

1. **Read the setup guide:** [`docs/SETUP.md`](docs/SETUP.md)
2. **Clone the repo:**
   ```bash
   git clone https://github.com/Wasswa-Phen/medvora.git
   ```
3. **Build & run:**
   ```bash
   cd medvora/desktop-client
   .\mvnw.cmd javafx:run     # Windows
   ./mvnw javafx:run          # macOS/Linux
   ```

---

## 📐 Project Conventions

### Branch Naming

| Pattern | Example | Use |
|:--------|:--------|:----|
| `feature/<name>` | `feature/inventory-dashboard` | New functionality |
| `bugfix/<name>` | `bugfix/login-validation` | Bug fixes |
| `hotfix/<name>` | `hotfix/critical-crash` | Urgent production fixes |
| `docs/<name>` | `docs/api-reference` | Documentation updates |

### Commit Messages

We follow [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: add medicine batch creation form
fix: correct FEFO sorting in inventory list
docs: update API endpoint documentation
style: align sign-in form spacing
refactor: extract validation logic from controller
test: add unit tests for SceneNavigator
chore: update JavaFX dependency to 21.0.7
```

### Code Style

- **Java 21** — Use records, sealed classes, pattern matching where appropriate
- **FXML** — Keep layout in FXML, logic in Controllers
- **CSS tokens** — Use design tokens from `styles.css`, no inline colour values
- **Naming:**
  - Controllers: `<ViewName>Controller.java` → matches `<view-name>-view.fxml`
  - Constants: `UPPER_SNAKE_CASE`
  - Methods: `camelCase`, prefixed with `handle` for FXML event handlers

---

## 🔀 Pull Request Process

1. Create a feature branch from `develop`
2. Make your changes with clear commit messages
3. Ensure `mvn compile` and `mvn test` pass
4. Open a PR against `develop`
5. Request review from at least one team member
6. Address any feedback
7. Squash-merge once approved

### PR Checklist

- [ ] Code compiles without warnings
- [ ] Tests pass
- [ ] New views use existing CSS design tokens
- [ ] New controllers are registered in `module-info.java`
- [ ] Commit messages follow conventional commit format
- [ ] Documentation updated if applicable

---

## 📚 Key Documentation

| Document | Path | Description |
|:---------|:-----|:------------|
| Setup Guide | [`docs/SETUP.md`](docs/SETUP.md) | Environment setup |
| API Reference | [`docs/API.md`](docs/API.md) | REST endpoint contracts |
| Database Schema | [`docs/DATABASE.md`](docs/DATABASE.md) | Table definitions & ER diagrams |
| Architecture Decisions | [`docs/ADR.md`](docs/ADR.md) | Technical decision records |

---

## 🤔 Need Help?

- Check the [Troubleshooting](docs/SETUP.md#troubleshooting) section
- Create a GitHub issue describing your problem
- Reach out to the project lead

---

*Welcome to the team! 🏥*
