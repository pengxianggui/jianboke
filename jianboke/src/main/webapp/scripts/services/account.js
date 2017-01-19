'use strict';

angular.module('jianbo')
  .factory('Account', function($resource) {
    return $resource('api/account', {}, {});
  });
