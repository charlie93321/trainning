<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: DXM_0093
  Date: 2020/9/2
  Time: 15:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>欢迎</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/statics/css/bootstrap.min.css"/>
    <script type="application/javascript" src="${pageContext.request.contextPath}/statics/js/jquery.min.js"></script>
    <script type="application/javascript" src="${pageContext.request.contextPath}/statics/js/bootstrap.min.js"></script>


    <style>
    </style>
</head>
<body class="container">

<table class="table table-striped table-hover table-condensed table-bordered">
    <thead>
    <tr class="blueTr" style="height: 15px;text-align: center;font-size: 16px">
        <th>id</th>
        <th>姓名</th>
        <th>年龄</th>
        <th>描述</th>
    </tr>
    </thead>
    <tbody>

    <c:forEach var="item" items="${contents}">
        <tr>
            <td class="blueTr">
                    ${item.id}
            </td>
            <td class="blueTr">
                    ${item.name}
            </td>
            <td class="blueTr">
                    ${item.age}
            </td>
            <td class="blueTr">
                    ${item.desc}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
