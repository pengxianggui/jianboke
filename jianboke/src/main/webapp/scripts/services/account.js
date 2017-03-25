'use strict';

angular.module('jianboke')
  .factory('Account', function($resource) {
    return $resource('api/account/:id', {}, {
        getUsername: {
            url: 'api/account/:id',
            method: 'GET'
        }
    });
  });
