package edu.pku.pkutodofrontend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import edu.pku.pkutodofrontend.R;
import edu.pku.pkutodofrontend.utils.StringUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 登录页面
 */
public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.name);
        password = findViewById(R.id.pwd);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean legal = true;
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                if(usernameStr.isEmpty() || passwordStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    legal = false;
                }
                if(legal) {
                    loginWithIAAA(usernameStr, passwordStr);
                }
            }
        });
    }

    private void loginWithIAAA(final String username, final String password) {
        // 创建 OkHttpClient 客户端
        OkHttpClient client = new OkHttpClient();

        // 构建表单数据
        RequestBody formBody = new FormBody.Builder()
                .add("appid", "portal2017")
                .add("userName", username)
                .add("password", password)
                .add("randCode", "")
                .add("smsCode", "")
                .add("otpCode", "")
                .add("redirUrl", "https://portal.pku.edu.cn/portal2017/ssoLogin.do")
                .build();

        // 构建请求
        Request request = new Request.Builder()
                .url("https://iaaa.pku.edu.cn/iaaa/oauthlogin.do")
                .post(formBody)
                .build();

        // 发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseData = response.body().string();
                if (!response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Response Data: " + responseData);
                            Toast.makeText(MainActivity.this, "响应错误: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         *  success Response Data: {"success":true,"token":"e2f5102228419d99b2f8984f0fe0b5da"}
                         *  error Response Data: {"success":false,"errors":{"code":"E01","msg":"User ID or Password is NOT correct."}}
                         */
                        String success = StringUtils.extractString(responseData, "success");
                        if (success.equals("false")) {
                            Toast.makeText(MainActivity.this, "用户名或密码错误！", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
                            String token = StringUtils.extractString(responseData, "token");
                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}

