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

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css"
          integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <title>Konwerter plików</title>
</head>
<body>
<div class="wrapper">
        <h1>Wybierz rodzaj konwersji</h1>
        <div class="menu">
                <button class="btn btn-primary"
                        onclick="window.location.href = 'app/wordToPdf'">Word -&gt; PDF
                </button>
                <button class="btn btn-primary"
                        onclick="window.location.href = 'app/pdfToPng'">PDF -&gt; PNG
                </button>
        </div>
</div>
</body>
</html>
