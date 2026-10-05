# 🛠 Medvora — Development Environment Setup Guide

This guide walks you through setting up a complete local development environment for the Medvora project.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Java 21 Installation](#1-java-21-installation)
- [IDE Setup (IntelliJ IDEA)](#2-ide-setup-intellij-idea)
- [MySQL Database Setup](#3-mysql-database-setup)
- [Clone & Build the Project](#4-clone--build-the-project)
- [Running the Desktop Client](#5-running-the-desktop-client)
- [Running the Application Service](#6-running-the-application-service)
- [Troubleshooting](#troubleshooting)

---

## Prerequisites

| Tool | Minimum Version | Download |
|:-----|:----------------|:---------|
| JDK | 21 (LTS) | [Eclipse Temurin](https://adoptium.net/) or [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) |
| Maven | 3.9+ | Bundled via `mvnw` — no install needed |
| MySQL | 8.x | [MySQL Community](https://dev.mysql.com/downloads/) |
| Git | 2.40+ | [git-scm.com](https://git-scm.com/) |
| IDE | IntelliJ IDEA 2024+ | [JetBrains](https://www.jetbrains.com/idea/) (Community edition works) |

---

## 1. Java 21 Installation

### Windows

1. Download the **Eclipse Temurin JDK 21** installer from [adoptium.net](https://adoptium.net/)
2. Run the installer — ensure "Set JAVA_HOME" and "Add to PATH" are checked
3. Verify:
   ```cmd
   java --version
   ```
   Expected output: `openjdk 21.x.x ...`

### macOS

```bash
# Using Homebrew
brew install --cask temurin@21

# Verify
java --version
```

### Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install openjdk-21-jdk
java --version
```

> **Important:** JavaFX 21 requires JDK 21. Do not use older JDK versions.

---

## 2. IDE Setup (IntelliJ IDEA)

### Opening the Project

1. Open IntelliJ IDEA
2. **File → Open** → select the `medvora/` root folder
3. IntelliJ should automatically detect the Maven project in `desktop-client/`

### Configuring the JDK

1. **File → Project Structure → Project**
2. Set **SDK** to JDK 21
3. Set **Language Level** to `21`

### Setting up Run Configuration

1. **Run → Edit Configurations → Add (+) → Application**
2. Configure:
   - **Name:** `Medvora Desktop`
   - **Module:** `com.medvora.desktopclient`
   - **Main class:** `com.medvora.desktopclient.Launcher`
   - **Working directory:** `$MODULE_DIR$`
3. Click **Apply → OK**

> **Why `Launcher` instead of `MedvoraApplication`?**
> Due to JPMS (Java Platform Module System) constraints, launching a JavaFX application directly requires the `Launcher` class as a non-`Application` entry point. This is a standard workaround.

### Enabling JavaFX Support

IntelliJ should auto-detect FXML files. If `.fxml` files show errors:
1. **File → Settings → Plugins**
2. Ensure **JavaFX** plugin is enabled
3. Restart IntelliJ

---

## 3. MySQL Database Setup

### Install MySQL

1. Download [MySQL 8.x Community Server](https://dev.mysql.com/downloads/mysql/)
2. During installation, set a root password (remember this!)
3. Start the MySQL service

### Create the Database

```sql
-- Connect to MySQL
mysql -u root -p

-- Create database and user
CREATE DATABASE medvora_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'medvora_user'@'localhost' IDENTIFIED BY 'your_secure_password';

GRANT ALL PRIVILEGES ON medvora_db.* TO 'medvora_user'@'localhost';

FLUSH PRIVILEGES;
```

> **Note:** The database schema migrations will be added in Phase 2. For now, only the desktop client is functional and does not require a database connection.

---

## 4. Clone & Build the Project

```bash
# Clone the repository
git clone https://github.com/Wasswa-Phen/medvora.git
cd medvora

# Build the desktop client
cd desktop-client

# Windows
.\mvnw.cmd clean compile

# macOS / Linux
./mvnw clean compile
```

A successful build will output:
```
[INFO] BUILD SUCCESS
```

---

## 5. Running the Desktop Client

```bash
# From the desktop-client/ directory

# Windows
.\mvnw.cmd javafx:run

# macOS / Linux
./mvnw javafx:run
```

The application should launch with:
1. A splash screen displaying the Medvora logo
2. After ~2.5 seconds, the Sign-In form

### Running Tests

```bash
# From the desktop-client/ directory
.\mvnw.cmd test        # Windows
./mvnw test            # macOS / Linux
```

---

## 6. Running the Application Service

> 🔜 **Coming in Phase 2.** The Spring Boot application service is not yet implemented.

When available, the service will start on port `8080`:

```bash
cd application-service
./mvnw spring-boot:run
```

---

## Troubleshooting

### JavaFX modules not found

**Error:** `java.lang.module.FindException: Module javafx.controls not found`

**Fix:** Ensure you're using JDK 21 and the Maven wrapper, which handles JavaFX module path automatically:
```bash
.\mvnw.cmd javafx:run
```

### FXML loading errors

**Error:** `javafx.fxml.LoadException`

**Fix:** Check that:
1. FXML file paths in `SceneNavigator.java` match the actual file locations
2. Controller class is opened in `module-info.java`:
   ```java
   opens com.medvora.desktopclient.controller to javafx.fxml;
   ```

### Maven wrapper permission denied (macOS/Linux)

```bash
chmod +x mvnw
./mvnw javafx:run
```

### Port conflicts for Application Service

If port 8080 is already in use:
```bash
# Find the process
netstat -aon | findstr :8080    # Windows
lsof -i :8080                   # macOS/Linux

# Or configure a different port in application.properties
server.port=8081
```

---

<div align="center">

*If you encounter issues not covered here, create a GitHub issue or reach out to the team lead.*

</div>
