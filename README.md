# Solución Vehículos

## Descripción general
Este proyecto implementa una solución simple de gestión de vehículos con:

- **Backend** en Java con Spring Boot
- **Frontend** en React con Vite
- **Datos en memoria**, sin base de datos

La aplicación permite:

- Consultar vehículos
- Filtrar por tipo de alerta
- Agregar nuevos vehículos
- Editar la velocidad de cada vehículo
- Recalcular la alerta de forma automática según la velocidad

## Estructura del proyecto
- `backend/`: API REST desarrollada con Spring Boot
- `frontend/`: interfaz web desarrollada con React

## Requisitos previos
Antes de ejecutar el proyecto, verificar que el entorno tenga instalado:

- **Java 17** o superior
- **Maven 3.8+**
- **Node.js**
- **npm**

## Ejecución del proyecto
### 1. Ejecutar el backend
Abrir una terminal en la carpeta del backend y ejecutar:

```bash
cd backend
mvn spring-boot:run
```

El backend quedará disponible en:

```text
http://localhost:8080/vehiculos
```

### 2. Ejecutar el frontend
Abrir una segunda terminal en la carpeta del frontend y ejecutar:

```bash
cd frontend
npm install
npm run dev
```

El frontend quedará disponible en:

```text
http://localhost:5173
```

## Funcionalidades implementadas
### Backend
Endpoints disponibles:

- `GET /vehiculos`
- `GET /vehiculos?alerta=RETRO|PARADO|OK|RIESGO|CRITICO`
- `POST /vehiculos`
- `PUT /vehiculos/{placa}/velocidad`

### Reglas de negocio
- Velocidad menor que `0` → `RETRO`
- Velocidad igual a `0` → `PARADO`
- Velocidad entre `1` y `79` → `OK`
- Velocidad entre `80` y `100` → `RIESGO`
- Velocidad mayor que `100` → `CRITICO`

### Frontend
La interfaz permite:

1. Cargar vehículos
2. Filtrar por alerta
3. Agregar un vehículo nuevo
4. Editar la velocidad de un vehículo existente
5. Visualizar los resultados en tabla

## Nota sobre despliegue
El proyecto no fue desplegado en la nube.

Se evaluó realizar el despliegue del backend en Oracle, pero el proceso de publicación y configuración requería más tiempo del disponible para la entrega. Por esa razón, se priorizó dejar documentada de forma clara la ejecución local del sistema para facilitar su revisión y validación funcional.
# proyecto_vehiculos
