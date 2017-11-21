# react-native-sms

## SendSMS
Use this RN component to send an SMS with a callback (completed/cancelled/error). iOS and Android are both supported.

Currently, only user-initiated sending of an SMS is supported. This means you can't use `react-native-sms` to send an SMS in the background-- this package displays the native SMS view (populated with any recipients/body you want), and gives a callback describing the status of the SMS (completed/cancelled/error). PRs are welcome!

## How to install
1. `npm install react-native-sms --save`

## Getting things set up

The compiler needs to know how to find your sweet new module! (Make sure rnpm is installed via `npm install rnpm -g`)

`rnpm link react-native-sms`

### Additional Android Setup

Note: If using RN < v0.47, use react-native-sms <= v1.4.2

Just a few quick & easy things you need to set up in order to get SendSMS up and running!

1. Navigate to your MainActivity.java (`MyApp/android/app/src/main/java/some/other/directories/MainActivity.java`)


At the top of the file
```Java
import android.content.Intent; // <-- include if not already there
import com.tkporter.sendsms.SendSMSPackage;
```

Inside MainActivity (place entire function if it's not there already)
```Java
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	//probably some other stuff here
	SendSMSPackage.getInstance().onActivityResult(requestCode, resultCode, data);
}
```

2. Head to your [MyApp]Application.java (`MyApp/android/app/src/main/java/so/many/dirs/MyAppApplication.java`)

Make sure `import com.tkporter.sendsms.SendSMSPackage;` is there

Then head down to `getPackages()`, it has to look similar to this
```Java
protected List<ReactPackage> getPackages() {
	//some variables

	return Arrays.<ReactPackage>asList(
		//probably some items like `new BlahPackage(),`
		//just add into the list (don't forget commas!):
		SendSMSPackage.getInstance()
	);
}
```

3. Navigate to your `AndroidManifest.xml` (at `MyApp/android/app/src/main/AndroidManifest.xml`), and add this near the top with the other permssions
```XML
<uses-permission android:name="android.permission.READ_SMS" />
```

Ensure your launchMode for `.MainActivity` is
```XML
android:launchMode="singleTask"
```

4. Navigate to your app's `build.gradle` (at `MyApp/android/app/build.gradle`), and add the project to your dependencies
```Java
dependencies {
	// other projects may be compiled here
	compile project(':react-native-sms')
}
```

## Using the module

Once everything is all setup, it's pretty simple:
```JavaScript
SendSMS.send(myOptionsObject, callback);
```

### Object Properties

`body` (String, optional)

The text that shows by default when the SMS is initiated

`recipients` (Array (strings), optional)

Provides the phone number recipients to show by default

`successTypes` (Array (strings), Andriod only, required)

An array of types that would trigger a "completed" response when using android

Possible values:
```JavaScript
'all' | 'inbox' | 'sent' | 'draft' | 'outbox' | 'failed' | 'queued'
```

## Example:

```JavaScript
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

## Troubleshooting:

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
