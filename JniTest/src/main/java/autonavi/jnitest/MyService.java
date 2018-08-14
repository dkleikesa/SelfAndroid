package autonavi.jnitest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    public MyService() {
        Log.e(MainActivity.TAG, "MyService MyService"+" taskID:" + "xxxx" + " " + this.toString());
    }

    public void onCreate(){
        Intent nintent = new Intent();
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        nintent.setClass(this, Main2Activity.class);
        this.startActivity(nintent);
        Log.e(MainActivity.TAG, "MyService onCreate"+" taskID:" + "xxxx" + " " + this.toString());

    }

    @Override
    public IBinder onBind(Intent intent) {
        Intent nintent = new Intent();
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        nintent.setClass(this, Main2Activity.class);
        this.startActivity(nintent);
        Log.e(MainActivity.TAG, "MyService onBind"+" taskID:" + "xxxx" + " " + this.toString());
        return null;
    }
}
