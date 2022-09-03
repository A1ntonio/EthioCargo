package com.puulapp.ethio_cargo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.spacecraft.BookSpacecraft;
import com.puulapp.ethio_cargo.childUi.Consigne;
import com.puulapp.ethio_cargo.spacecraft.RoutesListSpacecraft;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RoutesListAdapter extends RecyclerView.Adapter<RoutesListAdapter.ViewHolder> {

    private List<RoutesListSpacecraft> list;
    private List<BookSpacecraft> bookList;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currentUserId = mAuth.getCurrentUser().getUid();
    public RoutesListAdapter(List<RoutesListSpacecraft> list, Context context, List<BookSpacecraft> bookList) {
        this.list = list;
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_routes_lists, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        RoutesListSpacecraft space = list.get(position);
        BookSpacecraft bookSpacecraft = bookList.get(0);

        holder.origin_txt.setText(space.getOrigin());
        holder.air_craft_type_txt.setText(space.getAircraft_type());
        holder.dest_txt.setText(space.getDestination());
        holder.flight_no_txt.setText(space.getFlight_no());
        holder.routes_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Consigne.class);
                intent.putExtra("origin", bookSpacecraft.getOrigin());
                intent.putExtra("destination", bookSpacecraft.getDestination());
                intent.putExtra("date", bookSpacecraft.getDate());
                intent.putExtra("weight", bookSpacecraft.getWeight());
                intent.putExtra("pieces", bookSpacecraft.getPieces());
                intent.putExtra("length", bookSpacecraft.getLength());
                intent.putExtra("width", bookSpacecraft.getWidth());
                intent.putExtra("height", bookSpacecraft.getHeight());
                intent.putExtra("volume", bookSpacecraft.getVolume());
                intent.putExtra("total_volume", bookSpacecraft.getTotal_volume());
                intent.putExtra("agent", bookSpacecraft.getAgent());
                intent.putExtra("key", space.getAppoint_key());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView origin_txt, dest_txt, flight_no_txt, air_craft_type_txt;
        public AppCompatButton routes_book_btn;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            origin_txt = itemView.findViewById(R.id.origin_txt);
            dest_txt = itemView.findViewById(R.id.dest_txt);
            flight_no_txt = itemView.findViewById(R.id.flight_no_txt);
            air_craft_type_txt = itemView.findViewById(R.id.air_craft_txt);
            routes_book_btn = itemView.findViewById(R.id.routes_book_btn);


        }
    }
}
