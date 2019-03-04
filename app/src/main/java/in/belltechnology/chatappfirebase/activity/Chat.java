package in.belltechnology.chatappfirebase.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.belltechnology.chatappfirebase.R;
import in.belltechnology.chatappfirebase.model.StatusFriend;

public class Chat extends AppCompatActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    NestedScrollView scrollView;
   //String message=" ", user=" ", time=" ", status="offline";

    Firebase userToFriend, friendToUser, statusRecord, statusFetchRecord;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = findViewById(R.id.layout_vertical);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);

        // firebase references
        Firebase.setAndroidContext(this);

        userToFriend = new Firebase("https://chattapp-8f889.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.friend);
        String friendUrl= "https://chattapp-8f889.firebaseio.com/messages/"+ UserDetails.friend + "_" + UserDetails.username;
        friendToUser = new Firebase(friendUrl);

     /*  statusFetchRecord= new Firebase("https://chattapp-8f889.firebaseio.com/messages/"+ UserDetails.username + "_" + UserDetails.friend).child(UserDetails.friend);
       // +"/"+UserDetails.friend
        Log.e("","statusFetchRecord"+statusFetchRecord);*/

       /* HashMap<String, String> map2= new HashMap<>();
        map2.put("status","online");
        friendToUser.child(UserDetails.username).setValue(map2);

        statusRecord= new Firebase(friendUrl);
        HashMap<String , String> map3= new HashMap<>();
        map3.put("status","offline");
        statusRecord.child(UserDetails.username).onDisconnect().setValue(map3);*/

      // String refranceUrl="messages/"+UserDetails.username+"_"+UserDetails.friend;


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageArea.getText().toString();
              //  messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                //String timeStamp= String.valueOf(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis());

               // Right Code
                 Date currentTime = Calendar.getInstance().getTime();
                DateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
                String newDate= dateFormat.format(currentTime);

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<>();
                    map.put("Message", messageText);
                    map.put("User", UserDetails.username);
                    map.put("time", String.valueOf(newDate));
                    userToFriend.push().setValue(map);
                    friendToUser.push().setValue(map);
                    messageArea.setText("");
                }else {
                    Toast.makeText(Chat.this, "Please Enter Message\nCan't leave blank", Toast
                            .LENGTH_SHORT)
                            .show();
                }
            }
        });



        // event listener

       /* statusFetchRecord.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String statusUser = map.get("status").toString();
                StatusFriend(statusUser,3);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
               *//* Map map = dataSnapshot.getValue(Map.class);
                String statusUser = map.get("status").toString();
                StatusFriend(statusUser,3);*//*
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/


        userToFriend.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String  message = map.get("Message").toString();
                String  user = map.get("User").toString();
                String  newDate = map.get("time").toString();
                String time="";

                Date currentTime = Calendar.getInstance().getTime();
                DateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
               // DateFormat dateFormat2= new SimpleDateFormat("dd/MM/yyyy");

                String newDateFormat= dateFormat.format(currentTime);
               // String newDateFormat2= dateFormat2.format(map.get("time"));
                Log.e("","newDateFormat "+newDateFormat);
               // Log.e("","newDateFormat2 "+newDateFormat2);
                System.out.println("newDateFormat "+newDateFormat);
              //  System.out.println("newDateFormat2 "+newDateFormat2);
                if (map.get("time").equals(newDateFormat)){
                     time = "Today";
                    Log.e("","timeif "+time);
                } else {
                  //  DateFormat dateFormat3= new SimpleDateFormat("dd/MM/yyyy");
                   // time= map.get("time").toString();
                    time= map.get("time").toString();
                    Log.e("","timeBelow "+time);
                }
               /* String statusUser = map.get("status").toString();
                StatusFriend(statusUser,3);*/


                if(user.equals(UserDetails.username)){
                  //  addNameBox(UserDetails.username, 1);
                    addNameBox(time, 1);
                    addMessageBox(message, 1);



                }
                else{
                    addNameBox(time, 2);
                   // addNameBox(UserDetails.friend, 2);
                    addMessageBox( message, 2);

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

        /*DatabaseReference reference= FirebaseDatabase.getInstance().getReference("status").child(UserDetails.friend);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                StatusFriend statusFriend= dataSnapshot.getValue(StatusFriend.class);
               String status=statusFriend.getStatus();
                Status(status,3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }
    public void addMessageBox(String message, int type){

        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;


        if(type == 1) {

            layoutParams.gravity = Gravity.START;
            textView.setBackgroundResource(R.drawable.bubble_out);
            textView.setTextColor(getResources().getColor(R.color.chatBlack));
            textView.setPadding(20,20,20,20);
        }
        else{
            layoutParams.gravity = Gravity.END;
            textView.setBackgroundResource(R.drawable.bubble_in);
            textView.setTextColor(getResources().getColor(R.color.chatBlack));
            textView.setPadding(20,20,20,20);
        }
        textView.setLayoutParams(layoutParams);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }


    // who send Message
    public void addNameBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setTextSize(12);
        textView.setText(message);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;

        if(type == 1) {
            layoutParams.gravity = Gravity.START;
            textView.setPadding(20,20,20,20);
            textView.setTextColor(getResources().getColor(R.color.chatWhite));
        }
        else{
            layoutParams.gravity = Gravity.END;
            textView.setPadding(20,20,20,20);
            textView.setTextColor(getResources().getColor(R.color.chatWhite));
        }
        textView.setLayoutParams(layoutParams);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }

    public void Status(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setTextSize(15);
        textView.setText(message);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;

        if (type == 3){
            layoutParams.gravity = Gravity.CENTER|Gravity.TOP;
            textView.setPadding(20,20,20,20);
            if (message.equals("offline")){
                textView.setTextColor(Color.RED);

            }  else if (message.equals("online")){
                textView.setTextColor(Color.GREEN);
            } else {
                textView.setTextColor(getResources().getColor(R.color.chatWhite));
            }
            //  textView.setTextColor(getResources().getColor(R.color.chatWhite));
        }
       /* if(type == 1) {
            layoutParams.gravity = Gravity.START;
            textView.setPadding(20,20,20,20);
            textView.setTextColor(getResources().getColor(R.color.chatWhite));
        }
        else{
            layoutParams.gravity = Gravity.END;
            textView.setPadding(20,20,20,20);
            textView.setTextColor(getResources().getColor(R.color.chatWhite));
        }*/
        textView.setLayoutParams(layoutParams);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }

}
