'use strict';

angular.module('jianboke')
  .factory('PubBook', function($resource) {
    return $resource('pub/book/:id', {}, {
        queryAll: {
            url: 'pub/book/queryAll',
            method: 'GET'
        },
        queryAllByUsername: {
            url: 'pub/book/queryAllByUsername/:username',
            method: 'GET'
        }
    });
  });
