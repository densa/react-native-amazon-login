
#import "RNAmazonLogin.h"
#import <LoginWithAmazon/LoginWithAmazon.h>

@implementation RNAmazonLogin

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(login:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    AMZNAuthorizeRequest *request = [[AMZNAuthorizeRequest alloc] init];

    // Requesting 'profile' scopes for the current user.
    request.scopes = [NSArray arrayWithObject:[AMZNProfileScope profile]];
    [[AMZNAuthorizationManager sharedManager] authorize:request
                                            withHandler:^(AMZNAuthorizeResult * _Nullable amzResult, BOOL userDidCancel, NSError * _Nullable error) {
                                                if (error) {
                                                    reject([NSString stringWithFormat:@"%li", error.code], error.userInfo[@"AMZNLWAErrorNonLocalizedDescription"], error);
                                                } else {
                                                    NSDictionary *result = @{
                                                                             @"token": amzResult.token,
                                                                             @"user": amzResult.user.profileData,
                                                                             };
                                                    resolve(result);
                                                }
                                            }];
}

RCT_EXPORT_METHOD(logout:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[AMZNAuthorizationManager sharedManager] signOut:^(NSError * _Nullable error) {
        if (error) {
            reject([NSString stringWithFormat:@"%li", error.code], error.userInfo[@"AMZNLWAErrorNonLocalizedDescription"], error);
        } else {
            resolve(@YES);
        }
    }];
}

@end

