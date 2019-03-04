package in.belltechnology.chatappfirebase.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import in.belltechnology.chatappfirebase.CircularImage.CircleImageView;
import in.belltechnology.chatappfirebase.Interface.ItemClickListener;
import in.belltechnology.chatappfirebase.R;

/**
 * Created by Rahul on 13-08-2018.
 */

public class FriendHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener itemClickListener;
    public TextView name, phoneNumber;
    public CircleImageView profileImage;

    public FriendHolder(View view){
        super(view);
        name = view.findViewById(R.id.name);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        profileImage = view.findViewById(R.id.profileImage);

        view.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener= itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(), false);
    }
}
