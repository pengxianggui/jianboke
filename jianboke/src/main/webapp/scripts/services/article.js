'use strict';

angular.module('jianboke')
	.factory('Article', function($resource) {
		return $resource('api/article/:id', {}, {
			query: {
				url: 'api/articles/:filter',
				method: 'GET',
				isArray: true
			},
			save: {
				url: 'api/article',
				method: 'POST'
			}
		})
	})