# MindTrack API 🧠

> **Sistema de Seguimiento Psicológico — HealthTech de Grado Regulatorio**

API REST para la gestión clínica de psicólogos y sus pacientes. Permite registrar sesiones terapéuticas, monitorizar la evolución emocional mediante escalas psicométricas validadas y generar informes clínicos en PDF. Diseñada con arquitectura orientada al dominio, seguridad multi-inquilino y cumplimiento RGPD.

---

## Índice

- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Modelo de Datos](#modelo-de-datos)
- [Seguridad](#seguridad)
- [Endpoints](#endpoints)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Variables de Entorno](#variables-de-entorno)
- [Roadmap](#roadmap)

---

## Tecnologías

| Categoría | Tecnología |
|-----------|-----------|
| Framework | Spring Boot 3.5.11 |
| Lenguaje | Java 17 |
| Base de Datos | PostgreSQL 17 |
| ORM | Hibernate / Spring Data JPA |
| Seguridad | Spring Security + JWT (jjwt 0.11.5) |
| Generación PDF | OpenPDF 1.3.30 |
| Utilidades | Lombok, Bean Validation |
| Build | Maven (Maven Wrapper) |

---

## Arquitectura

```
src/main/java/com/mindtrack/mind_track_api/
├── config/
│   ├── ApplicationConfig.java      # UserDetailsService, AuthenticationProvider, PasswordEncoder
│   ├── SecurityConfig.java         # SecurityFilterChain, rutas públicas/protegidas
│   └── DataLoader.java             # Datos iniciales de prueba
├── controller/
│   ├── AuthController.java         # Registro y login
│   ├── PatientController.java      # CRUD de pacientes
│   ├── SessionController.java      # CRUD de sesiones terapéuticas
│   ├── EmotionalRecordController.java  # Escalas emocionales
│   └── ReportController.java       # Generación de PDFs
├── service/
│   ├── AuthService.java
│   ├── PatientService.java
│   ├── SessionService.java
│   ├── EmotionalRecordService.java
│   └── PdfReportService.java
├── entity/
│   ├── Psychologist.java
│   ├── Patient.java                # PatientStatus: ACTIVE, DISCHARGED, ON_HOLD
│   ├── Session.java                # SessionType: INDIVIDUAL, GROUP, FAMILY
│   ├── EmotionalRecord.java        # Escalas Likert 1-10
│   └── Diagnosis.java              # Código CIE-10
├── repository/                     # Spring Data JPA Repositories
├── security/
│   ├── JwtService.java             # Generación y validación de tokens
│   └── JwtAuthFilter.java          # Filtro por cada petición HTTP
└── dto/
    ├── request/                    # DTOs de entrada con validación
    └── response/                   # DTOs de salida
```

**Patrón arquitectónico:** Controller → Service → Repository. Separación estricta entre capas. La lógica de negocio reside exclusivamente en los Services.

---

## Modelo de Datos

```
Psychologist (1) ──────── (N) Patient
                                  │
                           (N) Session ──── (1) EmotionalRecord
                                  │
                           (N) Diagnosis
```

### Entidades principales

**Psychologist** — El profesional que usa el sistema. Autenticado por JWT. Campo `licenseNumber` vincula la actividad con la responsabilidad civil del colegiado.

**Patient** — Expediente del paciente vinculado al psicólogo. Estados: `ACTIVE`, `DISCHARGED`, `ON_HOLD`. Aislamiento total entre psicólogos.

**Session** — Registro de cada sesión terapéutica. Tipos: `INDIVIDUAL`, `GROUP`, `FAMILY`. Estados: `SCHEDULED`, `COMPLETED`, `CANCELLED`. Hasta 5.000 caracteres en notas clínicas.

**EmotionalRecord** — Escalas psicométricas por sesión (validación Likert 1-10):
- `anxietyLevel` — Nivel de ansiedad
- `moodLevel` — Estado de ánimo
- `sleepQuality` — Calidad del sueño
- `socialFunctioning` — Funcionamiento social
- `motivationLevel` — Motivación

**Diagnosis** — Diagnóstico con código CIE-10, campo `isPrimary` para jerarquía diagnóstica.

---

## Seguridad

### Autenticación JWT

Todas las rutas excepto `/api/auth/**` requieren un token JWT válido en la cabecera:

```
Authorization: Bearer <token>
```

### Multi-tenancy (Aislamiento de datos)

**Principio fundamental:** Un psicólogo solo puede ver, crear y modificar los datos de sus propios pacientes. Nunca puede acceder a expedientes ajenos, incluso conociendo el ID.

La defensa se implementa en cada Service mediante `getCurrentPsychologist()`:

```java
// Extraído del SecurityContextHolder — el email del token es la única fuente de verdad
String email = SecurityContextHolder.getContext().getAuthentication().getName();
Psychologist psychologist = psychologistRepository.findByEmail(email)...

// Verificación de propiedad antes de cualquier operación
if (!patient.getPsychologist().getId().equals(psychologist.getId()))
    throw new RuntimeException("Access denied");
```

### Flujo de autenticación

```
Cliente → POST /api/auth/register → 200 OK + JWT Token
Cliente → POST /api/auth/login    → 200 OK + JWT Token
Cliente → GET  /api/patients      → 401 sin token / 200 con token válido
```

---

## Endpoints

### Auth — Público

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/register` | Registro de psicólogo |
| POST | `/api/auth/login` | Login, devuelve JWT |

### Patients — Requiere JWT

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/patients` | Crear paciente |
| GET | `/api/patients` | Listar mis pacientes |
| GET | `/api/patients/{id}` | Obtener paciente por ID |

### Sessions — Requiere JWT

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/sessions` | Crear sesión |
| GET | `/api/sessions/patient/{patientId}` | Sesiones de un paciente |
| PATCH | `/api/sessions/{sessionId}/status` | Cambiar estado de sesión |

### Emotional Records — Requiere JWT

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/emotional-records` | Registrar escalas emocionales |
| GET | `/api/emotional-records/patient/{patientId}` | Historial emocional de un paciente |

### Reports — Requiere JWT

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/reports/patient/{patientId}/pdf` | Informe clínico PDF completo |

---

## Instalación y Ejecución

### Requisitos previos

- Java 17+
- PostgreSQL 17
- Maven (incluido Maven Wrapper)

### 1. Clonar el repositorio

```bash
git clone https://github.com/RublG97/mindtrack-api.git
cd mindtrack-api/mind-track-api
```

### 2. Crear la base de datos

```sql
CREATE DATABASE mindtrack_db;
```

### 3. Configurar credenciales

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mindtrack_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
app.jwt.secret=tu_clave_secreta_muy_larga
app.jwt.expiration=86400000
```

### 4. Arrancar

```bash
# Windows
.\mvnw.cmd clean spring-boot:run

# Linux / Mac
./mvnw clean spring-boot:run
```

La API estará disponible en `http://localhost:8080`

### Ejemplo rápido con curl

```bash
# Registrar psicólogo
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Ana","lastName":"García","email":"ana@test.com","password":"pass123","licenseNumber":"COPC-001"}'

# Crear paciente (sustituir TOKEN por el devuelto en el registro)
curl -X POST http://localhost:8080/api/patients \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Carlos","lastName":"López","birthDate":"1985-03-20","admissionDate":"2026-03-09"}'
```

---

## Variables de Entorno

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `spring.datasource.url` | URL de conexión a PostgreSQL | `jdbc:postgresql://localhost:5432/mindtrack_db` |
| `spring.datasource.username` | Usuario de BD | `postgres` |
| `spring.datasource.password` | Contraseña de BD | `tu_password` |
| `app.jwt.secret` | Clave para firmar tokens JWT | Mínimo 32 caracteres |
| `app.jwt.expiration` | Duración del token en ms | `86400000` (24h) |

---

## Roadmap

- [x] Autenticación JWT (registro / login)
- [x] CRUD de pacientes con multi-tenancy
- [x] CRUD de sesiones terapéuticas
- [x] Registro de escalas emocionales (Likert 1-10)
- [x] Generación de informes clínicos en PDF
- [ ] Frontend React con gráficas de evolución (Recharts)
- [ ] Análisis de sentimiento con IA (Claude API)
- [ ] Alertas clínicas automáticas (descenso de 40% en 7 días)
- [ ] Gestión de consentimiento informado (RGPD)
- [ ] Derecho al olvido y exportación de datos
- [ ] Despliegue en Railway / Render

---

## Autor

**Rubén Luis García** — Estudiante de DAM + Psicología  
Proyecto portfolio para demostrar competencias en desarrollo backend Java con dominio de contexto clínico.

GitHub: [@RublG97](https://github.com/RublG97)

---

> *MindTrack trasciende el concepto de un CRUD convencional para posicionarse como un sistema HealthTech que prioriza la seguridad multi-inquilino, la integridad de la narrativa clínica y el cumplimiento de normativas de protección de datos de salud.*
