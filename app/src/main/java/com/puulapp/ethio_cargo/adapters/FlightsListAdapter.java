package com.puulapp.ethio_cargo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.spacecraft.FlightsSpacecraft;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlightsListAdapter extends RecyclerView.Adapter<FlightsListAdapter.ViewHolder> {

    private List<FlightsSpacecraft> list;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currentUserId = mAuth.getCurrentUser().getUid();

    public FlightsListAdapter(List<FlightsSpacecraft> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_flights_lists, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        FlightsSpacecraft space = list.get(position);

        holder._from_.setText(space._from);
        holder._to_.setText(space._to);
        holder.departure_date_.setText(space.departure_date);
        holder.flight_no_.setText(space.flight_no);
        holder.air_craft_.setText(space.aircraft_type);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView _from_, _to_, departure_date_, flight_no_, air_craft_;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            _from_ = itemView.findViewById(R.id._from_);
            _to_ = itemView.findViewById(R.id._to_);
            departure_date_ = itemView.findViewById(R.id.departure_date_);
            flight_no_ = itemView.findViewById(R.id.flight_no_);
            air_craft_ = itemView.findViewById(R.id.air_craft_);


        }
    }
}
