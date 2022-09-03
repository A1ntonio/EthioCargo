package com.puulapp.ethio_cargo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.childUi.ViewMore;
import com.puulapp.ethio_cargo.spacecraft.NotSpacecraft;
import com.puulapp.ethio_cargo.spacecraft.TrackSpacecraft;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotListAdapter extends RecyclerView.Adapter<NotListAdapter.ViewHolder> {

    private List<NotSpacecraft> list;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currentUserId = mAuth.getCurrentUser().getUid();

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public NotListAdapter(List<NotSpacecraft> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_notification_lists, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        NotSpacecraft space = list.get(position);

        holder._message.setText(space.getNotification());

        holder.past = Long.parseLong(space.getNot_time());

        Calendar cal = Calendar.getInstance();
        holder.nowTime = cal.getTime();

        holder.dateDiff = holder.nowTime.getTime() - holder.past;
        holder.sec = TimeUnit.MILLISECONDS.toSeconds(holder.dateDiff);
        holder.min = TimeUnit.MILLISECONDS.toMinutes(holder.dateDiff);
        holder.hour = TimeUnit.MILLISECONDS.toHours(holder.dateDiff);
        holder.day = TimeUnit.MILLISECONDS.toDays(holder.dateDiff);

        holder.ago = getTimeAgo(holder.sec, holder.min, holder.hour, holder.day);

        holder.not_time.setText(holder.ago);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView _message, not_time;
        long past;
        long sec, min, hour, day;
        String ago;
        long dateDiff;
        Date nowTime;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            _message = itemView.findViewById(R.id.notification_text);
            not_time = itemView.findViewById(R.id.not_time);

        }
    }

    private String getTimeAgo(long sec, long min, long hour, long day) {
        String ago = null;
        if (sec < 60){
            ago =  "just now";
        } else if (min < 60){
            ago =  min + " minutes ago";
        } else if (hour < 24){
            ago =  hour + " hours ago";
        } else if (day >= 7){
            if (day > 360) {
                ago =  (day / 360) + " years ago";
            } else if (day > 30) {
                ago =  (day / 30) + " months ago";
            } else {
                ago =  (day / 7) + " weeks ago";
            }
        } else if (day < 7) {
            ago =  day + " days ago";
        }
        return ago;
    }
}
