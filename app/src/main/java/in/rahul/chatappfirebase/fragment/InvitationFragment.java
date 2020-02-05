package in.rahul.chatappfirebase.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import in.rahul.chatappfirebase.activity.ChatActivity;
import in.rahul.chatappfirebase.activity.UserDetails;
import in.rahul.chatappfirebase.model.FriendInviteModel;

public class InvitationFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private FirebaseRecyclerAdapter<FriendInviteModel, UserInvitationHolder> adapter;
    ListView usersList;
    TextView noUsers;
    ArrayList<String> list = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation, container, false);
        noUsers= view.findViewById(R.id.noUsersText);
        recyclerView= view.findViewById(R.id.recycleView);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadData();

        return view;
    }

    private void loadData() {
        reference=FirebaseDatabase.getInstance().getReference("friendReqGot").child(UserDetails.username);
        reference.keepSynced(true);

        FirebaseRecyclerOptions<FriendInviteModel> options= new FirebaseRecyclerOptions.Builder<FriendInviteModel>().setQuery(reference, FriendInviteModel.class).setLifecycleOwner(getActivity()).build();
        adapter = new FirebaseRecyclerAdapter<FriendInviteModel, UserInvitationHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UserInvitationHolder holder, int position, @NonNull final FriendInviteModel model) {

                holder.setImage(model.getImage());
                holder.setName(model.getName());
                holder.setPhoneNumber(model.getPhoneNumber());

                holder.setRequestButton("Accept Request");
                holder.setCancelButton("Reject Request");

                holder.requestBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        UserDetails.friend = model.getId();
                        final DatabaseReference friend= FirebaseDatabase.getInstance().getReference("friend");
                        DatabaseReference userData= FirebaseDatabase.getInstance().getReference("profile").child(UserDetails.username);
                        userData.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String name="", phone="", image="", id="";
                                name= dataSnapshot.child("name").getValue
                                        ().toString();
                                phone= dataSnapshot.child("phoneNumber").getValue().toString();
                                image= dataSnapshot.child("image").getValue().toString();
                                id= dataSnapshot.child("id").getValue().toString();

                                // phone= dataSnapshot.child("name").getValue().toString();
                               /* Toast.makeText(getActivity(), "Profile name: "+name+" waytwo ", Toast.LENGTH_LONG).show();*/
                                Map<String, String> map = new HashMap<>();
                                map.put("id",id);
                                map.put("image",image);
                                map.put("name",name);
                                map.put("phoneNumber",phone);
                                friend.child(UserDetails.friend).child(UserDetails.username).setValue(map);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Map<String, String> map = new HashMap<>();
                        map.put("id",model.getId());
                        map.put("image",model.getImage());
                        map.put("name",model.getName());
                        map.put("phoneNumber",model.getPhoneNumber());
                        friend.child(UserDetails.username).child(UserDetails.friend).setValue(map);

                    //    Toast.makeText(getActivity(), "Yes it works", Toast.LENGTH_SHORT).show();
                        UserDetails.friend = model.getId();
                        startActivity(new Intent(getActivity(), ChatActivity.class));
                        reference.child(UserDetails.friend).removeValue();

                    }
                });
                holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserDetails.friend = model.getId();
                        reference.child(UserDetails.friend).removeValue();
                    }
                });
            }

            @NonNull
            @Override
            public UserInvitationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.invitationl_request, parent, false);
                return new UserInvitationHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        progressDialog.dismiss();
    }

    public static class UserInvitationHolder extends RecyclerView.ViewHolder{
            View view;
            TextView userName, userPhoneNumber;
            Button requestBtn, cancelBtn;
            public UserInvitationHolder(View itemView){
                super(itemView);
                view= itemView;
            }
     public void setName(String name){
     userName= view.findViewById(R.id.name);
     userName.setText(name);
     }
     public void setPhoneNumber(String phoneNumber){
     userPhoneNumber= view.findViewById(R.id.phoneNumber);
     userPhoneNumber.setText(phoneNumber);
     }
     public void setImage(String profileImage){
     CircleImageView userImage= view.findViewById(R.id.profileImage);
     Picasso.get().load(profileImage).placeholder(R.drawable.profile_26).into(userImage);
     }
        public void setRequestButton(String btnRequest){
            requestBtn=view.findViewById(R.id.btn_sendReq);
            requestBtn.setText(btnRequest);
        }
        public void setCancelButton(String btnCancel){
            cancelBtn=view.findViewById(R.id.btn_cancel);
            cancelBtn.setText(btnCancel);
        }
    }

}

