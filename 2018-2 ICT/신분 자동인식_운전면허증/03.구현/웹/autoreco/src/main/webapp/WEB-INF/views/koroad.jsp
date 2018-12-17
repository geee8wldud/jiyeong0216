<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>koroad</title>
<!-- jquery ����� ���� �ڵ� -->
<script type="text/javascript" src="webjars/jquery/2.2.4/jquery.min.js"></script>

<script type="text/javascript">
	//���������� �������� Ȯ���� ���� �����͸� �����ְ� ����� ��ٸ���.  
	 $.ajax({
		    type : "POST",
		    url : 'https://rentdemo.kcc.co.kr/chkDriveLicenseMobileProc.json',
		    async:false,
		    data : {
		    	  license_no : '${license_no}',          //���������ȣ
			      licence_code : '${licence_code}',     //2�ڸ� ���������ڵ�
			      serialnum :'',        //6�ڸ� �������� ��ȣ�Ϸù�ȣ
			      rent_str_dt : '${rent_str_dt}',        //�뿩�Ⱓ ���� YYYYMMDD
			      rent_end_dt : '${rent_end_dt}',    //�뿩�Ⱓ ���� YYYYMMDD
			      license_nm : '${license_nm}',     //����
			      ssn1 : '${ssn1}',            //������� YYMMDD
			      rentcarid : 'KRMA',        //rent_car_id      KRMA
			      user_id : 'admin',        //user_id        admin
			      flag : 'RENT4U'            //table_space      RENT4U
		    },
		    success : function(data) {
		      //alert("�˻��� �Ϸ�Ǿ����ϴ�. ");
		    
		      if(data.rtn_code=="00"){
		    	  window.location.replace("resultO?session_id=<%=session.getId()%>");
		      }else {
		    	  window.location.replace("resultX?rtn_code="+data.rtn_code+"&session_id=<%=session.getId()%>");
		      }
		},
		error : function(request, error) {
		  alert("Error : " + error + "\n�ٽ� ������ �ּ���");
		}
		  });

  </script>
</head>
<body>
</body>
</html>