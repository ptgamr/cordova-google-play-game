cordova-google-play-game
========================

Cordova Plugin For Google Play Game Service

### Live demo

See this plugin working in a live app: 

[![WOW PUZZLES iOS](https://developer.apple.com/app-store/marketing/guidelines/images/badge-download-on-the-app-store.svg)](https://itunes.apple.com/vn/app/wow-puzzles/id916475017?mt=8)

[![WOW PUZZLES Android](https://developer.android.com/images/brand/en_app_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=com.a42.xephinhtuoitho)

How it works: http://trinhtrunganh.com/cordova-plugin-for-google-play-game-service/

### Before you start

Understand about **Leaderboard** and **Achievement**. Setting up your game in Google Play Developer Console https://developers.google.com/games/services/android/quickstart

## Install

```
cordova plugin add https://github.com/ptgamr/cordova-google-play-game.git --variable APP_ID=you_app_id_here
```

## Usage

### Authentication

#### Sign in
You should do this as soon as your `deviceready` event has been fired. The plugin handles the various auth scenarios for you.

```
googleplaygame.auth();
```

#### Sign out
You should provde the option for users to sign out

```
googleplaygame.signout();
```

#### Auth status
To check if the user is already logged in (eg. to determine weather to show the Log In or Log Out button), use the following

```
googleplaygame.isSignedIn(function (result) {
	// ‘result’ is a JSON object with a single boolean property of ‘isSignedIn’
	// {
	// 		“isSignedIn” : true
	// }
	
	console.log(“Do something with result.isSignedIn”);
});
```

#### Player Information
Fetch the currently authenticated player's data.

```
googleplaygame.showPlayer(function (playerData) {
	...
	console.log(“Authenticated as ”+playerData['displayName']);
});
```


### Leaderboards

#### Submit Score

Ensure you have had a successful callback from `googleplaygame.auth()` first before attempting to submit a score. You should also have set up your leaderboard(s) in Google Play Game Console and use the leaderboard identifier assigned there as the `leaderboardId`.

```
var data = {
    score: 10,
    leaderboardId: "board1"
};
googleplaygame.submitScore(data);
```

#### Show all leaderboards

Launches the native Play Games leaderboard view controller to show all the leaderboards.

```
googleplaygame.showAllLeaderboards();
```

#### Show specific leaderboard

Launches directly into the specified leaderboard:

```
var data = {
	leaderboardId: "board1"
};
googleplaygame.showLeaderboard(leaderboardId);
```

### Achievements
#### Unlock achievement

Unlocks the specified achievement:

```
var data = {
	achievementId: "achievementId1"
};

googleplaygame.unlockAchievement(data);
```

#### Increment achievement

Increments the specified incremental achievement by the provided numSteps:

```
var data = {	
	achievementId: "achievementId1",
	numSteps: 1
};

googleplaygame.incrementAchievement(data);
```

#### Show achievements

Launches the native Play Games achievements view controller to show the user’s achievements.

```
googleplaygame.showAchievements();
```

### Other

#### Success/Failure callbacks

For all methods, you can optionally provide custom success/failure callbacks.

For example:

```
var successfullyLoggedIn = function () { ... };
var failedToLogin = function () { ... };
googleplaygame.auth(successfullyLoggedIn, failedToLogin);

var data = { ... };
var successfullySubmittedScore  = function () { ... };
var failedToSubmitScore  = function () { ... };
googleplaygame.submitScore(data, successfullySubmittedScore, failedToSubmitScore);
```

## Platform

Currently, only Android is supported

## Donation:
Wish you dont mind buying me a cup of coffee (highfive)

[Donate](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=anh%2etrinhtrung%40gmail%2ecom&lc=US&item_name=Cordova%20Google%20Play%20Game&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)

## License

[MIT License](http://ilee.mit-license.org)
