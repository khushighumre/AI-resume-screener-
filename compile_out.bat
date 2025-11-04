@echo off
echo Compiling Java files to out directory...
javac -cp "lib/gson-2.10.1.jar;lib/pdfbox-app-3.0.5.jar;lib/poi-5.2.3.jar;lib/poi-ooxml-5.2.3.jar;lib/poi-ooxml-full-5.2.3.jar" -d out src\main\java\com\resumescreener\extractor\*.java src\main\java\com\resumescreener\model\*.java src\main\java\com\resumescreener\parser\*.java src\main\java\com\resumescreener\storage\*.java src\main\java\com\resumescreener\Main.java

if %errorlevel% == 0 (
    echo Compilation successful!
) else (
    echo Compilation failed!
)