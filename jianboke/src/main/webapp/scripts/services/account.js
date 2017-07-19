'use strict';

angular.module('jianboke')
  .factory('Account', function($resource) {
    return $resource('api/account/:id', {}, {
        getUsername: {
            url: 'api/account/:id',
            method: 'GET'
        },
        usernameUniqueValid: {
            url: 'api/account/usernameUniqueValid',
            method: 'POST'
        },
        emailUniqueValid: {
            url: 'api/account/emailUniqueValid',
            method: 'POST'
        },
        sendEmailValidCode: {
            url: 'api/account/sendEmailValidCode',
            method: 'POST'
        },
        register: {
            url: 'api/account/register',
            method: 'POST'
        },
        updateUsername: {
            url: 'api/account/updateUsername/:username',
            method: 'GET'
        },
        isShowDarkTheme: {
           url: 'api/account/getShowDarkTheme',
           method: 'GET'
        },
        saveShowDarkTheme: {
            url: 'api/account/saveShowDarkTheme/:showDarkTheme',
            method: 'GET'
        }
    });
  });
