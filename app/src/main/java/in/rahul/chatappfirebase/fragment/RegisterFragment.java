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

import java.util.HashMap;
import java.util.Map;

import in.rahul.chatappfirebase.R;
import in.rahul.chatappfirebase.activity.LoginActivity;
import in.rahul.chatappfirebase.activity.LoginRegActivity;

import static in.rahul.chatappfirebase.utility.ApiList.profileUrl;
import static in.rahul.chatappfirebase.utility.ApiList.statusUrl;
import static in.rahul.chatappfirebase.utility.ApiList.usersDataUrl;
import static in.rahul.chatappfirebase.utility.ApiList.usersUrl;

public class RegisterFragment extends Fragment {

    EditText etUserName, etPassword, etMobileNo, etName;
    Button registerButton;
    String stUserName, stPassword, stMobileNumber, stName;
//    TextView login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etUserName = view.findViewById(R.id.username);
        etPassword = view.findViewById(R.id.password);
        etMobileNo = view.findViewById(R.id.et_mobile_no);
        etName = view.findViewById(R.id.et_name);
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
                stUserName = etUserName.getText().toString();
                stPassword = etPassword.getText().toString();
                stName = etName.getText().toString();
                stMobileNumber = etMobileNo.getText().toString();

                if (stUserName.equals("")) {
                    etUserName.setError("This field is required");
                    etUserName.requestFocus();
                } else if (stName.equals("")) {
                    etName.setError("This fileld is required");
                    etName.requestFocus();
                } else if (stMobileNumber.equals("")) {
                    etMobileNo.setError("This field is required");
                    etMobileNo.requestFocus();
                } else if (stPassword.equals("")) {
                    etPassword.setError("This fileld is required");
                    etPassword.requestFocus();
                } else if (!stUserName.matches("[A-Za-z0-9]+")) {
                    etUserName.setError("Only letters and numbers");
                    etUserName.requestFocus();
                } else if (stUserName.length() < 4) {
                    etUserName.setError("Please use at least 4 characters");
                    etUserName.requestFocus();
                } else if (stPassword.length() < 4) {
                    etPassword.setError("Please use at least 4 characters");
                    etPassword.requestFocus();
                } else if (stMobileNumber.length() != 10 && !stMobileNumber.matches("[0-9]")) {
                    etMobileNo.setError("Invalid Mobile Number");
                    etMobileNo.requestFocus();
                } else {
                    registration();
                }
            }
        });

        return view;
    }

    protected void registration() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        // String url = "https://fir-chat-4efae.firebaseio.com/users.json";  old url
//        String url = "https://chattapp-8f889.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, usersDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String str) {
                Firebase reference = new Firebase(usersUrl);
                Firebase referenceProfile = new Firebase(profileUrl);


                if (str.equals("null")) {
                    reference.child(stUserName).child("password").setValue(stPassword);
                    Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_LONG).show();
                    Firebase status = new Firebase(statusUrl);
                    status.child(stUserName).child("status").setValue("offline");
                } else {
                    try {
                        JSONObject object = new JSONObject(str);

                        if (!object.has(stUserName)) {
                            Map<String, String> map = new HashMap<>();
                            Map<String, String> mapProfile = new HashMap<>();
                            map.put("password", stPassword);
                            map.put("name", stName);
                            map.put("phoneNumber", stMobileNumber);

                            mapProfile.put("name", stName);
                            mapProfile.put("phoneNumber", stMobileNumber);
                            mapProfile.put("id", stUserName);
                            reference.child(stUserName).setValue(map);
                            referenceProfile.child(stUserName).setValue(mapProfile);
//                             friend.child(UserDetails.friend).child(UserDetails.username).setValue(map);
//                            reference.child(stUserName).child("password").setValue(stPassword);
//                            reference.child(stUserName).child("name").setValue(stName);
//                            reference.child(stUserName).child("phoneNumber").setValue(stMobileNumber);
                            Toast.makeText(getActivity(), "Registration successful!", Toast.LENGTH_LONG).show();
                            Firebase status = new Firebase(statusUrl);
                            status.child(stUserName).child("status").setValue("offline");
                            startActivity(new Intent(getActivity(), LoginRegActivity.class));
                        } else {
                            Toast.makeText(getActivity(), "Username already exists", Toast.LENGTH_LONG).show();
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
