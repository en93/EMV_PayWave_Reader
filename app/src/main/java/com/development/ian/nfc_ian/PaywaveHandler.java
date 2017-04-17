package com.development.ian.nfc_ian;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import java.io.IOException;


//PaywaveHandler handles IO with the PayWave card
public class PaywaveHandler implements EMVReader.CardReader {

    private final IsoDep ISODEP;

    protected PaywaveHandler(Intent intent){
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        this.ISODEP = IsoDep.get(tag);

        try {
            ISODEP.connect();
            if(ISODEP.isConnected()){
                ISODEP.getHistoricalBytes();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public byte[] transceive(byte[] data) throws IOException {
        byte[] response= null;

        try {
            if(!ISODEP.isConnected()){
                ISODEP.connect();
                ISODEP.getHistoricalBytes();
                //Note: The getHistoricalBytes call assumes all cards are of the NFCa type.

            }
            if(ISODEP.isConnected()){
                response = ISODEP.transceive(data);
                if ((response.length == 2) && (response[0] == 0x61))
                {
                    byte[] getData = new byte[]
                            {
                                    0x00, (byte) 0xC0, 0x00, 0x00, response[1]
                            };
                    response = ISODEP.transceive(getData);
                }
            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}