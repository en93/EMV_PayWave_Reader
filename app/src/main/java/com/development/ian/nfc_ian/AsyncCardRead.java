package com.development.ian.nfc_ian;

import android.content.Intent;
import android.os.AsyncTask;
import java.io.IOException;

//AsyncCardRead starts an asynchronous task to process responses and carry out card IO
public class AsyncCardRead extends AsyncTask<Intent, Void, EMVReader> {

    private final MainActivity MAIN_ACTIVITY;

    protected AsyncCardRead(MainActivity mainActivity){
        MAIN_ACTIVITY = mainActivity;
    }

    @Override
    protected EMVReader doInBackground(Intent... intents) {
        Intent intent = intents[0];
        PaywaveHandler paywaveHandler = new PaywaveHandler(intent);
        EMVReader emvReader;

        try {
            emvReader = new EMVReader(paywaveHandler, null, paywaveHandler.transceive(EMVReader.SELECT_PPSE));
            emvReader.read();
            return emvReader;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(EMVReader emvReader){
        MAIN_ACTIVITY.updateEMVReader(emvReader);
    }
}
