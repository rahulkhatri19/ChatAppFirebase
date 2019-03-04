package in.belltechnology.chatappfirebase.model;

/**
 * Created by Rahul on 13-08-2018.
 */

public class FriendList {
    private String image;
    private String name;
    private String phoneNumber;
    private String status;

    public FriendList() {
    }

    public FriendList(String image, String name, String phoneNumber, String status) {
        this.image = image;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
