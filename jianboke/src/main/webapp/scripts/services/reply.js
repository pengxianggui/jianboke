'use strict';

angular.module('jianboke')
	.factory('Reply', function($resource) {
		return $resource('api/reply/:id', {}, {

		});
	})