//
//  SendSMS.m
//  SendSMS
//
//  Created by Trevor Porter on 7/13/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "SendSMS.h"
#import "AppDelegate.h"
#import "RCTConvert.h"

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
        messageController.body = [RCTConvert NSString:options[@"body"]];
        messageController.messageComposeDelegate = self;
        AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
        [delegate.rootViewController presentViewController:messageController animated:NO completion:nil];
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
    _callback(@[@(completed), @(cancelled), @(error)]);
}

@end
