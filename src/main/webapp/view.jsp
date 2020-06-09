<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="styles.css">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Прием пищи #${meal.id}</title>
</head>
<body>
<h3><a href="index.html">Главная</a></h3>
<hr>
<h2>Прием пищи ${meal.id}</h2>

<table class="minimalistBlack">
    <thead>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
    </tr>
    </tbody>
</table>


<button onclick="window.history.back()">Назад</button>
</body>
</html>
