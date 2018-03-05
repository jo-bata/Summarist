const schedule = require('node-schedule');

var job = schedule.scheduleJob('00 00 00 * * * *', function () {
    let mNow = new Date();
    console.log(mNow);
    var request = require("request");
    var cheerio = require("cheerio");
    var Iconv = require('iconv').Iconv;
    var iconv = new Iconv('euc-kr', 'utf-8//translit//ignore');
    var iconv2 = require('iconv-lite');
    var urlencode = require('urlencode');
    var mysql = require('mysql');
    var urlencode = require('urlencode');
    var client = {  // 데이터베이스와 연결합니다.
        user: 'root',
        password: 'woo1557!',
        database: 'Summarist'
    };
    var connection;
    var dt = new Date();
    var date = dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + (dt.getDate());
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
    var async = require('async');
    var main_url = "http://news.naver.com/main/ranking/searchWeek.nhn?mid=etc&sid1=111";
    var default_url = "http://news.naver.com";
    var main_sub_url = "/main/ranking/searchWeek.nhn?date=" + dt.getFullYear() + dt_month + dt_day + "&query=";
    var sql_query = 'INSERT INTO wkinfo(date, rank, keyword, num, title, url) VALUES (?, ?, ?, ?, ?, ?)';
    var sub_url1 = new Array();
    var sub_url2 = new Array();
    var keyword = new Array();

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

    function crawling_weekly_keyword1() {  // 여기서 얻을 것 -> date, rank, keyword
        var options = {
            url: main_url,
            encoding: null
        };
        request(options, function (error, response, body) {
            if (error) throw error;
            var html = iconv.convert(body).toString();
            var $ = cheerio.load(html);
            var firstStep = $("div.ranking_keyword ul");
            var firstStepLen = firstStep.find("li").length;
            var secondStep = firstStep.find("li").find("div").find("a");
            console.log("<First Step>");
            for (var i = 0; i < firstStepLen; i++) {
                keyword[i] = secondStep[i].childNodes[0].data
                sub_url1[i] = secondStep[i].attribs.href;
                var buf = iconv2.encode(keyword[i], "euc-kr");
                sub_url1[i] = sub_url1[i].replace(keyword[i], buf.toString('hex').replace(/([a-f0-9]{2})/g, "%$1"));  // euc-kr hexadecimalcode로 변환!!!!
                console.log("[" + i + "]sub_url1 : " + sub_url1[i] + ", keyword : " + keyword[i]);
            }
        });
    };

    function crawling_weekly_keyword2(sub_url, index) {  // 여기서 얻을 것 -> num, title, url
        var options2 = {
            url: default_url + sub_url,
            encoding: null
        };
        request(options2, function (error, response, body) {
            // console.log(options2);
            if (error) throw error;
            var html = iconv.convert(body).toString();
            var $ = cheerio.load(html);
            var firstStep = $("div.word_list ul");
            var firstStepLen = firstStep.find("li").find("a").length; // 30
            var secondStep = firstStep.find("li").find("a");
            var buf = iconv2.encode(keyword[index], "euc-kr");
            var keyword_url = new Array();
            var keyword_title = new Array();
            var reserve = function (cb) {
                process.nextTick(function () {
                    cb();
                });
            };
            var makeReserve = function (i) {
                reserve(function () {
                    keyword_title[i] = "";
                    keyword_url[i] = default_url + secondStep[i].attribs.href;
                    keyword_url[i] = keyword_url[i].replace(keyword[index], buf.toString('hex').replace(/([a-f0-9]{2})/g, "%$1"));
                    // console.log(secondStep[i]);
                    for (var j = 0; j < secondStep[i].childNodes.length; j++) {
                        if (secondStep[i].childNodes[j].type == "text")  // title i(30), j(3)
                            keyword_title[i] += secondStep[i].childNodes[j].data;
                        else
                            keyword_title[i] += secondStep[i].childNodes[j].childNodes[0].data;
                    }
                    connection.query(sql_query, [date, (index + 1), keyword[index], (i + 1), keyword_title[i], keyword_url[i]], function (err, result, fields) {
                        if (error) {
                            console.log('query error');
                        }
                        else {
                            console.log("[result] : " + result + ", [date] : " + date +  ", [index] + 1 : " + (index + 1) + ", [keyword[index]] : " + keyword[index] + ", [i + 1] : " + (i + 1) + ", [keyword_title[i]] : "  + keyword_title[i] + ", [url] : "  + keyword_url[i]);
                        }
                    });
                });
            };
            for(var i = 0; i < firstStepLen; i++) {
                makeReserve(i);
            }
        });
    };

    var tasks = [
        function (callback) {
            crawling_weekly_keyword1();
            callback(null, 'first');
        },
        function (callback) {
            setTimeout(function () {
                for (var i = 0; i < 20; i++) {
                    crawling_weekly_keyword2(sub_url1[i], i);
                }
                callback(null, 'second');
            }, 1000);
        }
    ];

    async.series(tasks, function (err, results) {
        console.log(results);
    });
});