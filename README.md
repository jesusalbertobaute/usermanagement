# usermanagement

Es una Api Rest para crear y administrar usuarios.

## Requerimientos
- Java 17 o superior.
- JAVA_PATH correctamente configurada como variable de entorno.

## Pasos para ejecutar la aplicaci&oacute;n

1. Ejecutar en la linea de comandos del sistema operativo el comando siguiente:
* Windows
````
mvnw.cmd spring-boot:run
````
* Linux
````
./mvnw spring-boot:run
````
2. Autenticarse como usuario administrador:
-  Ejecutar la solicitud HTTP: 
````
POST /user/login HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Content-Length: 66

{
   "email": "jesus@baute.org",
   "password": "hmn!gLgfsu2"
}
````
- Se obtendr&aacute; una respuesta similar a esta:
````
{
    "message": "Usuario Autenticado con Exito",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqZXN1c0BiYXV0ZS5vcmciLCJpYXQiOjE3MTIU..."
}
````
3. Registrar un usuario:
- Ejecutar la solicitud http siguiendo el patrón siguiente:
````
POST /user/register HTTP/1.1
Host: localhost:8080
Authorization: Bearer {{token}}
Content-Type: application/json
Content-Length: 187

{
"name": "usuario",
"email": "usuario@email.org",
"password": "passwordsecreto",
"phones": [
    {
    "number": "1234577",
    "citycode": "1",
    "contrycode": "56"
    }
]
}
````
> Usar el token obtenido previamente.
- Se obtendr&aacute; una respuesta similar a esta:
````
{
    "message": "Usuario Agregado con Exito",
    "user": {
        "id": "236ea570-72e5-45d5-9c90-290277b3b08e",
        "creationDate": "05-04-2024 07:46:14",
        "modifiedDate": "05-04-2024 07:46:14",
        "lastLogin": "05-04-2024 07:46:14",
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "active": true
    }
}
````
> Nota: para ejecutar las solicitudes HTTP se puede usar la colecci&oacute;n de postman subida al proyecto.

## Notas Adicionales
- Todos los usuarios creados con la app son usuarios de bajo nivel.
- Solo un usuario administrador se crea por defecto y se inserta autom&aacute;ticamente en la base de datos. Las credenciales del usuario son: 
````
{
   "email": "jesus@baute.org",
   "password": "hmn!gLgfsu2"
}
````
- Para configurar el formato del password se debe editar la propiedad "application.security.password.format" dentro del archivo secure-application.properties.
- En condiciones normales no se coloca ninguna credencial en el repositorio, sin embargo, para facilidad y comodidad del evaluador se sube el archivo secure-application.properties y se colocan credenciales en este archivo de README.
- Se cre&oacute; otro endpoint adicional para un usuario sin privilegios, para acceder al mismo basta con hacer la siguiente solicitud HTTP:
````
GET /user/verify HTTP/1.1
Host: localhost:8080
Authorization: Bearer {{token creado previamente cuando se registro el usuario}}
````
## Diagrama de la Soluci&oacute;n:
![Ver diagrama en src/resources/images](https://raw.githubusercontent.com/jesusalbertobaute/usermanagement/0d192327f346113b7726a63aa10e0fbb3f3ef6ed/src/main/resources/images/diagrama.svg)