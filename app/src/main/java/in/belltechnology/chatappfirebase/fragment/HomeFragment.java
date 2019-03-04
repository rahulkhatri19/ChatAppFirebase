package in.belltechnology.chatappfirebase.fragment;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import in.belltechnology.chatappfirebase.CircularImage.CircleImageView;
import in.belltechnology.chatappfirebase.R;
import in.belltechnology.chatappfirebase.activity.Chat;
import in.belltechnology.chatappfirebase.activity.UserDetails;
import in.belltechnology.chatappfirebase.model.FriendInvite;

public class HomeFragment extends Fragment {

    // ListView usersList;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<FriendInvite, HomeHolder> adapter;
    TextView noUsers;
    String friendStatus="";
    ArrayList<String> list = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

      //  usersList = view.findViewById(R.id.usersList);
        noUsers = view.findViewById(R.id.noUsersText);
        recyclerView=view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        loadData();

        DatabaseReference checkOnline= FirebaseDatabase.getInstance().getReference("status");

        Map<String, String> map = new HashMap<>();
        map.put("status", "online");
        checkOnline.child(UserDetails.username).setValue(map);

        Map<String, String> map2 = new HashMap<>();
        map2.put("status", "offline");
        checkOnline.child(UserDetails.username).onDisconnect().setValue(map2);
       /* Firebase.setAndroidContext(getActivity());

        String url = "https://chattapp-8f889.firebaseio.com/friend/"+UserDetails.username+".json";
        Log.e("","friend: "+url);

        Firebase checkonline= new Firebase("https://chattapp-8f889.firebaseio.com/status");

        Map<String, String> map = new HashMap<>();
        map.put("status", "online");
        checkonline.child(UserDetails.username).setValue(map);

        Map<String, String> map2 = new HashMap<>();
        map2.put("status", "offline");
        checkonline.child(UserDetails.username).onDisconnect().setValue(map2);

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

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.friend = list.get(position);
                startActivity(new Intent(getActivity(), Chat.class));

            }
        });*/

        return view;
    }

    private void loadData() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("friend").child
                (UserDetails.username);
        FirebaseRecyclerOptions<FriendInvite> options= new FirebaseRecyclerOptions.Builder<FriendInvite>().setQuery(reference, FriendInvite.class).setLifecycleOwner(getActivity()).build();

    adapter= new FirebaseRecyclerAdapter<FriendInvite, HomeHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull final HomeHolder holder, int position, @NonNull final FriendInvite model) {
            holder.setImage(model.getImage());
            holder.setName(model.getName());
            holder.setPhoneNumber(model.getPhoneNumber());

            UserDetails.friend = model.getId();
            DatabaseReference status = FirebaseDatabase.getInstance().getReference("status").child(UserDetails.friend);
            status.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    friendStatus= dataSnapshot.child("status").getValue().toString();

                    holder.setStatus(friendStatus);
                            /*if (friendStatus.equals("online")){
                                Toast.makeText(getActivity(), "online works", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "offline works", Toast.LENGTH_SHORT).show();
                            }*/
                    // Toast.makeText(getActivity(), "friendStatus "+friendStatus, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDetails.friend = model.getId();
                    startActivity(new Intent(getActivity(), Chat.class));
                    /*DatabaseReference status = FirebaseDatabase.getInstance().getReference("status").child(UserDetails.friend);
                    status.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String friendStatus="";
                            friendStatus= dataSnapshot.child("status").getValue().toString();

                            holder.setStatus(friendStatus);
                            *//*if (friendStatus.equals("online")){
                                Toast.makeText(getActivity(), "online works", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "offline works", Toast.LENGTH_SHORT).show();
                            }*//*
                           // Toast.makeText(getActivity(), "friendStatus "+friendStatus, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
*/
                }
            });
        }

        @NonNull
        @Override
        public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlayout, parent, false);

            return new HomeHolder(view);
        }
    };
    recyclerView.setAdapter(adapter);
    adapter.startListening();
    progressDialog.dismiss();
    }

    public static class HomeHolder extends RecyclerView.ViewHolder{
        View view;

        public HomeHolder(View itemView){
            super(itemView);
            view= itemView;
        }
        public void setName(String name){
            TextView userName= view.findViewById(R.id.name);
            userName.setText(name);
        }
        public void setPhoneNumber(String phoneNumber){
            TextView userPhoneNumber= view.findViewById(R.id.phoneNumber);
            userPhoneNumber.setText(phoneNumber);
        }
        public void setImage(String profileImage){
            CircleImageView userImage= view.findViewById(R.id.profileImage);
            Picasso.get().load(profileImage).placeholder(R.drawable.profile_26).into(userImage);
        }
        public void setStatus(String status){
            TextView userStatus= view.findViewById(R.id.status);
           // userStatus.setBackground(R.drawable.red_oval);
            if (status.equals("online")){
                userStatus.setBackgroundResource(R.drawable.green_oval);
            }
            else {
                userStatus.setBackgroundResource(R.drawable.red_oval);
            }
        }
    }

   /* public void doOnSuccess(String str){
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
            usersList.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list));
        }

        progressDialog.dismiss();
    }*/

}
