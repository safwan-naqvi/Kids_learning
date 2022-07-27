package com.example.kidsappfyp.Activities.VocalsAlphabet;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.kidsappfyp.HelperClasses.Voice;
import com.example.kidsappfyp.R;


import java.util.List;

public class AlphabetThingsAdapter extends RecyclerView.Adapter<AlphabetThingsAdapter.ViewHolder> {
    List<AlphabetThingsModel> alphabetThingsModels;
    Activity context;

    public AlphabetThingsAdapter(Activity context, List<AlphabetThingsModel> alphabetThingsModels) {
        this.context = context;
        this.alphabetThingsModels = alphabetThingsModels;
    }

    @NonNull
    @Override
    public AlphabetThingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alphabets_things_list_layout, parent, false);
        Voice.init(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlphabetThingsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.speak.setText(alphabetThingsModels.get(position).getAlpha_speak());
        holder.setAlphabetIcon(alphabetThingsModels.get(position).getAlpha_img());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Voice.speak(context,alphabetThingsModels.get(position).getAlpha_speak(),false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alphabetThingsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView speak;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.alpha_container);
            icon = itemView.findViewById(R.id.alphabet_thing);
            speak = itemView.findViewById(R.id.alphabet_thing_speak);


        }

        private void setAlphabetIcon(String iconURL) {
            Glide.with(itemView.getContext()).load(iconURL).apply(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888).placeholder(R.drawable.placeholder)).into(icon);
        }

    }
}
