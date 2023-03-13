# Files Converter Application

This application allows you to convert different file formats.

## Functions:
Conversions:
1. Word (DOC, DOCX) -> PDF;
2. PDF -> PNG (ZIP file containing several PNG files);
3. Images (ZIP file containing several JPG, JPEG, TIF, TIFF, GIF, BMP, PNG files) -> PDF;
4. Merge PDF files (ZIP file containing several PDF files -> One PDF file);
5. PDF -> TXT;

## Used technologies and libraries:
1. Java
2. Maven
3. Spring Boot
4. Free Spire.Doc
5. Apache PDFBox Tools
6. Apache Commons IO
7. Lombok
8. Spring Boot Starter Test, JUnit, MockMvc
9. Swagger
10. HTML, JSP, CSS
11. Bootstrap

## Environment variables that need to be set:
1. **LOGGING_FILE_NAME** - Log file path and name. Names can be an exact location (for instance, `C://logs/server.log`) or relative (for instance, `logs/server.log`) to the current directory (project root directory or directory containing packaged war/jar file). You can set an empty value ("" or " " - without quotes) when using only console logs (without saving logs to a file).
2. **TIME_ZONE** - Time zone, for instance `Europe/Warsaw`.
3. **MAX_FILE_SIZE** - Max request or file size. Value example: `100MB`.

## Steps to Setup

#### 1. Clone the repository

```bash
git clone https://github.com/marcinm312/springboot-files-converter.git
```

### Option 1

#### 2. Create a launch configuration in your favorite IDE

Using the example of IntelliJ IDE, select **JDK (Java) version 17**. Select the main class: `pl.marcinm312.filesconverter.FilesConverterApplication` and set the environment variables as described above.

#### 3. Run the application using the configuration created in the previous step.

### Option 2

#### 2. Configure the environment variables on your OS as described above

#### 3. Package the application and then run it like so

Type the following commands from the root directory of the project:
```bash
mvn clean package
java -Dfile.encoding=UTF-8 -jar target/files-converter-0.0.1-SNAPSHOT.war
```

## API documentation
After launching the application, the API documentation is available at the URL:
http://localhost:8080/swagger-ui/index.html
