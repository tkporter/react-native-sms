//
//  SendSMS.m
//  SendSMS
//
//  Created by Trevor Porter on 7/13/16.


#import "SendSMS.h"

@implementation SendSMS

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(send:(NSDictionary *)options :(RCTResponseSenderBlock)callback)
{
    _callback = callback;
    MFMessageComposeViewController *messageController = [[MFMessageComposeViewController alloc] init];
    if([MFMessageComposeViewController canSendText])
    {

        NSString *body = options[@"body"];
        NSArray *recipients = options[@"recipients"];

        if (body) {
          messageController.body = body;
        }

        if (recipients) {
          messageController.recipients = recipients;
        }

        messageController.messageComposeDelegate = self;
        UIViewController *rootView = [UIApplication sharedApplication].keyWindow.rootViewController;
        [rootView presentViewController:messageController animated:YES completion:nil];
    }
}

-(void) messageComposeViewController:(MFMessageComposeViewController *)controller didFinishWithResult:(MessageComposeResult)result {
    bool completed = NO, cancelled = NO, error = NO;
    switch (result) {
        case MessageComposeResultSent:
            completed = YES;
            break;
        case MessageComposeResultCancelled:
            cancelled = YES;
            break;
        default:
            error = YES;
            break;
    }
    NSString *activityType = [NSString stringWithString:@"com.apple.UIKit.activity.Message"];
    
    _callback(@[RCTNullIfNil(activityType), @(completed), @(cancelled), @(error)]);

    [controller dismissViewControllerAnimated:YES completion:nil];
}

@end
