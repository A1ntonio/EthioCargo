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
import com.puulapp.ethio_cargo.spacecraft.ManageSpacecraft;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ManageListAdapter extends RecyclerView.Adapter<ManageListAdapter.ViewHolder> {

    private List<ManageSpacecraft> list;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currentUserId = mAuth.getCurrentUser().getUid();

    public ManageListAdapter(List<ManageSpacecraft> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_manage_lists, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ManageSpacecraft space = list.get(position);

        holder._origin_m.setText(space.get_origin());
        holder._dest_m.setText(space.get_dest());
        holder._status_m.setText(space.get_status());
        holder._action_m.setText(space.get_action());
        holder._date_m.setText(space.get_date());
        holder._view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewMore.class);
                intent.putExtra("key", space.get_key());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView _origin_m, _dest_m, _status_m, _action_m, _date_m, _view_more;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            _origin_m = itemView.findViewById(R.id._origin_m);
            _dest_m = itemView.findViewById(R.id._dest_m);
            _status_m = itemView.findViewById(R.id._status_m);
            _action_m = itemView.findViewById(R.id._action_m);
            _date_m = itemView.findViewById(R.id._date_m);
            _view_more = itemView.findViewById(R.id._view_more);


        }
    }
}
