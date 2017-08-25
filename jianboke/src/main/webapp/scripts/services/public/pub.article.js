'use strict';

angular.module('jianboke')
	.factory('PubArticle', function($resource) {
		return $resource('pub/article/:id', {}, {
            queryAll: {
                url: 'pub/article/queryAll',
                method: 'GET'
            },
            queryAllByUsername: {
                url: 'pub/article/queryAllByUsername/:username',
                method: 'GET'
            },
            queryAllLikesByArticleId: {
                url: 'pub/article/queryAllLikesByArticleId/:articleId',
                method: 'GET',
                isArray: true
            }
		})
	})