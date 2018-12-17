<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- result.css 사용 -->
<link rel="stylesheet" type="text/css" href="/resources/css/result.css" />
<!-- jquery 사용을 위한 코드 -->
<script type="text/javascript" src="webjars/jquery/2.2.4/jquery.min.js"></script>
        
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

<title>resultX</title>

  </head>
  <body>
  
  <div id="Box">
  
      <div id="head">

	        <h3>
	        <a id="alink" onclick="goBack()"><img src="https://i.imgur.com/9WcxUGu.png" alrt="arrow" weight="30" height="30">
	        </a>
	        &nbsp;&nbsp;&nbsp;운전면허증 인식   <br/><br/>
	        </h3>
        
      </div>
      
      <div id="center"> 
      	<img id="resultImg" src="https://i.imgur.com/yM77Nbs.png" alrt="result"/>
        <br/>
     
		<%
		   //파라미터 값으로 결과코드를 받아와 그에 맞는 결과화면을 보여준다. 
			String rtn_code=request.getParameter("rtn_code");
				
				if(rtn_code.equals("01")){  
				   out.print("<h3>면허 번호가 없습니다.</h3>");
				}else if(rtn_code.equals("02")){
				   out.print("<h3>재발급된 면허입니다.</h3>");
				    
				}else if(rtn_code.equals("03")){
				   out.print("<h3>분실된 면허입니다. </h3>");
				    
				}else if(rtn_code.equals("04")){
				     out.print("<h3>사망 취소된 면허입니다. </h3>");
				    
				}else if(rtn_code.equals("11")){
				     out.print("<h3>취소된 면허입니다. </h3>");
				 
				}else if(rtn_code.equals("12")){
				     out.print("<h3>정지된 면허입니다. </h3>");
				    
				}else if(rtn_code.equals("13")){
				       out.print("<h3>기간 중 취소면허입니다. </h3>");
				     
				}else if(rtn_code.equals("14")){
				      out.print("<h3>기간 중 정지면허입니다. </h3>");
				     
				}else if(rtn_code.equals("21")){
				   out.print("<h3>정보가 불일치합니다.(이름)</h3>");
				    
				}else if(rtn_code.equals("22")){
				  out.print("<h3>정보가 불일치합니다.(생년월일)</h3>");
					   
				}else if(rtn_code.equals("23")){
					  out.print("<h3>정보가 불일치합니다.(암호일련번호)</h3>");
						    
				}else if(rtn_code.equals("24")){
				 out.print("<h3>정보가 불일치합니다.(면허종별)</h3>");
				
				}else if(rtn_code.equals("31")){
					    out.print("<h3>암호화가 되지 않은 면허입니다. </h3>");	    
				}
		   %>
		      	
      </div>
      
      
      <div id="bottom">
	      <br/><br/><br/>
	      <button class="form-submit" onClick="location.href='/'">확인 </button>
      </div>
  </div>
 </body>
</html>