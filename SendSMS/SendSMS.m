//
//  SendSMS.m
//  SendSMS
//
//  Created by Trevor Porter on 7/13/16.


#import "SendSMS.h"
#import "RCTUtils.h"

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
        NSDictionary *attachment = options[@"attachment"];

        if (body) {
          messageController.body = body;
        }

        if (recipients) {
          messageController.recipients = recipients;
        }

        if (attachment) {
          NSString *attachmentUrl = attachment[@"url"];
          NSString *attachmentType = attachment[@"iosType"];
          NSString *attachmentFilename = attachment[@"iosFilename"];

          NSError *error;
          NSData *attachmentData = [NSData dataWithContentsOfURL:[NSURL URLWithString:attachmentUrl]
                                                     options:(NSDataReadingOptions)0
                                                       error:&error];

          bool attached = [messageController addAttachmentData:attachmentData typeIdentifier:attachmentType filename:attachmentFilename];

          if (!attached) {
            bool completed = NO, cancelled = NO, error = YES;
            _callback(@[@(completed), @(cancelled), @(error)]);
          }
        }


        messageController.messageComposeDelegate = self;
        UIViewController *currentViewController = [UIApplication sharedApplication].keyWindow.rootViewController;
        while(currentViewController.presentedViewController) {
            currentViewController = currentViewController.presentedViewController;
        }
        [currentViewController presentViewController:messageController animated:YES completion:nil];
    } else {
        bool completed = NO, cancelled = NO, error = YES;
        _callback(@[@(completed), @(cancelled), @(error)]);
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
    [controller dismissViewControllerAnimated:YES completion:^{
        _callback(@[@(completed), @(cancelled), @(error)]);
    }];
}

@end
