package in.gitsquare.demo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.gitsquare.demo.Model.User;
import in.gitsquare.demo.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<User> userList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text_user_name, txt_user_repo, txt_contri_count;
        CircleImageView img_user;

        public MyViewHolder(View view) {
            super(view);
            text_user_name = (TextView) view.findViewById(R.id.text_user_name);
            txt_user_repo = (TextView) view.findViewById(R.id.txt_user_repo);
            txt_contri_count = (TextView) view.findViewById(R.id.txt_contri_count);
            img_user = (CircleImageView) view.findViewById(R.id.img_user);


        }
    }


    public UserAdapter(List<User> userList,Context context) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.text_user_name.setText(user.getUserName());
        holder.txt_user_repo.setText(user.getUserRepo());
        Linkify.addLinks( holder.txt_user_repo, Linkify.WEB_URLS);
        holder.txt_contri_count.setText(user.getUserContribution());
        Glide.with(context).load(user.userImage).into(holder.img_user);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}