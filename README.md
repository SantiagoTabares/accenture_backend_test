# To-Do List Backend - Prueba Técnica

## 📋 Descripción
Backend reactivo para gestión de tareas desarrollado con:
- API REST reactiva para crear, actualizar, eliminar y listar tareas por categoría.
- Arquitectura limpia con separación de capas: `application`, `domain`, `infrastructure`.
- Persistencia en **MongoDB** de manera no bloqueante.
- Pruebas unitarias con **JUnit 5 y Mockito**.
- Documentación automática de API con **Swagger OpenAPI (springdoc)**.
- Despliegue y entorno reproducible con **Docker Compose**.
- Seguridad basada en JWT.

## 🏗️ Estructura Clean Architecture

```text
src/main/java/com/accenture/to_do_List_backend/
├── application/          # Capa de aplicación
│   ├── dto/              # Objetos de transferencia
│   └── service/          # Lógica de negocio reactiva
├── domain/               # Núcleo del dominio
│   ├── model/            # Entidades
│   └── repository/       # Interfaces repositorio
└── infrastructure/       # Detalles de infra
    ├── config/           # Configuraciones
    └── web/              # Controladores y excepciones
```
### ¿Por qué Clean Architecture?
Se sigue Clean Architecture para garantizar:

**Separación de responsabilidades:** cada capa tiene un propósito claro.

**Independencia de frameworks:** el dominio no depende de Spring.

**Alta mantenibilidad y testabilidad:** cada componente puede ser probado de forma aislada.

**Escalabilidad:** permite adaptar o cambiar tecnología sin afectar reglas de negocio.

## 🚀 Instrucciones para ejecutar con Docker Compose

### ✅ Requisitos previos

- Tener instalado [Docker](https://www.docker.com/get-started) y [Docker Compose](https://docs.docker.com/compose/).
- Verificar que el puerto `8080` (backend) y `27017` (MongoDB) estén libres.

### 📂 Estructura relevante

El proyecto contiene un archivo `docker-compose.yml` que define:

- Un contenedor de MongoDB.
- El contenedor de la aplicación backend construido desde el `Dockerfile`.

### 📦 Paso a paso para construir y levantar los contenedores

1. **Clonar el repositorio**:

```bash
git clone https://github.com/SantiagoTabares/to-do_List_backend.git
cd to-do_List_backend
```

**NOTA:** Es necesario tener instalado Java 17 y Maven para compilar el proyecto, para probar se agrega el .jar en el repo  de necesitar cambiarlo se ejecuta:

```bash
 ./mvnw clean install -U
```

2. **Construir y levantar servicios**:

```bash
docker-compose up --build
```

Para detener los servicios, usa:

```bash
docker-compose down
```
3. **Verificar que todo esté funcionando**:

   - Accede a la API en `http://localhost:8080/webjars/swagger-ui/index.html`.

## Funcionamiento de la API

Desde la interfaz de Swagger, puedes los endpoints disponibles, pero se debe realizar autenticación para acceder a los recursos de tareas y categorías.

### Autenticación
Para autenticarte, utiliza el endpoint `/auth/register` para crear un usuario y luego `/auth/login` para obtener un token JWT. Este token se debe incluir en el header `Authorization` de las peticiones a los endpoints protegidos.:

![img_1.png](img_1.png)

Con el token obtenido se agrega en 'Authorize' en la parte superior, puedes acceder a los endpoints de tareas y categorías. Por ejemplo, para listar las tareas:

![img_2.png](img_2.png)

**Nota:** El SECRET_KEY esta en el repositorio ya que es una prueba tecnica, en un entorno real se recomienda almacenarlo de forma segura y no exponerlo en el código fuente.

Ya con eso se podrán probar todas las funcionalidades de la API, como crear, actualizar y eliminar tareas y  categorías.



## 🧪 Pruebas unitarias
Las pruebas fueron desarrolladas con JUnit 5 y Mockito, enfocadas en los servicios y controladores principales:
- AuthControllerTest

- AuthServiceImplTest

- CategoryControllerTest

- CategoryServiceImplTest

- TaskControllerTest

- TaskServiceImplTest

Las pruebas unitarias están implementadas utilizando JUnit 5 y Mockito. Para ejecutarlas, puedes usar el siguiente comando:

```bash
./mvnw test
```

![img.png](img.png)


## 🔧 Posibles mejoras
- **Implementar paginación y ordenamiento** en las listas de tareas y categorías.
- **Agregar validaciones más robustas** en los DTOs y servicios.
- **Implementar pruebas de integración** para verificar el flujo completo.
- **Mejorar la seguridad** con roles y permisos más granulares.
- **Optimizar consultas a MongoDB** para mejorar el rendimiento en grandes volúmenes de datos.
- **Uso de mapper para DTOs** para evitar código repetitivo en conversiones.
- **Cambiar logica para que las tareas esten rellacionadas a cada usuario**

## 📞 Contacto
Para cualquier duda o consulta, puedes contactarme a través de:
- Correo: satabaresmo@gmail.com
