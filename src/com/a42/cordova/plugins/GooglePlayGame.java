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
	
	private static final String LOGTAG = "GooglePlayGame";
	
	private static final String ACTION_AUTH = "auth";
    private static final String ACTION_SUBMIT_SCORE = "submitScore";
    private static final String ACTION_SHOW_LEADERBOARD = "showLeaderboard";
    private static final String ACTION_REPORT_ACHIVEMENT = "reportAchievement";
    
    private static final int ACTIVITY_CODE_SHOW_LEADERBOARD = 0;
    
    private boolean isGpsAvailable = false;
    
    private GameHelper gameHelper;
    
    private CallbackContext authCallbackContext;
    
    @Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    	super.initialize(cordova, webView);
    	isGpsAvailable = (GooglePlayServicesUtil.isGooglePlayServicesAvailable(cordova.getActivity()) == ConnectionResult.SUCCESS);
    	Log.w(LOGTAG, String.format("isGooglePlayServicesAvailable: %s",  isGpsAvailable?"true":"false"));
    	
    	gameHelper = new GameHelper(cordova.getActivity(), BaseGameActivity.CLIENT_GAMES);
    	gameHelper.setup(this);
	}
    
	@Override
	public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {
		
		PluginResult result = null;
		
		if (ACTION_AUTH.equals(action)) {
			JSONObject options = inputs.optJSONObject(0);
			result = executeAuth(options, callbackContext);
		} else if (ACTION_SUBMIT_SCORE.equals(action)) {
			JSONObject options = inputs.optJSONObject(0);
			result = executeSubmitScore(options, callbackContext);
		} else if (ACTION_SHOW_LEADERBOARD.equals(action)) {
			JSONObject options = inputs.optJSONObject(0);
			result = executeShowLeaderboard(options, callbackContext);
		} else if(ACTION_REPORT_ACHIVEMENT.equals(action)){
			JSONObject options = inputs.optJSONObject(0);
			result = executeReportAchivement(options, callbackContext);
		}
		
		if(result != null) callbackContext.sendPluginResult( result );
		
        return true;
	}
	
	private PluginResult executeAuth(JSONObject options, final CallbackContext callbackContext) {
		Log.w(LOGTAG, "executeAuth");
		
		authCallbackContext = callbackContext;
		
		cordova.getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
            	gameHelper.beginUserInitiatedSignIn();
            	callbackContext.success();
            }
		});
    	return null;
	}
	
	private PluginResult executeSubmitScore(final JSONObject options, final CallbackContext callbackContext) throws JSONException {
		Log.w(LOGTAG, "executeSubmitScore");
		cordova.getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
            	try {
            		if (gameHelper.isSignedIn()) {
            			Games.Leaderboards.submitScore(gameHelper.getApiClient(), options.getString("leaderboardId"), options.getInt("score"));
            			callbackContext.success();
            		} else {
            			callbackContext.error("not yet signed in");
            		}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callbackContext.error("error while submitting score");
				}
            }
		});
    	return null;
	}
	
	private PluginResult executeShowLeaderboard(JSONObject options, final CallbackContext callbackContext) {
		Log.w(LOGTAG, "executeShowLeaderboard");
		
		final GooglePlayGame plugin = this;
		
		cordova.getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
            	
            	if (gameHelper.isSignedIn()) {
            		Intent allLeaderboardsIntent = Games.Leaderboards.getAllLeaderboardsIntent(gameHelper.getApiClient());
            		cordova.startActivityForResult(plugin, allLeaderboardsIntent, ACTIVITY_CODE_SHOW_LEADERBOARD);
            		callbackContext.success();
            	} else {
            		callbackContext.error("Not yet signed in");
            	}
            }
		});
    	return null;
	}
	
	private PluginResult executeReportAchivement(final JSONObject options, final CallbackContext callbackContext) {
		Log.w(LOGTAG, "executeReportAchivement");
		cordova.getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
            	
            	if (gameHelper.isSignedIn()) {
            		Games.Achievements.unlock(gameHelper.getApiClient(), options.optString("achivementId"));
            		callbackContext.success();
            	} else {
            		callbackContext.error("executeReportAchivement: Not yet signed in");
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
}
