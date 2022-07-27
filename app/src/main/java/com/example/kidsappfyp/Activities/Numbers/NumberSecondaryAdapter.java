package com.example.kidsappfyp.Activities.Numbers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidsappfyp.R;

import java.util.List;

public class NumberSecondaryAdapter extends RecyclerView.Adapter<NumberSecondaryAdapter.ViewHolder> {
    Context context;
    private List<NumbersSecondaryModel> NumbersSecondaryList;
    private MediaPlayer mediaPlayer;

    public NumberSecondaryAdapter(Context context, List<NumbersSecondaryModel> numbersSecondaryList) {
        this.context = context;
        NumbersSecondaryList = numbersSecondaryList;
    }

    @NonNull
    @Override
    public NumberSecondaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NumberSecondaryAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.numbers_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NumberSecondaryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imgNumber.setImageResource(NumbersSecondaryList.get(position).getImageNumber());
        holder.imgHandIcon.setImageResource(NumbersSecondaryList.get(position).getHandNumber());
        holder.imgNumberName.setText(NumbersSecondaryList.get(position).getTextNumber());
        holder.lloutNumbers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switch (NumbersActivity.NUMBER){
                    case 0:
                        mediaPlayer = MediaPlayer.create(context, R.raw.zero);
                        break;
                    case 1:
                        mediaPlayer = MediaPlayer.create(context, R.raw.one);
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(context, R.raw.two);
                        break;
                    case 3:
                        mediaPlayer = MediaPlayer.create(context, R.raw.three);
                        break;
                    case 4:
                        mediaPlayer = MediaPlayer.create(context, R.raw.four);
                        break;
                    case 5:
                        mediaPlayer = MediaPlayer.create(context, R.raw.five);
                        break;
                    case 6:
                        mediaPlayer = MediaPlayer.create(context, R.raw.six);
                        break;
                    case 7:
                        mediaPlayer = MediaPlayer.create(context, R.raw.seven);
                        break;
                    case 8:
                        mediaPlayer = MediaPlayer.create(context, R.raw.eight);
                        break;
                    case 9:
                        mediaPlayer = MediaPlayer.create(context, R.raw.nine);
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
        return NumbersSecondaryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgNumber,imgHandIcon;
        TextView imgNumberName;
        ConstraintLayout lloutNumbers;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgNumber = view.findViewById(R.id.number_digit_icon);
            imgNumberName = view.findViewById(R.id.number_digit_text);
            imgHandIcon = view.findViewById(R.id.number_hand_icon);
            lloutNumbers = view.findViewById(R.id.constrain_numbers);
        }
    }

}