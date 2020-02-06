package in.rahul.chatappfirebase.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import in.rahul.chatappfirebase.MainActivity;
import in.rahul.chatappfirebase.R;

public class LoginActivity extends AppCompatActivity {
//    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;
    ImageView showPassword, hidePassword;
    boolean onBackPressTwice = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

//        registerUser = findViewById(R.id.register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        showPassword = findViewById(R.id.showPassword);
        hidePassword = findViewById(R.id.hidePassword);
        hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Hide Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                hidePassword.setVisibility(View.GONE);
                showPassword.setVisibility(View.VISIBLE);
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                showPassword.setVisibility(View.GONE);
                hidePassword.setVisibility(View.VISIBLE);
            }
        });


        /*if (new SharedPreferencesUtils(LoginActivity.this).getLoginFlag()){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
           // new SharedPreferencesUtils(LoginActivity.this).setLoginFlag(false);
        }*/
     /*   registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent p =  new Intent(LoginActivity.this, RegisterActivity.class);
                p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(p);
            }
        });*/

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    username.setError("Thies field is required");
                } else if (pass.equals("")) {
                    password.setError("This field is required");
                } else {
                    login();
                }

            }
        });
    }

    protected void login() {
        // String url = "https://fir-chat-4efae.firebaseio.com/users.json";
        String url = "https://chattapp-8f889.firebaseio.com/users.json";
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("null")) {
                    Toast.makeText(LoginActivity.this, "UserModel not found", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(user)) {
                            Toast.makeText(LoginActivity.this, "UserModel not found", Toast.LENGTH_LONG).show();
                        } else if (obj.getJSONObject(user).getString("password").equals(pass)) {
                            UserDetails.username = user;
                            UserDetails.password = pass;
                            // startActivity(new Intent(LoginActivity.this, UsersActivity.class));

                            Intent p = new Intent(LoginActivity.this, MainActivity.class);
                            p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(p);
                        } else {
                            Toast.makeText(LoginActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                progressDialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
        rQueue.add(request);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!UserDetails.username.equals("")) {
            Intent p = new Intent(LoginActivity.this, MainActivity.class);
            p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(p);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if(onBackPressTwice){
            super.onBackPressed();
            finish();
        } else {
            onBackPressTwice = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressTwice = false;
                }
            }, 2000);
        }
//        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
//        alert.setTitle("Exit").setMessage("Do you want to Exit From App").setPositiveButton("yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                LoginActivity.super.onBackPressed();
//                finish();
//            }
//        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).create().show();
    }
}
