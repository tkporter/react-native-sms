declare module "react-native-sms" {
  export interface AttachmentOptions {
    url: string;
    iosType?: string;
    iosFilename?: string;
    androidType?: string;
  }

  export interface SendSmsOptions {
    body?: string;
    recipients?: string[];
    allowAndroidSendWithoutReadPermission?: boolean;
    attachment?: AttachmentOptions;
  }

  export function send(options: SendSmsOptions, callback: (completed: boolean, cancelled: boolean, error: boolean) => void): Promise<void>;

}
