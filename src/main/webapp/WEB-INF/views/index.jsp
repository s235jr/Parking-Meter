<%--
  Created by IntelliJ IDEA.
  User: tomasz
  Date: 22.08.18
  Time: 20:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Parking Meter</title>
</head>
<body>
<h1 style="text-align: center">Parking Meter</h1>

<fieldset style="display: block; border: 1px black solid;">
    <legend>Start the parking meter</legend>
    <form action="/startPark" method="get">
        <div><label>Registration Number</label>
            <input type="text" name="regNumber" placeholder="registration number"/></div>
        <div><label>Check if you are disabled</label>
            <input type="checkbox" name="disabled"/></div>
        <div><input type="submit" value="Start"/></div>
    </form>
</fieldset>

<fieldset style="display: block; border: 1px black solid;">
    <legend>Check if the vehicle has started the parking meter</legend>
    <form action="/checkVehicle">
        <div><label>Registration Number</label>
            <input type="text" name="regNumber" placeholder="registration number"/></div>
        <div><input type="submit" value="Check vehicle"/></div>
    </form>
</fieldset>

<fieldset style="display: block; border: 1px black solid;">
    <legend>How much I have to pay for parking.</legend>
    <form action="/checkValue">
        <div><label>Registration Number</label>
            <input type="text" name="regNumber" placeholder="registration number"/></div>
        <div><input type="submit" value="Check value"/></div>
    </form>
</fieldset>

<fieldset style="display: block; border: 1px black solid;">
    <legend>Pay</legend>
    <form action="/pay">
        <div><label>Registration Number</label>
            <input type="text" name="regNumber" placeholder="registration number"/></div>
        <div><input type="submit" value="Pay"/></div>
    </form>
</fieldset>

<fieldset style="display: block; border: 1px black solid;">
    <legend>How much money I earn</legend>
    <form action="/checkProfit">
        <div><label>Check day</label>
            <input type="date" name="date"/></div>
        <div><input type="submit" value="Check profit"/></div>
    </form>
</fieldset>

</body>
</html>
