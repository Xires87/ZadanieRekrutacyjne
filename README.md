### Description

Application retrieves some information about non-fork repositories for selected GitHub user.

### Technology

* Java 21
* Spring Boot 3.5.0
* Gradle 8.10

### Starting application

To run application:
```
    ./gradlew bootRun
```
To run tests:
```
    ./gradlew test
```
Local server port: `8080`

### Endpoints

* `GET` /{userName}
    - Lists all non-fork repositories for selected GitHub user
    - Example response (`/Xires87`):
```json
[
  {
    "owner_login": "Xires87",
    "repo_name": "CraftingManipulator",
    "branches": [
      {
        "name": "1.19.2-new",
        "last_commit_sha": "b4818ccff3a0a9d4528ab96bd658db2a4db3e628"
      },
      {
        "name": "1.19.3-new",
        "last_commit_sha": "803830579e886c1ff55987b47546c3e911c3896c"
      },
      {
        "name": "1.19.4-new",
        "last_commit_sha": "50ac6b3f5da380cad09e1be1ab3f4b167e859f34"
      },
      {
        "name": "1.20",
        "last_commit_sha": "c08efc1b87914aa1d7bc00b2d6c55b8272a28e92"
      },
      {
        "name": "master",
        "last_commit_sha": "7291b6f1b6cacd7c16ef9bcf05b662ffeb1efbda"
      }
    ]
  },
  {
    "owner_login": "Xires87",
    "repo_name": "FrycStructMod",
    "branches": [
      {
        "name": "master",
        "last_commit_sha": "fea0957a0b217f7eaa4c1add0c81821745e604b5"
      }
    ]
  },
  
  [...]
]
```

### Additional info

Application uses GitHub REST API 2022-11-28 - https://docs.github.com/en/rest?apiVersion=2022-11-28


