//@flow
'use strict';

import { NativeModules, PermissionsAndroid, Platform } from 'react-native'

async function send(options: Object, callback: () => void) {
  if (Platform.OS === 'android') {
    try {
      let authorized = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.READ_SMS)
    } catch (error) {

    }
  }
  NativeModules.SendSMS.send(options, callback);
}

let SendSMS = {
  send
}

module.exports = SendSMS;
