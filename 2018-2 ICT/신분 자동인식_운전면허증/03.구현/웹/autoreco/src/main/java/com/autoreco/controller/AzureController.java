package com.autoreco.controller;

import org.springframework.stereotype.Controller;
import java.io.IOException;

import java.net.URI;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

@Controller
public class AzureController extends Thread {
   
   // Microsoft Azure 구독키와 사용 지역 리젼의 url
   private static final String subscriptionKey = "ee2b200af8dc4d8c821edcc640ea5dc0";
   private static final String uriBase = "https://southeastasia.api.cognitive.microsoft.com/vision/v2.0/ocr";
  
      /*   도로교통부 인증을 위해 필요한 필수 정보값을 받을 배열 ["종별코드","면허번호 지역/지역코드","면허번호1","면허번호2","면허번호3","이름","생년월일"]
           ex) ["1종대형", "11", "22", "333333", "홍길동", "111111"]   */
    private static ArrayList<String> polishedList;
       
    // polishedList를 반환값으로 ViewController로 넘겨주도록 하는 함수
    public ArrayList<String> getPolishedList(byte[] imageFromApp){
       
       try {
          polishedList = sendToAzure(imageFromApp);
      
       } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
       }
        
        polishedList = infoList(polishedList);//수정창에 뿌리기 편하게 리스트 수정 ->  ex) ["1종대형", "11", "22", "333333", "홍길동", "111111"]
       
