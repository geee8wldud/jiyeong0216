


**1. Restful API 규칙**
-----------------

**1)게시판 관리**

 ①글 생성

* Description   :    body로 받아온 정보들을 이용해 새로운 게시글을 생성한다. 

*  Method          :  POST

* URL      :     /board/write

* Test url :   :    http://localhost:3000/board/write

* body
```
{
            "title":"bb",
            "writer":"user1",
            "password": "aa",
            "content" : "aa"
        }
```
* Json Result Ex
```
{
    "message": [
        {
            "idx": 9
        }
    ],
    "status": 200
}
```
②글 조회

* Description   :       데이터베이스에서 board에 저장되어있던 정보들을 모두 조회한 뒤 리스트에 보여준다.

*  Method          :  GET 

* URL      :     /board/list

* Test url :   :    http://localhost:3000/board/list

* Json Result Ex
```
{
    "message": [
        {
            "idx": 1,
            "title": "aa",
            "writer": "aa",
            "hit": 1,
            "moddate": "2018/10/01 11:47:55"
        },
        {
            "idx": 2,
            "title": "bb",
            "writer": "bb",
            "hit": 1,
            "moddate": "2018/10/01 13:12:21"
        },
        {
            "idx": 3,
            "title": "cc",
            "writer": "cc",
            "hit": 1,
            "moddate": "2018/10/01 13:12:27"
        },
        {
            "idx": 6,
            "title": "ee",
            "writer": "ee",
            "hit": 1,
            "moddate": "2018/10/01 14:30:22"
        },
        {
            "idx": 8,
            "title": "aaaaaaa",
            "writer": "aa",
            "hit": 2,
            "moddate": "2018/10/01 15:19:16"
        }
    ],
    "status": 200
}
```

③특정 게시글 조회

* Description    :   param으로 받아온 idx값을 이용해 idx번째 게시글을 조회한다. 

* Method          :  GET

* URL        :   /board/read/:idx


* Test url :   :    http://localhost:3000/board/read/9

* Json Result Ex
```
{
    "message": [
        {
            "idx": 9,
            "title": "bb",
            "content": "aa",
            "writer": "user1",
            "hit": 2,
            "moddate": "2018/10/01 16:18:20",
            "regdate": "2018/10/01 16:18:20"
        }
    ],
    "status": 200
}
```

④글 수정 

* Description    :   idx값과 수정된 값들을 받아와서  idx번째 게시글을 받아온 정보에 맞게 수정한다.

* Method          :  PUT

* URL        :  /board/modify


* Test url :   :    http://localhost:3000/board/modify

* body 
```
{
            "idx":"8",
            "title": "aa",
            "content" : "aa"
        }
```
* Json Result Ex
```
{
    "status": 200
}
```

⑤글 삭제 

* Description    :   idx번째 게시글을 삭제한 후 리스트화면을 redirect한다.

* Method          :  DELETE

* URL        :  /board/delete


* Test url :   :    http://localhost:3000/board/delete

* body
```
{
         "idx":"3",
}
```
* Json Result Ex
```
{
    "message": "Delete success",
    "status": 200
}
```



> Written with [StackEdit](https://stackedit.io/).
