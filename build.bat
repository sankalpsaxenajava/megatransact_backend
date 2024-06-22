@ECHO OFF
SETLOCAL
CALL :getIsAdmin IS_ADMIN

IF %IS_ADMIN%==true (
    ECHO Building backend docker image.
    docker build --rm -t megatransact-backend .

    ECHO Cleaning untagged docker images.
    FOR /F "tokens=*" %%i IN ('docker images -q -f "dangling=true"') DO (
        IF NOT "%%i"=="" (
            docker rmi -f %%i
        )
    )
) ELSE (
    ECHO.
    ECHO WARNING: THIS SCRIPT MUST BE EXECUTED WITH ADMINISTRATOR PRIVILEGES, EXITING.
    ECHO.
)

@REM START AUXILIAR FUNCTIONS
GOTO :jumpAuxiliarFunctions
:getIsAdmin
NET SESSION >nul 2>&1
    IF %ERRORLEVEL% == 0 (
        SET %~1=true
    ) ELSE (
        SET %~1=false
    )

:jumpAuxiliarFunctions
@REM END AUXILIAR FUNCTIONS
