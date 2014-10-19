var exec = require("cordova/exec");
var GOOGLE_PLAY_GAME = "GooglePlayGame";

var GooglePlayGame = function () {
    this.name = GOOGLE_PLAY_GAME;
};

var actions = ['auth', 'signOut', 
               'submitScore', 'showAllLeaderboards', 'showLeaderboard',
               'unlockAchievement', 'incrementAchievement', 'showAchievements'];

actions.forEach(function (action) {
    GooglePlayGame.prototype[action] = function (data, success, failure) {
        data = data || {};
        success = success || function (message) {
            console.log(GOOGLE_PLAY_GAME + '.' + action + ': executed successfully');
            message && console.log(message);
        };

        failure = failure || function (message) {
            console.warn(GOOGLE_PLAY_GAME + '.' + action + ': failed on execution');
            message && console.warn(message);
        };
        exec(success, failure, GOOGLE_PLAY_GAME, action, [data]);
    }
});

module.exports = new GooglePlayGame();