<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page contentType="text/html; charset=utf8"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>貸出一覧履歴｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
        <h2>貸出履歴一覧</h2>
        <div class="container">
            <table class="table table-bordered">
                <thead class="table-primary">
                    <tr>
                        <th scope="col">書籍名</th>
                        <th scope="col">貸出日</th>
                        <th scope="col">返却日</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="rentbookInfo" items="${rentBookList}">
                        <tr>
                            <th scope="row">
                                <form method="post" action="<%=request.getContextPath()%>/details">
                                    <input type="hidden" name="bookId" value="${rentbookInfo.bookId}"> <a href="javascript:void(0)" onclick="this.parentNode.submit();">${rentbookInfo.title}</a>
                                </form>
                            </th>
                            <td>${rentbookInfo.rentDate}</td>
                            <td>${rentbookInfo.returnDate}</td>
                        </tr>
                    </c:forEach>
            </table>
        </div>
    </main>
</body>
</html>
