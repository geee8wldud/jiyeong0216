<!-- 게시글 작성하는 view -->

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
<title>게시글 작성</title>
</head>
<body>
	<jsp:include page="main.jsp" flush="false"/>
	<div id="content">
    <h3>게시글 작성</h3>
    <div style="padding : 30px;">
        <form method="POST" action="/board/post">
          <div class="form-group">
            <label>제목</label>
            <input type="text" name="subject" class="form-control">
          </div>
          <div class="form-group">
            <label>작성자</label>
            <input type="text" name="username" class="form-control">
          </div>
          <div class="form-group">
            <label>내용</label>
            <textarea name="contents" class="form-control" rows="10"></textarea>
          </div>
          <button type="submit" class="btn btn-default">작성</button>
        </form>
    </div>
    </div>
</body>
</html>