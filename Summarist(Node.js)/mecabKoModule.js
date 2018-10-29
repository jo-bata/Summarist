const schedule = require('node-schedule');

const job = schedule.scheduleJob('*/60 * * * *', function() {
    var Mecab = require('./mecab-mod.js');
    var mecab = new Mecab();
    var mysql = require('mysql');
    var client = {  // 데이터베이스와 연결합니다.
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

    handleDisconnect();

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
                return date2
            case 5:
                return dt.getHours();
        }
    }

    var keywordInfoArr = new Array();
    console.log("first : " + JSON.stringify(keywordInfoArr));

    var sql_q = 'SELECT * FROM tnInfo WHERE date="' + makeDate(0) + '"';
    console.log(sql_q);
    var sql_i = 'INSERT INTO tkinfo SET ?';

    connection.query(sql_q, function (error, result, fields) {
        if (error) {
            console.log('query error1');
        }
        else {
            for (var i in result) {
                var text = result[i].title;
                var afterText = text.replace(/\[(.*?)\]/gi, "");
                afterText = afterText.replace(/\<(.*?)\>/gi, "");
                console.log("[" + i + "] : " + result[i].title);
                mecab.parse(afterText, function (items) {
                    for (var i in items) {
                        var k = items[i];
                        var word = k[0];
                        var pos = k[1];
                        var data = new Object();
                        if (k == "EOS") continue;
                        if ((pos == "NNG" || pos == "NNP" || pos == "SL" || pos == "SH") && word.length > 1) {
                            var state = false;
                            keywordInfoArr.filter(function (item) {
                                if (item.keyword === k[0]) {
                                    state = true;
                                    item.count++;
                                }
                            });
                            if (!state) {
                                data.keyword = k[0];
                                data.count = 1;
                                keywordInfoArr.push(data);
                            }
                            // console.log(k[0] + ":" + k[1]);
                        }
                    }
                });
            }
            keywordInfoArr.sort(function (a, b) {
                return b["count"] - a["count"];
            });
            for (var j = 0; j < 50; j++) {
                const params = {
                    date: makeDate(0),
                    hour: makeDate(5),
                    rank: (j + 1),
                    keyword: keywordInfoArr[j].keyword,
                    count: keywordInfoArr[j].count
                };
                connection.query(sql_i, params, function (error, result, fields) {
                    if (error) {
                        console.log('query error2 : ' + error);
                    }
                    // else {
                    //
                    // }
                });
                var jsonInfo = JSON.stringify(keywordInfoArr[j]);
                console.log("[" + j + "] : " + jsonInfo);
            }
        }
    });
});