//
//  SendSMS.h
//  SendSMS
//
//  Created by Trevor Porter on 7/13/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "RCTBridgeModule.h"
#import <MessageUI/MessageUI.h>

@interface SendSMS : NSObject <MFMessageComposeViewControllerDelegate, RCTBridgeModule> {
    RCTResponseSenderBlock _callback;
}

@end
