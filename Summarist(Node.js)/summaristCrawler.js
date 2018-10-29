const schedule = require('node-schedule');

const job = schedule.scheduleJob('*/60 * * * *', function() {
    const request = require("request");
    const cheerio = require("cheerio");
    const Iconv = require('iconv').Iconv;
    const iconv = new Iconv('euc-kr', 'utf-8//translit//ignore');
    const mysql = require('mysql');
    const client = {  // 데이터베이스와 연결합니다.
        host : 'localhost',
        user : 'root',
        password : '1234',
        database : 'Summarist'
    };
    let connection = mysql.createConnection(client);

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

    function makeDate(num) {
        const dt = new Date();
        let dtMonth;
        if (dt.getMonth() < 9)
            dtMonth = "0" + (dt.getMonth() + 1);
        else
            dtMonth = (dt.getMonth() + 1);
        let dtDay;
        if (dt.getDate() < 10)
            dtDay = "0" + dt.getDate();
        else
            dtDay = dt.getDate();
        let dtHours;
        if (dt.getHours() < 10)
            dtHours = "0" + dt.getHours();
        else
            dtHours = dt.getHours();
        const date = dt.getFullYear() + '-' + dtMonth + '-' + dtDay;
        switch (num) {
            case 0:
                return date;
            case 1:
                return dtMonth;
            case 2:
                return dtDay;
            case 3:
                return dtHours;
        }
    }

    function keywordCrawling() {
        const keywordUrl = 'https://www.naver.com/';
        const keywordSubUrl = "https://search.naver.com/search.naver?where=nexearch&sm=tab_lve&ie=utf8&query=";
        const sqlQuery = 'INSERT INTO rankinginfo SET ?';
        request(keywordUrl, function (error, response, body) {
            if (error) throw error;
            const $ = cheerio.load(body);
            const rankingElements = $("div.ah_roll div.ah_roll_area");
            rankingElements.each(function () {
                const rankingTitle = $(this).find("a.ah_a").text();
                const after1 = rankingTitle.split('\n');
                let cnt = 0;
                for (let i = 0; i < after1.length; i++) {
                    if (after1[i].length > 0 && isNaN(after1[i])) {
                        cnt++;
                        let params;
                        if (cnt <= 10) {
                            params = {
                                type : 10,
                                date : makeDate(0),
                                hour : makeDate(3),
                                rank : cnt,
                                title : after1[i],
                                url : keywordSubUrl + encodeURIComponent(after1[i])
                            };
                        }
                        else if (cnt <= 20) {
                            params = {
                                type : 11,
                                date : makeDate(0),
                                hour : makeDate(3),
                                rank : cnt,
                                title : after1[i],
                                url : keywordSubUrl + encodeURIComponent(after1[i])
                            };
                        }
                        connection.query(sqlQuery, params, function (err, result) {
                            if(err)
                                console.log(err);
                            else
                                console.log(result);
                        });
                        console.log(after1[i]);
                    }
                }
            });
        });
    }

    function newsCrawling(num) {
        const newsUrl = 'http://news.naver.com';
        const sqlQuery = 'INSERT INTO rankinginfo SET ?';
        const options = {
            url: newsUrl,
            encoding: null
        };
        request(options, function (error, response, body) {
            if (error) throw error;
            const divId = '#ranking_' + num;
            const $ = cheerio.load(iconv.convert(body).toString());
            const rankingElements = $(divId).find('li');
            rankingElements.each(function (index) {
                const rankAndTitle = $(this).text().split('\n');
                const rankingUrl = $(this).find('a').attr('href');
                const params = {
                    type : num,
                    date : makeDate(0),
                    hour : makeDate(3),
                    rank : rankAndTitle[0],
                    title : rankAndTitle[1],
                    url : newsUrl + rankingUrl
                };
                connection.query(sqlQuery, params, function (err, result) {
                    if(err)
                        console.log(err);
                    // else
                    //     console.log(result);
                });
                console.log(rankAndTitle[0] + ', ' + rankAndTitle[1] + ', ' + newsUrl + rankingUrl);
            });
        });
    }

    handleDisconnect();
    keywordCrawling();
    newsCrawling(100); // 정치
    newsCrawling(101); // 경제
    newsCrawling(102); // 사회
    newsCrawling(103); // 생활/문화
    newsCrawling(104); // 세계
    newsCrawling(105); // IT/과학
    console.log(makeDate(0));
});