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
})