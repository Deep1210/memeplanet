package in.incourt.incourtnews.helpers;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.helpers.interfaces.TextToSpeechInterface;

import java.util.HashMap;

/**
 * Created by bhavan on 2/15/17.
 */

public class IncourtTextSpeachHelper {

    TextToSpeechInterface textToSpeechInterface;

    public void speak(final TextToSpeechInterface textToSpeechInterface, String text, final int position){

        setTextToSpeechInterface(textToSpeechInterface);

        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, String.valueOf("1"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            IncourtApplication.getTextToSpeech().speak(text, TextToSpeech.QUEUE_FLUSH, map);
        }


        IncourtApplication.getTextToSpeech().setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                textToSpeechInterface.onStart(position);
            }

            @Override
            public void onDone(String s){
                textToSpeechInterface.onDone(position);
            }

            @Override
            public void onError(String s){
                textToSpeechInterface.onError(position);
            }
        });


    }

    public static boolean isSpeaking(){
        return IncourtApplication.getTextToSpeech().isSpeaking();
    }

    public static void stop(){
        IncourtApplication.getTextToSpeech().stop();
    }

    public TextToSpeechInterface getTextToSpeechInterface() {
        return textToSpeechInterface;
    }

    public void setTextToSpeechInterface(TextToSpeechInterface textToSpeechInterface) {
        this.textToSpeechInterface = textToSpeechInterface;
    }

}
