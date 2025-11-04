@echo off
echo Compiling GUI Application...
javac -cp "lib/gson-2.10.1.jar;lib/pdfbox-app-3.0.5.jar;lib/poi-5.2.3.jar;lib/poi-ooxml-5.2.3.jar;lib/poi-ooxml-full-5.2.3.jar;lib/commons-compress-1.21.jar" -d target/classes src/main/java/com/resumescreener/model/Person.java src/main/java/com/resumescreener/model/Candidate.java src/main/java/com/resumescreener/extractor/DataExtractor.java src/main/java/com/resumescreener/parser/Parser.java src/main/java/com/resumescreener/parser/PDFParser.java src/main/java/com/resumescreener/parser/DocParser.java src/main/java/com/resumescreener/storage/JSONWriter.java src/main/java/com/resumescreener/ui/ResumeScreenerGUI.java

if %errorlevel% == 0 (
    echo GUI Application compiled successfully!
    echo Run 'run-gui.bat' to start the application
) else (
    echo Compilation failed!
)