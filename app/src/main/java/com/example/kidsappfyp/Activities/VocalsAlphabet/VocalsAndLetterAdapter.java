package com.example.kidsappfyp.Activities.VocalsAlphabet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kidsappfyp.R;

import java.util.List;


public class VocalsAndLetterAdapter extends RecyclerView.Adapter<VocalsAndLetterAdapter.ViewHolder> {

    Activity context;
    int[] alphabets;

    public VocalsAndLetterAdapter(Activity context, int[] alphabets) {
        this.context = context;
        this.alphabets = alphabets;
    }

    @NonNull
    @Override
    public VocalsAndLetterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VocalsAndLetterAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.alphabets_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VocalsAndLetterAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imgAlphabets.setImageResource(alphabets[position]);
        holder.lloutAlphabets.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switch (position){
                    case 0:
                        VocalAndLetterActivity.ALPHABET = "A";
                        break;
                    case 1:
                        VocalAndLetterActivity.ALPHABET = "B";
                        break;
                    case 2:
                        VocalAndLetterActivity.ALPHABET = "C";
                        break;
                    case 3:
                        VocalAndLetterActivity.ALPHABET = "D";
                        break;
                    case 4:
                        VocalAndLetterActivity.ALPHABET = "E";
                        break;
                    case 5:
                        VocalAndLetterActivity.ALPHABET = "F";
                        break;
                    case 6:
                        VocalAndLetterActivity.ALPHABET = "G";
                        break;
                    case 7:
                        VocalAndLetterActivity.ALPHABET = "H";
                        break;
                    case 8:
                        VocalAndLetterActivity.ALPHABET = "I";
                        break;
                    case 9:
                        VocalAndLetterActivity.ALPHABET = "J";
                        break;
                    case 10:
                        VocalAndLetterActivity.ALPHABET = "K";
                        break;
                    case 11:
                        VocalAndLetterActivity.ALPHABET = "L";
                        break;
                    case 12:
                        VocalAndLetterActivity.ALPHABET = "M";
                        break;
                    case 13:
                        VocalAndLetterActivity.ALPHABET = "N";
                        break;
                    case 14:
                        VocalAndLetterActivity.ALPHABET = "O";
                        break;
                    case 15:
                        VocalAndLetterActivity.ALPHABET = "P";
                        break;
                    case 16:
                        VocalAndLetterActivity.ALPHABET = "Q";
                        break;
                    case 17:
                        VocalAndLetterActivity.ALPHABET = "R";
                        break;
                    case 18:
                        VocalAndLetterActivity.ALPHABET = "S";
                        break;
                    case 19:
                        VocalAndLetterActivity.ALPHABET = "T";
                        break;
                    case 20:
                        VocalAndLetterActivity.ALPHABET = "U";
                        break;
                    case 21:
                        VocalAndLetterActivity.ALPHABET = "V";
                        break;
                    case 22:
                        VocalAndLetterActivity.ALPHABET = "W";
                        break;
                    case 23:
                        VocalAndLetterActivity.ALPHABET = "X";
                        break;
                    case 24:
                        VocalAndLetterActivity.ALPHABET = "Y";
                        break;
                    case 25:
                        VocalAndLetterActivity.ALPHABET = "Z";
                        break;
                }
                context.finish();
                context.overridePendingTransition( R.anim.fade_in, R.anim.fade_out);
                context.startActivity(new Intent(context,VocalAndLetterActivity.class));
                context.overridePendingTransition( R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alphabets.length;
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
