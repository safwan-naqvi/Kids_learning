package com.example.kidsappfyp.Activities.GeoShapes;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidsappfyp.Activities.Numbers.NumberSecondaryAdapter;
import com.example.kidsappfyp.Activities.Numbers.NumbersActivity;
import com.example.kidsappfyp.R;

import java.util.List;

public class ShapesAdapterSecondary extends RecyclerView.Adapter<ShapesAdapterSecondary.ViewHolder> {

    private List<ShapesModel> ShapesList;
    private Activity context;
    private MediaPlayer mediaPlayer;
    public ShapesAdapterSecondary(List<ShapesModel> shapesList, Activity context) {
        ShapesList = shapesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ShapesAdapterSecondary.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShapesAdapterSecondary.ViewHolder(LayoutInflater.from(context).inflate(R.layout.shapes_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShapesAdapterSecondary.ViewHolder holder, int position) {
        holder.shapeImage.setImageResource(ShapesList.get(position).getShapeImage());
        holder.shapeName.setText(ShapesList.get(position).getShapeName());
        holder.shapeDesc.setText(ShapesList.get(position).getShapeDescription());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (ShapesActivity.SHAPE){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.circle);
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.triangle);
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.rhombus);
                        break;
                    case 3:
                        mediaPlayer = MediaPlayer.create(context, R.raw.heart);
                        break;
                    case 4:
                        mediaPlayer = MediaPlayer.create(context, R.raw.pentagon);
                        break;
                    case 5:
                        mediaPlayer = MediaPlayer.create(context, R.raw.star);
                        break;
                    case 6:
                        mediaPlayer = MediaPlayer.create(context, R.raw.square);
                        break;
                    case 7:
                        mediaPlayer = MediaPlayer.create(context, R.raw.rectangle);
                        break;
                }
                mediaPlayer.setLooping(false);
                mediaPlayer.setVolume(1, 1);
                mediaPlayer.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ShapesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView shapeImage;
        TextView shapeName, shapeDesc;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shapeImage = itemView.findViewById(R.id.shape_image);
            shapeName = itemView.findViewById(R.id.shape_name);
            shapeDesc = itemView.findViewById(R.id.shape_desc);
            linearLayout = itemView.findViewById(R.id.llShape);

        }
    }

}
