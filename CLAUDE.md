# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an enterprise-grade inspection management system (巡检App一体化项目) with comprehensive frontend/backend architecture supporting mobile app, web admin panel, and RESTful API services.

**Key Components:**
- **Mobile App**: Android native app with embedded H5 WebView (`App/inspectionapp/`)
- **Web App**: Vue3-based H5/PC web interface (`App/web/`)
- **Admin Web**: Vue3 + Ant Design Vue admin management system (`admin-web/`)
- **Backend Server**: Spring Boot RESTful API service (`check-app-server/`)
- **Design Docs**: 35+ comprehensive design documents (`design/`)

## Common Development Commands

### Frontend Development

**Web App (App/web/)**:
```bash
cd App/web
npm install
npm run dev     # Development server (default port 5173)
npm run build   # Production build
```

**Admin Web (admin-web/)**:
```bash
cd admin-web
npm install
npm run dev     # Development server (default port 3000)
npm run build   # Production build
npm run lint    # ESLint check
```

### Backend Development

**Spring Boot Server (check-app-server/)**:
```bash
cd check-app-server
mvn spring-boot:run                    # Development server (port 8080)
mvn clean package -DskipTests          # Build JAR
mvn test                               # Run tests

# Docker deployment
docker build -t check-app-server .
docker run -p 8080:8080 check-app-server

# Health check
curl http://localhost:8080/actuator/health
```

### Mobile App Development

**Android App (App/inspectionapp/)**:
```bash
cd App/inspectionapp
./gradlew assembleDebug                # Build debug APK
./gradlew assembleRelease             # Build release APK
# APK output: app/build/outputs/apk/debug/
```

### Testing Commands

**Backend Tests**:
```bash
cd check-app-server
mvn test                              # Run all tests
mvn test -Dtest=ControllerTest        # Run specific test class
```

**Frontend Tests**:
```bash
# In respective frontend directories
npm run test                          # Run unit tests (if configured)
```

## Architecture Overview

### Technology Stack

**Frontend Technologies:**
- **Vue 3** + Vite (web apps)
- **Ant Design Vue** (admin) + **Vant** (mobile web)
- **Pinia** state management
- **Axios** HTTP client
- **Vue Router** routing

**Backend Technologies:**
- **Spring Boot 2.7.5** + Java 8
- **MyBatis-Plus** ORM
- **Spring Security** + **JWT** authentication
- **MySQL** database + **Redis** caching
- **LDAP** for AD domain integration

**Mobile Technologies:**
- **Android** native (Java/Kotlin)
- **WebView** with JavaScript Bridge
- **Gradle** build system

### Project Structure

```
├── App/                    # Frontend applications
│   ├── inspectionapp/     # Android native app
│   ├── web/               # H5/PC web app (Vue3)
│   └── Arch/              # App architecture docs
├── admin-web/             # Admin management system (Vue3 + Ant Design)
├── check-app-server/      # Spring Boot backend service
├── design/                # 35+ design documents (architecture, API, database)
├── Admin-UI/              # Admin UI prototypes (HTML)
├── UI原型/                # Mobile UI prototypes (HTML)
└── PRD/                   # Product requirement documents
```

### Key Design Patterns

**Backend Architecture:**
- **Layered Architecture**: Controller → Service → Mapper → Entity
- **DTO Pattern**: Separate data transfer objects for API requests/responses
- **Global Exception Handling**: Unified error response format
- **JWT Authentication**: Stateless token-based auth with Spring Security

**Frontend Architecture:**
- **Component-based**: Vue3 Composition API with modular components
- **State Management**: Pinia stores for user auth, app state
- **API Abstraction**: Axios interceptors for request/response handling
- **Route Guards**: Authentication and permission-based navigation control

**Mobile Integration:**
- **Hybrid Architecture**: Android WebView hosting Vue3 H5 pages
- **JavaScript Bridge**: Native-JS communication for camera, scanning, file operations
- **Offline Support**: Local data caching and sync capabilities

