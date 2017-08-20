'use strict';

angular.module('jianboke')
  .factory('PubComment', function($resource) {
    return $resource('pub/comment/:id', {}, {
        query: {
            url: 'pub/comment/query/:articleId',
            method: 'GET',
            isArray: true
        },
        pageQuery: {
            url: 'pub/comment/pageQuery',
            method: 'GET'
        }
    });
  });