       return polishedList;
    }
    
    
    /*   카메라 앱에서 이미지 parameter로 넘겨받아 azure에 요청, 응답받는 함수 
       return된 list : 추출된 필수값 정보  ["종별코드","면허번호","이름","주민등록번호"]
       ex) [1종대형, 112233333344, 홍길동, 123456-9999999]      */
   public static ArrayList<String> sendToAzure(byte[] imageInByte) throws IOException {//imgaeInByte : 앱에서 넘겨받은 이미지 파일
      //azure 연결
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        ArrayList<String> list = new ArrayList<String>();
        
        try {
           
            URIBuilder uriBuilder = new URIBuilder(uriBase);

            uriBuilder.setParameter("language", "ko");
            uriBuilder.setParameter("detectOrientation", "true");

            // Request parameters.
            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);
            
            // Request headers.
            request.setHeader("Content-Type", "application/octet-stream");//octet-stream<-binary형식 아무거나 받음
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
            
            //Request body (이미지 byte형식)
            request.setEntity(new ByteArrayEntity(imageInByte));
            
            // Call the REST API method and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                
                //필요한 데이터만 추출
                list = polishData(jsonString);
             }
           
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
      
        return list;
   }
   
      /* json형식의 정보값 파싱해 필수 데이터를 추출하는 함수 
         bounding box 범위값에 있는 텍스트만 추출해 코로드 형식에 맞게 데이터 수정
           return된 list : 추출된 필수값 정보  
         ex) [1종대형, 112233333344, 홍길동, 123456-9999999] */
      public static ArrayList<String> polishData(String jsonString) throws ParseException {

         ArrayList<String> list = new ArrayList<String>();
         
         // 지정한 boundingbox x,y 범위
         int[] typeXrange, typeYrange, otherXrange, otherYrange;
         
         typeXrange= new int[] {5,90};
         typeYrange= new int[] {5,80};
         otherXrange= new int[] {285,340};
         otherYrange= new int[] {75,205};
         
         // OCR 데이터 json 파싱
         JSONParser jsonParser = new JSONParser();
         JSONObject jsonObject = (JSONObject)jsonParser.parse(jsonString);
           
           
         JSONArray regions = (JSONArray)jsonObject.get("regions");
     
         int num=1;
           
         for(int i=0; i<regions.size();i++) {
            JSONObject tmp = (JSONObject)regions.get(i);
             JSONArray lines = (JSONArray)tmp.get("lines");
              
             for(int k=0;k<lines.size();k++) {
                JSONObject line = (JSONObject)lines.get(k);
                 String boundingBox = (String)line.get("boundingBox");
                 
                 // 범위 지정 후 배열에 넣기
                 String[] boundingvalues = boundingBox.split(",");
                 String x1 = boundingvalues[0];//text 가로 시작 위치
                 String y1 = boundingvalues[1];//text 세로 시작 위치
                   
                 String text1 = null,text2=null;
                 
                 // boundingBox 위치값으로 면허종별은 따로 받고 나머지는 같이 받음     
                 // 면허종별 추출
                 if((typeXrange[0]<=Integer.parseInt(x1))&&(Integer.parseInt(x1)<=typeXrange[1]) && (typeYrange[0]<=Integer.parseInt(y1)) && (Integer.parseInt(y1)<=typeYrange[1])) {
                    JSONArray words = (JSONArray)line.get("words");
                     for(int j=0; j<words.size();j++) {
                        JSONObject word = (JSONObject)words.get(j);
                         String text =(String)word.get("text");
                          
                         // 띄어쓰기마다 나눠서 읽어오는 text값들을 합쳐 면허증 상에 줄단위 형식으로 바꾸기
                         if(j==0) text1=text;
                         else text1 = text1+" "+text;
                         }
                     
                     //면허 종별 list의 첫번째 원소에 삽입
                     list.add(text1);
                 }
                 
                 // 면허번호, 이름, 생년월일 추출
                 if((otherXrange[0]<=Integer.parseInt(x1))&&(Integer.parseInt(x1)<=otherXrange[1]) && (otherYrange[0]<=Integer.parseInt(y1)) && (Integer.parseInt(y1)<=otherYrange[1])) {
                    JSONArray words = (JSONArray)line.get("words");
                     
                     // 면허종별을 못받아왔을때 처리
                     if(list.size()==0) {
                        list.add("00");//의미없는 값 채워주는 작업
                     }
                     
                     for(int j=0;j<words.size();j++) {
                         JSONObject word= (JSONObject)words.get(j);
                         String text = (String)word.get("text");
                           
                         // 띄어쓰기마다 나눠서 읽어오는 text값들을 합쳐 면허증 상에 줄단위 형식으로 바꾸기
                         if(j==0) text2=text;
                         else text2 = text2+" "+text;
                     }
                     
                     // 면허번호, 이름, 생년월일을 순서대로 list에 삽입
                     list.add(num,text2);
                     num++;
                 }
             }
         }
         
         // 필수 데이터를 제대로 가져오지 못했을때 처리
         if(list.size()<4) {
            int size = list.size();
            for(int i=size;i<4;i++) {
               list.add(i, "000000");
            }
         }
         
          // 데이터 다듬기(하이픈 제거)
         list.set(1, list.get(1).replace("-", ""));
         
         // 데이터 다듬기(공백제거)
         for(int i=0;i<list.size();i++) {
            list.set(i, list.get(i).replace(" ", ""));
         }
                  
         // 데이터 다듬기(면허종별)
         if(list.get(0).contains("1")) {
            if(list.get(0).contains("대")) {
               list.set(0, "1종대형");
            }
            if(list.get(0).contains("보")) {
               list.set(0, "1종보통");
            }
            if(list.get(0).contains("소")) {
               list.set(0, "1종소형");
            }
         }else if(list.get(0).contains("2")) {
                if(list.get(0).contains("보")) {
                   list.set(0, "2종보통");
                }else if(list.get(0).contains("소")) {
                   list.set(0, "2종소형");
                }else if(list.get(0).contains("원")||list.get(0).contains("자")) {
                   list.set(0, "2종원자");
                }
          }
         
         // 줄단위로 전체 출력
         for(int k=0; k<list.size();k++) {
            //System.out.println(list.get(k));
         }

         return list;//-> ex) [1종대형, 112233333344, 홍길동, 123456-9999999]
      }

      // info.jsp 수정창 형식에 맞게 정리해서 다시 list에 넣는 함수
      public static ArrayList<String> infoList(ArrayList<String> list) {
        
         ArrayList<String> licenseList = new ArrayList<String>();
         
          licenseList.add(list.get(0));//면허종별

          String LicenseNum = list.get(1);//면허번호

           // 만약 면허 번호가 12보다 작을 시 0으로 셋팅 후 사용자가 수정하게 한다
           if(LicenseNum.length()<12) {
             
              int num=12-LicenseNum.length();
              
              // 처음 글자가 문자, 두번째 글자가 문자 || 아예 숫자
              if((!isNumber(LicenseNum.substring(0,1))&&!isNumber(LicenseNum.substring(1,2)))||(isNumber(LicenseNum.substring(0,1))))
              {
                 String a="0";
                 for(int i=0;i<num-1;i++) {
                    a=a+"0";
                 }
                 list.set(1, LicenseNum+a);         
              }
              
              // 처음글자는 문자, 두번째 글자는 숫자 (문자 위에 0삽입 후, 나머지 숫자 채워줌)
              if(!isNumber(LicenseNum.substring(0,1))&&isNumber(LicenseNum.substring(1,2)))
              {
                 String a = LicenseNum.substring(0,1)+"0";
                 String b="0";
                 for(int i=0;i<=num-3;i++) {
                    b=b+"0";
                 }
                 list.set(1, a+LicenseNum.substring(1)+b);
              }
           }
            
           // 운전면허 번호
           licenseList.add((String)list.get(1).subSequence(0, 2));
           licenseList.add((String)list.get(1).subSequence(2,4));
           licenseList.add((String)list.get(1).subSequence(4,10));
           licenseList.add((String)list.get(1).substring(10));
           
           licenseList.add(list.get(2));//면허자 이름
           licenseList.add(list.get(3).substring(0, 6));//생년월일
            
           return licenseList;//->ex) ["1종대형", "11", "22", "333333", "홍길동", "111111"]
       }
      
      //문자열이 숫자(정수, 실수)인지 아닌지 판별한다.
      static boolean isNumber(String str) {
        char tempCh;
        int dotCount = 0;   //실수일 경우 .의 개수를 체크하는 변수
        boolean result = true;

        for (int i=0; i<str.length(); i++){
          tempCh= str.charAt(i);   //입력받은 문자열을 문자단위로 검사
          //아스키 코드 값이 48 ~ 57사이면 0과 9사이의 문자이다.
          if ((int)tempCh < 48 || (int)tempCh > 57){
            //만약 0~9사이의 문자가 아닌 tempCh가 .도 아니거나
            //.의 개수가 이미 1개 이상이라면 그 문자열은 숫자가 아니다.
            if(tempCh!='.' || dotCount > 0){
              result = false;
              break;
            }else{
              //.일 경우 .개수 증가
              dotCount++;
            }
          }
        }
        return result;
      }
} 