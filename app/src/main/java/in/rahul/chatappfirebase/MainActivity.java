package in.rahul.chatappfirebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.rahul.chatappfirebase.activity.LoginActivity;
import in.rahul.chatappfirebase.activity.UserDetails;
import in.rahul.chatappfirebase.adapter.ViewPagerAdapter;
import in.rahul.chatappfirebase.utility.SharedPreferencesUtils;


public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chat App");
        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //  finish ();
//
//                Intent p = new Intent(MainActivity.this, LoginActivity.class);
//                p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(p);
//            }
//        });

        viewPager = findViewById(R.id.viewPager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
       /* BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if (savedInstanceState == null){
            getFragmentManager().beginTransaction().add(R.id.frame, new HomeFragment()).commit();getFragmentManager().beginTransaction().add(R.id.frame, new HomeFragment()).commit();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this, "You have Signed out.", Toast.LENGTH_SHORT).show();
                    new SharedPreferencesUtils(MainActivity.this).setLoginFlag(false);
//                   user offline in Database
                    DatabaseReference drOffline = FirebaseDatabase.getInstance().getReference("status");
                    drOffline.child(UserDetails.username).child("status").setValue("offline");
//                    removing username
                    UserDetails.username = "";
                    // Close activity
                    finish();
                }
            });
        }
        return true;
    }
    /*private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.home: getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, new HomeFragment()).commit();
                return true;

                case R.id.friendRequest: getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, new FriendReqFragment()).commit();
                    return true;

                case R.id.invitation: getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, new InvitationFragment()).commit();
                    return true;
            }
            return false;
        }
    };*/

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(MainActivity.this, LoginActivity.class));
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Exit").setMessage("Do you want to Exit From App").setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // MainActivity.super.onBackPressed();
                finish();
            }
        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Chat App");
//            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (UserDetails.username.equals("")) {
            Intent p = new Intent(MainActivity.this, LoginActivity.class);
            p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(p);
        }

    }
}
