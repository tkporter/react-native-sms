package com.tkporter.sendsms;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Callback;

public class SendSMSModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private final ReactApplicationContext reactContext;
    private Callback callback = null;

    public SendSMSModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "SendSMS";
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //System.out.println("in module onActivityResult() request " + requestCode + " result " + resultCode);
        //canceled intent
        if (resultCode == Activity.RESULT_CANCELED) {
            sendCallback(false, true, false);
        }
    }

    public void sendCallback(Boolean completed, Boolean cancelled, Boolean error) {
        if (callback != null) {
            callback.invoke(completed, cancelled, error);
            callback = null;
        }
    }

    @ReactMethod
    public void send(ReadableMap options, final Callback callback) {
        try {
            this.callback = callback;
            new SendSMSObserver(reactContext, this, options).start();

            String body = options.hasKey("body") ? options.getString("body") : "";
            ReadableArray recipients = options.hasKey("recipients") ? options.getArray("recipients") : null;

            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body", body);

            //if recipients specified
            if (recipients != null) {
                //Samsung for some reason uses commas and not semicolons as a delimiter
                String separator = "; ";
                if(android.os.Build.MANUFACTURER.equalsIgnoreCase("Samsung")){
                    separator = ", ";
                }
                String recipientString = "";
                for (int i = 0; i < recipients.size(); i++) {
                    recipientString += recipients.getString(i);
                    recipientString += separator;
                }
                sendIntent.putExtra("address", recipientString);
            }

            sendIntent.setType("vnd.android-dir/mms-sms");
            reactContext.startActivityForResult(sendIntent, 1, sendIntent.getExtras());
        } catch (Exception e) {
            //error!
            sendCallback(false, false, true);
            throw e;
        }
    }

}

