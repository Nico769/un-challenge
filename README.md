# Unravel Challenge Setup

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── io/
│   │       └── unravel/
│   │           └── challenge/    # A package for each exercise
│   │               ├── first/    # Each package contains a README on how the exercise was addressed
│   │               ├── second/
│   │               ├── third/
│   │               ├── fourth/
│   │               └── fifth/
│   └── resources/                # Configuration files
└── test/                         # Unit tests for the first exercise only
```

## Prerequisites

Before you begin, ensure you have the following tools installed:

- **Java 17+**
- **Git**
- **IntelliJ IDEA** (for easily running each exercise)
- **VisualVM** 
- **Apache Bench**

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
```

### 2. Import Project in IntelliJ

- Open IntelliJ IDEA
- Select **File → Open** and choose the project directory
- IntelliJ will automatically detect the Maven project

### 3. Running the exercises

- For the first exercise, there's no runner since the instrumenting code is the [SessionManagerTest](src/test/java/io/unravel/challenge/first/SessionManagerTest.java) test class
- For the other exercises, select one the predefined run configurations in the IDE toolbar and run it

### 4. Running the last exercise

- For the last exercise, it's more convenient to close the IDE and start the Spring Boot application with the following command
```bash
./mvnw spring-boot:run \
  -Dspring-boot.run.jvmArguments=" \
    -Dcom.sun.management.jmxremote \
    -Dcom.sun.management.jmxremote.port=9010 \
    -Dcom.sun.management.jmxremote.rmi.port=9010 \
    -Dcom.sun.management.jmxremote.authenticate=false \
    -Dcom.sun.management.jmxremote.ssl=false \
    -Djava.rmi.server.hostname=localhost"
```
- Launch VisualVM, it should automatically detect the JVM process
- Run the following command for load testing with Apache Bench (this example will run 1000 requests with 10 concurrent connections)
```bash
ab -n 1000 -c 10 http://localhost:8080/benchmark
```
