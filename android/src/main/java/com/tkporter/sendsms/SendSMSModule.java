package com.tkporter.sendsms;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsManager;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

public class SendSMSModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private static final int REQUEST_CODE = 5235;
    private final ReactApplicationContext reactContext;
    private Callback callback = null;

    public SendSMSModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "SendSMS";
    }


    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        //System.out.println("in module onActivityResult() request " + requestCode + " result " + resultCode);
        //canceled intent
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            sendCallback(false, true, false);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

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

            if (options.hasKey("direct_send") ? options.getBoolean("direct_send") : false) {
                sendDirect(options, callback);
                return;
            }

            new SendSMSObserver(reactContext, this, options).start();

            String body = options.hasKey("body") ? options.getString("body") : "";
            ReadableArray recipients = options.hasKey("recipients") ? options.getArray("recipients") : null;

            Intent sendIntent;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(reactContext);
                sendIntent = new Intent(Intent.ACTION_SEND);
                if (defaultSmsPackageName != null) {
                    sendIntent.setPackage(defaultSmsPackageName);
                }
                sendIntent.setType("text/plain");
            } else {
                sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("vnd.android-dir/mms-sms");
            }

            sendIntent.putExtra("sms_body", body);
            sendIntent.putExtra(sendIntent.EXTRA_TEXT, body);
            sendIntent.putExtra("exit_on_sent", true);

            //if recipients specified
            if (recipients != null) {
                //Samsung for some reason uses commas and not semicolons as a delimiter
                String separator = ";";
                if (android.os.Build.MANUFACTURER.equalsIgnoreCase("Samsung")) {
                    separator = ",";
                }
                String recipientString = "";
                for (int i = 0; i < recipients.size(); i++) {
                    recipientString += recipients.getString(i);
                    recipientString += separator;
                }
                sendIntent.putExtra("address", recipientString);
            }

            reactContext.startActivityForResult(sendIntent, REQUEST_CODE, sendIntent.getExtras());
        } catch (Exception e) {
            //error!
            sendCallback(false, false, true);
            throw e;
        }
    }

    /**
     * todo: do it in a background process
     *
     * @param options
     * @param callback
     */
    private void sendDirect(ReadableMap options, Callback callback) {

        String msg = options.hasKey("body") ? options.getString("body") : "";

        ReadableArray recipients = options.hasKey("recipients") ? options.getArray("recipients") : null;
        for (int i = 0; i < recipients.size(); i++) {
            String phoneNo = recipients.getString(i);

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            } catch (Exception ex) {
                ex.printStackTrace();
                sendCallback(false, false, true);
                return;
            }
        }

        sendCallback(true, false, false);

    }
}
