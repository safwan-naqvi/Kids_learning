package com.example.kidsappfyp.HelperClasses;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Voice {

    private static MediaPlayer mediaPlayer;
    private static TextToSpeech mTTS;
    private static String[] temp;


    public static void init(final Activity activity) {
        mTTS = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    mTTS.setLanguage(Locale.ENGLISH);
                    /*int result=mTTS.setLanguage(new Locale("hi","IN"));
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }else{
                        mTTS.setLanguage(Locale.ENGLISH);
                    }*/
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        mTTS.setPitch((float) 1.2);
        mTTS.setSpeechRate((float) 0.8);
    }


    private static void stopTTS() {
        if (Voice.mTTS != null) {
            Voice.mTTS.stop();
        }
    }


    public static void release() {
        stopTTS();

    }

    public static void speak(Activity activity, final String s, boolean flag) {

        Log.i("speak debug", "speak: in ocr :: " + s);
        release();
        if (mTTS != null)
            mTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null);


    }

}
