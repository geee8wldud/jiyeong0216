<!-- 게시판 목록을 보는 view -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content= "text/html; charset=UTF-8">
<!-- BootStrap CDN -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/resources/css/style.css">
<title>Board</title>
</head>
<body>
	<jsp:include page="main.jsp" flush="false"/>
	<div id="content">
    <h3>게시글 목록</h3>
    <button class="btn btn-default" style="float: right; margine: 10px;" onclick="location.href='/board/post'">작성</button>
    <table class="table">
        <tr>
            <th>No</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일자</th>
            <th>조회수</th>
        </tr>
        <c:forEach var="board" items="${list}">
        <tr>
            <td>${board.board_id}</td>
            <td><a href="/board/${board.board_id}">${board.subject}</a></td>
            <td>${board.username}</td>
            <td><fmt:formatDate value="${board.regdate}" pattern="MM.dd" /></td>
            <td>${board.hits}</td>
        </tr>
        </c:forEach>
    </table>
    </div>
</body>
</html>