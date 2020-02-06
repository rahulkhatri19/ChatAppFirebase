package in.rahul.chatappfirebase.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import in.rahul.chatappfirebase.R;
import in.rahul.chatappfirebase.activity.LoginActivity;
import in.rahul.chatappfirebase.activity.LoginRegActivity;

public class RegisterFragment extends Fragment {

    EditText username, password;
    Button registerButton;
    String user, pass;
//    TextView login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        registerButton = view.findViewById(R.id.registerButton);
//        login = view.findViewById(R.id.login);

        Firebase.setAndroidContext(getActivity());

//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//            }
//        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("This field is required");
                }
                else if(pass.equals("")){
                    password.setError("This fileld is required");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("Only letters and numbers");
                }
                else if(user.length()<4){
                    username.setError("Please use at least 4 characters");
                }
                else if(pass.length()<4){
                    password.setError("Please use at least 4 characters");
                }
                else {
                    registration();
                }
            }
        });

        return view;
    }

    protected void registration(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        // String url = "https://fir-chat-4efae.firebaseio.com/users.json";
        String url = "https://chattapp-8f889.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String str) {
                Firebase reference = new Firebase("https://chattapp-8f889.firebaseio.com/users");


                if(str.equals("null")) {
                    reference.child(user).child("password").setValue(pass);
                    Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_LONG).show();
                    Firebase status = new Firebase("https://chattapp-8f889.firebaseio.com/status");
                    status.child(user).child("status").setValue("offline");
                }
                else {
                    try {
                        JSONObject object = new JSONObject(str);

                        if (!object.has(user)) {
                            reference.child(user).child("password").setValue(pass);
                            Toast.makeText(getActivity(), "Registration successful!", Toast.LENGTH_LONG).show();
                            Firebase status = new Firebase("https://chattapp-8f889.firebaseio.com/status");
                            status.child(user).child("status").setValue("offline");
                        } else {
                            Toast.makeText(getActivity(), "Username already exists", Toast.LENGTH_LONG).show();
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
                System.out.println("" + volleyError );
                progressDialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

        startActivity(new Intent(getActivity(), LoginRegActivity.class));
    }
}
