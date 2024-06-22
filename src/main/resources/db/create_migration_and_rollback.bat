@ECHO OFF
@REM --------------------------------------------------
@REM Currently working on: Windows 10 (add more here when working on other OSs)
@REM --------------------------------------------------
@REM
@REM This script creates files based in utc timestamp granting liabilty when creating database migrations.
@REM By default, the migration file will be empty but the rollback will be created with a warning text at the top.
@REM They will be placed inside a directory called migration and rollback.
@REM Those must be in the same directory level as the script.
@REM
@REM If you want to create a migration without rollback, use the create_migration.bat script instead.
@REM If you want to change the output directory, change MIGRATION_OUTPUT_FOLDER and/or ROLLBACK_OUTPUT_FOLDER.
@REM
@REM Just enter a name you want when asked, and it will be created with the following pattern:
@REM OUTPUT_FILE_PREFIX + CURRENT_UTC_TIMESTAMP + "__" + [MIGRATION_FILE_NAME or ROLLBACK_FILE_NAME] + OUTPUT_FILE_SUFFIX
@REM

SETLOCAL

:: SETTINGS START
SET MIGRATION_OUTPUT_FOLDER=./migration
SET ROLLBACK_OUTPUT_FOLDER=./rollback
SET OUTPUT_FILE_PREFIX=V
SET OUTPUT_FILE_SUFFIX=.sql
CALL :getCurrentUtcTimestamp CURRENT_UTC_TIMESTAMP
:: SETTINGS END

ECHO --------------------------------------------------
ECHO CREATE MIGRATION AND ROLLBACK FILE
ECHO --------------------------------------------------
ECHO Type a name for your file (blank spaces are not supported):
ECHO ---- Use snake_case_in_your_name to separate words.
ECHO ---- SIGINT (CTRL + C) to cancel.

SET /P BRANCH_NAME=""
SET "MIGRATION_PATH=%MIGRATION_OUTPUT_FOLDER:/=%"
SET "MIGRATION_PATH=%MIGRATION_PATH:\=%"
SET "MIGRATION_PATH=%MIGRATION_PATH:.=%"
SET "ROLLBACK_PATH=%ROLLBACK_OUTPUT_FOLDER:/=%"
SET "ROLLBACK_PATH=%ROLLBACK_PATH:\=%"
SET "ROLLBACK_PATH=%ROLLBACK_PATH:.=%"

SET MIGRATION_FILE_NAME=%OUTPUT_FILE_PREFIX%%CURRENT_UTC_TIMESTAMP%__%BRANCH_NAME%%OUTPUT_FILE_SUFFIX%
SET ROLLBACK_FILE_NAME=%OUTPUT_FILE_PREFIX%%CURRENT_UTC_TIMESTAMP%__rollback_%BRANCH_NAME%%OUTPUT_FILE_SUFFIX%

ECHO Creating migration output folder %MIGRATION_OUTPUT_FOLDER% (if not exists)
IF NOT EXIST %MIGRATION_PATH% (
  MKDIR %MIGRATION_PATH%
)
ECHO Creating rollback output folder %ROLLBACK_OUTPUT_FOLDER% (if not exists)
IF NOT EXIST %ROLLBACK_PATH% (
  MKDIR %ROLLBACK_PATH%
)

ECHO Creating migration file %MIGRATION_FILE_NAME% in %MIGRATION_OUTPUT_FOLDER%
TYPE NUL > "%MIGRATION_PATH%\%MIGRATION_FILE_NAME%"

ECHO Creating rollback file %ROLLBACK_FILE_NAME% in %ROLLBACK_OUTPUT_FOLDER%
ECHO -- -------------------------------------------- >> %ROLLBACK_PATH%\%ROLLBACK_FILE_NAME%
ECHO -- WARNING: THIS IS A DATABASE ROLLBACK SCRIPT! >> %ROLLBACK_PATH%\%ROLLBACK_FILE_NAME%
ECHO -- -------------------------------------------- >> %ROLLBACK_PATH%\%ROLLBACK_FILE_NAME%

ECHO --------------------------------------------------
ECHO Finished creating migration and rollback files.
ECHO --------------------------------------------------

:: START AUXILIAR FUNCTIONS
GOTO :jumpAuxiliarFunctions
:getCurrentUtcTimestamp
  FOR /F "tokens=2 delims==" %%i IN ('WMIC TIMEZONE GET BIAS /VALUE') DO SET /A inputTzOffsetMinutes=%%i
  FOR /F "delims=" %%A in ('powershell -Command "Get-Date -UFormat '%%s'"') do SET "currentDateTime=%%A"
  SET /A tzOffsetSeconds=%inputTzOffsetMinutes%*60
  SET /A currentLocalTimestamp=%currentDateTime:~0,10%
  SET /A currentLocalToUtcTimestamp=%currentLocalTimestamp%-%tzOffsetSeconds%
  SET %1=%currentLocalToUtcTimestamp%
:jumpAuxiliarFunctions
:: END AUXILIAR FUNCTIONS
