package in.rahul.chatappfirebase.model;

/**
 * Created by Rahul on 13-08-2018.
 */

public class FriendInvite {
    private String Id;
    private String Image;
    private String Name;
    private String PhoneNumber;

    public FriendInvite() {
    }

    public FriendInvite(String id, String image, String name, String phoneNumber) {
        Id = id;
        Image = image;
        Name = name;
        PhoneNumber = phoneNumber;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
