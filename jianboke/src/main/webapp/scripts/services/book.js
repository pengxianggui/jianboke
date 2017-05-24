'use strict';

angular.module('jianboke')
	.factory('Book', function($resource) {
		return $resource('api/book/:id', {}, {
		    getFirstBookByArticleId: {
		        url: 'api/book/getFirstBookByArticleId/:articleId',
		        method: 'GET'
		    }
		});
	})