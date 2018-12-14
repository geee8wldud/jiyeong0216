
//express모듈 사용 - npm install express
const express = require('express');
//const routes = require('./routes')
const app =  express();


//npm install --global jade  jade템플릿을 이용하도록 setting.
app.set('view engine', 'ejs');
app.set('views','/views');


//애플리케이션이 문자열을 전달하는 대신 템플릿을 렌더링하도록 지시하려면 동적 값을 표시하기위해 템플릿 안에서 접근 가능한 객체와 템플릿 이름을 지정 
//response.render함수 호출 
app.get('/', (request, response) => {
    response.render('index', {message: 'Hello World'});

});



//  http://localhost:8080/
app.listen(8080);