## Database & API Design

**Database Architecture:**
- **MySQL** relational database with comprehensive entity relationships
- **Key Entities**: User, Role, Area, Inspection, Schedule, Record, Issue
- **AD Integration**: LDAP authentication with role mapping
- **Audit Trails**: Created/updated timestamps, user tracking

**API Design Principles:**
- **RESTful**: Standard HTTP methods with resource-based URLs
- **Consistent Response Format**: Unified JSON structure with status codes
- **Authentication**: Bearer token (JWT) in Authorization header
- **Validation**: Comprehensive input validation with error messages
- **Documentation**: Detailed API specs in `design/` directory (32+ documents)

## Development Standards

### Code Quality Rules
The project follows comprehensive Cursor AI rules located in `.cursor/rules/`:
- **General Development**: DRY principle, KISS principle, testability
- **Vue.js Standards**: Composition API, Pinia patterns, performance optimization
- **Java/Spring Boot**: Clean architecture, RESTful design, security practices
- **Security**: Input validation, authentication, authorization
- **Git Workflow**: Meaningful commits, feature branches, code review

### Key Development Guidelines

**Security Requirements:**
- Never expose API keys, tokens, or sensitive data in code
- Always validate and sanitize user inputs
- Use HTTPS for all production communications
- Implement proper JWT token expiration and refresh

**Performance Considerations:**
- Frontend: Component lazy loading, API request optimization
- Backend: Database query optimization, caching strategies
- Mobile: Offline data management, efficient WebView usage

**Testing Approach:**
- Backend: Unit tests with JUnit, integration tests for controllers
- Frontend: Component testing with Vue Test Utils
- Mobile: Android instrumentation tests

## Deployment Configuration

**Development Environment:**
- Frontend: Local Vite/Vue CLI dev servers
- Backend: Local Spring Boot with H2/MySQL
- Mobile: Android Studio with device/emulator

**Production Deployment:**
- **Frontend**: Build artifacts served via Nginx
- **Backend**: Docker containerized Spring Boot application
- **Database**: Dedicated MySQL server with Redis cache
- **Mobile**: APK distribution with embedded web assets

**Docker Configuration:**
- Multi-stage builds for optimized image sizes
- Health checks and logging configured
- Kubernetes Helm charts available for both frontend and backend

## Important File Locations

**Configuration Files:**
- Backend config: `check-app-server/src/main/resources/application.yml`
- Web app config: `App/web/vite.config.js`
- Admin web config: `admin-web/vue.config.js`
- Android config: `App/inspectionapp/app/build.gradle`

**Key Documentation:**
- System architecture: `design/002-architecture-20250609.md`
- API specifications: `design/` directory (35+ detailed documents)
- Database design: Multiple docs in `design/` covering schema and relationships
- AD integration: `design/021-ad-integration-design-20250619.md`

**Development Resources:**
- UI prototypes: `Admin-UI/` and `UI原型/` directories
- Product requirements: `PRD/巡检App-PRD.md`
- Design document index: `design/design-documents-index.md`

## Business Domain Context

This is an enterprise inspection management system designed for large organizations with the following core workflows:

1. **User Authentication**: Local accounts + AD domain integration with role-based access
2. **Schedule Management**: Admin assignment of inspection tasks to staff with calendar management
3. **Inspection Execution**: Mobile app for QR code scanning, form completion, photo capture
4. **Issue Management**: Problem reporting, tracking, and resolution workflows  
5. **Analytics & Reporting**: Comprehensive statistics, data export, and management dashboards

**Key Business Entities:**
- **Users**: Multiple roles (admin, inspector, viewer) with AD group mapping
- **Areas**: Hierarchical organization of inspection locations with QR codes
- **Schedules**: Time-based task assignments with conflict detection
- **Records**: Inspection results with photos, forms, and issue tracking
- **Issues**: Problem reports with status tracking and resolution workflows