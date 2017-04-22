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
        }
    });
  });
