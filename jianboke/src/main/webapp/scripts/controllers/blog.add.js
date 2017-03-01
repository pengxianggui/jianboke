'use strict';

angular.module('jianboke')
	.controller('NewBlogCtrl', function($scope, $element, $mdConstant, entity, Book, $mdDialog, Article, $rootScope, $state) {
		console.log('NewBlogCtrl');
		$scope.ifOpenSet = false;
		$scope.article = entity;
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
			Article.save($scope.article).$promise.then(function(resp) {
                // 保存成功后提示用户去选择要归入Book的章节，跳转页面去进行操作。如果用户没有选择归类书籍，则直接在点击保存的时候提示用户即可。
                $rootScope.confirmMessage('温馨提示: ','是否去归档此文章？', false, '去归档', '算了吧', null)
                    .then(function() { // 去归档
                        $state.go('blog.addToBook', {id: resp.id});
                    }, function() { // 不归档，回到首页
                        $state.go('dashboard');
                    });
            });
		}
	})