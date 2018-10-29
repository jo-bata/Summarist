const schedule = require('node-schedule');

const job = schedule.scheduleJob('*/60 * * * *', function() {
    const request = require("request");
    const cheerio = require("cheerio");
    const Iconv = require('iconv').Iconv;
    const iconv = new Iconv('euc-kr', 'utf-8//translit//ignore');
    const mysql = require('mysql');
    const client = {  // 데이터베이스와 연결합니다.
        host: 'localhost',
        user: 'root',
        password: '1234',
        database: 'Summarist'
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
        const date2 = dt.getFullYear() + dtMonth + dtDay;
        switch (num) {
            case 0:
                return date;
            case 1:
                return dtMonth;
            case 2:
                return dtDay;
            case 3:
                return dtHours;
            case 4:
                return date2;
        }
    }

    function todayNewsCrawling(num) {
        const mainUrl = 'http://news.naver.com/main/list.nhn?mode=LPOD&mid=sec&oid=';
        const subUrl = '&listType=title&date=' + makeDate(4);
        const pagingUrl = '&page=';
        const sqlQuery = 'INSERT INTO tninfo SET ?';
        const options = {
            url: mainUrl + num + subUrl,
            encoding: null
        };
        request(options, function (error, response, body) {
            if (error) throw error;
            const $ = cheerio.load(iconv.convert(body).toString());
            const pagingElements = $('.paging').text().split('\n');
            const paging = pagingElements[pagingElements.length - 2];
            console.log(paging);
            for (let i = 1; i <= paging; i++) {
                const options2 = {
                    url: mainUrl + num + subUrl + pagingUrl + i,
                    encoding: null
                };
                request(options2, function (error, response, body) {
                    const dollar = cheerio.load(iconv.convert(body).toString());
                    const bodyElements = dollar('.newspaper_area').find('ul').find('li');
                    const bodyElemnets2 = dollar('.type02').find('a');
                    bodyElements.each(function (index) {
                        const tnTitle = dollar(this).find('a').text();
                        console.log('[0]' + ', ' + i + ', ' + index + ' : ' + tnTitle);
                        const params = {
                            type: num,
                            date: makeDate(0),
                            title: tnTitle
                        };
                        connection.query(sqlQuery, params, function (err, result) {
                            if (err)
                                console.log(err);
                            // else
                            //     console.log(result);
                        });
                    });
                    bodyElemnets2.each(function (index) {
                        const tnTitle = dollar(this).text();
                        console.log('[1]' + ', ' + i + ', ' + index + ' : ' + tnTitle);
                        const params = {
                            type: num,
                            date: makeDate(0),
                            title: tnTitle
                        };
                        connection.query(sqlQuery, params, function (err, result) {
                            if (err)
                                console.log(err);
                            // else
                            //     console.log(result);
                        });
                    });
                });
            }
        });
    }

    handleDisconnect();
    todayNewsCrawling('032'); // 경향신문
    todayNewsCrawling('005'); // 국민일보
    todayNewsCrawling('020'); // 동아일보
    todayNewsCrawling('021'); // 문화일보
    todayNewsCrawling('081'); // 서울신문
    todayNewsCrawling('022'); // 세계일보
    todayNewsCrawling('023'); // 조선일보
    todayNewsCrawling('025'); // 중앙일보
    todayNewsCrawling('028'); // 한겨례
    todayNewsCrawling('469'); // 한국일보
});