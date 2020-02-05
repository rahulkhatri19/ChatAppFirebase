package in.rahul.chatappfirebase.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import in.rahul.chatappfirebase.MainActivity;
import in.rahul.chatappfirebase.R;
import in.rahul.chatappfirebase.fragment.LoginFragment;
import in.rahul.chatappfirebase.fragment.RegisterFragment;

public class LoginRegActivity extends AppCompatActivity {

    TextView tvLogin, tvReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);

        tvLogin = findViewById(R.id.tv_login);
        tvReg = findViewById(R.id.tv_reg);

        if (savedInstanceState == null){
//            getFragmentManager().beginTransaction().add(R.id.frame, new HomeFragment()).commit();
            getFragmentManager().beginTransaction().add(R.id.frame_layout, new LoginFragment()).commit();
        }

        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvLogin.setVisibility(View.VISIBLE);
                tvReg.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, new RegisterFragment()).addToBackStack(null).commit();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvLogin.setVisibility(View.GONE);
                tvReg.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, new LoginFragment()).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Exit").setMessage("Do you want to Exit From App").setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!UserDetails.username.equals("")) {
            Intent p = new Intent(LoginRegActivity.this, MainActivity.class);
            p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(p);
        }
    }
}
