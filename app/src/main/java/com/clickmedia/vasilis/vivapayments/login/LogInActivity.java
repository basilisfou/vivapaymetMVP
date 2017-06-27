package com.clickmedia.vasilis.vivapayments.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clickmedia.vasilis.vivapayments.R;
import com.clickmedia.vasilis.vivapayments.details.DetailsActivity;
import com.clickmedia.vasilis.vivapayments.model.User;
import com.clickmedia.vasilis.vivapayments.util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LogInActivity extends AppCompatActivity {
    private EditText username, password;
    private Button login;
    private TextView reset;
    private String username_string, password_string;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private static final String TAG = "LogIn2";
    private String cookie,userName,niceName,usersMail,usersID,fullName;
    private SharedPreferences sharedPreferences;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        progressDialog = new ProgressDialog(this);
        context = this;

        sharedPreferences = getSharedPreferences("gadgetflow", 0);
        editor            = sharedPreferences.edit();

        username = (EditText)findViewById(R.id.username_log_in);
        password = (EditText)findViewById(R.id.password_log_in);
        login    = (Button)findViewById(R.id.login_button);
        reset    = (TextView) findViewById(R.id.reset_pass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username_string = username.getText().toString();
                password_string = password.getText().toString();

                if(username_string.length() != 0 && password_string.length() != 0){
                    httpLogin();
                } else {
                    Toast.makeText(LogInActivity.this,"Please fill in an username and a password to proceed",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //back button
//        if(id == android.R.id.home){
//            this.finish();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,LogInActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
    }

    /** Get Login request **/
    public void httpLogin(){

        CommonUtils.showDialog(this,"Loading...",true);

        //todo login, handler simulates login request
        new Handler().postDelayed(new Runnable(){
            public void run() {
                User user = getUser();
                Intent intent = new Intent(LogInActivity.this,DetailsActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                CommonUtils.hideDialog();

            }}, 4000);

    }

    private User getUser(){
        return new User("Vasilis","vasilisfouroulis@gmail.com","Fouroulis","6936783201");
    }
}
