Rest API 규칙
-----------
**1) 게시판 관리**

**1. 게시글 조회**

- Description : 게시판 전체 목록을 리턴한다.

- Method    : GET
   
- URL        : /

- testurl : http://localhost:3000

- Body :  
	```
	> `{
			"num" : "1",
			"title" : "제목",
			"author" : "최미리",
			"board_date" : "Wed Sep 19 2018 13:40:35",
			"views" : "12"
> }`
	```

- Json Result Ex : 


		```
	> ``{    "success": true,
		    "data": [
		 {  "views": 0,
            "board_date": "2018-09-19T01:48:18.485Z",
            "_id": "5ba1ab0936d98f2214f062b3",
            "comments": [],
            "title": "Restful test",
            "contents": "테스트 중입니다.",
            "author": "최별이",
            "__v": 0,
            "id": "5ba1ab0936d98f2214f062b3" }
	}`


**2. 특정 게시글 조회**


- Description : id값의 게시판을 리턴한다.

- Method    : GET
   
- URL        : /board/:id

- testurl : http://localhost:3000

- Body :  



		> `{
			"title" : "제목",
			"contents" : "내용입니다.",
			"author" : "최미리",
			`}
		



 - Json Result Ex : 

		```
	> `{    "success": true,
		    "data": {
		        "views": 2,
		        "board_date": "2018-09-19T01:48:18.485Z",
		        "_id": "5ba1ab0936d98f2214f062b3",
		        "comments": [],
		        "title": "Restful test",
		        "contents": "테스트 중입니다.",
		        "author": "최별이",
		        "__v": 0,
		        "id": "5ba1ab0936d98f2214f062b3" }}`
	```


**3. 수정하기**


- Description : 수정하기 페이지를 리턴한다.

- Method    : GET
   
- URL        : /board/:id/edit

- testurl : http://localhost:3000/board/5ba1ab0936d98f2214f062b3/edit


- Body :  
	
		> `{
			"title" : "제목",
			"contents" : "내용입니다.",
			"author" : "최미리",
			`}
	


 - Json Result Ex : 
 

		```
> `{    "success": true,
	    "data": {
		        "views": 4,
		        "board_date": "2018-09-19T01:48:18.485Z",
		        "_id": "5ba1ab0936d98f2214f062b3",
		        "comments": [],
		        "title": "Restful test",
		        "contents": "테스트 중입니다.",
		        "author": "최별이",
		        "__v": 0,
		        "id": "5ba1ab0936d98f2214f062b3"
		         }}
 ```
 
 
**4. 게시글 등록**


- Description : 게시글을 새롭게 등록한다.

- Method    : POST
   
- URL        : /board/write

- testurl : http://localhost:3000/board/5ba1ab0936d98f2214f062b3/edit

- Body :  
	```
	> `{
			"title" : "제목",
		   "contents" : "안녕하세요",
		   "author" : "김민지"
		}`
	```

   - Json Result Ex : 
	```
	> `{    "success": true,
			        "data": {
			        "views": 0,
			        "board_date": "2018-09-19T02:31:58.406Z",
			        "_id": "5ba1b5290507593254ff440a",
			        "comments": [],
			        "title": "제목",
			        "contents": "안녕하세요",
			        "author": "김민지",
			        "__v": 0,
			        "id": "5ba1b5290507593254ff440a"
					    }
		}`

		```
	
		
**5. 댓글 등록**


- Description : 게시판 댓글을 등록한다.

- Method    : POST
   
- URL        : Comment/write

- testurl : http://localhost:3000/comment/write

- Body :  
	```
	> `{
			"contents" : "내용입니다.",
			"author" : "최미리",

	> }`
	```

   - Json Result Ex : 
	```
	> `{    
			"contents" : " 댓글기능 테스트 ",
		    "author" : "최미리"
		}`
	```


		```
	> `{    
				"success": true,
	   	        "data": null
		}`
		```


**6. 게시글 수정**


- Description : 게시글의 id 값을 찾아 그 게시글을 수정한다.

- Method    : PUT
   
- URL        :  /board/:id

- testurl : http://localhost:3000/board/5ba1ab0936d98f2214f062b3

- Body :  
	```
	> `{
			"id" : "5ba1ab0936d98",
	  	    "title" : "수정test중입니다.",
	   	    "contents" : "내용입니다.",
	   	    "author" : "최미리"
 }`
	```

   - Json Result Ex : 

		```
	> `{    
			"title" : "수정중입니다",
	  	     "contents" : "수정test중입니다.",
	   	     "author" : "최진실"
		}`
		```


		```
	> `{    "success": true,
			    "data": {
		        "views": 16,
		        "board_date": "2018-09-19T04:32:57.145Z",
		        "_id": "5ba1ab0936d98f2214f062b3",
		        "comments": [
		            {
		                "comment_date": "2018-09-19T02:22:10.946Z",
		                "_id": "5ba1b303cf3e6d2cc04fba06",
		                "contents": "d",
		                "author": "d"
		            }
		        ],
		        "title": "수정중입니다",
		        "contents": "수정test중입니다.",
		        "author": "최진실",
		        "__v": 0,
		        "id": "5ba1ab0936d98f2214f062b3"
	    }
		```
		
**7. 게시글 삭제**

- Description :  id값의 게시글을 삭제한다.

- Method    : DELETE

- URL        : /board/:id

- testurl : http://localhost:3000/board/5ba1b5290507593254ff440a

- Body :  
	```
	> `{
			"id" : "5ba1b529054ff440a"
	> }`
	```

   - Json Result Ex : 
	```
	> `{    
			"title" : "수정중입니다",
	  	     "contents" : "수정test중입니다.",
	   	     "author" : "최진실"
		}`
	```


		```
	> `{    
			"success": true,
	 	    "data": {
			        "n": 1,
		 	       "ok": 1
				    }
		}`
		```









