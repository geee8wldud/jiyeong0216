<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">


<!-- 
   운전면허 번호 - name : licenseNo -> 칸이 4개로 나누어져 있음 11-11-111111-11 형식
   운전 면허자 이름 - name : username
   생년월일 -name : birth
   면허종별 - name : licenseType
   대여 시작일 - name :  startDate
   대여 종료일 - name : endDate

 -->
 
 <!--info.css 사용 -->
 <link rel="stylesheet" type="text/css" href="/resources/css/info.css" />

 <script>
	
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
				 }else {
					 goAppStoreOrPlayStore();
				 }
			}
			//사용자가 버튼을 두번 눌렀을 경우 
			else
			{
			       alert("지금 처리중입니다. 잠시만 기다려 주세요.");
			      
			}
		}
	//재촬영버튼을 눌렀을때 실행되는 함수 -> 재촬영 앱 실행 후 info.jsp를 리로드한다. 
    function changePage(){
        update_form.endDate.value=null;
        appStart();
          window.location.reload();  
       }
	//뒤로가기 화살표 버튼을 눌렀을때 실행하는 함수
    function btn_event(){
       if(confirm("입력된 정보를 삭제합니다. ")== true){
          window.location.replace("/");
       }else{
          return;
       }
    }
   
	//원래 모바일 핸드폰에 있는 뒤로가기 버튼 비활성화
	history.pushState(null, null, location.href);
	window.onpopstate = function () {
	    history.go(1);
	};
	
	//submit 버튼 클릭시 실행 함수 
	var chk_submit = 0;
	function check_onclick(){	
		// 중복 클릭 방지 
		if (chk_submit == 0)
		{
			chk_submit = 1;
			 if(update_form.licenseNo2.value==null){
		         update_form.licenseNo2.focus();
		         return false;
		      }else if(update_form.licenseType.value==null){
		         update_form.licenseType.focus();
		         return false;
		      }else {return true;}
		}
		//사용자가 버튼을 두번 눌렀을 경우 
		else
		{
		       alert("지금 처리중입니다. 잠시만 기다려 주세요.");
		}
	}

</script>

<title>info</title>
</head>


<body>

 <div id="Box">

      <div id="head">
	        <h3>
	        <a id="alink" onclick="btn_event()"><img src="https://i.imgur.com/9WcxUGu.png" alrt="arrow" weight="30" height="30">
	        </a>
	        &nbsp;&nbsp;운전면허증 인식   <br/><br/>
	        </h3>
      </div>
    <div id=center>
	   <div class="flex-cont">
	   <div class="form-cont">
	      
	      <form action="/update" name="update_form" onsubmit="return check_onclick()" class="form" method="Post" modelAttribute="info">
	         <div class="form-content">
	           <div class="form-row">
	               <label for="licenseNo" class="form-label">운전 면허 번호</label>
	               <div id="licenseNo">
	               <c:choose>
	                  <c:when test="${isNum==true}">
	                  <input type="number" size="2" maxlength="2" id="licenseNo1" name="region" value="${infoList.get(1)}" class="form-textbox input-animate-target" required>
	                  </c:when>
	                  
	                  <c:when test="${isNum==false}">
		                  <select id="licenseNo2" name="region"  class="form-textbox input-animate-target" required>
			                  <c:if test="${isRegion==false}">
			                         <option style="color:gray" value="" disabled="disabled" selected="selected"> <지역선택> </option>
			                  </c:if>
			                  
		                      <c:forEach var="i" begin="0" end="${fn:length(regionList)-1}">
		                         <c:choose>
		                            <c:when test="${infoList.get(1) eq regionList[i]}">
		                            	<option value="${regionCode[i]}" selected="selected">${regionList[i]}</option>
		                            </c:when>
		                            <c:when test="${infoList.get(1) ne regionList[i]}">
		                            	<option value="${regionCode[i]}">${regionList[i]}</option>
		                            </c:when>
		                         </c:choose>
		                      </c:forEach>
		                      
		                  </select>
	                  </c:when>
	               </c:choose>
	               
	               <input type="number" size="2" maxlength="2"  id="licenseNo1" name="licenseNo" value="${infoList.get(2)}" class="form-textbox input-animate-target" required>
	               <input type="number" size="2" maxlength="6" id="licenseNo2" name="licenseNo" value="${infoList.get(3)}" class="form-textbox input-animate-target" required>
	               <input type="number" size="2" maxlength="2" id="licenseNo1" name="licenseNo" value="${infoList.get(4)}" class="form-textbox input-animate-target" required>
	               </div>
	               <div class="input-animate"></div>
	               <div class="form-check-icon"></div>
	            </div>
	            
	            <div class="form-row">
	               <label for="name" class="form-label">운전면허자 이름</label>
	               <input type="text" id="name" placeholder="홍길동" name="username" value="${infoList.get(5)}" class="form-textbox input-animate-target" required>
	               <div class="input-animate"></div>
	               <div class="form-check-icon"></div>
	            </div>
	            
	            <div class="form-row">
	               <label for="birth" class="form-label">생년월일</label>
	               <input type="text" size="6" maxlength="6" id="birth" name="birth" value="${infoList.get(6)}" placeholder="YYMMDD" class="form-textbox input-animate-target" required>
	               <div class="input-animate"></div>
	               <div class="form-check-icon"></div>
	            </div>
	            
	            <div class="form-row">
	               <label for="licenseType" class="form-label">면허종별</label>
	               <select id="licenseType"  name="licenseType"  class="form-textbox input-animate-target" required>
	                    <c:if test="${isType==false}">
	                         <option style="color:gray; width:100%" value=""  disabled="disabled" selected="selected"><면허종별선택></option>
	                    </c:if>
	                    
	                    <c:forEach var="i" begin="0" end="${fn:length(typeList)-1}">
	                      <c:choose>
	                         <c:when test="${infoList.get(0) eq typeList[i]}">
	                         <option value="${typeCode[i]}" selected="selected">${typeList[i]}</option>
	                         </c:when>
	                         <c:when test="${infoList.get(0) ne typeList[i]}">
	                         <option value="${typeCode[i]}">${typeList[i]}</option>
	                         </c:when>
	                      </c:choose>
	                   </c:forEach>                   
	               </select>
	               <div class="input-animate"></div>
	               <div class="form-check-icon"></div>
	            </div>
	            
	            <div class="form-row">
	               <label for="startDate" class="form-label">대여 시작일</label>
	               <input type="date"  id="startDate" name="startDate" class="form-textbox input-animate-target" required>
	               <div class="input-animate"></div>
	               <div class="form-check-icon"></div>
	            </div>
	            
	            <div class="form-row">
	               <label for="endDate" class="form-label">대여 종료일</label>
	               <input type="date"  id="endDate" name="endDate" class="form-textbox input-animate-target" required>
	               <div class="input-animate"></div>
	               <div class="form-check-icon"></div>
	            </div>
     
           <div id="bottom">
              <button id="btn" class="form-submit" onClick="changePage()">재촬영</button>
              <input type="submit" value="확인" class="form-submit">
           </div>
	       </div>
	     </form>
	  </div></div></div></div>
</body>
</html>