
package com.amazonlogin;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.authorization.User;
import com.amazon.identity.auth.device.api.workflow.RequestContext;

public class RNAmazonLoginModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

  private static final String TAG = RNAmazonLoginModule.class.getName();

  private final ReactApplicationContext reactContext;
  private RequestContext requestContext;

  @Nullable
  private Promise mAuthPromise;

  public RNAmazonLoginModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
      @Override
      public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        super.onActivityResult(activity, requestCode, resultCode, data);
      }
    };
    reactContext.addLifecycleEventListener(this);
    reactContext.addActivityEventListener(mActivityEventListener);
  }

  @Override
  public String getName() {
    return "RNAmazonLogin";
  }

  @ReactMethod
  public void login(final Promise promise) {
    mAuthPromise = promise;
    AuthorizationManager.authorize(
                        new AuthorizeRequest.Builder(requestContext)
                                .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
                                .build()
                );
  }

  @ReactMethod
  public void logout(final Promise promise) {
    AuthorizationManager.signOut(getReactApplicationContext().getApplicationContext(), new Listener<Void, AuthError>() {
      @Override
      public void onSuccess(Void response) {
        promise.resolve(response);
      }

      @Override
      public void onError(AuthError authError) {
          promise.reject(TAG, authError.getMessage());
      }
  });
  }

  @Override
  public void initialize() {
    requestContext = RequestContext.create(getCurrentActivity());
    requestContext.registerListener(new AuthorizeListener() {
      /* Authorization was completed successfully. */
      @Override
      public void onSuccess(AuthorizeResult authorizeResult) {
        final WritableMap userMap = Arguments.createMap();
        User user = authorizeResult.getUser();
        userMap.putString("id", user.getUserId());
        userMap.putString("name", user.getUserName());
        userMap.putString("email", user.getUserEmail());
        userMap.putString("postalCode", user.getUserPostalCode());

        final WritableMap result = Arguments.createMap();
        result.putString("token", authorizeResult.getAccessToken());
        result.putMap("user", userMap);
        mAuthPromise.resolve(result);
        mAuthPromise = null;
      }

      /* There was an error during the attempt to authorize the application */
      @Override
      public void onError(AuthError authError) {
          mAuthPromise.reject(TAG, authError.getMessage());
          mAuthPromise = null;
      }

      /* Authorization was cancelled before it could be completed. */
      @Override
      public void onCancel(AuthCancellation authCancellation) {
          mAuthPromise.reject(TAG, "User cancelled authorization");
          mAuthPromise = null;
      }
    });
  }

  @Override
  public void onHostResume() {
    if (requestContext != null) {
      requestContext.onResume();
    }
  }

  @Override
  public void onHostPause() {
  }

  @Override
  public void onHostDestroy() {
  }

}