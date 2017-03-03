'use strict';

angular.module('jianboke')
	.controller('ArticleCtrl', function($scope) {
		console.log('ArticleCtrl');
	})
	.controller('AddToBookCtrl', function($scope, entity) {
	    console.log('AddToBookCtrl');
	    $scope.article = entity;
	});