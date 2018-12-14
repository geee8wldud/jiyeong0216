module.exports = function(){
    var mysql = require('mysql');
    var connection = mysql.createConnection({
        host: 'localhost', // DB가 위치한 IP주소
        port: 3306,          // DB와 연결할 포트번호
        user: 'root',        // 계정이름
        password: '111111',    // 계정 비밀번호
        database: 'board01'    // 데이터베이스 이름
      });
      connection.connect();

      return connection;
}

