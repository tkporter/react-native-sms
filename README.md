# react-native-sms

## SendSMS
Use this RN component to send an SMS with a callback (completed/cancelled/error). iOS and Android are both supported.

Currently, only user-initiated sending of an SMS is supported. This means you can't use `react-native-sms` to send an SMS in the background-- this package displays the native SMS view (populated with any recipients/body you want), and gives a callback describing the status of the SMS (completed/cancelled/error). PRs are welcome!

## How to install
1. `npm install react-native-sms --save`

## Getting things set up

The compiler needs to know how to find your sweet new module!

`react-native link react-native-sms`

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
	super.onActivityResult(requestCode, resultCode, data);
	//probably some other stuff here
	SendSMSPackage.getInstance().onActivityResult(requestCode, resultCode, data);
}
```

Then head to your [MyApp]Application.java (`MyApp/android/app/src/main/java/so/many/dirs/MyAppApplication.java`)

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

Navigate to your `AndroidManifest.xml` (at `MyApp/android/app/src/main/AndroidManifest.xml`), and add this near the top with the other permssions
```XML
<uses-permission android:name="android.permission.READ_SMS" />
```

Ensure your launchMode for `.MainActivity` is
```XML
android:launchMode="singleTask"
```

## Using the module

Once everything is all setup, it's pretty simple:
```JavaScript
SendSMS.send(myOptionsObject, callback);
```

### Object Properties
|Key|Type|Platforms|Required?|Description|
|-|-|-|-|-|
| `body` | String | iOS/Android | No | The text that shows by default when the SMS is initiated |
| `recipients` | Array (strings) | iOS/Android | No | Provides the phone number recipients to show by default |
| `successTypes` | Array (strings) | Android | Yes | An array of types that would trigger a "completed" response when using android <br/><br/> Possible values: <br/><br/> `'all' 'inbox' 'sent' 'draft' 'outbox' 'failed' 'queued'` |
| `allowAndroidSendWithoutReadPermission` | boolean | Android | No | By default, SMS will only be initiated on Android if the user accepts the `READ_SMS` permission (which is required to provide completion statuses to the callback). <br/><br/> Passing `true` here will allow the user to send a message even if they decline the `READ_SMS` permission, and will then provide generic callback values (all false) to your application. |
|`attachment` | Object { url: string, iosType?: string, iosFilename?: string, androidType?: string } | iOS/Android | No | Pass a url to attach to the MMS message. <br/><br/>Currently known to work with images.

## Example:

```JavaScript
import SendSMS from 'react-native-sms'

//some stuff

someFunction() {
	SendSMS.send({
		body: 'The default body of the SMS!',
		recipients: ['0123456789', '9876543210'],
		successTypes: ['sent', 'queued'],
		allowAndroidSendWithoutReadPermission: true
	}, (completed, cancelled, error) => {

		console.log('SMS Callback: completed: ' + completed + ' cancelled: ' + cancelled + 'error: ' + error);

	});
}
```

## Attachment example

```JavaScript
import SendSMS from 'react-native-sms'
import resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource'

someFunction() {
	const image = require('assets/your-image.jpg');
	const metadata = resolveAssetSource(image);
	const url = metadata.uri;

	const attachment = {
		url: url,
		iosType: 'public.jpeg',
		iosFilename: 'Image.jpeg',
		androidType: 'image/*'
	};

	SendSMS.send({
		body: 'The default body of the SMS!',
		recipients: ['0123456789', '9876543210'],
		successTypes: ['sent', 'queued'],
		allowAndroidSendWithoutReadPermission: true,
		attachment: attachment
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
