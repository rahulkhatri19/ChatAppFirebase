package in.rahul.chatappfirebase.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rahul on 16-08-2018.
 */

public class SharedPreferencesUtils {
    private Context context;
    SharedPreferences pref;

    public SharedPreferencesUtils(Context context){
        this.context = context;
        try {
            pref = this.context.getSharedPreferences("ChatAppFirebase", Context.MODE_PRIVATE);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setLoginFlag(boolean isLogin ){
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("loginFlag", isLogin);
        editor.commit();
    }
    public boolean getLoginFlag(){
        if (pref.contains("loginFlag"))
            return pref.getBoolean("loginFlag", false);
        else
            return false;
    }
}
