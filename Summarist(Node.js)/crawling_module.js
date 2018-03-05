const schedule = require('node-schedule');

var job = schedule.scheduleJob('10 00 * * * * *', function () {
    let mNow = new Date();
    console.log(mNow);
    var request = require("request");
    var cheerio = require("cheerio");
    var Iconv = require('iconv').Iconv;
    var iconv = new Iconv('euc-kr', 'utf-8//translit//ignore');
    var mysql = require('mysql');
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
    var keyword_url_total = "https://www.naver.com/";  // 종합
    var keyword_url_field = "https://datalab.naver.com/home/sectionSearch.naver";  // 분야별
    var keyword_sub_url = "https://search.naver.com/search.naver?where=nexearch&sm=tab_lve&ie=utf8&query=";
    var sql_query = 'INSERT INTO rankinginfo(type, date, hour, rank, title, url) VALUES (?, ?, ?, ?, ?, ?)';

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

    function crawling_keyword_total(params_url) {  // 키워드 종합 크롤링 함수
        request(params_url, function (error, response, body) {
            if (error) throw error;
            var $ = cheerio.load(body);
            var rankingElements = $("div.ah_roll div.ah_roll_area");
            rankingElements.each(function () {
                var rankingTitle = $(this).find("a.ah_a").text();
                var after1 = rankingTitle.split('\n');
                var cnt = 0;
                for (var i = 0; i < after1.length; i++) {
                    if (after1[i].length > 0 && isNaN(after1[i])) {
                        cnt++;
                        if (cnt <= 10) {
                            connection.query(sql_query, [10, date, dt.getHours(), cnt, after1[i], (keyword_sub_url + encodeURIComponent(after1[i]))], function () {  // 데이터베이스 쿼리를 실행합니다.

                            });
                            console.log(after1[i]);
                        }
                    }
                }
            });
        });
    };

    function crawling_keyword_field(params_url, rank_title_string, index) {  // 키워드 분야별 크롤링 함수
        request(params_url, function (error, response, body) {
            if (error) throw error;
            var $ = cheerio.load(body);
            var rankingElements = $("div.carousel_area div.keyword_rank");
            rankingElements.each(function () {
                var rank_title = $(this).find("strong.rank_title").text();
                if (rank_title == rank_title_string) {
                    var rankingTitle = $(this).find("a.list_area").text();
                    var after1 = rankingTitle.split('\n');
                    var arr_title = new Array();
                    var cnt = 0;
                    for (var i = 0; i < after1.length; i++) {
                        if (after1[i].trim().length > 0 && isNaN(after1[i].trim())) {
                            arr_title[cnt++] = after1[i].trim();
                        }
                    }
                    for (var j = 0; j < arr_title.length; j++) {
                        connection.query(sql_query, [index, date, dt.getHours(), ((j % 10) + 1), arr_title[j], (keyword_sub_url + encodeURIComponent(arr_title[j]))], function () {  // 데이터베이스 쿼리를 실행합니다.

                        });
                        console.log(index + ", " + date + ", " + dt.getHours() + ", " + ((j % 10) + 1) + ", " + arr_title[j]);
                    }
                }
            });
        });
    };

    function crawling_News_1(sectionId, news_type) {  // 종합, 정치, 경제, 사회, 생활/문화, 세계, IT/과학
        var url_total = "http://news.naver.com/main/ranking/popularDay.nhn?rankingType=popular_day&sectionId=" + sectionId + "&date=" + dt.getFullYear() + dt_month + dt_day;  // 
        var options = {
            url: url_total,
            encoding: null
        };
        request(options, function (error, response, body) {
            if (error) throw error;
            var html = iconv.convert(body).toString();
            var $ = cheerio.load(html);
            var rankingElements_top3 = $("div.ranking_top3"); // ranking 1~3
            var rankingUrl = new Array();
            var rankingTitle = new Array();
            var sub_url = "http://news.naver.com";
            for (var i = 0; i < 3; i++) {
                rankingUrl[i] = sub_url + $(rankingElements_top3).find("li.num" + (i + 1) + " a").attr("href");
                rankingTitle[i] = $(rankingElements_top3).find("li.num" + (i + 1) + " a").attr("title");
                connection.query(sql_query, [news_type, date, dt.getHours(), (i + 1), rankingTitle[i], rankingUrl[i]], function () {  // 데이터베이스 쿼리를 실행합니다.

                });
                console.log(rankingUrl[i]); console.log(rankingTitle[i]);
            }
            var rankingElements_extra = $("div.ranking_section"); // ranking 4~10
            var rankingTitle_extra = new Array();
            for (i; i < 10; i++) {
                rankingUrl[i] = sub_url + $(rankingElements_extra).find("li.gnum" + (i + 1) + " a").attr("href");
                rankingTitle[i] = $(rankingElements_extra).find("li.gnum" + (i + 1) + " a").attr("title");
                connection.query(sql_query, [news_type, date, dt.getHours(), (i + 1), rankingTitle[i], rankingUrl[i]], function () {  // 데이터베이스 쿼리를 실행합니다.

                });
                console.log(rankingUrl[i]); console.log(rankingTitle[i]);
            }
        });
    };

    function crawling_News_Entertain(sectionId, news_type) {  // 연예
        var url_total = "http://entertain.naver.com/ranking?rankingType=popular_day&sectionId=" + sectionId + "&date=" + dt.getFullYear() + dt_month + dt_day;  // 종합
        request(url_total, function (error, response, body) {
            if (error) throw error
            var $ = cheerio.load(body);
            var rankingElements = $("div._tab_data div.rank_lst ul li a");
            var rankingUrl = new Array();
            var rankingTitle = new Array();
            var sub_url = "http://entertain.naver.com";
            var after1;
            for (var i = 0; i < 10; i++) {
                rankingUrl[i] = sub_url + $(rankingElements[i]).attr("href");
                after1 = $(rankingElements[i]).text();
                if (i < 9)
                    rankingTitle[i] = after1.substring(1, after1.length);
                else
                    rankingTitle[i] = after1.substring(2, after1.length);
                connection.query(sql_query, [news_type, date, dt.getHours(), (i + 1), rankingTitle[i], rankingUrl[i]], function () {  // 데이터베이스 쿼리를 실행합니다.

                });
                console.log(rankingUrl[i]);
                console.log(rankingTitle[i]);
            }
        });
    };

    function crawling_News_Sports(sectionId, news_type) {  // 스포츠
        var url_total = "http://news.naver.com/main/ranking/popularDay.nhn?rankingType=popular_day&sectionId=" + sectionId + "&date=" + dt.getFullYear() + dt_month + dt_day;
        var options = {
            url: url_total,
            encoding: null
        };
        request(options, function (error, response, body) {
            if (error) throw error;
            var html = iconv.convert(body).toString();
            var $ = cheerio.load(html);
            // ranking 1~3
            var rankingElements_top3 = $("div.ranking_top3");
            var rankingUrl = new Array();
            var rankingTitle = new Array();
            for (var i = 0; i < 3; i++) {
                rankingUrl[i] = $(rankingElements_top3).find("li.num" + (i + 1) + " a").attr("href");
                rankingTitle[i] = $(rankingElements_top3).find("li.num" + (i + 1) + " a").attr("title");
                connection.query(sql_query, [news_type, date, dt.getHours(), (i + 1), rankingTitle[i], rankingUrl[i]], function () {

                });
                console.log(rankingUrl[i]);
                console.log(rankingTitle[i]);
            }
            // ranking 4~10
            var rankingElements_extra = $("div.ranking_section");
            var rankingTitle_extra = new Array();
            for (i; i < 10; i++) {
                rankingUrl[i] = $(rankingElements_extra).find("li.gnum" + (i + 1) + " a").attr("href");
                rankingTitle[i] = $(rankingElements_extra).find("li.gnum" + (i + 1) + " a").attr("title");
                connection.query(sql_query, [news_type, date, dt.getHours(), (i + 1), rankingTitle[i], rankingUrl[i]], function () {

                });
                console.log(rankingUrl[i]);
                console.log(rankingTitle[i]);
            }
        });
    };
    crawling_keyword_total(keyword_url_total);  // 검색어 종합
    crawling_keyword_field(keyword_url_field, "TV오락", 11);  // 검색어 분야별
    crawling_keyword_field(keyword_url_field, "쇼핑", 12);
    crawling_keyword_field(keyword_url_field, "영화", 13);
    crawling_keyword_field(keyword_url_field, "자동차", 14);
    crawling_keyword_field(keyword_url_field, "게임", 15);
    crawling_keyword_field(keyword_url_field, "싱글남", 16);
    crawling_keyword_field(keyword_url_field, "싱글녀", 17);
    crawling_keyword_field(keyword_url_field, "주부", 18);
    crawling_keyword_field(keyword_url_field, "대학생", 19);
    crawling_keyword_field(keyword_url_field, "청소년", 20);
    crawling_News_1("000", 1);  // 종합
    crawling_News_1("100", 2);  // 정치
    crawling_News_1("101", 3);  // 경제
    crawling_News_1("102", 4);  // 사회
    crawling_News_1("103", 5);  // 생활/문화
    crawling_News_1("104", 6);  // 세계
    crawling_News_1("105", 7);  // IT/과학
    crawling_News_Entertain("106", 8);  // 연예
    crawling_News_Sports("107", 9);  // 스포츠
});