'use strict';

angular.module('jianboke')
	.controller('BookAddCtrl', function($scope, $mdDialog, Upload, $timeout, Entity, Book, $rootScope, $state) {
		console.log('BookAddCtrl');
		$scope.book = Entity;
		console.log($scope.book);
		$scope.cancel = function() {
			$mdDialog.cancel();
		}
		
		$scope.selectImage = function(file) {
			console.log(file);
			$scope.picFile = file;
		}
		
		var uploadPic = function () {
	        return Upload.upload({
	        	url: 'api/book/cover',
	        	data: {file: $scope.picFile}
	        })
	    }
		
		$scope.saveBook = function() {
			var saveBookAction = function(book) {
				// 调用Book服务的save方法
				console.log(book);
				Book.save(book).$promise.then(function(resp){
					// book 保存成功
					$rootScope.popMessage('保存成功', true);
					$scope.cancel();
					$state.reload();
				}, function(resp){
					// book 保存失败
					$rootScope.popMessage('保存失败', false);
				});
			}
			if ($scope.picFile && $scope.picFile.size > 0) { // 存在$scope.coverPic
				console.log($scope.picFile);
				uploadPic().then(function(resp) {
					console.log('上传成功');
					$scope.book.bookCoverPath = resp.data.path;
					saveBookAction($scope.book);
				}, function(resp) {
					console.log('上传失败');
					saveBookAction($scope.book);
				});
			} else {
				saveBookAction($scope.book);
			}
		}
	})