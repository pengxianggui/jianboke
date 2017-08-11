'use strict';

angular.module('jianboke')
  .factory('PubAccount', function($resource) {
    return $resource('pub/account/:id', {}, {
        queryAll: {
            url: 'pub/account/queryAll',
            method: 'GET'
        },
        getByUsername : {
            url: 'pub/account/:username',
            method: 'GET'
        },
        getAuthorNameByArticleId: {
            url: 'pub/account/getAuthorNameByArticleId/:id',
            method: 'GET',
            isArray: true
        },
        queryAttentions: {
            url: 'pub/account/attentions/:username',
            method: 'GET'
        },
        queryFans: {
            url: 'pub/account/fans/:username',
            method: 'GET'
        },
        getNumOfAttentions: {
            url: 'pub/account/getNumOfAttentions/:id',
            method: 'GET'
        },
        getNumOfFans: {
            url: 'pub/account/getNumOfFans/:id',
            method: 'GET'
        },
        getNumOfArticles: {
            url: 'pub/account/getNumOfArticles/:id',
            method: 'GET'
        }

    });
  });
