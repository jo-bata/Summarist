package kr.ac.skuniv.oopsla.jobata.summarist;

import android.os.AsyncTask;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONTaskBN extends AsyncTask<String, String, String> {
    BNViewModel bnViewModel;
    public JSONTaskBN(BNViewModel bnViewModel) {
        this.bnViewModel = bnViewModel;
    }

    @Override
    protected String doInBackground(String... urls) {
        // JSONObject를 만들고 key value 형식으로 값을 저장해준다.
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", "androidTest");
            jsonObject.accumulate("name", "jobata");
            HttpURLConnection con = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);  // url을 가져온다
                con = (HttpURLConnection)url.openConnection();
                con.connect();  // 연결 수행
                InputStream stream = con.getInputStream();  // 입력 스트림 생성
                reader = new BufferedReader(new InputStreamReader(stream));  // 속도 up, 부하 down, 버퍼 선언
                StringBuffer buffer = new StringBuffer();  // 실제 데이터를 받는곳
                String line = "";  // line별 스트링을 받기 위한 temp 변수
                while((line = reader.readLine()) != null)  // 실제 reader에서 데이터를 가져오는 부분, node.js서버로부터 데이터를 가져옴
                    buffer.append(line);
                return buffer.toString();  // 다 가져오면 string형태로 변환
            } catch(MalformedURLException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null) {  // 종료되면 disconnect메소드 호출
                    con.disconnect();
                }
                try {
                    if(reader != null) {  // 버퍼를 닫아줌
                        reader.close();
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // 서버로부터 받은 값을 출력해주는 부
        Log.d("hi" , "response : " + result);
        Gson gson = new Gson();
        BNVO bnVO[] = gson.fromJson(result, BNVO[].class);
        bnViewModel.getBnAdapter().bnList.clear();
        for(int i = 0; i < bnVO.length; i++)
            bnViewModel.getBnAdapter().addOne(bnVO[i]);
        bnViewModel.getBnAdapter().notifyDataSetChanged();
    }
}