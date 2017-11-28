//@flow
'use strict';

import { NativeModules, PermissionsAndroid, Platform } from 'react-native'

async function send(options: Object, callback: () => void, requestPermission: boolean = true) {
  if (Platform.OS === 'android' && requestPermission) {
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
