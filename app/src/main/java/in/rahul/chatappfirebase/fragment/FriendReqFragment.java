package in.rahul.chatappfirebase.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.rahul.chatappfirebase.utility.CircleImageView;
import in.rahul.chatappfirebase.R;
import in.rahul.chatappfirebase.activity.UserDetails;
import in.rahul.chatappfirebase.model.FriendInviteModel;


public class FriendReqFragment extends Fragment {
    //  ListView usersList;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private FirebaseRecyclerAdapter<FriendInviteModel, UserHolder> adapter;
    TextView noUsers;
    String userName = "";
    ArrayList<String> list = new ArrayList<>();
    int totalUsers = 0;
    public String RequestStatus = "0";
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_req, container, false);

        // usersList = view.findViewById(R.id.usersList);
        noUsers = view.findViewById(R.id.noUsersText);
        recyclerView = view.findViewById(R.id.recycleView);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.show();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadFriendData();

        return view;
    }

    public void loadFriendData() {
        reference = FirebaseDatabase.getInstance().getReference("profile");
        reference.keepSynced(true);
        FirebaseRecyclerOptions<FriendInviteModel> options = new FirebaseRecyclerOptions.Builder<FriendInviteModel>().setQuery(reference, FriendInviteModel.class).setLifecycleOwner(getActivity()).build();


        adapter = new FirebaseRecyclerAdapter<FriendInviteModel, UserHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final UserHolder holder, int position, @NonNull final FriendInviteModel model) {

                UserDetails.friend = model.getId();

                holder.setimage(model.getImage());
                holder.setname(model.getName());
                holder.setphoneNumber(model.getPhoneNumber());


                holder.setRequestButton("Send Request");
                holder.setCancelButton("Cancel Request");


                final DatabaseReference alreadyFriend = FirebaseDatabase.getInstance().getReference("friend").child(UserDetails.username).child(UserDetails.friend);
                alreadyFriend.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("id")) {
                            holder.setRequestButton("Friend");
                            holder.requestBtn.setClickable(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                final DatabaseReference requestStatusCheck = FirebaseDatabase.getInstance().getReference("friendReqGot").child(UserDetails.friend).child(UserDetails.username);
                requestStatusCheck.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("reqStatus")) {
                            RequestStatus = dataSnapshot.child("reqStatus").getValue
                                    ().toString();
                            if (RequestStatus.equals("1")) {
                                holder.cancelBtn.setVisibility(View.VISIBLE);
                                holder.requestBtn.setVisibility(View.GONE);
                            } else {
                                if (model.getId().equals(UserDetails.username)) {
                                    holder.requestBtn.setVisibility(View.GONE);
                                    holder.cancelBtn.setVisibility(View.GONE);
                                } else {
                                    holder.cancelBtn.setVisibility(View.GONE);
                                    holder.requestBtn.setVisibility(View.VISIBLE);
                                }


                            }
                        } else {
                            if (model.getId().equals(UserDetails.username)) {
                                holder.requestBtn.setVisibility(View.GONE);
                                holder.cancelBtn.setVisibility(View.GONE);
                            } else {
                                holder.cancelBtn.setVisibility(View.GONE);
                                holder.requestBtn.setVisibility(View.VISIBLE);
                            }

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.requestBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (model.getId().equals(UserDetails.username)) {
                            Toast.makeText(getActivity(), "You Can't Send Request to yourself", Toast.LENGTH_SHORT).show();

                        } else {
                            UserDetails.friend = model.getId();
                            holder.cancelBtn.setVisibility(View.VISIBLE);
                            holder.requestBtn.setVisibility(View.GONE);
                            final DatabaseReference friend = FirebaseDatabase.getInstance().getReference("friendReqGot");
                                   /*Map<String, String> mapStatus = new HashMap<>();
                                   mapStatus.put("reqStatus", "1");
                                   friend.child(UserDetails.username).setValue(mapStatus);*/

                            DatabaseReference userData = FirebaseDatabase.getInstance().getReference("profile").child(UserDetails.username);
                            userData.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String name = "", phone = "", image = "", id = "";
                                    name = dataSnapshot.child("name").getValue
                                            ().toString();
                                    phone = dataSnapshot.child("phoneNumber").getValue().toString();
                                    image = dataSnapshot.child("image").getValue().toString();
                                    id = dataSnapshot.child("id").getValue().toString();

                                    // phone= dataSnapshot.child("name").getValue().toString();
                                    /* Toast.makeText(getActivity(), "Profile name: " + name + " waytwo ", Toast.LENGTH_LONG).show();*/
                                    Map<String, String> map = new HashMap<>();
                                    map.put("id", id);
                                    map.put("image", image);
                                    map.put("name", name);
                                    map.put("phoneNumber", phone);
                                    map.put("reqStatus", "1");
                                    friend.child(UserDetails.friend).child(UserDetails.username).setValue(map);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
                holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.requestBtn.setVisibility(View.VISIBLE);
                        holder.cancelBtn.setVisibility(View.GONE);
                        UserDetails.friend = model.getId();
                        final DatabaseReference friend = FirebaseDatabase.getInstance().getReference("friendReqGot");
                              /* Map<String, String> mapStatus = new HashMap<>();
                               mapStatus.put("reqStatus", "0");
                               friend.child(UserDetails.friend).child(UserDetails.username).setValue(mapStatus);*/
                        friend.child(UserDetails.friend).child(UserDetails.username)
                                .removeValue();


                    }
                });

                      /* holder.view.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                           }
                       });*/
            }

            @NonNull
            @Override
            public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invitationl_request, parent, false);
                return new UserHolder(view);
            }

            ;

        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        progressDialog.dismiss();
        //  recyclerView.setAdapter(adapter);
    }

    public static class UserHolder extends RecyclerView.ViewHolder {
        View view;
        TextView userName, userPhoneNumber;
        Button requestBtn, cancelBtn;


        public UserHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setname(String name) {
            userName = view.findViewById(R.id.name);
            userName.setText(name);
        }

        public void setphoneNumber(String phoneNumber) {
            userPhoneNumber = view.findViewById(R.id.phoneNumber);
            userPhoneNumber.setText(phoneNumber);
        }

        public void setimage(String profileImage) {
            CircleImageView userImage = view.findViewById(R.id.profileImage);
            Picasso.get().load(profileImage).placeholder(R.drawable.profile_26).into(userImage);
        }

        public void setRequestButton(String btnRequest) {
            requestBtn = view.findViewById(R.id.btn_sendReq);
            requestBtn.setText(btnRequest);
        }

        public void setCancelButton(String btnCancel) {
            cancelBtn = view.findViewById(R.id.btn_cancel);
            cancelBtn.setText(btnCancel);
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
