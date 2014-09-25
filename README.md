cordova-google-play-game
========================

Cordova Plugin For Google Play Game Service

### Live demo

See this plugin working in a live app: 

[![WOW PUZZLES iOS](https://developer.apple.com/app-store/marketing/guidelines/images/badge-download-on-the-app-store.svg)](https://itunes.apple.com/vn/app/wow-puzzles/id916475017?mt=8)

[![WOW PUZZLES Android](https://developer.android.com/images/brand/en_app_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=com.a42.xephinhtuoitho)

How it works: http://trinhtrunganh.com/cordova-plugin-for-google-play-game-service/

### Before you start

Understand about `Leaderboard` and `Achivement`. Setting up your game in Google Play Developer Console https://developers.google.com/games/services/android/quickstart

## Install

```
cordova plugin add https://github.com/ptgamr/cordova-google-play-game.git --variable APP_ID=you_app_id_here
```

## Usage

### Auth

You should do this as soon as your deviceready event has been fired. The plug handles the various auth scenarios for you.

```
googleplaygame.auth(successCallback, failureCallback);
```

### Submit Score

Ensure you have had a successful callback from `gamecenter.auth()` first before attempting to submit a score. You should also have set up your leaderboard(s) in Google Play Game Console and use the leaderboard identifier assigned there as the leaderboardId.

```
var data = {
    score: 10,
    leaderboardId: "board1"
};
googleplaygame.submitScore(successCallback, failureCallback, data);
```

### Show leaderboard

Launches the native Game Center leaderboard view controller for a leaderboard.

```
googleplaygame.showLeaderboard(successCallback, failureCallback, {});
```

### Report achievement

Reports an achievement to the game center:

```
var data = {
	achievementId: "MyAchievementName"
};

googleplaygame.reportAchievement(successCallback, failureCallback, data);
```

## Platform

Currently, only Android is supported

## Donation:
Wish you dont mind buying me a cup of coffee (highfive)

[Donate](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=anh%2etrinhtrung%40gmail%2ecom&lc=US&item_name=Cordova%20Google%20Play%20Game&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)

## License

[MIT License](http://ilee.mit-license.org)
