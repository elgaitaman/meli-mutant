FROM openjdk:8-jdk-alpine as dependencies-resolver
	WORKDIR /app
	COPY ./mvnw pom.xml ./
	COPY ./.mvn/wrapper/ ./.mvn/wrapper
	RUN /bin/chmod +x ./mvnw
	RUN ./mvnw verify --fail-never
		            
FROM dependencies-resolver as builder
	WORKDIR /app
	COPY ./src ./src
	RUN ./mvnw clean package

FROM builder
	WORKDIR /app
	RUN apk --no-cache add curl unzip bash
	COPY --from=builder /app/target/app.jar ./app.jar
	EXPOSE 8080
	CMD ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-jar", "app.jar"]
	
