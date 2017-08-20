'use strict';

angular.module('jianboke')
	.factory('Comment', function($resource) {
		return $resource('api/comment/:id', {}, {

		});
	})