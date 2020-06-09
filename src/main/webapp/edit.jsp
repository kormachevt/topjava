<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="styles.css">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Прием пищи</title>
</head>
<body>
<h3><a href="index.html">Главная</a></h3>
<hr>
<h2>Прием пищи</h2>
<form method="post" action="meals" id="createForm" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="id" value="${meal.id}">

    <label for="datetime">Дата/Время:</label><br>
    <input type="datetime-local" id="dateTime" name="dateTime" value="${meal.dateTime}"><br>

    <label for="description">Описание:</label><br>
    <input type="text" id="description" name="description" size=50 value="${meal.description}"><br>

    <label for="calories">Калорийность</label><br>
    <input type="number" id="calories" name="calories" value="${meal.calories}"><br>
</form>
<br>
<br>
<button type="submit" form="createForm">Сохранить</button>
<button onclick="window.history.back()">Назад</button>
</body>
</html>
