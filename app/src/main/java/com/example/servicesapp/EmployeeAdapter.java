package com.example.servicesapp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MVH> {

    private Activity activity;
    private ArrayList<User> users;

    public EmployeeAdapter(Activity activity, ArrayList<User> users) {
        this.activity = activity;
        this.users = users;
    }

    @NonNull
    @Override
    public EmployeeAdapter.MVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                activity.getLayoutInflater().inflate(R.layout.employee_list_item, parent, false);
        return new MVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.MVH holder, int position) {

        Picasso
                .get()
                .load(users.get(position).getPicture())
                .placeholder(R.drawable.profile)
                .into(holder.iv_person);

        holder.iv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (users.get(position).getLat() == 0)
                    Toast.makeText(activity, "Employee didn't provide a location", Toast.LENGTH_SHORT).show();
                else {
                    Uri gmmIntentUri =
                            Uri.parse("geo:"
                                    + users.get(position).getLat()
                                    + "," + users.get(position).getLon());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    activity.startActivity(mapIntent);
                }
            }
        });

        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL
                        , Uri.parse("tel:" + users.get(position).getPhone()));
                activity.startActivity(phoneIntent);
            }
        });

        holder.tv_name.setText(users.get(position).getName());
        holder.tv_price_value.setText(users.get(position).getHourlyRate() + "$");
        holder.about_value.setText(users.get(position).getAbout());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MVH extends RecyclerView.ViewHolder {

        private ImageView iv_person;
        private ImageView iv_map;
        private ImageView iv_call;
        private TextView tv_name;
        private TextView tv_price_value;
        private TextView about_value;

        public MVH(@NonNull View itemView) {
            super(itemView);
            iv_person = itemView.findViewById(R.id.iv_person);
            iv_map = itemView.findViewById(R.id.iv_map);
            iv_call = itemView.findViewById(R.id.iv_call);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price_value = itemView.findViewById(R.id.tv_price_value);
            about_value = itemView.findViewById(R.id.about_value);

        }
    }
}
