package in.rahul.chatappfirebase.model;

/**
 * Created by Rahul on 09-08-2018.
 */

public class UserModel {
    private String userName;
    private String password;

    public UserModel() {
    }

    public UserModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}