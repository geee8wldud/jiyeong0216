<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <%@ page session="true"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Cache-Control" content="no-cache"/> 
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="Pragma" content="no-cache"/> 
<!-- main.css 사용 -->
<link rel="stylesheet" type="text/css" href="/resources/css/main.css" />

<script>
	//뒤로가기 화살표 버튼 눌렀을 때 실행되는 함수
	function goBack() {
	    window.history.back();
	}
	
	// OS 확인- android와 iphone 구분해주는 함수 
	var uAgent = navigator.userAgent.toLocaleLowerCase();
	if(uAgent.indexOf("android") != -1)  
	    OSName = "android";
	else if(uAgent.indexOf("iphone") != -1 || uAgent.indexOf("ipad") != -1 || uAgent.indexOf("ipod") != -1) 
	    OSName="ios";
	else 
	    OSName="is not mobile";
	
	//스토어 URL 등록 -> 앱이 설치되어있지 않은 경우 appstore로 가게 해준다. 
	function goAppStoreOrPlayStore() {
		var storeURL ="";
		if (OSName == "android") {
			// id 뒤에 앱 패키지명--> 이건 나중에 수정
			storeURL = "https://play.google.com/store/apps/details?id=com.example.autoreco";
		} else 	if(OSName=="ios"){
			// App ID값 
			storeURL = "http://itunes.apple.com";
		}else {
			return;
		}
		location.replace(storeURL);
	}

	//화면을 info.jsp로 전환해주는 함수 
	function changePage(){
		   window.location.replace("info?session_id=<%=session.getId()%>");
		}
	
	//앱 실행 함수
	var chk_send = 0;
	function appStart(){	
		// 중복 클릭 방지 
		if (chk_send == 0)
		{
		       chk_send = 1;
		   	var appUriScheme = "autoreco://kcc?session_id=<%=session.getId()%>";
			 if(appUriScheme){
				 window.open(appUriScheme, '_blank');	 
				 changePage();
			 }else {
				 goAppStoreOrPlayStore();
			 }
		}
		//사용자가 버튼을 두번 눌렀을 경우 
		else
		{
		       alert("지금 처리중입니다. 잠시만 기다려 주세요.");
		      // return false;
		}
	}
	


</script>

  
<title>main</title>
</head>

<body>


  <div id="Box">
  
      <div id="head">
        <h3>
        <a id="alink" onclick="goBack()"><img src="https://i.imgur.com/9WcxUGu.png" alrt="arrow" weight="30" height="30">
        </a>
        &nbsp;&nbsp;운전면허증 인식   <br/><br/>
        </h3>
      </div>
      
      <div id="center">    
         <div id="left">
           <div id="left1"><img src="https://i.imgur.com/nQbwaUX.png" alt="step1" weight="110" height="110"/></div>
           <div id="left1"><img src="https://i.imgur.com/XCuoQ3C.png" alt="step2" weight="110" height="110"/></div>
           <div id="left1"><img src="https://i.imgur.com/vz3nPZb.png" alt="step3" weight="110" height="110"/> </div>   
         </div>
        
         <div id="right">
		    <div id="right1"><p>Step1. 운전면허증을 화면의 네모박스에 맞추어 주세요.</p></div>
            <div id="right1"><p>Step2. 스캔된 정보가 맞는지 확인해주세요.</p></div>
            <div id="right1"><p>Step3. 확인버튼을 누른 후 잠시 기다려주세요.</p></div>     
         </div>
    </div>  
      
      <div id="bottom">
     	 <button id="btn" class="form-submit" onClick="appStart()">운전면허증 촬영</button>
      </div>
  
  </div>

</body>
</html>