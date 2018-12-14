<!-- 게시글 상세보기 view -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content= "text/html; charset=UTF-8">
<!-- BootStrap CDN -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/resources/css/style.css">
<title>게시글 상세</title>
</head>
<body>
	<jsp:include page="main.jsp" flush="false"/>
	<div id="content">
	<div id="head">
    	<h2><a href="/board/${board.board_id}">${board.subject}</a></h2>
    </div>
    <div id="body">
      <div id="article_info" style= "float:right;">
       	<label><fmt:formatDate value="${board.regdate}" pattern="yyyy.MM.dd HH:mm" /></label>
        <p><label>작성자: ${board.username}</label></p>
      </div>
      <div id="article">${board.contents}</div>
      <div style= "float:right;">
           이 글을 <label>${board.hits+1}</label>번째 보는 사람이에요
      </div>
      </div>
      <button class="btn btn-default" style = "float: left;" onclick='location.href="/board/post/${board.board_id}"'>수정</button>
      <form:form action="/board/post/${board.board_id}" method="DELETE"><a></a>
      <button type="submit" class="btn btn-default">삭제</button>
      </form:form>
    </div>
</body>
</html>