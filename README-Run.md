Running the ResumeScreener UI and Server (Windows / PowerShell)

This project contains:
- com.resumescreener.ui.ResumeUploaderUI — a Swing UI to choose a resume and parse it locally or upload to the server.
- com.resumescreener.server.ResumeServer — a tiny embedded HTTP server that accepts POST /upload and runs the same parsing + extraction pipeline.

Prerequisites
- Java 11+ JDK installed and JAVA_HOME set.
- Maven installed and on PATH (or run from an IDE that handles Maven projects).

Build (produces a fat/shaded jar)
Open PowerShell in the project root and run:

```powershell
mvn clean package
```

After a successful build a shaded jar will be available at:

```
target/ai-resume-screener-1.0.0-shaded.jar
```

Run the server

Start the server (port 8080):

```powershell
java -cp target/ai-resume-screener-1.0.0-shaded.jar com.resumescreener.server.ResumeServer
```

You should see: "ResumeServer started on port 8080"

Run the Swing UI

In another terminal/PowerShell run:

```powershell
java -cp target/ai-resume-screener-1.0.0-shaded.jar com.resumescreener.ui.ResumeUploaderUI
```

UI notes
- Choose a .pdf or .docx file using the "Choose Resume" button.
- If you want the UI to upload the file to the backend server, check "Upload to backend (http://localhost:8080)" then click "Parse Selected File".
- If unchecked, the UI will parse the file locally in-process (no server needed).
- Click "Save Result to JSON" to write the single parsed candidate to `output/resumes.json`.

Frontend and server artifacts removed

The Swing UI, embedded HTTP server, and browser upload page that were previously added have been removed/reverted.

To run the original command-line resume processing (as before):

Build and run the existing Main class (processes resumes from sample folder):

```powershell
mvn compile
java -cp target/classes com.resumescreener.Main
```

If you prefer to run from an IDE, open the project as a Maven project and run `com.resumescreener.Main`.
