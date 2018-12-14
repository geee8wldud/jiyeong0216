# **Node.js 게시판**

> Node.js를 기반으로 만든 게시판 홈페이지입니다.
-------------------------------------------------------------
## 1. Restful API 규칙
### 1) 계정 관리

① 회원가입
- Description : 파라미터 값을 받아 새로운 user를 생성합니다.
- Method : POST
- URL : /users/
- Test URL : http://localhost:3000/users/new
- body
<pre>{
	"username" : "a12345",
	"password" : "123456s",
	"name" : "name",
	"email" : "a123@naver.com",
	"passwordConfirmation" : "123456s"
}</pre>
- json Result EX
<pre>{
    "_id": "5bb1bc439966d50670ff8587",
    "username": "a12345",
    "password": "$2a$10$5KT9MAGZiy0QjaoEWQSPueJiEyXQetNVKKdHUy8tMeIkXdZbxvCr6",
    "name": "name",
    "email": "a123@naver.com",
    "__v": 0,
    "passwordConfirmation": "123456s",
    "id": "5bb1bc439966d50670ff8587"
}</pre>

② 로그인
- Description : 입력받은 파라미터 값이 가입된 정보와 일치하면 로그인합니다.
- Method : POST
- URL : /login/
- Test URL : http://localhost:3000/login
- body 1 (올바르게 로그인)
<pre>{
	"username" : "a12345",
	"password" : "123456s"
}</pre>
- json Result EX 1
<pre>{
    "errors": {}
}</pre>

- body 2 (틀린 정보로 로그인)
<pre>{
	"username" : "a12345",
	"password" : "1"
}</pre>
- json Result EX 2
<pre>{
    "username":"a12345",
    "errors": {"login":"ID 또는 비밀번호가 맞지 않습니다."}
}</pre>

③ 로그아웃
- Description : 로그인 전 화면을 불러와 로그아웃 합니다.
- Method : GET
- URL : /logout
- Test URL : http://localhost:3000/logout

④ 회원 조회
- Description : 모든 회원의 아이디를 출력하는 화면을 불러옵니다. (*②로그인 필요)
- Method : GET
- URL : /users
- Test URL : http://localhost:3000/users
- json Result EX
<pre>{
    "users": [
        {
            "_id": "5bb1bc439966d50670ff8587",
            "username": "a12345",
            "name": "name",
            "email": "a123@naver.com",
            "__v": 0,
            "id": "5bb1bc439966d50670ff8587"
        },
        {
            "_id": "5ba1cedcb65b1930d4c2b763",
            "username": "abc12",
            "name": "신지영",
            "email": "shzy232@naver.com",
            "__v": 0,
            "id": "5ba1cedcb65b1930d4c2b763"
        },
        {
            "_id": "5ba1960c876f3725289c0cf7",
            "username": "abc123",
            "name": "name",
            "email": "abc123@naver.com",
            "__v": 0,
            "id": "5ba1960c876f3725289c0cf7"
        },
        {
            "_id": "5b97161781c79a379c7ba37a",
            "username": "kcc12",
            "name": "정보통",
            "email": "kcc12@naver.com",
            "__v": 0,
            "id": "5b97161781c79a379c7ba37a"
        },
        {
            "_id": "5b95ce31b7c43913ccdb7f6b",
            "username": "test12",
            "name": "테스트용",
            "email": "test12@naver.com",
            "__v": 0,
            "id": "5b95ce31b7c43913ccdb7f6b"
        }
    ]
}</pre>

⑤ 내 정보 조회
- Description : username이 현재 로그인한 {username}인 사용자의 가입 정보를 불러옵니다. (*②로그인 필요)
- Method : GET
- URL : /users/{username}
- Test URL : http://localhost:3000/users/a12345
- json Result EX
<pre>{
    "user": {
        "_id": "5bb1bc439966d50670ff8587",
        "username": "a12345",
        "name": "name",
        "email": "a123@naver.com",
        "__v": 0,
        "id": "5bb1bc439966d50670ff8587"
    }
}</pre>

⑥ 내 정보 수정
- Description : username이 {username}인 사용자의 가입 정보를 입력받은 값으로 수정합니다. (*②로그인 필요)
- Method : PUT
- URL : /users/{username}/
- Test URL : http://localhost:3000/users/a12345/
- body
<pre>{
	"currentPassword": "123456s",
	"username" : "a12345",
	"name" : "이름변경"
}</pre>
- json Result EX
<pre>{
    "_id": "5bb1bc439966d50670ff8587",
    "password": "$2a$10$5KT9MAGZiy0QjaoEWQSPueJiEyXQetNVKKdHUy8tMeIkXdZbxvCr6",
    "username": "a12345",
    "name": "이름변경",
    "originalPassword": "$2a$10$5KT9MAGZiy0QjaoEWQSPueJiEyXQetNVKKdHUy8tMeIkXdZbxvCr6",
    "currentPassword": "123456s",
    "id": "5bb1bc439966d50670ff8587"
}</pre>

---

### 2) 게시판 관리
① 글 생성
- Description : 파라미터 값을 받아 새로운 post를 생성합니다. (*②로그인 필요)
- Method : POST
- URL : /posts/
- Test URL : http://localhost:3000/posts/
- body
<pre>{
	"title" : "글 제목입니다.",
	"body" : "글 내용입니다."
}</pre>
- json Result EX
<pre>{
    "_id": "5bb1c3dbfaf8f91ca8c4c4f2",
    "title": "글 제목입니다.",
    "body": "글 내용입니다.",
    "author": "5bb1bc439966d50670ff8587",
    "createdAt": "2018-10-01T06:51:07.341Z",
    "__v": 0,
    "createdDate": "2018-10-01",
    "createdTime": "15:51:07",
    "id": "5bb1c3dbfaf8f91ca8c4c4f2"
}</pre>

