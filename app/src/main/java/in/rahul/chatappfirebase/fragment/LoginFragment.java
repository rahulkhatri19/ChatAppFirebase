package in.rahul.chatappfirebase.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import in.rahul.chatappfirebase.activity.UserDetails;

public class LoginFragment extends Fragment {

//    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;
    ImageView showPassword, hidePassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);

//        registerUser = view.findViewById(R.id.register);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.loginButton);
        showPassword = view.findViewById(R.id.showPassword);
        hidePassword = view.findViewById(R.id.hidePassword);
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
        /*registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent p =  new Intent(getActivity(), RegisterActivity.class);
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

        return view;
    }
    protected void login() {
        // String url = "https://fir-chat-4efae.firebaseio.com/users.json";
        String url = "https://chattapp-8f889.firebaseio.com/users.json";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("null")) {
                    Toast.makeText(getActivity(), "UserModel not found", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(user)) {
                            Toast.makeText(getActivity(), "UserModel not found", Toast.LENGTH_LONG).show();
                        } else if (obj.getJSONObject(user).getString("password").equals(pass)) {
                            UserDetails.username = user;
                            UserDetails.password = pass;
                            // startActivity(new Intent(LoginActivity.this, UsersActivity.class));

                            Intent p = new Intent(getActivity(), MainActivity.class);
                            p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(p);
                        } else {
                            Toast.makeText(getActivity(), "incorrect password", Toast.LENGTH_LONG).show();
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

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);
    }
}
