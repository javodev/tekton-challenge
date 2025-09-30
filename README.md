<h1 align="center"> backend-challenge-v1 </h1> <br>

<p>
Project developed to connect and do whatever process in the mem database about categories.
</p>


## Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Quick Start](#quick-start)


## Features
The main features are:

* get categoty information 


## Local requirements

* [Java 21 SDK](https://www.oracle.com/java/technologies/downloads/#java21)
* [Maven 3.8.4](https://maven.apache.org/download.cgi)
* [Docker_Desktop](https://www.docker.com/products/docker-desktop/)


### Run with Docker_Desktop (Recommended)
```bash
# Clonar el repositorio
git clone https://github.com/javodev/tekton-challenge.git
cd local-file/tekton-challenge
```
```bash
# Ejecutar
docker-compose up -d
```

### Run test

```bash
$ mvn clean test
```

## Architecture
The project structure is:

* <b>business:</b> Capa de negocio, la lógica principal de la aplicación se encuentra aquí.
* <b>client:</b> Simualción de llamada a un servicio externo, se genera un número aleatorio entre 0 y 100.
* <b>expose:</b> Dentro de este paquete se encuentran los endpoints expuestos y modelos de datos que se entregan como respuesta.\
* <b>repository:</b> Capa de negocio que nos ayuda a comunicarnos con la base de datos.
* <b>Resources:</b> Aquí encontramos archivos de configuración y properties propios de nuestra aplicación.
* <b>Test:</b> Folder dónde testeamos nuestro código.

## Documentacion e Información de endpoints:
* <b>Swagger:</b>  http://localhost:8080/docs



