'use strict';

angular.module('jianbo')
	.factory('Register', function($resource) {
		return $resource('api/register', {}, {});
	})