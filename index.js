//@flow
'use strict';

import { NativeModules, PermissionsAndroid, Platform } from 'react-native'

async function checkForAndroidAuthorized() {
  if (Platform.OS !== 'android') {
    return false;
  }

  let authorized;

  try {
    authorized = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.READ_SMS);
  } catch (error) {
    // do nothing
  }

  return authorized === PermissionsAndroid.RESULTS.GRANTED;
}

async function send(options: Object, callback: () => void) {
  const isAndroidAuthorized = await checkForAndroidAuthorized();
  if (Platform.OS !== 'android' || isAndroidAuthorized) {
    NativeModules.SendSMS.send(options, callback);
  }
}

const SendSMS = {
  send
}

module.exports = SendSMS;
