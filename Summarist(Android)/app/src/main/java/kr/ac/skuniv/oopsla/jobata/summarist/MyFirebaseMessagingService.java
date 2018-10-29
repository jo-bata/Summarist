package kr.ac.skuniv.oopsla.jobata.summarist;


import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final static String TAG = "FCM_MESSAGE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            String body = remoteMessage.getNotification().getBody();
            Map<String, String> data = remoteMessage.getData();
            String msg = data.get("message");
            if(msg == null || msg.length() == 0) {
                Log.d(TAG, body);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher) // 알림 영역에 노출 될 아이콘.
                        .setContentTitle(getString(R.string.app_name)) // 알림 영역에 노출 될 타이틀
                        .setContentText(body); // Firebase Console 에서 사용자가 전달한 메시지내용

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                notificationManagerCompat.notify(0x1001, notificationBuilder.build());
            }
            else {
                String msgSplit[] = msg.split(" ");
                Log.d(TAG, "Notification Body: " + body);
                Log.d(TAG, "Notification title: " + msg);
//            try {
//                JSONObject jsonObject = new JSONObject(body);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher) // 알림 영역에 노출 될 아이콘.
                        .setContentTitle(getString(R.string.app_name)) // 알림 영역에 노출 될 타이틀
                        .setContentText(msg); // Firebase Console 에서 사용자가 전달한 메시지내용

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                if(BNViewModel.PUSH_STATE && checkKeyword(msgSplit))
                    notificationManagerCompat.notify(0x1001, notificationBuilder.build());
            }
        }
    }
    public Boolean checkKeyword(String msgSplit[]) {
        if(BNViewModel.KEYWORD_STATE.equals(""))
            return true;
        else {
            for (int i = 0; i < msgSplit.length; i++) {
                int cnt = 0;
                int length = (msgSplit[i].length() < BNViewModel.KEYWORD_STATE.length()) ? msgSplit[i].length() : BNViewModel.KEYWORD_STATE.length();
                for (int j = 0; j < length; j++) {
                    if (msgSplit[i].charAt(j) == BNViewModel.KEYWORD_STATE.charAt(j))
                        cnt += 1;
                }
                if ((cnt / length) > 0.5 && length > 1) {
                    return true;
                }
            }
            return false;
        }
    }
}

