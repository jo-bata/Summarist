var express = require('express');
var bodyParser = require('body-parser');
var mysql = require('mysql');  // 모듈을 추출합니다.
var client = mysql.createConnection({  // 데이터베이스와 연결합니다.
    user: 'root',
    password: 'woo1557!',
    database: 'Summarist'
});
var app = express();
var dt = new Date();

app.use(bodyParser.json());

app.listen(3000, () => {
    console.log('Example app listening on port 3000!');
});

function getResUsers(contents_type) {  // 안드로이드에게 get방식으로 json 객체 전달
    app.get('/users' + contents_type, (req, res) => {
        dt = new Date();
        console.log('who get in here/users' + contents_type);
        var sql_q = 'SELECT * FROM rankinginfo WHERE (type=' + contents_type + ' AND date="' + dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + '" AND hour=' + dt.getHours() + ')';
        console.log(sql_q);
        client.query(sql_q, function (error, result, fields) {
            if (error) {
                console.log('query error');
            }
            else {
                res.json(result);
            }
        });
    });
};

function postResPost() {  // 안드로이드로 부터 받은 POST 데이터(JSON 객체)를 받아 그에 해당하는 json 객체 안드로이드에게 전달
    app.post('/post', (req, res) => {
        dt = new Date();
        console.log('who get in here post/users');
        var body = req.body;
        var sql_d = 'SELECT * FROM rankinginfo WHERE (type=' + body.type + ' AND date="' + body.date + '" AND hour=' + body.hour + ')';
        console.log(sql_d);
        client.query(sql_d, function (error, result, fields) {
            if (error) {
                console.log('query error');
            }
            else {
                res.json(result);
            }
        });
    });
};

function getResBN() {  // 안드로이드에게 get방식으로 속보 정보를 정렬하여 json 객체 전달
    app.get('/bn', (req, res) => {
        console.log('who get in here/bn');
        var dt_date;
        dt = new Date();
        if(dt.getDate() < 10)
            dt_date = "0" + dt.getDate();
        else
            dt_date = dt.getDate();
        var sql_q = 'SELECT * FROM bninfo WHERE date="' + dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt_date + '" ORDER BY time DESC';
        console.log(sql_q);
        client.query(sql_q, function (error, result, fields) {
            if (error) {
                console.log('query error');
            }
            else {
                res.json(result);
            }
        });
    });
};

function getResWK() {  // 안드로이드에게 get방식으로 속보 정보를 정렬하여 json 객체 전달
    app.get('/getwk', (req, res) => {
        console.log('who get in here/getwk');
        var dt_date;
        dt = new Date();
        if (dt.getDate() < 10)
            dt_date = "0" + dt.getDate();
        else
            dt_date = dt.getDate();
        var sql_q = 'SELECT * FROM wkinfo WHERE date="' + dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + '"';
        console.log(sql_q);
        client.query(sql_q, function (error, result, fields) {
            if (error) {
                console.log('query error');
            }
            else {
                res.json(result);
            }
        });
    });
};


function postResWK() {  // 안드로이드로 부터 받은 POST 데이터(JSON 객체)를 받아 그에 해당하는 json 객체 안드로이드에게 전달
    app.post('/wk', (req, res) => {
        dt = new Date();
        console.log('who get in here post/wk');
        var body = req.body;
        var sql_d = 'SELECT * FROM wkinfo WHERE rank=' + (body.state + 1) + ' AND date="' + dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + '"';
        console.log(sql_d);
        client.query(sql_d, function (error, result, fields) {
            if (error) {
                console.log('query error');
            }
            else {
                res.json(result);
            }
        });
    });
};   

postResPost();

getResUsers(1); // 뉴스 
getResUsers(10); // 검색어
getResBN();
getResWK();
postResWK();