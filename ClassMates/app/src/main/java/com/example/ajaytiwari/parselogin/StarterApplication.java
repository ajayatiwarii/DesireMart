/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.example.ajaytiwari.parselogin;

import android.app.Application;

import com.example.ajaytiwari.parselogin.utils.Constants;
import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    FacebookSdk.sdkInitialize(getApplicationContext());

    Parse.initialize(this,
            Constants.KEY,
            Constants.Client
    );
    ParseFacebookUtils.initialize(this);
    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    ParseACL.setDefaultACL(defaultACL, true);
  }
}
