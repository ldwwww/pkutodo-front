package edu.pku.pkutodofrontend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.pku.pkutodofrontend.R;
import edu.pku.pkutodofrontend.utils.APIUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText email,code,name,pwd;
    Button btnbacklogin,btnreg,btnemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        email = this.findViewById(R.id.email);
        code = this.findViewById(R.id.code);
        name = this.findViewById(R.id.username);            //用户名输入框
        pwd = this.findViewById(R.id.userpwd);              //密码输入框
        btnbacklogin = this.findViewById(R.id.backlogin);         //登录按钮
        btnreg = this.findViewById(R.id.reg);
        btnemail = this.findViewById(R.id.sendemail);

        btnbacklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnreg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean legal = true ;
                // 获取输入值
                String emailAddress = email.getText().toString();
                if(legal && emailAddress.isEmpty()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                        }
                    });
                    legal = false;
                }else if (!emailAddress.matches("\"^[^@]+@pku\\.edu\\.cn$|^[^@]+@stu\\.pku\\.edu\\.cn$")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "请使用pku邮箱后缀", Toast.LENGTH_SHORT).show();
                        }
                    });
                    legal = false;
                }

                String verifyCode = code.getText().toString();
                if (legal){
                    if (verifyCode.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT);
                            }
                        });
                        legal = false;
                        // 六位整数验证码
                    } else if (!verifyCode.matches("^[0-9]{6}$")){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "请填写六位数验证码", Toast.LENGTH_SHORT).show();
                            }
                        });
                        legal = false;
                    }

                }

                String username = name.getText().toString();
                if (legal && username.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                        }
                    });
                    legal = false;
                }

                String password = pwd.getText().toString();
                if (legal && password.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        }
                    });
                    legal = false;
                }

                if(legal){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 构造请求体
                                String form = "{\n" +
                                        "  \"email\": \"" + emailAddress + "\",\n" +
                                        "  \"verifyCode\": " + verifyCode + ",\n" +
                                        "  \"username\": \"" + username+ "\",\n" +
                                        "  \"password\": \"" + password + "\"\n" +
                                        "}";


                                MediaType JSON = MediaType.get("application/json; charset=utf-8");
                                RequestBody requestBody = RequestBody.create(JSON, form);

                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url(APIUtils.REGISTER)
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
                                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (msg.equals("验证码过期或不存在")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "验证码过期或不存在", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (msg.equals("验证码错误")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (msg.equals("用户已存在")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        btnemail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                boolean legal = true;
                String emailAddress = email.getText().toString();
                if (emailAddress.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                        }
                    });
                    legal = false;
                } else if (!emailAddress.matches("\"^[^@]+@pku\\.edu\\.cn$|^[^@]+@stu\\.pku\\.edu\\.cn$")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "请使用pku邮箱后缀", Toast.LENGTH_SHORT).show();
                        }
                    });
                    legal = false;
                }
                if (legal){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String form = "{\n" +
                                        "  \"email\": \"" + emailAddress + "\"\n" +
                                        "}";

                                MediaType JSON = MediaType.get("application/json; charset=utf-8");
                                RequestBody requestBody = RequestBody.create(JSON, form);

                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url(APIUtils.SEND_EMAIL)
                                        .post(requestBody)
                                        .build();

                                Response response = client.newCall(request).execute();
                                final String responseData = response.body().string();
                                System.out.println(responseData);

                                // 解析 JSON 响应
                                JSONObject jsonResponse = new JSONObject(responseData);

                                // 校验 msg 字段
                                String msg = jsonResponse.getString("msg");
                                if (msg.equals("ok")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "验证码已发送， 请查收", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (msg.equals("邮箱已注册")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "邮箱已注册", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "邮件发送失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }  catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });
    }
}
