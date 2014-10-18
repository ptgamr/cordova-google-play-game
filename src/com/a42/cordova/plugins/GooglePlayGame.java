/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.a42.cordova.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;

import com.a42.cordova.plugins.GameHelper.GameHelperListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.games.Games;

public class GooglePlayGame extends CordovaPlugin implements GameHelperListener {

    private static final String LOGTAG = "a42-CordovaGooglePlayGame";

    private static final String ACTION_AUTH = "auth";
    private static final String ACTION_SIGN_OUT = "signOut";
    private static final String ACTION_SUBMIT_SCORE = "submitScore";
    private static final String ACTION_SHOW_LEADERBOARD = "showLeaderboard";
    private static final String ACTION_SHOW_ACHIEVEMENTS = "showAchievements";
    private static final String ACTION_REPORT_ACHIEVEMENT = "reportAchievement";

    private static final int ACTIVITY_CODE_SHOW_LEADERBOARD = 0;
    private static final int ACTIVITY_CODE_SHOW_ACHIEVEMENTS = 1;

    private boolean isGpsAvailable = false;

    private GameHelper gameHelper;

    private CallbackContext authCallbackContext;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        isGpsAvailable = (GooglePlayServicesUtil.isGooglePlayServicesAvailable(cordova.getActivity()) == ConnectionResult.SUCCESS);
        Log.d(LOGTAG, String.format("isGooglePlayServicesAvailable: %s", isGpsAvailable ? "true" : "false"));

        gameHelper = new GameHelper(cordova.getActivity(), BaseGameActivity.CLIENT_GAMES);
        gameHelper.setup(this);

        cordova.setActivityResultCallback(this);
    }

    @Override
    public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {

        PluginResult result = null;
        JSONObject options = inputs.optJSONObject(0);

        if (ACTION_AUTH.equals(action)) {
            result = executeAuth(options, callbackContext);
        } else if (ACTION_SIGN_OUT.equals(action)) {
            result = executeSignOut(options, callbackContext);
        } else if (ACTION_SUBMIT_SCORE.equals(action)) {
            result = executeSubmitScore(options, callbackContext);
        } else if (ACTION_SHOW_LEADERBOARD.equals(action)) {
            result = executeShowLeaderboard(options, callbackContext);
        } else if (ACTION_SHOW_ACHIEVEMENTS.equals(action)) {
            result = executeShowAchievements(options, callbackContext);
        } else if (ACTION_REPORT_ACHIEVEMENT.equals(action)) {
            result = executeReportAchievement(options, callbackContext);
        }

        if (result != null) {
            callbackContext.sendPluginResult(result);
        }

        return true;
    }

    private PluginResult executeAuth(JSONObject options, final CallbackContext callbackContext) {
        Log.d(LOGTAG, "executeAuth");

        authCallbackContext = callbackContext;

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameHelper.beginUserInitiatedSignIn();
                callbackContext.success();
            }
        });
        return null;
    }

    private PluginResult executeSignOut(JSONObject options, final CallbackContext callbackContext) {
        Log.d(LOGTAG, "executeSignOut");

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameHelper.signOut();
                callbackContext.success();
            }
        });
        return null;
    }

    private PluginResult executeSubmitScore(final JSONObject options, final CallbackContext callbackContext) throws JSONException {
        Log.d(LOGTAG, "executeSubmitScore");

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (gameHelper.isSignedIn()) {
                        Games.Leaderboards.submitScore(gameHelper.getApiClient(), options.getString("leaderboardId"), options.getInt("score"));
                        callbackContext.success();
                    } else {
                        callbackContext.error("executeSubmitScore: not yet signed in");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callbackContext.error("executeSubmitScore: error while submitting score");
                }
            }
        });
        return null;
    }

    private PluginResult executeShowLeaderboard(JSONObject options, final CallbackContext callbackContext) {
        Log.d(LOGTAG, "executeShowLeaderboard");

        final GooglePlayGame plugin = this;

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (gameHelper.isSignedIn()) {
                    Intent allLeaderboardsIntent = Games.Leaderboards.getAllLeaderboardsIntent(gameHelper.getApiClient());
                    cordova.startActivityForResult(plugin, allLeaderboardsIntent, ACTIVITY_CODE_SHOW_LEADERBOARD);
                    callbackContext.success();
                } else {
                    callbackContext.error("executeShowLeaderboard: not yet signed in");
                }
            }
        });
        return null;
    }

    private PluginResult executeReportAchievement(final JSONObject options, final CallbackContext callbackContext) {
        Log.d(LOGTAG, "executeReportAchievement");

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (gameHelper.isSignedIn()) {
                    Games.Achievements.unlock(gameHelper.getApiClient(), options.optString("achievementId"));
                    callbackContext.success();
                } else {
                    callbackContext.error("executeReportAchievement: not yet signed in");
                }
            }
        });
        return null;
    }

    private PluginResult executeShowAchievements(final JSONObject options, final CallbackContext callbackContext) {
        Log.d(LOGTAG, "executeShowAchievements");

        final GooglePlayGame plugin = this;

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (gameHelper.isSignedIn()) {
                    Intent achievementsIntent = Games.Achievements.getAchievementsIntent(gameHelper.getApiClient());
                    cordova.startActivityForResult(plugin, achievementsIntent, ACTIVITY_CODE_SHOW_ACHIEVEMENTS);
                    callbackContext.success();
                } else {
                    callbackContext.error("executeShowAchievements: not yet signed in");
                }
            }
        });
        return null;
    }

    @Override
    public void onSignInFailed() {
        authCallbackContext.error("SIGN IN FALIED");
    }

    @Override
    public void onSignInSucceeded() {
        authCallbackContext.error("SIGN IN SUCCESS");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        gameHelper.onActivityResult(requestCode, resultCode, intent);
    }
}
