# Message Decoder
Decoder es una proyecto el cual determina la posiscion de una nave y un mensaje enviado a 3 satelites.

### Despliegue 📋
* Descargar e instalar Java Development Kit 8
* Configurar variable de entorno JAVA_HOME
* Descargar e instalar Apache Maven 3.6.3
* Configurar variable de entorno M2_HOME
* Importar proyecto maven a eclipse
* Ejecutar la clase **DecoderApplication.java** que se encuentra en el paquete **co.test.meli.decoder**
* Enviar requests desde un cliente REST a los siguientes end points
  * http://127.0.0.1:8080/api/v1/topsecret    
    * Json (https://github.com/hfelipe77/messages_decoder/blob/master/json.json)
  * http://127.0.0.1:8080/api/v1/topsecret_split/{satellite_name}  
    * json_split (https://github.com/hfelipe77/messages_decoder/blob/master/json_split.json)
    
    
### Tecnologías 🔧
* [Java 8](https://www.java.com/es/) - Lenguaje usado
* [Maven 3.6.3](https://maven.apache.org/) - Manejador de dependencias
* [Spring boot](https://spring.io/projects/spring-boot) - Frameweork usado en el backend
* [Trilateration](https://github.com/lemmingapex/trilateration) - Libreria para realizar triangulación
* [Eclipse 2020-09](https://www.eclipse.org/downloads/packages/release/2020-09) - IDE usado para el desarrollo3.
* [Postman](https://www.postman.com/) - Cliente Rest usado para las pruebas

### API en Heroku
El API se encuentra hosteado en heroku con los siguientes endPoints.
 * https://decoder-messages.herokuapp.com/api/v1/topsecret
 * https://decoder-messages.herokuapp.com/api/v1/topsecret_split/{satellite_name}
