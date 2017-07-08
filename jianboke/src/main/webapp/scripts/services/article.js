'use strict';

angular.module('jianboke')
	.factory('Article', function($resource) {
		return $resource('api/article/:id', {}, {
			save: {
				url: 'api/article',
				method: 'POST'
			},
			update: {
			    url: 'api/article',
			    method: 'PUT'
			}
		})
	})