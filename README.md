# AI Resume Screener

## How to Compile and Run

### Method 1: Using Batch Files (Recommended)
1. Double-click `compile.bat` to compile the project
2. Double-click `run.bat` to run the project

### Method 2: Using Command Line
1. Open PowerShell/Command Prompt in the project root directory
2. Compile: `javac -cp "lib/gson-2.10.1.jar;lib/pdfbox-app-3.0.5.jar;lib/poi-5.2.3.jar;lib/poi-ooxml-5.2.3.jar" -d target/classes src/main/java/com/resumescreener/model/Person.java src/main/java/com/resumescreener/model/Candidate.java src/main/java/com/resumescreener/extractor/DataExtractor.java src/main/java/com/resumescreener/parser/Parser.java src/main/java/com/resumescreener/parser/PDFParser.java src/main/java/com/resumescreener/storage/JSONWriter.java src/main/java/com/resumescreener/Main.java`
3. Run: `java -cp "target/classes;lib/gson-2.10.1.jar;lib/pdfbox-app-3.0.5.jar;lib/poi-5.2.3.jar;lib/poi-ooxml-5.2.3.jar" com.resumescreener.Main`

## Setup
1. Place PDF or DOCX resume files in `src/main/resources/sample_resumes/` folder
2. Run the program
3. Check `output/resumes.json` for extracted data

## Supported File Formats
- PDF files (.pdf)
- Word documents (.docx)
- Note: Legacy .doc files are not supported - please convert to .docx format

## Project Structure
- `src/main/java/com/resumescreener/` - Main source code
- `lib/` - External JAR dependencies
- `target/classes/` - Compiled class files
- `output/` - Generated JSON output