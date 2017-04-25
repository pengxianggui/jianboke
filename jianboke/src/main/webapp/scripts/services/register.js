'use strict';

angular.module('jianboke')
	.factory('Register', function($resource) {
		return $resource('api/account/register', {}, {});
	})