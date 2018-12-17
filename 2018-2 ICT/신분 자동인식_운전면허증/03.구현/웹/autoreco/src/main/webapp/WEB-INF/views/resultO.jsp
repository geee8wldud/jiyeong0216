<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- result.css 사용 -->
<link rel="stylesheet" type="text/css" href="/resources/css/result.css" />

	 <script>
		//뒤로가기 화살표 버튼 눌렀을 때 실행되는 함수
		function goBack() {
		    window.location.replace("/");
		}
		
		//원래 모바일 핸드폰에 있는 뒤로가기 버튼 비활성화
		history.pushState(null, null, location.href);
		window.onpopstate = function () {
		    history.go(1);
		};
	</script>
    <title>resultO</title>
    
</head>
  <body>
  
  <div id="Box">
      <div id="head">
	        <h3>
	        <a id="alink" onclick="goBack()"><img src="https://i.imgur.com/9WcxUGu.png" alt="arrow" weight="30" height="30">
	        </a>
	        &nbsp;&nbsp;&nbsp;운전면허증 인식   <br/><br/>
	        </h3>     
      </div>
      
      <div id="center">
     	 <img id="resultImg" src="https://i.imgur.com/kY9Nkom.png" alt="result" />
      </div>
          
      <div id="bottom">
      	<br/><br/><br/><br/><br/><br/>
      	<button class="form-submit" onClick="location.href='/'">확인 </button>
      </div>
           
  </div>     
  </body>
</html>