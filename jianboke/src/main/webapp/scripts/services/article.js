'use strict';

angular.module('jianboke')
	.factory('Article', function($resource) {
		return $resource('api/article', {}, {
			query: {
				url: 'api/article/:filter',
				method: 'GET',
				isArray: true
			},
			save: {
				url: 'api/article',
				method: 'POST'
			}
		})
	})