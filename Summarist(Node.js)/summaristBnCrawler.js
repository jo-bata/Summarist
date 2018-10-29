const schedule = require('node-schedule');

const job = schedule.scheduleJob('*/1 * * * *', function() {
    let mNow = new Date();
    console.log(mNow);
    var http = require("http");
    var request = require("request");
    var cheerio = require("cheerio");
    var Iconv = require('iconv').Iconv;
    var iconv = new Iconv('euc-kr', 'utf-8//translit//ignore');
    var mysql = require('mysql');
    var async = require('async');
    var client = {  // 데이터베이스와 연결합니다.
        user: 'root',
        password: '1234',
        database: 'Summarist'
    };
    var connection;
    var options = {
        url: 'https://search.naver.com/search.naver?where=news&query=%EC%86%8D%EB%B3%B4&ie=utf8&sm=tab_opt&sort=1&photo=0&field=1&reporter_article=&pd=0&ds=&de=&docid=&nso=so%3Add%2Cp%3Aall%2Ca%3At&mynews=0&mson=1&refresh_start=0&related=0',
        headers: {
            "Host": "search.naver.com",
            "Referer": "https://search.naver.com/search.naver?where=news&query=%EC%86%8D%EB%B3%B4&ie=utf8&sm=tab_opt&sort=1&photo=0&field=1&reporter_article=&pd=0&ds=&de=&docid=&nso=so%3Add%2Cp%3Aall%2Ca%3At&mynews=0&mson=1&refresh_start=0&related=0"
        }
    };
    var FCM = require('fcm-node');
    var serverKey = 'serverKey';
    var client_token = 'client_token';
    var push_data;
    var fcm = new FCM(serverKey);
    var dt = new Date();
    var date;
    var dt_month;
    if (dt.getMonth() < 10)
        dt_month = "0" + (dt.getMonth() + 1);
    else
        dt_month = (dt.getMonth() + 1);
    var dt_day;
    if (dt.getDate() < 10)
        dt_day = "0" + dt.getDate();
    else
        dt_day = dt.getDate();
    date = dt.getFullYear() + '-' + dt_month + '-' + dt_day;
    var dt_hours;
    if (dt.getHours() < 10)
        dt_hours = "0" + dt.getHours();
    else
        dt_hours = dt.getHours();
    var dt_minutes;
    if (dt.getMinutes() < 10)
        dt_minutes = "0" + dt.getMinutes();
    else
        dt_minutes = dt.getMinutes();
    var dt_seconds;
    if (dt.getSeconds() < 10)
        dt_seconds = "0" + dt.getSeconds();
    else
        dt_seconds = dt.getSeconds();
    var time = dt_hours + ':' + dt_minutes + ':' + dt_seconds;

    var insert_query = 'INSERT INTO bnInfo(time, title) VALUES (?, ?)';

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

    handleDisconnect();

    function crawling_breaking_news() {
        request(options, function (error, response, body) {
            if (error) throw error;
            var $ = cheerio.load(body);
            // console.log(body);
            var bnElements = $("ul.type01");
            var bnLen = $("ul.type01").find("li").length;
            var bnTitle = new Array();
            var jsonArray = new Array();
            var jsonObj = new Array();
            var afterTitle = new Array();
            var exceptArray = new Array();
            for (var i = 0; i < bnLen; i++)
                exceptArray[i] = 1;
            for (var i = 0; i < bnLen; i++) {
                bnTitle[i] = $('#sp_nws' + (i + 1), '.type01').find("dl").find("dt").find("a").attr("title");
                console.log("bnTitle[" + i + "] : " + bnTitle[i]);
                afterTitle[i] = specialCharRemove(bnTitle[i]);
                var splitArray = afterTitle[i].split(' ');
                jsonArray[i] = '{';
                for (var j = 0; j < splitArray.length; j++) {
                    if (splitArray[j] != "")
                        jsonArray[i] += '"' + splitArray[j] + '":0,';
                }
                jsonArray[i] = jsonArray[i].slice(0, -1);
                jsonArray[i] += '}';
                // console.log("after : " + bnTitle[i]);
                console.log(jsonArray[i]);
                jsonObj[i] = JSON.parse(jsonArray[i]);
                for (var key in jsonObj[i]) {
                    console.log(key + '->' + jsonObj[i][key]);
                }
            }
            for (var i = bnLen - 1; i >= 0; i--) {  // 크롤링 한 속보들 중에서 유사도가 높아 중복이 있는 속보 찾기
                for (var j = i - 1; j >= 0; j--) {
                    var value = 0;
                    for (var key in jsonObj[i]) {
                        jsonObj[i][key] = 0;
                        for (var key2 in jsonObj[j]) {
                            if (key == key2) {
                                jsonObj[i][key] = 1;
                                value += 1;
                            }
                            else {
                                var cnt = 0;
                                var length = (key.length < key2.length) ? key.length : key2.length;
                                for (var a = 0; a < length; a++) {
                                    if (key.charAt(a) == key2.charAt(a))
                                        cnt += 1;
                                }
                                if ((cnt / length) > 0.5 && length > 1) {
                                    // console.log("key : " + key + ", key2 : " + key2 + ", cnt : " + cnt + ", length : " + length + ", cnt / length : " + cnt/length);
                                    jsonObj[i][key] = 1;
                                    value += 1;
                                }
                            }
                        }
                    }
                    // console.log("[" + i + " & " + j + "]length : " + Object.keys(jsonObj[i]).length + ", value : " + value + ", value / length : " + value / Object.keys(jsonObj[i]).length);
                    if ((value / Object.keys(jsonObj[i]).length) > 0.5) {
                        // console.log("[" + i + " & " + j + "]length : " + Object.keys(jsonObj[i]).length + ", value : " + value + ", value / length : " + value / Object.keys(jsonObj[i]).length);
                        exceptArray[j] = 0;
                    }
                }
            }
            // console.log("jsonObj[0] : " + JSON.stringify(jsonObj[0]));
            // console.log(bnLen);
            var reserve = function (cb) {
                process.nextTick(function () {
                    cb();
                });
            };
            var makeReserve = function (i) {
                reserve(function () {
                    if (exceptArray[i] == 1) {
                        if("");
                        var sql_q = 'SELECT * FROM bnInfo ORDER BY date DESC, time DESC LIMIT 10'
                        connection.query(sql_q, function (error, result, fields) {
                            if (error) {
                                console.log('query error1');
                            }
                            else {
                                for (var b = 0; b < Object.keys(result).length; b++) {
                                    var value = 0;
                                    var afterSelect = specialCharRemove(result[b].title);
                                    var splitSelect = afterSelect.split(' ');
                                    var jsonSelect = '{';
                                    for (var c = 0; c < splitSelect.length; c++) {
                                        if (splitSelect[c] != "")
                                            jsonSelect += '"' + splitSelect[c] + '":0,';
                                    }
                                    jsonSelect = jsonSelect.slice(0, -1);
                                    jsonSelect += '}';
                                    // console.log(jsonSelect);
                                    // console.log("i = " + i);
                                    // console.log(JSON.stringify(jsonObj[i]));
                                    var selectObj = JSON.parse(jsonSelect);
                                    for (var key in jsonObj[i]) {
                                        jsonObj[i][key] = 0;
                                        for (var key2 in selectObj) {
                                            if (key == key2) {
                                                jsonObj[i][key] = 1;
                                                value += 1;
                                            }
                                            else {
                                                var cnt = 0;
                                                var length = (key.length < key2.length) ? key.length : key2.length;
                                                for (var d = 0; d < length; d++) {
                                                    if (key.charAt(d) == key2.charAt(d))
                                                        cnt += 1;
                                                }
                                                if ((cnt / length) > 0.5 && length > 1) {
                                                    // console.log("key : " + key + ", key2 : " + key2 + ", cnt : " + cnt + ", length : " + length + ", cnt / length : " + cnt / length);
                                                    jsonObj[i][key] = 1;
                                                    value += 1;
                                                }
                                            }
                                        }
                                    }
                                    // console.log("value : " + value);
                                    if ((value / Object.keys(jsonObj[i]).length) > 0.5) {
                                        console.log("[" + i + " & " + b + "]length : " + Object.keys(jsonObj[i]).length + ", value : " + value + ", value / length : " + value / Object.keys(jsonObj[i]).length);
                                        exceptArray[i] = 0;
                                        // console.log("exceptArray[" + i + "] = " + exceptArray[i]);
                                    }
                                }
                                if (exceptArray[i] == 1) {
                                    push_data = {  // 발송할 push 메세지 내용
                                        // 수신대상
                                        to: client_token,
                                        // App이 실행중이지 않을 때 상태바 알림으로 등록할 내용
                                        notification: {
                                            title: "Hello Node",
                                            body: "Node로 발송하는 Push 메세지 입니다.",
                                            sound: "default",
                                            click_action: "FCM_PLUGIN_ACTIVITY",
                                            icon: "fcm_push_icon"
                                        },
                                        // 메시지 중요도
                                        priority: "high",
                                        // App 패키지 이름
                                        restricted_package_name: "kr.ac.skuniv.oopsla.jobata.summarist",
                                        // App에게 전달할 데이터
                                        data: {
                                            title : "" + bnTitle[i]
                                        }
                                    };
                                    fcm.send(push_data, function (err, response) {
                                        if(err) {
                                            console.error('push메시지 발송 실패.');
                                            console.error(err);
                                            return;
                                        }
                                        console.log('push메시지 발송 성공');
                                        console.log(response);
                                    })
                                    var insert_query = 'INSERT INTO bnInfo(date, time, title) VALUES (?, ?, ?)';
                                    connection.query(insert_query, [date, time, bnTitle[i]], function (error, result, fields) {
                                        if (error) {
                                            console.log(error);
                                            console.log('query error2');
                                        }
                                        else {
                                            console.log(result);
                                        }
                                    });
                                }
                                // JSON.stringify(result);
                                // console.log(Object.keys(result).length);
                                // console.log(bnTitle[0]);
                            }
                        });
                    }
                });
            };

            for (var i = bnLen - 1; i >= 0; i--) {
                makeReserve(i);
            }
        });
    };

    function specialCharRemove(obj) {  // 특수문자 와 속보 제거
        var val = obj.toString();
        val = val.replace("속보", "");
        val = val.replace(/·/gi, " ");
        val = val.replace(/…/gi, " ");
        val = val.replace(/[^가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\s]/gi, "");
        return val;
    };

    crawling_breaking_news();
});