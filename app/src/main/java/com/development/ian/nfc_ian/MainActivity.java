package com.development.ian.nfc_ian;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

//MainActivity manages the UI and foreground dispatch
public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView pan, issuer, expires, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setup foreground dispatch
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //If NFC is turned off let the user know
        if(!nfcAdapter.isEnabled()){
            Snackbar nfcNotEnabledMessage = Snackbar.make(findViewById(R.id.content_view), R.string.message_nfc_off, Snackbar.LENGTH_INDEFINITE);
            nfcNotEnabledMessage.show();
        }
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        pan = (TextView) findViewById(R.id.pan);
        issuer = (TextView) findViewById(R.id.issuer);
        expires = (TextView) findViewById(R.id.expiry);
        message = (TextView) findViewById(R.id.message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //disable options menu
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        super.onResume();
    }

    @Override
    public void onPause(){
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent){
        //Carry out IO operations and reading of responses in asynchronous task
        AsyncCardRead asyncCardRead = new AsyncCardRead(this);
        asyncCardRead.execute(intent);
    }

    private void displayTrack2(EMVReader emvReader){
        //Update the UI with card info
        expires.setText(getResources().getString(R.string.expiry,
                emvReader.expiryMonth, emvReader.expiryYear));
        pan.setText(getResources().getString(R.string.pan, emvReader.pan));
        issuer.setText(getResources().getString(R.string.issuer, emvReader.issuer));
        message.setText(getResources().getString(R.string.message_post));

        Snackbar updatedMessage = Snackbar.make(findViewById(R.id.content_view), R.string.message_ui_updated, Snackbar.LENGTH_LONG);
        updatedMessage.show();
    }

    private void displayError() {
        Snackbar errorMessage = Snackbar.make(findViewById(R.id.content_view), R.string.message_read_error, Snackbar.LENGTH_LONG);
        errorMessage.show();
    }

    protected void updateEMVReader(EMVReader reader){
        //Verify all information needed is present
        if(reader == null || reader.issuer == null || reader.expiryYear == null ||
                reader.expiryMonth == null || reader.pan == null){
            displayError();
        }else {
            displayTrack2(reader);
        }
    }



}
