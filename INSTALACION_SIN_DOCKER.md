# Guía de Instalación - Sin Docker

## Paso 1: Instalar MySQL

### En Windows:
1. Descarga MySQL Community Server desde: https://dev.mysql.com/downloads/mysql/
2. Ejecuta el instalador
3. Usa credenciales por defecto:
   - Usuario: `root`
   - Contraseña: `root`
4. Instala MySQL Workbench (opcional, para administrar la BD)

### En Linux (Ubuntu):
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

### En macOS:
```bash
brew install mysql
brew services start mysql
mysql_secure_installation
```

## Paso 2: Crear la Base de Datos

Abre una terminal MySQL y ejecuta:

```bash
# Con línea de comandos
mysql -u root -p
```

Luego copia y pega el contenido de `backend/src/main/resources/db/init.sql`:

```sql
CREATE DATABASE IF NOT EXISTS mobilestav;
USE mobilestav;
-- (el resto del archivo init.sql)
```

O directamente desde terminal:
```bash
mysql -u root -proot < backend/src/main/resources/db/init.sql
```

## Paso 3: Compilar el Backend

```bash
cd backend
mvn clean package
```

## Paso 4: Ejecutar el Backend

```bash
java -jar target/evaluacion2-backend-1.0.0.jar
```

El backend estará en: `http://localhost:8080/api`

## Credenciales Admin

- **Email:** `admin@mobilestav.com`
- **Usuario:** `admin`
- **Contraseña:** `admin123`

## Solucionar Problemas

### Error: "Connection refused" (Puerto 3306)
- MySQL no está corriendo
- En Windows: revisar que MySQL Service esté iniciado
- En Linux: `sudo service mysql start`

### Error: "Access denied"
- Verificar credenciales en `application.yml`
- Por defecto: usuario `root`, contraseña `root`

### Error: "Database doesn't exist"
- Ejecutar el script `init.sql` de nuevo

## Para EC2

1. Instala MySQL en la instancia EC2
2. Copia el archivo `init.sql`
3. Ejecuta: `mysql -u root -p < init.sql`
4. Compila y ejecuta el backend
5. Abre puerto 8080 en el Security Group de EC2
