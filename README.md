# megatransact-backend

## Common settings

- Context: /api (included in the endpoints URLs)

## Development settings

- Requirements:
  - Docker and Docker Compose plugin
  - JDK 17
- Profile: default
- Server port: 8080

## Production settings (via docker compose)

- Profile: prd
- Server port: 9090

## Building docker image

- Linux and MacOS (need administrator privileges)
  - `sh build.sh`
- Linux, MacOS and Windows with Git Bash
  - `sh build_no_sudo.sh`
- Windows (need administrator privileges)
  - `build.bat`

## Other instructions

- Starting development database only:
  - `docker compose up mysqldb -d`