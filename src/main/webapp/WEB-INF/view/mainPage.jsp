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
    <div>
        <h1>Word -> PDF</h1>
        <form:form method="post" action="/convertWordToPdf" enctype="multipart/form-data">
            <input type="file" name="file" accept=".doc, .docx" required/>
            <input type="submit" class="btn btn-primary" value="Konwertuj do PDF!"/>
        </form:form>
        <p class="message">${result}</p>
    </div>
</div>
</body>
</html>
