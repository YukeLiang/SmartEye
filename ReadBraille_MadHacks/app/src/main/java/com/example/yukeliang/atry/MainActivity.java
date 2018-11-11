package com.example.yukeliang.atry;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements
        TextToSpeech.OnInitListener {

    /** Called when the activity is first created. */
    private TextToSpeech tts;
    private Button btnSpeak;
    private EditText txtText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this, this);

        btnSpeak = (Button) findViewById(R.id.btnSpeak);

        txtText = (EditText) findViewById(R.id.txtText);

        // button on click event
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                speakOut();
            }

        });

        Client client = new Client("EA34ADB1G3", "0da7cf0b19d66065c71405c6a2f34418");

        Index index = client.initIndex("signs");
        try {
        index.addObjectAsync(new JSONObject()
                .put("name", "start")
                .put("meaning", "you should start"), null);

            index.addObjectAsync(new JSONObject()
                    .put("name", "stop")
                    .put("meaning", "you got to stop."), null);


//            JSONObject settings = new JSONObject().accumulate("customRanking", "desc(followers)");
//            index.setSettingsAsync(settings, null);

            CompletionHandler completionHandler = new CompletionHandler() {
                @Override
                public void requestCompleted(JSONObject content, AlgoliaException error) {
                    // [...]
//                    System.out.println(content.toString());
                }
            };
            // Search for a first name
            System.out.println("test2 " + index.searchAsync(new Query("start"), completionHandler));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                btnSpeak.setEnabled(true);
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {

        CharSequence text = txtText.getText();

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"id1");
    }
}