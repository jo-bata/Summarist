package kr.ac.skuniv.oopsla.jobata.summarist;

import android.os.AsyncTask;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONTaskTK extends AsyncTask<String, String, String> {
    TextView ranking[] = new TextView[10];
    int type, hour;
    String date;

    public JSONTaskTK(TextView rank[], int type, String date, int hour) {
        for (int i = 0; i < 10; i++)
            ranking[i] = rank[i];
        this.type = type;
        this.date = date;
        this.hour = hour;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            // JSONObject를 만들고 key value 형식으로 값을 저장해준다.
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("type", type);
            jsonObject.accumulate("date", date);
            jsonObject.accumulate("hour", hour);
            HttpURLConnection con = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);
                con = (HttpURLConnection) url.openConnection();  // 연결을 함
                con.setRequestMethod("POST");  // POST 방식으로 보냄
                con.setRequestProperty("cache-Control", "no-cache");  // 캐시 설정
                con.setRequestProperty("Content-Type", "application/json");  // application JSON 형식으로 전송
                con.setRequestProperty("Accept", "text/html");  // 서버에 response 데이터를 html로 받음
                con.setDoOutput(true);  // outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true);  // inputstream으로 서버로부터 응답을 받겠다는 의미
                con.connect();
                OutputStream outputStream = con.getOutputStream();  // 서버로 보내기위해서 스트림 만듬
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));  // 버퍼를 생성하고 넣음
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();  // 버퍼를 닫아줌
                InputStream stream = con.getInputStream();  // 서버로부터 데이터를 받음
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null)
                    buffer.append(line);
                return buffer.toString();  // 서버로부터 받은 값을 리턴해줌
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) con.disconnect();
                try {
                    if (reader != null) reader.close();  // 버퍼를 닫아줌
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // 서버로부터 받은 값을 출력해주는 부
        Log.d("tk" , "response : " + result);
        Gson gson = new Gson();
        TKVO tkVO[] = gson.fromJson(result, TKVO[].class);
        for (int i = 0; i < 10; i++)
            ranking[i].setText("" + tkVO[i].getKeyword() + " [" + tkVO[i].getCount() + "]");
    }
}