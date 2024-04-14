# OOPP team 12 - Splitty project

## Introduction
This is Splitty, a software used to split expenses between event participants.

## Requirements
- Java 21 is required to run this application
## How to run the application

### Server

```console
./gradlew bootRun
```

### Client

- Have the server already running
- Run:
```console
./gradlew client:run
```

### Admin

- Have the server already running
- Run:
```console
./gradlew admin:run
```

## Contributions

### Accessibility features
// TODO

### Participants
Jerzy Karremans, Rares Diaconescu, Blago Gunev, Alessandro Neri, Cristian Ţurcan, Ștefan Lupşan.

### Checkstyle rules
These are some of the checkstyle rules that we agreed on and checked throughout the project implementation:

- **NestedIfDepth** max value = 3
- **No Magic Numbers**
- **White space after comma**
- **camelCase** for variable, method names
- **PascalCase** for Class names

