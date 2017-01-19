'use strict';

angular.module('jianbo')
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