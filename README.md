#react-native-sms

##SendSMS
Use this component to send an SMS with a callback (completed/cancelled/error). Works for iOS and Android!

##How to install
1. `npm install react-native-sms --save`

##Getting things set up

The compiler needs to know how to find your sweet new module! (Make sure rnpm is installed via `npm install rnpm -g`)

`rnpm link react-native-sms`

###Additional Android Setup
Just a few quick & easy things you need to set up in order to get SendSMS up and running!

1. Navigate to your MainActivity.java (`MyApp/android/app/src/main/java/some/other/directories/MainActivity.java`)


At the top of the file
```
import android.content.Intent;
import com.tkporter.sendsms.SendSMSPackage
```

Head over to the class and add this variable...
```
public class MainActivity extends ReactActivity {
	//maybe some other private variables up here
	private SendSMSPackage sendSMS;
```

Then add this function to the class
```
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	//If this function is already inside MainActivity, just add the line below
	sendSMS.onActivityResult(requestCode, resultCode, data);
}
```

Then head down to `getPackages()`, it has to look similar to this
```
..... getPackages() {
	//maybe some other variables
	sendSMS = new SendSMSPackage();

	return Arrays.<ReactPackage>asList(
		//probably some items like `new BlahPackage(),`
		//just add into the list (don't forget commas!):
		sendSMS
	);
}
```

Navigate to your `AndroidManifest.xml` (at `MyApp/android/app/src/main/AndroidManifest.xml`), and add this near the top with the other permssions

`<uses-permission android:name="android.permission.READ_SMS" />`

Ensure your launchMode for `.MainActivity` is

`android:launchMode="singleTask"`


##Using the module

Once everything is all setup, it's pretty simple:

`SendSMS.send(myOptionsObject, callback);`

###Object Properties

`body` (String, optional)

The text that shows by default when the SMS is initiated

`recipients` (Array (strings), optional)

Provides the phone number recipients to show by default

`successTypes` (Array (strings), Andriod only, required)

An array of types that would trigger a "completed" response when using android
	Possible values:
		```
		'all' |
		'inbox' |
		'sent' |
		'draft' |
		'outbox' |
		'failed' |
		'queued'
		```

##Example:

```
import SendSMS from 'react-native-sms'

//some stuff

someFunction() {

	SendSMS.send({
		body: 'The default body of the SMS!',
		recipients: ['0123456789', '9876543210'],
		successTypes: ['sent', 'queued']
	}, (completed, cancelled, error) => {

		console.log('SMS Callback: completed: ' + completed + ' cancelled: ' + cancelled + 'error: ' + error);

	});
}
```

##Troubleshooting:

Having errors with import statements on Android? Something happened with linking

Go to your `settings.gradle` (in `MyApp/android/settings.gradle`) and add:
```
include ':react-native-sms'
project(':react-native-sms').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-sms/android')
```

Then go to `MyApp/android/app/build.gradle` and add inside `dependencies { }`:
```
compile project(':react-native-sms')
```
