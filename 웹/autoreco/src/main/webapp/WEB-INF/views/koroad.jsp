<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>koroad</title>
<!-- jquery 사용을 위한 코드 -->
<script type="text/javascript" src="webjars/jquery/2.2.4/jquery.min.js"></script>

<script type="text/javascript">
	//운전면허증 진위여부 확인을 위해 데이터를 보내주고 결과를 기다린다.  
	 $.ajax({
		    type : "POST",
		    url : 'https://rentdemo.kcc.co.kr/chkDriveLicenseMobileProc.json',
		    async:false,
		    data : {
		    	  license_no : '${license_no}',          //운전면허번호
			      licence_code : '${licence_code}',     //2자리 면허종별코드
			      serialnum :'',        //6자리 운전면허 암호일련번호
			      rent_str_dt : '${rent_str_dt}',        //대여기간 시작 YYYYMMDD
			      rent_end_dt : '${rent_end_dt}',    //대여기간 종료 YYYYMMDD
			      license_nm : '${license_nm}',     //성명
			      ssn1 : '${ssn1}',            //생년월일 YYMMDD
			      rentcarid : 'KRMA',        //rent_car_id      KRMA
			      user_id : 'admin',        //user_id        admin
			      flag : 'RENT4U'            //table_space      RENT4U
		    },
		    success : function(data) {
		      //alert("검색이 완료되었습니다. ");
		    
		      if(data.rtn_code=="00"){
		    	  window.location.replace("resultO?session_id=<%=session.getId()%>");
		      }else {
		    	  window.location.replace("resultX?rtn_code="+data.rtn_code+"&session_id=<%=session.getId()%>");
		      }
		},
		error : function(request, error) {
		  alert("Error : " + error + "\n다시 실행해 주세요");
		}
		  });

  </script>
</head>
<body>
</body>
</html>