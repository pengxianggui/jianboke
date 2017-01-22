'use strict';

angular.module('jianboke')
	.controller('BlogCtrl', function($scope, $rootScope) {
		console.log('BlogCtrl');
		console.log($scope);
		$scope.shareOpen = false;
		
		$scope.openSetting = function() {
//			console.log($scope.$parent); //调用上层scope
			console.log($scope.$$childTail); // 调用下层scope
			$scope.$$childTail.toggleSettings($scope.$$childTail.ifOpenSet);
		}
	});