'use strict';

angular.module('jianboke')
/*
 * 传入localDateTime,并转换为需要的格式，目前仅支持year-month-date hour:minute:second
 */
 .filter('localDateTimeToStr', function() {
    var fn = function(localDateTime) {
        var year = localDateTime.year;
        var month = localDateTime.monthValue;
        var date = localDateTime.dayOfMonth;
        var hour = localDateTime.hour;
        var minute = localDateTime.minute;
        var second = localDateTime.second;
        var formatStr =  year + '-' + month + '-' + date + ' ' + hour + ':' + minute + ':' + second;
        return formatStr;
    }
    return fn;
});
/**
 * 数据展示转换。视图上将id装换为对应的title或name。如authorId转换为username
 * articleId转换为articleTitle用于呈现
 */
//.filter('exchangeData', function($sce, $rootScope, Account, Book) {
//    var fn = function(id, obj) {
//        switch (obj.name) {
//            case "article":
//                if (obj.targetObj === 'user') {
//                   var username = Account.get().$promise;
//                   return $sce.trustAsHtml(username);
//                } else if (obj.targetObj === 'book') {
//                    return $sce.trustAsHtml("");
//                }
//                break;
//        }
//        return $sce.trustAsHtml("");
//    }
//
//    return fn;
//})