<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert Form</title>
</head>
<script language="javaScript">
function check_onclick(){
	if(insert_form.subject.value==""){
		alert("제목을 입력하세요.");
		insert_form.subject.focus();
		return false;
	}
	else if(insert_form.writer.value==""){
		alert("작성자를 입력하세요.")
		insert_form.writer.focus();
		return false;
	}
	else if(insert_form.password.value==""){
		alert("패스워드를 입력하세요.");
		insert_form.password.focus();
		return false;
	}else{return true;}
}
</script>
<body>
<h2>게시글 작성</h2>
<div class="container">
	<form name="insert_form" onsubmit="return check_onclick()" action="/board/insertProc" method="post">
		<div class="from-group">
			<label for="subject">제목</label>
			<input type="text" class="form-control" id="subject" name="subject" placeholder="제목을 입력하세요.">
		</div>
		<div class="form-group">
        	<label for="writer">작성자</label>
        	<input type="text" class="form-control" id="writer" name="writer" placeholder="내용을 입력하세요.">
        </div>
        <div class="form-group">
        	<label for="content">내용</label>
        	<textarea class="form-control" id="content" name="content" rows="3"></textarea>
        </div>
        <div class="form-group">
        	<label for="password">비밀번호</label>
        	<input type="password" class="form-control" id="password" name="password">
        </div>
        <button type="submit" class="btn btn-primary">작성</button>
    </form>
    
</div>
<%@ include file="bootstrap.jsp" %>
</body>
</html>