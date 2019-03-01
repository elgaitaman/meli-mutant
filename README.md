# mutantz

Solución al ejercicio de MeLi sobre Magneto y el análisis de ADN.

## Tecnologías/frameworks utilizados
- [Docker](https://www.docker.com/)
- [Docker-compose](https://docs.docker.com/compose/)
- [Maven](https://maven.apache.org/)
- [Java 1.8](https://www.oracle.com/index.html)
- [Spring Boot](https://projects.spring.io/spring-boot/)
- [MongoDB](https://www.mongodb.com)
- [Slf4j](https://www.slf4j.org/)
- [JUnit](http://junit.org/junit5/)
- [Flapdoodle Embedded MongoDB](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)

## Servicios utilizados para Cloud-Computing
- [Heroku](https://www.heroku.com/)
- [mongoDB Cloud](https://cloud.mongodb.com)

## Comentarios sobre la solución planteada
* El algoritmo desarrollado para el análisis del ADN intenta optimizar tanto el tiempo de análisis como el uso de memoria. Para esto basa su proceso en leer solo una vez la matriz y mantener la ínima información necesaria para el anális, por lo tanto la matriz podría crecer en tamaño 'n' afectando linealmente su análisis.
* Según el enunciado, interpreté (ambas cosas pueden ser adaptadas con falicidad): 
	* que el tamaño de la secuencia es de cuatro exclusivo, es decir que si la cadena tiene más de cuatro letras iguales no se toman en cuenta.
	* solo toma en cuenta las cadenas oblícuas según el ejemplo.
* Para optimizar el proceso, la persistencia del ADN en la base de datos se realiza en forma ascíncrona utilizando un sistema de "qeueu". Por una cuestión de tiempo para la "qeueu" se implementó usando LinkedBlockingQueue, pero para mejorar se podría agregar un Redis encolando los ADNs, lo que permitiría también distribuir la carga.
* El envío de ADN repetido se procesa siempre ya que el algoritmo es más ópitmo que la consulta en la base de datos, si bien en la base de datos solo se guarda una sola vez el ADN correspondiente.
* El análisis de ADNs repetidos no afectan las estadísticas.
* Al guardar la información se está generando Hash de la cadena ADNs para evitar insertar repetidos

## Para iniciar el servicio localmente
Se utilizó `docker` y `docker-compose` para generar e iniciar todo el entorno necesario sin tener que instalar localmente frameworks y librerías adicionales ni modificar configuración de la API, por lo tanto se deben tener instaladas ambas aplicaciones.
Con `docker-compose` se instancia un MongoDB por un lado y la API por otro que se conecta internamente a la instacia de mongo.

Una vez clonado el repo y dentro de la carpeta del proyecto, ejecutar:

```sh
docker-compose up
```

Una vez finalizado el deploy, los servicios quedarán disponibles en:

### a) Consulta de Estadísitica:

GET http://localhost:8080/api/v1/stats

```sh
curl GET http://localhost:8080/api/v1/stats
```

### b) Análisis de ADN:

POST http://localhost:8080/api/v1/mutant 
	{
	"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
	}

```sh
curl -o /dev/null -s -w "Response -> %{http_code}\n" \
  --header "Content-Type: application/json" \
  --request POST \
  --data '{ "dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"] }' \
  http://localhost:8080/api/v1/mutant 
```

### c) Borrar Estadísiticas:

DEL http://localhost:8080/api/v1/stats

```sh
curl -X "DELETE" http://localhost:8080/api/v1/stats
```

# Despliegue de la API en "la nube"

Se utilizó el servicio gratuito de Heroku(https://www.heroku.com) para desplegar la API y el de mongoDB(https://cloud.mongodb.com) como instancia de MongoDB para la persistencia.
La URL de la aplicación desplegada en Heroku es: http://mutantz.herokuapp.com

Como es un servicio gratuito, luego de no usar la API por cierto tiempo Heroku las pone en modo "ahorro de energía" y es por eso que tarda en responder a la primer llamada. Por lo tanto para validar el estado inicialmente se puede ingresar en: http://mutantz.herokuapp.com/health

### a) Consulta de Estadísitica:

```sh
curl GET http://mutantz.herokuapp.com/api/v1/stats
```

### b) Análisis de ADN:

```sh
curl -o /dev/null -s -w "Response -> %{http_code}\n" \
  --header "Content-Type: application/json" \
  --request POST \
  --data '{ "dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"] }' \
  http://mutantz.herokuapp.com/api/v1/mutant 
```

### c) Borrar Estadísiticas:

```sh
curl -X "DELETE" http://mutantz.herokuapp.com/api/v1/stats
```

# Cobertura de UnitTests

La cobertura de los test automáticos es superior al 85%.

![screenshot con el análisis de cobertura](https://i.imgur.com/PEaSs4a.png)



