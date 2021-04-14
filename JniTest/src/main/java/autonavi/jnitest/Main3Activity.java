package autonavi.jnitest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Main3Activity extends Activity {

    String TAG = MainActivity.TAG;
    static String EDD = "dd";
    Context mContext;
    ListView listView;

    public Main3Activity() {
        System.out.print("eee");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "3 onCreate" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button btn = (Button) findViewById(R.id.m2btn);
        mContext = this;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "m3 activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(mContext, Main2Activity.class);
                mContext.startActivity(intent);
            }
        });
        listView = findViewById(R.id.listView1);

        // 数组适配器
        ArrayAdapter<String> adapter = new WeekAdapter(this,
                R.layout.week_item, buildItemArray());
        // 为ListView设置适配器
        listView.setAdapter(adapter);

    }

    List buildItemArray() {
        List arr = new ArrayList();
        String[] weeks = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};
        int[] res = {R.drawable.week1, R.drawable.week2, R.drawable.week3, R.drawable.week4, R.drawable.week5, R.drawable.week6, R.drawable.week7};
        for (int i = 0; i < 10000; i++) {
            Week w = new Week();
            w.name = weeks[i % 7];
            w.id = res[i % 7];
            arr.add(w);
        }
        return arr;
    }

    public static class Week {
        String name;
        int id;
    }

    @Override
    protected void onStart() {
        Log.e(TAG, "3 onStart" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onStart();

    }

    @Override
    protected void onResume() {
        Log.e(TAG, "3 onResume" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onResume();

    }


    @Override
    protected void onPause() {
        Log.e(TAG, "3 onPause" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onPause();

    }

    @Override
    protected void onStop() {
        Log.e(TAG, "3 onStop" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "3 onDestroy" + " taskID:" + this.getTaskId() + " " + this.toString());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

    @Override
    protected void onRestart() {
        Log.e(TAG, "3 onRestart" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onRestart();

    }

}
