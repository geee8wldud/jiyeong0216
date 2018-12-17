package com.autoreco.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ViewController extends Thread{

	//session_id별로 받아온 image 짝 맞춰서 저장 할 HashMap
	private static HashMap<String, byte[]> imageHash = new HashMap<String, byte[]>();
	
	//{url}이 가 주소창에 입력되었을 때 main.jsp 를 표시하는 함수
	@RequestMapping("/")
	public String main(Model model) {

		return "main"; 
	}

	 //앱으로부터 image 받는 http통신 부분(post api)
   @PostMapping(path="/info", produces="application/json", consumes="application/json")
   public String PostInfo(@RequestHeader Map<String,String> headers, @RequestBody String jsonString) throws ParseException, IOException{
      
       //카메라 앱으로부터 넘겨받은 image jsonString->byte[]
       JSONParser jsonParser = new JSONParser();
       JSONObject jsonObject = (JSONObject)jsonParser.parse(jsonString);
          
       String imageString = (String) jsonObject.get("image");
       byte[] image = Base64.decodeBase64(imageString.getBytes());
       
     //*************************************************************************************************	    
	    //OpenCV로 이미지 전처리
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //opencv 라이브러리 사용 선언
	    Mat mat = Imgcodecs.imdecode(new MatOfByte(image), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE); //byte[]->mat

	    Imgproc.adaptiveThreshold(mat, mat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 391, 65); // 글씨 흰색, 배경 검정색

	    MatOfByte mob = new MatOfByte();
	    Imgcodecs.imencode(".jpg", mat, mob);
	    byte a[] = mob.toArray();
	    	    
	    BufferedImage bi=ImageIO.read(new ByteArrayInputStream(a));  //mat->bufferedImage
	    
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(bi, "jpg", baos);
	    baos.flush();
	    byte changed_image[] = baos.toByteArray(); //bufferedImage->byte[] // = 전처리 완료된 이미지 byte[]
	    baos.close();  
	   //*************************************************************************************************
	    
       String session_id = null;
       Iterator<String> iter = imageHash.keySet().iterator();
       
       //session_id와 앱에서 받아온 uuid가 같으면 그 session_id와 image를 Hashmap에 넣어준다. 
       while(iter.hasNext()) {
          session_id = iter.next();
          //카메라 앱 호출 시 넘겨준 session_id값과 앱으로부터 넘겨받은 header값이 같은지 확인(중간에 다른 유저와 섞이지 않았는지)
          if(session_id.equals(headers.get("uuid"))){
             imageHash.put(session_id, changed_image);  //***
             System.out.println("sessionId : "+session_id+"넣어준 image : "+imageHash.get(session_id));
             break;
          }
       }
      
      return "infoProc";
   }
	   
    //{url}/info.jsp 가 주소창에 입력되었을 때 info.jsp 를 표시하는 함수
   @RequestMapping(value="/info")
    public String getinfo(Model model, HttpServletRequest request){
	 //main들어왔을 때 생성된 session_id값을 넘겨받음
	  String session_id = request.getParameter("session_id");
      
	  //사용자가 main을 안거치고 info로 접속을 시도했을때 main으로 페이지를 넘겨줌
	  if(session_id==null) {
	      return "redirect:/";
	  }
	  
      byte[] image_null = null;
      
      imageHash.put(session_id, image_null);
      
      while(true) {
         if(imageHash.get(session_id)!=null) {break;}
         try {
            sleep(1000);
         } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      
      //hashMap에서 해당 session_id값을 key값으로 이미지 가져옴
      byte[] image = imageHash.get(session_id);
      
      //info가 실행될 때 AzureController를 호출해서 infolist(운전면허증 데이터 필수값 리스트를 그대로 넣어줄 리스트)를 만들도록 함. 
      AzureController azure=new AzureController();
      ArrayList<String> infoList=azure.getPolishedList(image);
      
      
      //view화면에 넘겨줄 지역 코드, 지역 리스트
      String[] regionCode= {"11","12","13","13","14","15","16","17","18","19","20","21","22","23","24","25","26","28"};
      String[] regionList = {"서울","부산","경기","경기남부","강원","충북","충남","전북","전남","경북","경남","제주","대구","인천","광주","대전","울산","경기북부"};
      //view화면에 넘겨줄 면허종별 코드, 면허종별 리스트
      String[] typeCode= {"11","12","13","32","33","38","16","14","15"};
      String[] typeList= {"1종대형","1종보통","1종소형","2종보통","2종소형","2종원자","소형견인차","대형견인차(트레일러)","구난차(레커)"};
      
      //지역부분이 코드인지 한글인지 확인
      boolean isNum=isStringDouble(infoList.get(1));
      
      //면허종별 데이터 잘 가져왔는지 확인
      String type=infoList.get(0);//면허종별
      boolean isType=false;
      
      for(int i=0;i<typeList.length;i++) {
         if(type.equals(typeList[i])) {isType=true;}
      }
      
      //지역부분 데이터 잘 가져왔는지 확인
      String region=infoList.get(1);//지역
      boolean isRegion=false;
      
      if(isNum==true) {//지역부분 코드일 경우
         for(int i=0;i<regionCode.length;i++) {
            if(region.equals(regionCode[i])) {isRegion=true;}
         }
      }else {//지역부분 한글일 경우
         for(int i=0;i<regionList.length;i++) {
            if(region.equals(regionList[i])) {isRegion=true;}
         }
      }
      
      //view화면에 데이터 넘겨주는 부분
      model.addAttribute("infoList",infoList);//필수 데이터 리스트
      model.addAttribute("isNum", isNum);//지역부분이 번호인지
      model.addAttribute("isRegion",isRegion);//지역 데이터 잘 가져왔는지
      model.addAttribute("isType",isType);//면허종별 데이터 잘 가져왔는지
      model.addAttribute("regionList",regionList);//지역리스트
      model.addAttribute("regionCode", regionCode);//지역코드
      model.addAttribute("typeList", typeList);//면허종별리스트
      model.addAttribute("typeCode", typeCode);//면허종별코드
      
      return "info";
   }
   
   
   //{url}/resultO.jsp 가 주소창에 입력되었을 때 resultO.jsp 를 표시하는 함수
   @RequestMapping("/resultO")
   public String resultO(Model model, HttpServletRequest request) {
	   //해당 세션의 이미지를 지운다. 
	   String session_id = request.getParameter("session_id");
	   imageHash.remove(session_id);
	   
	   
	   return "resultO";
   }
   
   //{url}/resultX.jsp 가 주소창에 입력되었을 때 resultX.jsp 를 표시하는 함수
   @RequestMapping("/resultX")
   public String resultX(Model model, HttpServletRequest request) {
	   //해당 세션의 이미지를 지운다. 
	   String session_id = request.getParameter("session_id");
	   imageHash.remove(session_id);
	   
	   return "resultX";
   }
  
    //info.jsp에서 ‘확인’ 버튼을 눌렀을 때 텍스트 정보를 인자로 koroad API를 호출하는 함수
 	@RequestMapping(value="/update", method=RequestMethod.POST)
 	public String infoFormPost(@ModelAttribute("info") Info info, Model model) {
 		
 		//운전면허증 번호 추출 -> region + licenseNo[]
 		String[] licenseNo=info.getLicenseNo();
 		String region=info.getRegion();
 		String lno=""+region;
 		for(int i=0;i<licenseNo.length;i++) {
 			lno=lno+licenseNo[i];
 		}
 		
 		//koroad.jsp에 넣어줄 데이터 (운전면허증 진위여부 파악을 위해 필요한 데이터들)
 		model.addAttribute("license_no", lno);
 		model.addAttribute("licence_code", info.getLicenseType());
 		model.addAttribute("rent_str_dt", info.getStartDate());
 		model.addAttribute("rent_end_dt", info.getEndDate());
 		model.addAttribute("license_nm", info.getUsername());
 		model.addAttribute("ssn1", info.getBirth());
 		
 		return "koroad";
 	}
 	
    //지역부분이 한글인지 코드인지 확인하는 함수
    public boolean isStringDouble(String str){
       try{
          Double.parseDouble(str);
          return true;
       } catch(NumberFormatException e){
          return false;
       }
    } 
    
 
}
