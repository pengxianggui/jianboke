'use strict';

angular.module('jianboke')
	.controller('BlogCtrl', function($scope, $rootScope) {

	})
	.controller('NewBlogCtrl', function($scope, $element, $mdConstant, entity, Book, $mdDialog, Article, $rootScope, $state) {
      		console.log('NewBlogCtrl');
            $scope.article = entity;
      		if ($state.params.id != 'new') {
      		    if ($scope.article == null || $scope.article.labels == null) {
      		        $scope.labels = [];
      		    } else {
                  $scope.labels = $scope.article.labels.split(',');
      		    }
      		} else {
      		    $scope.labels = [];
      		}
      		$scope.keys = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA];
      		// The md-select directive eats keydown events for some quick select
      		// logic. Since we have a search input here, we don't need that logic.
      		$element.find('input').on('keydown', function(ev) {
      			ev.stopPropagation();
      		});
      		$scope.saveArticle = function() {
      			if ($scope.article.content == null || $scope.article.content == '') {
      				$rootScope.tipMessage('正文内容不能为空!');
      				return;
      			}
      			if (Array.isArray($scope.labels)) {
      			    $scope.article.labels = $scope.labels.join(','); // 处理labels
      			} else {
      			    $scope.article.labels = $scope.labels;
      			}
      			console.log($scope.article);
      			Article.save($scope.article).$promise.then(function(resp) {
                      // 保存成功后提示用户去选择要归入Book的章节，跳转页面去进行操作。如果用户没有选择归类书籍，则直接在点击保存的时候提示用户即可。
                      if ($state.params.id != 'new') { //编辑
                        $state.go('blog.readBlog', {id: resp.data.id});
                      } else { // 新建
                        $rootScope.confirmMessage('温馨提示: ', '保存成功！是否去归档此文章？', false, '是', '否', null)
                          .then(function() { // 去归档
                            console.log('是');
                            console.log(resp);
                            $state.go('blog.addToBook', {id: resp.data.id});
                          }, function() { // 不归档，查看
                            console.log('否');
                            $state.go('blog.readBlog', {id: resp.data.id});
                          });
                      }
                  });
      		}
      	})
    .controller('ReadBlogCtrl', function($rootScope, $scope, entity, Article, Book, Account, $mdSidenav) {
        console.log('ReadBlogCtrl');
        $scope.shareOpen = false; // 分享
        $scope.toggleRight = buildToggler('right');
        $scope.isOpenRight = function(){
            return $mdSidenav('right').isOpen();
        };

        $scope.article = entity;
        console.log(entity);
        $scope.authorNameArr = [];
        var getAuthorName = function(article) {
            $scope.authorNameArr.push($rootScope.account.username);
            if (article.secondAuthorId) {
                Account.getUsername({id: article.secondAuthorId}).$promise.then(function(result) {
                    $scope.authorNameArr.push(result.data);
                }).catch(function(httpResponse){});
            }
        }
        getAuthorName($scope.article);

        function buildToggler(navID) {
          return function() {
            $mdSidenav(navID).toggle().then(function() {
                console.log('toggle over');
            });
          };
        }
    })
    .controller('BlogSetCtrl', function ($scope, $rootScope, Article, $state) {
        console.log('BlogSetCtrl');
        console.log($scope.article);
        $scope.saveArticle = function() {
            Article.update($scope.article).$promise.then(function(resp) {
                if (resp.code === '0000') {
                    $rootScope.popMessage('设置成功', true);
                } else {
                    $rootScope.popMessage(resp.data, false);
                }
            });
        };

        $scope.$watch('article.ifPublic', function(newValue, oldValue) {
            if (!newValue) {
                $scope.article.ifAllowReprint = false;
                $scope.article.ifAllowComment = false;
                $scope.article.ifAllowSecondAuthor = false;
            }
        });

        $scope.watchIfPublic = function() {
            if (!$scope.article) return;
            if (!$scope.article.ifPublic) {
                $scope.article.ifAllowReprint = false;
                $scope.article.ifAllowComment = false;
                $scope.article.ifAllowSecondAuthor = false;
            }
        }
    });