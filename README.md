# megatransact-backend

## Common settings

- Context: /api (included in the endpoints URLs)
- Requirements:
  - Docker and Docker Compose plugin
  - JDK 17

## Development settings

- Profile: dev
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

### Starting development database only:

- Open terminal
- Run command: `docker compose up mysqldb -d`

### Creating migration template file

- Open terminal
- Navigate to `src/main/resources/db` folder
  - In Windows: run command `create_migration.bat`
  - In Linux, MacOS or Windows with Git Bash: run command `create_migration.sh`
- Follow the instructions
- The corresponding migration `.sql` template file will be created inside `src/main/resources/db/migration` folder

### Creating migration and rollback template file

- Open terminal
- Navigate to `src/main/resources/db` folder
  - In Windows: run command `create_migration_and_rollback.bat`
  - In Linux, MacOS or Windows with Git Bash: run command `create_migration_and_rollback.sh`
- Follow the instructions
- The corresponding migration `.sql` template file will be created inside `src/main/resources/db/migration` folder
- The corresponding rollback `.sql` template file will be created inside `src/main/resources/db/rollback` folder
