package com.tkporter.sendsms;

import android.app.Activity;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import android.content.Intent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SendSMSPackage implements ReactPackage {

    private SendSMSModule sendSms = null;
    private static SendSMSPackage instance = null;

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        sendSms = new SendSMSModule(reactApplicationContext);
        if (instance == null) {
            instance = this;
        }
        return Arrays.<NativeModule>asList(sendSms);
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    public static SendSMSPackage getInstance() {
        if (instance == null) {
            instance = new SendSMSPackage();
        }

        return instance;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (sendSms != null) {
            sendSms.onActivityResult(null, requestCode, resultCode, data);
        }
    }
}
