package in.belltechnology.chatappfirebase.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.firebase.ui.auth.data.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import in.belltechnology.chatappfirebase.R;

public class FriendRequestActivity extends AppCompatActivity {
    ListView usersList;
    TextView noUsers;
    ArrayList<String> list = new ArrayList<>();
    int totalUsers = 0;
    int status=0;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        usersList = findViewById(R.id.requestList);
        noUsers = findViewById(R.id.tv_noUser);

        progressDialog = new ProgressDialog(FriendRequestActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        Firebase.setAndroidContext(this);

        String url = "https://chattapp-8f889.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String str) {
                doOnSuccess(str);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(FriendRequestActivity.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        UserDetails.friend = list.get(position);
             Firebase userFriendRequest = new Firebase("https://chattapp-8f889.firebaseio.com/friendReqSend/"+ UserDetails.username );

             Firebase userFriendReceived = new Firebase("https://chattapp-8f889.firebaseio.com/friendReqGot/"+ UserDetails.friend );
                Map<String, String> map = new HashMap<>();
                map.put("status", "0");
                userFriendRequest.child(UserDetails.friend).setValue(map);

                Map<String, String> map2 = new HashMap<>();
                map2.put("status", "0");
                userFriendReceived.child(UserDetails.username).setValue(map2);
                /*userToFriend.push().setValue(map);
                friendToUser.push().setValue(map);
                messageArea.setText("");*/  Toast.makeText(FriendRequestActivity.this, "Friend request Send", Toast.LENGTH_SHORT).show();


               /* if(status == 1){
                    startActivity(new Intent(FriendRequestActivity.this, Chat.class));
                } else {
                    Toast.makeText(FriendRequestActivity.this, "Wait For Friend request To be Accepted", Toast.LENGTH_SHORT).show();
                }*/

            }
        });
    }
    public void doOnSuccess(String str){
        try {
            JSONObject obj = new JSONObject(str);

            Iterator i = obj.keys();
            String key;

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(UserDetails.username)) {
                    list.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsers.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsers.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        }

        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FriendRequestActivity.this,Users.class));
    }
}
