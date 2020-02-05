package in.rahul.chatappfirebase.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import in.rahul.chatappfirebase.R;
import in.rahul.chatappfirebase.model.FriendRequestModel;
import in.rahul.chatappfirebase.adapter.InvitationAdapter;

public class InvitationActivity extends AppCompatActivity {
    ListView usersList;
    TextView noUsers;
    ArrayList<String> list = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        usersList = findViewById(R.id.usersList);
        noUsers = findViewById(R.id.noUsersText);

        progressDialog = new ProgressDialog(InvitationActivity.this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();
        Firebase.setAndroidContext(this);

       // String url = "https://chattapp-8f889.firebaseio.com/friendReqGot/"+UserDetails.username+ ".json";
        String dataUrl=  "https://chattapp-8f889.firebaseio.com/friendReqGot/"+UserDetails.username;
        Log.e("","Got: "+dataUrl);
        reference= FirebaseDatabase.getInstance().getReference(dataUrl);
        FirebaseRecyclerOptions<FriendRequestModel> options= new FirebaseRecyclerOptions.Builder<FriendRequestModel>().setQuery(reference, FriendRequestModel.class).build();

        FirebaseRecyclerAdapter<FriendRequestModel, InvitationAdapter> adapter= new FirebaseRecyclerAdapter<FriendRequestModel, InvitationAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull InvitationAdapter holder, int position, @NonNull FriendRequestModel model) {

            }

            @NonNull
            @Override
            public InvitationAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat, parent, false);

                return new InvitationAdapter(view);
            }
        };

       /* StringRequest request = new StringRequest(FriendRequestModel.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String string) {
                doOnSuccess(string);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(InvitationActivity.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.friend = list.get(position);
              Firebase userFriend = new Firebase("https://chattapp-8f889.firebaseio.com/friend/"+ UserDetails.username);
              Firebase friendUser = new Firebase("https://chattapp-8f889.firebaseio.com/friend/"+ UserDetails.friend);
              Log.e("","userFriend "+userFriend);
              Log.e("","friendUser "+friendUser);
                Map<String, String> map = new HashMap<>();
                map.put("status", "1");
                userFriend.child(UserDetails.friend).setValue(map);

                Map<String, String> map2 = new HashMap<>();
                map2.put("status", "1");
                friendUser.child(UserDetails.username).setValue(map2);

                startActivity(new Intent(InvitationActivity.this, ChatActivity.class));
            }
        });*/
    }
 /*   public void doOnSuccess(String str){
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

        if(totalUsers <=0){
            noUsers.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsers.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        }

        progressDialog.dismiss();
    }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(InvitationActivity.this, UsersActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
     //  adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }
}
