package in.incourt.incourtnews.helpers.interfaces;

/**
 * Created by bhavan on 3/7/17.
 */

public interface TextToSpeechInterface {


    public void onStart(int position);

    public void onDone(int position);

    public void onError(int position);

}
