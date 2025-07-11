# API de Registro de Usuarios

API REST desarrollada en Spring Boot que permite registrar usuarios con sus datos personales y teléfonos, aplicando validaciones y generando un token JWT al momento del registro.

## Requisitos

- Java 17 o superior
- Gradle

## Instalación y ejecución

```bash
./gradlew bootRun
```

## Tests

```bash
./gradlew test
```

## Endpoints

- `POST /api/usuarios` - Crea un nuevo usuario

### Body de ejemplo

```json
{
  "name": "Juan Rodriguez",
  "email": "juan@rodriguez.org",
  "password": "Password12",
  "phones": [
    {
      "number": "123456789",
      "citycode": "1",
      "contrycode": "56"
    }
  ]
}
```

### Respuesta de éxito

```json
{
  "mensaje": "El usuario se creó exitosamente",
  "data": {
    "id": "uuid",
    "name": "nombre",
    "email": "email",
    "created": "fecha",
    "modified": "fecha",
    "lastLogin": "fecha",
    "token": "jwt-token",
    "active": true
  }
}
```

## Diagrama de la Solución

- [Diagrama de arquitectura](./docs/diagrama-arquitectura.png)
- [Diagrama entidad relación](./docs/diagrama-er.png)

## Base de Datos

- H2 en memoria
- Consola disponible en `http://localhost:8080/h2-console`
- Usuario: `sa` | Contraseña: _(vacía por defecto)_
