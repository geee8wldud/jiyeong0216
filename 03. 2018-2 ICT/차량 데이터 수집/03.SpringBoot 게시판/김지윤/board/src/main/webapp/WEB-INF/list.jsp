<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>List</title>
</head>
<body>
<%! int x=0; %>

<h2>게시글 목록</h2>

<button class="btn btn-primary" onclick="location.href='/board/insert'">글쓰기</button>

<div class="container">
	<table class="table table-hover">
		<tr>
			<th>No</th>
			<th>Subject</th>
			<th>Writer</th>
			<th>Date</th>
		</tr>
		<% x=0; %>
		<c:forEach var="i" items="${list}">
			<tr onclick="location.href='/board/detail/${i.bno}'">
				<%x++; %>
				<th><%=x %></th>
				<th>${i.subject}</th>
				<th>${i.writer}</th>
				<th>${i.reg_date}</th>
			</tr>
		</c:forEach>
	</table>
	<!-- 
	<select name="col">
		<option value="none">전체 목록</option>
		<option value="writer">작성자</option>
		<option value="subject">제목</option>
		<option value="content">내용</option>
	</select>
	<input type="text" name="word" value="">
	<button type="submit" onclick="location.href='/search'">검색</button>
	-->
</div>
<%@ include file="bootstrap.jsp" %>
</body>
</html>