② 글 조회
- Description : 현재 게시된 모든 글을 작성 순으로 불러옵니다. {id} 파라미터가 있으면, posts의 id가 {id}인 글을 불러옵니다.
- Method : GET
- URL : /posts 또는 /posts/{:id}
- Test URL : http://localhost:3000/posts 또는 http://localhost:3000/posts/5bb1c3dbfaf8f91ca8c4c4f2
- json Result EX 1 (모든 글 조회)
<pre>[
    {
        "_id": "5b95ce44b7c43913ccdb7f6c",
        "title": "테스트용 글",
        "body": "내용",
        "author": {
            "_id": "5b95ce31b7c43913ccdb7f6b",
            "username": "test12",
            "name": "테스트용",
            "email": "test12@naver.com",
            "__v": 0,
            "id": "5b95ce31b7c43913ccdb7f6b"
        },
        "createdAt": "2018-09-10T01:52:04.444Z",
        "__v": 0,
        "createdDate": "2018-09-10",
        "createdTime": "10:52:04",
        "id": "5b95ce44b7c43913ccdb7f6c"
    },
    {
        "_id": "5b95ec99d109b83b44b992a4",
        "title": "s",
        "body": "ssss",
        "author": {
            "_id": "5b95ce31b7c43913ccdb7f6b",
            "username": "test12",
            "name": "테스트용",
            "email": "test12@naver.com",
            "__v": 0,
            "id": "5b95ce31b7c43913ccdb7f6b"
        },
        "createdAt": "2018-09-10T04:01:29.809Z",
        "__v": 0,
        "updatedAt": "2018-09-10T04:22:18.841Z",
        "createdDate": "2018-09-10",
        "createdTime": "13:01:29",
        "updatedDate": "2018-09-10",
        "updatedTime": "13:22:18",
        "id": "5b95ec99d109b83b44b992a4"
    },
    {
        "_id": "5b97163181c79a379c7ba37b",
        "title": "test",
        "body": "test",
        "author": {
            "_id": "5b97161781c79a379c7ba37a",
            "username": "kcc12",
            "name": "정보통",
            "email": "kcc12@naver.com",
            "__v": 0,
            "id": "5b97161781c79a379c7ba37a"
        },
        "createdAt": "2018-09-11T01:11:13.333Z",
        "__v": 0,
        "createdDate": "2018-09-11",
        "createdTime": "10:11:13",
        "id": "5b97163181c79a379c7ba37b"
    },
    {
        "_id": "5bb1c3dbfaf8f91ca8c4c4f2",
        "title": "글 제목입니다.",
        "body": "글 내용입니다.",
        "author": {
            "_id": "5bb1bc439966d50670ff8587",
            "username": "a12345",
            "name": "이름변경",
            "email": "a123@naver.com",
            "__v": 0,
            "id": "5bb1bc439966d50670ff8587"
        },
        "createdAt": "2018-10-01T06:51:07.341Z",
        "__v": 0,
        "createdDate": "2018-10-01",
        "createdTime": "15:51:07",
        "id": "5bb1c3dbfaf8f91ca8c4c4f2"
    }
]</pre>

- json Result EX 2 (특정 글 조회)
<pre>{
    "_id": "5bb1c3dbfaf8f91ca8c4c4f2",
    "title": "글 제목입니다.",
    "body": "글 내용입니다.",
    "author": {
        "_id": "5bb1bc439966d50670ff8587",
        "username": "a12345",
        "name": "이름변경",
        "email": "a123@naver.com",
        "__v": 0,
        "id": "5bb1bc439966d50670ff8587"
    },
    "createdAt": "2018-10-01T06:51:07.341Z",
    "__v": 0,
    "createdDate": "2018-10-01",
    "createdTime": "15:51:07",
    "id": "5bb1c3dbfaf8f91ca8c4c4f2"
}</pre>

③ 글 수정
- Description : posts의 id가 {id}인 글을 입력받은 값으로 수정합니다. (*②로그인 필요)
- Method : PUT
- URL : /posts/{id}/
- Test URL : http://localhost:3000/posts/5bb1c3dbfaf8f91ca8c4c4f2/edit
- body
<pre>{
	"title" : "글 수정 제목입니다.",
	"body" : "글 수정 내용입니다."
}</pre>
- json Result EX
<pre>{
    "_id": "5bb1c3dbfaf8f91ca8c4c4f2",
    "title": "글 수정 제목입니다.",
    "body": "글 수정 내용입니다.",
    "author": "5bb1bc439966d50670ff8587",
    "createdAt": "2018-10-01T06:51:07.341Z",
    "__v": 0,
    "updatedAt": "2018-10-01T07:10:48.386Z",
    "createdDate": "2018-10-01",
    "createdTime": "15:51:07",
    "updatedDate": "2018-10-01",
    "updatedTime": "16:10:48",
    "id": "5bb1c3dbfaf8f91ca8c4c4f2"
}</pre>

④ 글 삭제
- Description : posts의 id가 {id}인 글을 삭제합니다. (*②로그인 필요)
- Method : DELETE
- URL : /posts/{:id}
- Test URL : http://localhost:3000/posts/5bb1c3dbfaf8f91ca8c4c4f2
- json Result EX
<pre>{
    "_id": "5bb1c3dbfaf8f91ca8c4c4f2"
}</pre>