package com.example.kidsappfyp.Activities.Numbers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidsappfyp.Activities.VocalsAlphabet.VocalAndLetterActivity;
import com.example.kidsappfyp.Activities.VocalsAlphabet.VocalsAndLetterAdapter;
import com.example.kidsappfyp.R;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.ViewHolder> {
    Activity context;
    int[] numbers;

    public NumbersAdapter(Activity context, int[] numbers) {
        this.context = context;
        this.numbers = numbers;
    }

    @NonNull
    @Override
    public NumbersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NumbersAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.alphabets_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NumbersAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imgAlphabets.setImageResource(numbers[position]);
        holder.lloutAlphabets.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(NumbersActivity.NUMBER != position){
                    NumbersActivity.NUMBER = position;
                    context.finish();
                    context.overridePendingTransition( R.anim.fade_in, R.anim.fade_out);
                    context.startActivity(new Intent(context,NumbersActivity.class));
                    context.overridePendingTransition( R.anim.fade_in, R.anim.fade_out);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return numbers.length;
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
