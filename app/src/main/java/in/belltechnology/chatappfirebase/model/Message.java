package in.belltechnology.chatappfirebase.model;

/**
 * Created by Rahul on 09-08-2018.
 */

public class Message {
    private String message;
    private String user;

    public Message() {
    }

    public Message(String message, String user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
