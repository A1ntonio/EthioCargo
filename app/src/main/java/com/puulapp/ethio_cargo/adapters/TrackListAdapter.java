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
import com.puulapp.ethio_cargo.spacecraft.TrackSpacecraft;
import com.puulapp.ethio_cargo.childUi.ViewMore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {

    private List<TrackSpacecraft> list;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currentUserId = mAuth.getCurrentUser().getUid();

    public TrackListAdapter(List<TrackSpacecraft> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_track_lists, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        TrackSpacecraft space = list.get(position);

        holder._origin_m.setText(space.get_initial());
        holder._dest_m.setText(space.get_final());
        holder._status_m.setText(space.get_status());
        holder._date_m.setText(space.get_date());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView _origin_m, _dest_m, _status_m, _date_m, _view_more;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            _origin_m = itemView.findViewById(R.id.track_origin);
            _dest_m = itemView.findViewById(R.id.track_dest);
            _status_m = itemView.findViewById(R.id.track_status);
            _date_m = itemView.findViewById(R.id.track_date);


        }
    }
}
