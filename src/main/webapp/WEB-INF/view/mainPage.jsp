<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
                integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <title>Konwerter plików</title>
</head>
<body>
<div class="wrapper">
        <h1>Wybierz rodzaj konwersji</h1>
        <div class="menu">
                <button class="btn btn-primary"
                        onclick="window.location.href = 'app/wordToPdf/'">Word -&gt; PDF
                </button>
                <button class="btn btn-primary"
                        onclick="window.location.href = 'app/pdfToPng/'">PDF -&gt; PNG
                </button>
                <button class="btn btn-primary"
                        onclick="window.location.href = 'app/imagesToPdf/'">Obrazy -&gt; PDF
                </button>
                <button class="btn btn-primary"
                        onclick="window.location.href = 'app/mergePdf/'">Połącz pliki PDF
                </button>
                <button class="btn btn-primary"
                        onclick="window.location.href = 'app/pdfToTxt/'">PDF -&gt; TXT
                </button>
                <button class="btn btn-primary"
                         onclick="window.location.href = 'app/jpgToPng/'">JPG -&gt; PNG
                </button>
                <button class="btn btn-primary"
                         onclick="window.location.href = 'app/pngToJpg/'">PNG -&gt; JPG
                </button>

                <button class="btn btn-outline-primary"
                                onclick="window.location.href = 'swagger-ui/index.html'">Dokumentacja API
                </button>
        </div>
</div>
</body>
</html>
