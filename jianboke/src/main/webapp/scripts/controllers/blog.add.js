'use strict';

angular.module('jianboke')
	.controller('NewBlogCtrl', function($scope, $element, $mdConstant, Book, $mdDialog, Article, $rootScope, IntegralUITreeViewService) {
		console.log('NewBlogCtrl');
		$scope.ifOpenSet = false;
		$scope.article = {
				id: null,
				title: '',
				content: '',
				labels: '',
				authorId: null,
				bookId: null
		}
		$scope.labels = [];
		$scope.keys = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA];
		// The md-select directive eats keydown events for some quick select
		// logic. Since we have a search input here, we don't need that logic.
		$element.find('input').on('keydown', function(ev) {
			ev.stopPropagation();
		});
		$scope.toggleSettings = function(ifOpenSet) {
			$scope.ifOpenSet = !ifOpenSet;
		}
		$scope.saveArticle = function() {
			console.log($scope.article.content);
			if ($scope.article.content == null || $scope.article.content == '') {
				$rootScope.tipMessage('正文内容不能为空!');
				return;
			}
			$scope.article.labels = $scope.labels.join(','); // 处理labels
			console.log($scope.article.labels);
			if ($scope.selectedBooks == null || $scope.selectedBooks.length == 0) {
				
			}
			return;
			Article.save($scope.article).$promise.then(function(resp) {
				// 保存成功后提示用户去选择要归入Book的章节，跳转页面去进行操作。如果用户没有选择归类书籍，则直接在点击保存的时候提示用户即可。
				console.log(resp);
			});
		}
		
		
		
		
	})