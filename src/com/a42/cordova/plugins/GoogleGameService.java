package com.a42.cordova.plugins;

import com.google.example.games.basegameutils.BaseGameActivity;

public class GoogleGameService extends BaseGameActivity{

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		
	}
	
	public void doSignIn() {
		beginUserInitiatedSignIn();
	}
}
