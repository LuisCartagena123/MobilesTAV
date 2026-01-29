# 📚 Evaluación 2 - Sistema de Gestión de Libros

Proyecto integral con **Frontend Android (Kotlin + Jetpack Compose)** + **Backend REST API (Spring Boot + JWT)**

## 📁 Estructura del Proyecto

```
MobilesTAV-main/
│
├── app/                          # 📱 Android App (Frontend)
│   ├── src/main/java/com/example/evaluacion2/
│   │   ├── data/
│   │   │   ├── AppDatabase.kt   # Room Database
│   │   │   ├── RoomRepository.kt
│   │   │   ├── dao/             # Data Access Objects
│   │   │   ├── entities/        # Entity classes
│   │   │   └── network/         # Google Books API
│   │   ├── viewmodel/
│   │   │   └── AppViewModel.kt  # MVVM State Management
│   │   ├── views/               # Composable screens
│   │   ├── ui/
│   │   │   └── theme/           # Material3 Theme
│   │   └── MainActivity.kt
│   ├── build.gradle.kts
│   └── proguard-rules.pro
│
├── backend/                      # 🔧 Spring Boot Backend
│   ├── src/main/java/com/example/evaluacion2/
│   │   ├── usuarios/            # User Management
│   │   ├── libros/              # Book Management
│   │   ├── carro/               # Shopping Cart
│   │   ├── auth/                # Authentication & JWT
│   │   └── common/              # Utilities
│   ├── src/main/resources/
│   │   └── application.yml      # Configuration
│   ├── src/test/                # Unit Tests
│   ├── pom.xml                  # Maven Dependencies
│   ├── README.md
│   └── .gitignore
│
├── docker-compose.yml           # 🐳 Docker Compose (MySQL + phpMyAdmin)
├── build.gradle.kts             # Gradle configuration
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## 🎯 Tecnologías

### Frontend (Android)
- **Kotlin 1.9+**
- **Jetpack Compose** (UI moderna)
- **Room Database v2.6.1** (Persistencia local)
- **Retrofit 2.11.0** + **OkHttp 4.12.0** (API calls)
- **Coil 2.6.0** (Image loading)
- **Material3** (Design system)
- **Google Books API** (Búsqueda de libros)

### Backend (Spring Boot)
- **Java 11**
- **Spring Boot 3.2.1**
- **Spring Data JPA** (ORM)
- **Spring Security** (Autenticación)
- **JWT** (JSON Web Tokens)
- **MySQL 8.0** (Base de datos)
- **Maven 3.6+** (Build tool)
- **JUnit 5 + Mockito** (Testing)

### DevOps
- **Docker** (Containerización)
- **Docker Compose** (Orquestación local)
- **Git + GitHub** (Version control)

## 📋 Características Implementadas

### ✅ Frontend (Android)
- ✅ Autenticación (Login/Registro)
- ✅ Sistema de roles (Admin/Usuario)
- ✅ Gestión de libros (CRUD - solo admin)
- ✅ Búsqueda en Google Books API
- ✅ Carrito de compras persistente (por usuario)
- ✅ Interfaz Material3
- ✅ Room Database (offline-first)
- ✅ MVVM Architecture

### ✅ Backend (Spring Boot)
- ✅ REST API endpoints (CRUD)
- ✅ Autenticación JWT
- ✅ Gestión de usuarios
- ✅ Gestión de libros
- ✅ Carrito de compras
- ✅ Validación de entradas
- ✅ Manejo de excepciones global
- ✅ Base de datos MySQL

### 📊 Bases de Datos

**Esquema ER:**
```
USUARIOS
├── email (PK)
├── username (UNIQUE)
├── nombre
├── password (BCrypt)
└── is_admin

LIBROS
├── id (PK)
├── titulo
├── autor
├── paginas
├── descripcion
├── imagen_url
└── precio

CARRO
├── id (PK)
├── usuario_email (FK → USUARIOS)
├── libro_id (FK → LIBROS)
└── created_at
```

## 🚀 Instalación y Ejecución

### Requisitos Previos
- **Java 11+**
- **Maven 3.6+** o **Gradle 8.0+**
- **MySQL 8.0+** (o Docker)
- **Android Studio** (para el frontend)
- **Docker + Docker Compose** (opcional, para BD)

### Backend (Spring Boot)

**Opción 1: Con Docker**
```bash
# Iniciar MySQL + phpMyAdmin
docker-compose up -d

# Compilar y ejecutar
cd backend
mvn clean install
mvn spring-boot:run
```

**Opción 2: Con MySQL local**
```bash
# Crear BD
mysql -u root -p
> CREATE DATABASE libros_db;

# Ejecutar
cd backend
mvn clean install
mvn spring-boot:run
```

**Base de datos disponible en:**
- API: http://localhost:8080/api
- phpMyAdmin: http://localhost:8081 (usuario: root, password: root)

### Frontend (Android)

```bash
# En Android Studio
1. Open Project → MobilesTAV-main
2. Sync Gradle
3. Run on emulator or device
4. App port: http://localhost:8080/api (configurado en GoogleBooksClient)
```

## 📚 Endpoints de API

### Autenticación
```bash
POST   /api/auth/registro    - Registrar usuario
POST   /api/auth/login       - Login
```

### Libros
```bash
GET    /api/libros           - Obtener todos
GET    /api/libros/{id}      - Obtener por ID
POST   /api/libros           - Crear (admin)
PUT    /api/libros/{id}      - Actualizar (admin)
DELETE /api/libros/{id}      - Eliminar (admin)
```

### Usuarios
```bash
GET    /api/usuarios         - Listar todos (admin)
GET    /api/usuarios/{email} - Obtener por email
PUT    /api/usuarios/{email}/admin - Cambiar rol
```

### Carrito
```bash
GET    /api/carro/{email}              - Ver carrito
POST   /api/carro/{email}/agregar/{id} - Agregar libro
DELETE /api/carro/{email}/quitar/{id}  - Quitar libro
DELETE /api/carro/{email}/limpiar      - Limpiar carrito
GET    /api/carro/{email}/total        - Calcular total
```

## 🧪 Tests

```bash
# Ejecutar tests
cd backend
mvn test

# Con cobertura
mvn test jacoco:report
# Reporte: target/site/jacoco/index.html
```

## 📦 APK Firmado

```bash
# Generar keystore
keytool -genkey -v -keystore keystore.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias evaluacion2

# Compilar APK de release
./gradlew bundleRelease

# APK: app/build/outputs/bundle/release/
```

## 🔒 Seguridad

- ✅ Contraseñas encriptadas con **BCrypt**
- ✅ Autenticación con **JWT** (24 horas de expiración)
- ✅ Validación de entradas (annotations)
- ✅ Manejo de excepciones global
- ✅ CORS configurado para desarrollo

## 📝 Requisitos del Proyecto

- ✅ Google Books API integrada
- ✅ Interfaz mejorada (imágenes a la izquierda)
- ✅ Carrito persistente por usuario
- ✅ Spring Boot Microservices
- ⏳ Unit Tests (80% coverage) - En progreso
- ⏳ APK Firmado - En progreso
- ⏳ Documentación - En progreso

## 🔗 Referencias

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [JWT.io](https://jwt.io)
- [Google Books API](https://developers.google.com/books)

## 👤 Autor

**Luis Cartagena**
- GitHub: https://github.com/LuisCartagena123/MobilesTAV
- Email: luis@example.com

---

**Proyecto:** Evaluación 2 - Sistema de Gestión de Libros  
**Fecha:** 29 de Enero de 2026  
**Estado:** En Desarrollo ✨
