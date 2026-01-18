@echo off
setlocal enabledelayedexpansion

echo Compiling Library Management System with Enhanced Dashboard...

REM Clean and create bin directory
if exist bin (
    rmdir /s /q bin
)
mkdir bin

REM Compile all Java files
echo Compiling Java source files...
javac -d bin ^
    com\lms\Book.java ^
    com\lms\CsvUtils.java ^
    com\lms\LibraryManager.java ^
    com\lms\Loan.java ^
    com\lms\Member.java ^
    com\lms\UserSettings.java ^
    com\lms\ui\LibraryAppGUI.java ^
    com\lms\ui\SplashScreen.java ^
    com\lms\ui_enhancements\DashboardPanel.java^
    com\lms\ui_enhancements\EnhancedGUI.java^
    com\lms\ui_enhancements\ModernButton.java^
    com\lms\ui_enhancements\ThemeManager.java

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo COMPILATION FAILED - See errors above
    echo ========================================
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo Compilation successful!
echo ========================================
echo.

REM Create JAR file
echo Creating JAR file...

set "CURRENT_JAR_CMD=jar"

REM Attempt to use JAVA_HOME if set
if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\jar.exe" (
        set "CURRENT_JAR_CMD=%JAVA_HOME%\bin\jar.exe"
        echo Using jar from JAVA_HOME: %JAVA_HOME%
    ) else (
        echo Warning: JAVA_HOME is set but jar.exe not found in "%JAVA_HOME%\bin". Attempting default locations.
    )
)

REM If CURRENT_JAR_CMD is still 'jar', try common JDK 25 path
if "%CURRENT_JAR_CMD%"=="jar" (
    if exist "C:\Program Files\Java\jdk-25\bin\jar.exe" (
        set "CURRENT_JAR_CMD=C:\Program Files\Java\jdk-25\bin\jar.exe"
        echo Using jar from assumed JDK 25 installation path: C:\Program Files\Java\jdk-25
    ) else if exist "C:\Program Files\Java\jdk-17\bin\jar.exe" (
        set "CURRENT_JAR_CMD=C:\Program Files\Java\jdk-17\bin\jar.exe"
        echo Using jar from assumed JDK 17 installation path: C:\Program Files\Java\jdk-17
    ) else if exist "C:\Program Files\Java\jdk-11\bin\jar.exe" (
        set "CURRENT_JAR_CMD=C:\Program Files\Java\jdk-11\bin\jar.exe"
        echo Using jar from assumed JDK 11 installation path: C:\Program Files\Java\jdk-11
    ) else (
        echo Warning: Could not locate jar.exe in common JDK paths or JAVA_HOME. Relying on system PATH.
    )
)

"%CURRENT_JAR_CMD%" cfm LibraryManagementSystem.jar META-INF\MANIFEST.MF -C bin com

if exist LibraryManagementSystem.jar (
    echo JAR file created successfully: LibraryManagementSystem.jar
    echo.
) else (
    echo Warning: JAR file creation had issues
    echo Continuing to run application from compiled classes...
    echo.
)

echo ========================================
echo Starting application...
echo ========================================
echo.

REM Run the LibraryAppGUI
java -cp bin com.lms.ui.LibraryAppGUI

