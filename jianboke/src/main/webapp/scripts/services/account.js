'use strict';

angular.module('jianboke')
  .factory('Account', function($resource) {
    return $resource('api/account', {}, {});
  });
