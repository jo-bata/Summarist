var express = require('express');
var bodyParser = require('body-parser');
var mysql = require('mysql');
var client = {  // 데이터베이스와 연결합니다.
    host : 'localhost',
    user : 'root',
    password : '1234',
    database : 'Summarist'
};
let connection = mysql.createConnection(client);
var app = express();
var dt = new Date();

app.use(bodyParser.json());

app.listen(3000, () => {
    console.log('Example app listening on port 3000!');
});

function handleDisconnect() {
    connection = mysql.createConnection(client); // Recreate the connection, since
    // the old one cannot be reused.

    connection.connect(function (err) {              // The server is either down
        if (err) {                                     // or restarting (takes a while sometimes).
            console.log('error when connecting to db:', err);
            setTimeout(handleDisconnect, 2000); // We introduce a delay before attempting to reconnect,
        }                                     // to avoid a hot loop, and to allow our node script to
    });                                     // process asynchronous requests in the meantime.
    // If you're also serving http, display a 503 error.
    connection.on('error', function (err) {
        console.log('db error', err);
        if (err.code === 'PROTOCOL_CONNECTION_LOST') { // Connection to the MySQL server is usually
            handleDisconnect();                         // lost due to either server restart, or a
        } else {                                      // connnection idle timeout (the wait_timeout
            throw err;                                  // server variable configures this)
        }
    });
};

function getResUsers(contents_type) {  // 안드로이드에게 get방식으로 json 객체 전달
    app.get('/users' + contents_type, (req, res) => {
        dt = new Date();
        console.log('who get in here/users' + contents_type);
        var sql_q = 'SELECT * FROM rankingInfo WHERE (type=' + contents_type + ' AND date="' + dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + '" AND hour=' + dt.getHours() + ')';
        console.log(sql_q);
        connection.query(sql_q, function (error, result, fields) {
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
        var sql_d = 'SELECT * FROM rankingInfo WHERE (type=' + body.type + ' AND date="' + body.date + '" AND hour=' + body.hour + ')';
        console.log(sql_d);
        connection.query(sql_d, function (error, result, fields) {
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
        var dt_month;
        if(dt.getMonth() < 9)
            dt_month = "0" + (dt.getMonth() + 1);
        else
            dt_month = dt.getMonth() + 1;
        var sql_q = 'SELECT * FROM bnInfo WHERE date="' + dt.getFullYear() + '-' + dt_month + '-' + dt_date + '" ORDER BY time DESC';
        console.log(sql_q);
        connection.query(sql_q, function (error, result, fields) {
            if (error) {
                console.log('query error');
            }
            else {
                res.json(result);
            }
        });
    });
};

function postResTK() {  // 안드로이드로 부터 받은 POST 데이터(JSON 객체)를 받아 그에 해당하는 json 객체 안드로이드에게 전달
    app.post('/tk', (req, res) => {
        dt = new Date();
        console.log('who get in here post/tk');
        var body = req.body;
        var sql_d = 'SELECT * FROM tkInfo WHERE date="' + body.date + '" and hour=' + body.hour + ' ORDER BY rank ASC LIMIT ' + (body.type * 10) + ', ' + ((body.type + 1) * 10);
        console.log(sql_d);
        connection.query(sql_d, function (error, result, fields) {
            if (error) {
                console.log('query error');
            }
            else {
                res.json(result);
            }
        });
    });
};

// function getResWK() {  // 안드로이드에게 get방식으로 속보 정보를 정렬하여 json 객체 전달
//     app.get('/getwk', (req, res) => {
//         console.log('who get in here/getwk');
//         var dt_date;
//         dt = new Date();
//         if (dt.getDate() < 10)
//             dt_date = "0" + dt.getDate();
//         else
//             dt_date = dt.getDate();
//         var sql_q = 'SELECT * FROM wkinfo WHERE date="' + dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + '"';
//         console.log(sql_q);
//         connection.query(sql_q, function (error, result, fields) {
//             if (error) {
//                 console.log('query error');
//             }
//             else {
//                 res.json(result);
//             }
//         });
//     });
// };
//
//
// function postResWK() {  // 안드로이드로 부터 받은 POST 데이터(JSON 객체)를 받아 그에 해당하는 json 객체 안드로이드에게 전달
//     app.post('/wk', (req, res) => {
//         dt = new Date();
//         console.log('who get in here post/wk');
//         var body = req.body;
//         var sql_d = 'SELECT * FROM wkinfo WHERE rank=' + (body.state + 1) + ' AND date="' + dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + '"';
//         console.log(sql_d);
//         connection.query(sql_d, function (error, result, fields) {
//             if (error) {
//                 console.log('query error');
//             }
//             else {
//                 res.json(result);
//             }
//         });
//     });
// };
handleDisconnect();

postResPost();

getResUsers(100); // 뉴스
getResUsers(10); // 검색어
getResBN();
postResTK();
// getResWK();
// postResWK();