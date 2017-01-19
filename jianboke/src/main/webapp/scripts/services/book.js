'use strict';

angular.module('jianbo')
	.factory('Book', function($resource) {
		return $resource('api/book', {}, {});
	})