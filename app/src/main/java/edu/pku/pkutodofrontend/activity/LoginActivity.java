package edu.pku.pkutodofrontend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import edu.pku.pkutodofrontend.R;
import edu.pku.pkutodofrontend.utils.APIUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    EditText name,pwd;
    Button btnlogin,btngotoreg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        name = this.findViewById(R.id.name);            //用户名输入框
        pwd = this.findViewById(R.id.pwd);              //密码输入框
        btnlogin = this.findViewById(R.id.login);         //登录按钮
        btngotoreg = this.findViewById(R.id.gotoreg);      //注册按钮

        // 设置登录按钮点击事件
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入值
                String username = name.getText().toString();
                String password = pwd.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 构造请求体
                            String form = "{\n" +
                                    "  \"username\": \"" + username+ "\",\n" +
                                    "  \"password\": \"" + password + "\"\n" +
                                    "}";

                            MediaType JSON = MediaType.get("application/json; charset=utf-8");
                            RequestBody requestBody = RequestBody.create(JSON, form);

                            System.out.println("cons json");
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(APIUtils.LOGIN)
                                    .post(requestBody)
                                    .build();

                            Response response = client.newCall(request).execute();
                            final String responseData = response.body().string();
                            System.out.println(responseData);

                            // 解析 JSON 响应
                            JSONObject jsonResponse = new JSONObject(responseData);

                            // 校验 msg 字段
                            String msg = jsonResponse.getString("msg");
                            if (msg.equals("ok")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            } else if (msg.equals("用户名或密码错误")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        btngotoreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
