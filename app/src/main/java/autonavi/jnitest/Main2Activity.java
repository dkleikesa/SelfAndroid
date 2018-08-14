package autonavi.jnitest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class Main2Activity extends FragmentActivity {

    String TAG = MainActivity.TAG;
    static String EDD = "dd";
    Context mContext;

    public Main2Activity() {
        System.out.print("eee");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "2 onCreate" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button btn = (Button) findViewById(R.id.m2btn);
        mContext = this;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "m2 activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.setClass(mContext, Main3Activity.class);
                mContext.startActivity(intent);
            }
        });
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_account_test_page_input);
        View v = LayoutInflater.from(this).inflate(R.layout.login_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(v, params);
        final EditText account = (EditText) v.findViewById(R.id.et_account_testpage_login_account);
        final EditText pwd = (EditText) v.findViewById(R.id.et_account_testpage_login_password);
        Button login = (Button) v.findViewById(R.id.btn_account_testpage_login_submit);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "account:" + account.getText().toString() + " pwd:" + pwd.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        initSpinner();

    }

    Spinner mSpinner;

    public void initSpinner() {
        mSpinner = (Spinner) findViewById(R.id.sp_account_test_page_function);

        List<CharSequence> algoo = new ArrayList();
        algoo.add("登陆");
        algoo.add("绑定");
        ArrayAdapter<CharSequence> providerAdapter = new ArrayAdapter<CharSequence>(
                this, android.R.layout.simple_spinner_item, algoo);
        providerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(providerAdapter);
        mSpinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                });
    }

    @Override
    protected void onStart() {
        Log.e(TAG, "2 onStart" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onStart();

    }

    @Override
    protected void onResume() {
        Log.e(TAG, "2 onResume" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onResume();

    }


    @Override
    protected void onPause() {
        Log.e(TAG, "2 onPause" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onPause();

    }

    @Override
    protected void onStop() {
        Log.e(TAG, "2 onStop" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "2 onDestroy" + " taskID:" + this.getTaskId() + " " + this.toString());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

    @Override
    protected void onRestart() {
        Log.e(TAG, "2 onRestart" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onRestart();

    }


}
