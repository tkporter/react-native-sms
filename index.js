//@flow
'use strict';

import { NativeModules, PermissionsAndroid, Platform } from 'react-native'

async function sendSms(options: Object, callback: () => void) {
  if (Platform.OS === 'android') {
    try {
      let authorized = await PermissionsAndroid.requestPermission(PermissionsAndroid.PERMISSIONS.READ_SMS)
    } catch (error) {

    }
  }
  NativeModules.SendSMS.send(options, callback);
}

module.exports = {
  sendSms
};
