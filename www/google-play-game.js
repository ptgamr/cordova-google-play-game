var exec = require("cordova/exec");
var GOOGLE_PLAY_GAME = "GooglePlayGame";

var GooglePlayGame = function () {
    this.name = GOOGLE_PLAY_GAME;
};

var actions = ['auth', 'signOut', 'isSignedIn',
               'submitScore', 'showAllLeaderboards', 'showLeaderboard',
               'unlockAchievement', 'incrementAchievement', 'showAchievements', 'showPlayer'];

actions.forEach(function (action) {
    GooglePlayGame.prototype[action] = function (data, success, failure) {
        var defaultSuccessCallback = function () {
                console.log(GOOGLE_PLAY_GAME + '.' + action + ': executed successfully');
            };

        var defaultFailureCallback = function () {
                console.warn(GOOGLE_PLAY_GAME + '.' + action + ': failed on execution');
            };

        if (typeof data === 'function') {
            // Assume providing successCallback as 1st arg and possibly failureCallback as 2nd arg
            failure = success || defaultFailureCallback;
            success = data;
            data = {};
        } else {
            data = data || {};
            success = success || defaultSuccessCallback;
            failure = failure || defaultFailureCallback;
        }

        exec(success, failure, GOOGLE_PLAY_GAME, action, [data]);
    }
});

module.exports = new GooglePlayGame();