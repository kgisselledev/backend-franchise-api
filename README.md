# API Test Backend - backend-franchise-api

Aplicacion APIRestful para lista de franquicias. Realizado con Java 17, SpringBoot 2.6.7, Bases de Datos No SQL: MongoDB, Apache Maven 4.0.0, empaquetado JAR, pruebas unitarias JUnit5.
Programación reactiva con Reactor y WebFlux. Arquitectura Limpia con separación de responsabilidades del sistema, con escabilidad y mantenibilidad del código.
Aplicación contenarizada en Docker para facilitar el despliegue y portabilidad.

# Imagen publicada en DockerHub
DockerHub: https://hub.docker.com/layers/luna10/api-franchise/latest/images/sha256-e4387cf0e76b93652119679f1ca9201304e25c5082849f4aab1bdf85e6d9c43e?tab=layers

# Requisitos

* Java versión 17
* Spring Boot 2.6.7
* MongoDB o MongoDB Compass
* Docker
* Postman
* IDE Intellij IDEA
* Git

# Instalación

1. Clonar el repositorio con el comando: git clone https://github.com/kgisselledev/backend-franchise-api.git
2. Acceder al directorio con: backend-franchise-api
3. Compilar el proyecto con la consola en Maven: mvn clean package
4. Construir la imagen Docker en la consola: docker build -t franchise-api:latest .
5. Ejecutar el Contenedor Docker con el comando: docker run -p 8080:8080 franchise-api:latest
6. Probar el API en postman con http://localhost:8081/api/v1

# Importar Bases de Datos a MongoDB

1. Localizar el archivo exportado JSON api_franchise_db.franchises.json
2. En la consola enviar el comando: mongoimport --db <nombre_base_datos> --collection <nombre_coleccion> --file <ruta_al_archivo>.json --jsonArray
3. Si desea usar MongoDB Compass en el menu de Connect seleccionar importar conexiones guardadas y seleccionar el archivo exportado y al final importar.

# Importar Colección de Postman
1. Abrir Postman y seleccionar en el menú File y luego Import
2. Seleccionar el archivo Lista Franquicias.postman_collection.json y se importa la colección

# Endpoints
# 1. Crear franquicia:
   POST: /api/v1/franchises
   request:
   {
      "name": "Frisby",
      "branches": []
    }

# 2. Crear Sucursal:
   POST: /api/v1/franchises/{franchiseName}/branches
   request:
   {
  "name": "Soacha",
  "products": []
  }

# 3. Crear producto en Sucursal:
POST: /api/v1/franchises/{franchiseName}/branches/{branchName}/products
   request:
   {
  "name": "Hamburguesa junior",
  "stock": 5
}

# 4. Modificar nombre de Franquicia:
   PUT: /api/v1/franchises/{franchiseName}
   request:
   {
  "name": "Cream Helado"
  }

# 5. Modificar nombre de Sucursal:
    PUT: /api/v1/franchises/{franchiseName}/branches/{branchName}
   request:
   {
  "name": "Fontibon"
  }
# 6. Modificar nombre de Producto:
   PUT: /api/v1/franchises/{franchiseName}/branches/{branchName}/stock
   request:
  {
  "name": "Crepe personal",
  "stock": 200
}

# 7. Modificar stock de Producto:
    PUT: /api/v1/franchises/{franchiseName}/branches/{branchName}/name
   request:
  {
  "name": "Picada de carne",
  "stock": 100
}

# 8. Listar productos con mayor stock por sucursal:
    GET: /api/v1/franchises/{franchiseName}/products
# 9. Listar franquicias:
   GET: /api/v1/franchises/
# 10. Eliminar producto:
    DELETE: /api/v1/franchises/{franchiseName}/branches/{branchName}/products/{productName}


