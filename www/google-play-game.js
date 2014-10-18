var exec = require("cordova/exec");

var GooglePlayGame = function () {
    this.name = "GooglePlayGame";
};

GooglePlayGame.prototype.auth = function (success, failure) {
    exec(success, failure, "GooglePlayGame", "auth", []);
};

GooglePlayGame.prototype.signOut = function (success, failure) {
    exec(success, failure, "GooglePlayGame", "signOut", []);
};

GooglePlayGame.prototype.getPlayerImage = function (success, failure) {
    exec(success, failure, "GooglePlayGame", "getPlayerImage", []);
};

GooglePlayGame.prototype.submitScore = function (success, failure, data) {
    exec(success, failure, "GooglePlayGame", "submitScore", [data]);
};

GooglePlayGame.prototype.showLeaderboard = function (success, failure, data) {
    exec(success, failure, "GooglePlayGame", "showLeaderboard", [data]);
};

GooglePlayGame.prototype.reportAchievement = function (success, failure, data) {
    exec(success, failure, "GooglePlayGame", "reportAchievement", [data]);
};
               
GooglePlayGame.prototype.resetAchievements = function (success, failure) {
    exec(success, failure, "GooglePlayGame", "resetAchievements", []);
};

GooglePlayGame.prototype.showAchievements = function (success, failure, data) {
    exec(success, failure, "GooglePlayGame", "showAchievements", []);
};

module.exports = new GooglePlayGame();