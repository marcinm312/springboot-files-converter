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
    <title>${title}</title>
</head>
<body>
<div class="wrapper">
        <h1>${title}</h1>
        <form:form method="post" enctype="multipart/form-data">
            <input type="file" name="file" accept="${acceptedFileTypes}" onclick="clearResult()" required/>
            <input type="submit" class="btn btn-success" value="Konwertuj!"/>
        </form:form>
        <p id="result" class="message">${result}</p>
        <button type="button" class="btn btn-danger" onclick="window.location.href = '../..'">Anuluj</button>
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/clearResult.js"></script>
</html>
