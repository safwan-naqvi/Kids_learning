package com.example.kidsappfyp.Activities.GeoShapes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.shapes.Shape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidsappfyp.Activities.Numbers.NumbersActivity;
import com.example.kidsappfyp.R;

public class ShapesAdapterPrimary extends RecyclerView.Adapter<ShapesAdapterPrimary.ViewHolder> {
    Activity context;
    int[] shapes;

    public ShapesAdapterPrimary(Activity context, int[] shapes) {
        this.context = context;
        this.shapes = shapes;
    }

    @NonNull
    @Override
    public ShapesAdapterPrimary.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShapesAdapterPrimary.ViewHolder(LayoutInflater.from(context).inflate(R.layout.alphabets_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShapesAdapterPrimary.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imgAlphabets.setImageResource(shapes[position]);
        holder.lloutAlphabets.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ShapesActivity.SHAPE != position) {
                    ShapesActivity.SHAPE = position;
                    context.finish();
                    context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    context.startActivity(new Intent(context, ShapesActivity.class));
                    context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shapes.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAlphabets;
        LinearLayout lloutAlphabets;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgAlphabets = view.findViewById(R.id.imgVocalsAlphas);
            lloutAlphabets = view.findViewById(R.id.vocals_ll);
        }
    }
}
