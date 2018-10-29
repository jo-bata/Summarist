package kr.ac.skuniv.oopsla.jobata.summarist;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private final static String TAG = "FCM_ID";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FirebaseInstanceId Refreshed token: " + refreshedToken);
    }
}
