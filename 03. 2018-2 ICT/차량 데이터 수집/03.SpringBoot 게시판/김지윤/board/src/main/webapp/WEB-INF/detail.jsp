<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Detail</title>
</head>
<script language="javascript">
function PWcheck(){
	var inputPW = prompt("패스워드를 입력하세요.", "");
	if(inputPW=='${detail.password}'){
		console.log('${detail.password}');
		return true;
	}else if(inputPW==null){
		alert("취소하셨습니다.");
	}
	else{
		alert("패스워드가 다릅니다.")
	}
}
</script>

<body>
<h2>게시글 상세</h2>
<button class="btn btn-primary" onclick="if(PWcheck()){location.href='/board/update/${detail.bno}'}">수정</button>
<button class="btn btn-primary" onclick="if(PWcheck()){location.href='/board/delete/${detail.bno}'}">삭제</button>
<div class="container">
	<form action="/insertProc" method="post">
		<div class="form=group">
			<label>제목</label>
			<p>${detail.subject}</p>
		</div>
		<div class="form=group">
			<label>작성자</label>
			<p>${detail.writer}</p>
		</div>
		<div class="form-group">
        	<label>작성날짜</label>
        	<p>${detail.reg_date}</p>
        </div>
        <div class="form-group">
        	<label>내용</label>
        	<p>${detail.content}</p>
        </div>
        <a href='/board/list' class="btn btn-primary">목록보기</a>
        </form>
</div>


<%@ include file="bootstrap.jsp" %>
</body>
</html>