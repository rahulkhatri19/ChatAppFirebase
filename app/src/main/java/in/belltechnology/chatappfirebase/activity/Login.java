package in.belltechnology.chatappfirebase.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import in.belltechnology.chatappfirebase.MainActivity;
import in.belltechnology.chatappfirebase.R;
import in.belltechnology.chatappfirebase.utility.SharedPreferencesUtils;

public class Login extends AppCompatActivity {
    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;
    ImageView showPassword, hidePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerUser = findViewById(R.id.register);
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


        /*if (new SharedPreferencesUtils(Login.this).getLoginFlag()){
            startActivity(new Intent(Login.this,MainActivity.class));
           // new SharedPreferencesUtils(Login.this).setLoginFlag(false);
        }*/
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("Thies field is required");
                }
                else if(pass.equals("")){
                    password.setError("This field is required");
                }
                else{
                    login();
                }

            }
        });
    }
    protected void login(){
        // String url = "https://fir-chat-4efae.firebaseio.com/users.json";
        String url = "https://chattapp-8f889.firebaseio.com/users.json";
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(s.equals("null")){
                    Toast.makeText(Login.this, "User not found", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        JSONObject obj = new JSONObject(s);

                        if(!obj.has(user)){
                            Toast.makeText(Login.this, "User not found", Toast.LENGTH_LONG).show();
                        }
                        else if(obj.getJSONObject(user).getString("password").equals(pass)){
                            UserDetails.username = user;
                            UserDetails.password = pass;
                            // startActivity(new Intent(Login.this, Users.class));

                            Intent p=new Intent(Login.this,MainActivity.class);                    p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(p);
                        }
                        else {
                            Toast.makeText(Login.this, "incorrect password", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                progressDialog.dismiss();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                progressDialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Login.this);
        rQueue.add(request);
    }
}
