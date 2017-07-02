'use strict';

angular.module('jianboke')
	.controller('DashBoardCtrl', function($scope, $timeout, $mdSidenav, $log, $state, $mdDialog, Book, Article, $rootScope) {
		console.log('DashBoardCtrl');
		console.log($state.current.name);
		$scope.state = $state.current.name;
		$scope.setChoseItem = function(param) {
		    $scope.choseItem = param;
		}
		$scope.choseItem = 'blogs';
		if ($scope.state === 'dashboard') {
		    $state.go('dashboard.blogs');
		}

		$scope.toggleLeft = buildDelayedToggler('left');
        function debounce(func, wait, context) {
          var timer;
          return function debounced() {
            var context = $scope,
                args = Array.prototype.slice.call(arguments);
            $timeout.cancel(timer);
            timer = $timeout(function() {
              timer = undefined;
              func.apply(context, args);
            }, wait || 10);
          };
        }
        function buildDelayedToggler(navID) {
          return debounce(function() {
            // Component lookup should always be available since we are not using `ng-if`
            $mdSidenav(navID)
              .toggle()
              .then(function () {
                $log.debug("toggle " + navID + " is done");
              });
          }, 200);
        }

		$scope.books;
	    var getBooks = function() {
	    	Book.query().$promise.then(function(data){
    			$scope.books = data;
	    	});
	    };
	    /**
	     * 弹框添加一本book
	     */
	    $scope.popToAddBook = function(ev) {
	      $mdDialog.show({
	        controller: "BookAddCtrl",
	        templateUrl: 'views/book.add.html',
	        parent: angular.element(document.body),
	        targetEvent: ev,
	        locals: {
	        	Entity:  {
	        		bookName: null,
	        		bookIntro: null,
	        		authorId: null,
	        		bookCoverPath: null,
	        	}
	        },
	        clickOutsideToClose: false,
	      });
	    }
	    getBooks();
	})
	.controller('BlogListCtrl', function($scope, $timeout, $state, Article, Book, $mdSidenav, $log, $mdDialog, $rootScope) {
	    console.log('BlogListCtrl');

	    var pageSize = 10;
        $scope.query;
        $scope.articles = [];
        $scope.end = false; // 文章显示是否到底了
        $scope.goToWrite = function() {
            $state.go('blog.newblog');
        }
        // 删除文章
        $scope.remove = function(id) {
            $rootScope.confirmMessage("提示:", "删除文章会一并删除该文章的归档记录。确定要这样做吗？", true, "确定", "取消")
              .then(function() {
                Article.delete({id: id}).$promise.then(function(data) {
                    $rootScope.popMessage("删除成功！", true);
                    // 重新从第一行开始查询
                    $scope.articles = [];
                    initQueryPage($scope.query.findBy, null, null, $scope.query.filter);
                    getAllArticle($scope.query); // 重新获取articles
                }).catch(function(httpResponse) {
                    $rootScope.popMessage("删除失败！", false);
                });
            }, function() {});
        }

        // 更新文章权限
        $scope.updateArticleSet = function(article) {
            $mdDialog.show({
                controller: function($scope, $mdDialog) {
                    $scope.article = article;
                    $scope.cancel = function() {
                        $mdDialog.cancel();
                    }
                },
                template: '<pxg-blog-set article="article"></pxg-blog-set>',
//                templateUrl: 'views/blog.set.html',
                parent: angular.element(document.body),
                clickOutsideToClose: true
            });
        }

        var getBooks = function() {
            Book.query().$promise.then(function(data){
                $scope.books = data;
            });
        };

        // 追加式获取所有article,分页，排序，分组查询
        var getAllArticle = function(criteria) {
            Article.get(criteria, function(result, responseHeader) {
                if (result.result == 'success') {
                    if (result.data.length > 0)
                        $scope.articles.push.apply($scope.articles, result.data);
                        console.log($scope.articles);
                    if (result.data.length < pageSize)
                        $scope.end = true;
                } else {
                    $rootScope.popMessage("加载失败", false);
                }
            }).$promise;
        };

        // 加载更多...
        $scope.getMoreArticle = function() {
            initQueryPage($scope.query.findBy, ++$scope.query.page, $scope.query.initSize, $scope.query.filter); // 通过initPage++实现加载下一页
            getAllArticle($scope.query);
        }

        $scope.tiles = [
             {'icon': '', 'title': 'title1', 'background': 'md-bg-green'},
             {'icon': '', 'title': 'title2', 'background': 'md-bg-gray'},
             {'icon': '', 'title': 'title3', 'background': 'md-bg-purple'},
             {'icon': '', 'title': 'title4', 'background': 'md-bg-blue'},
             {'icon': '', 'title': 'title5', 'background': 'md-bg-black'},
             {'icon': '', 'title': 'title6', 'background': 'md-bg-green'}
        ];

        // 刷新articleList
        $scope.refreshArticleList = function() {
            $scope.articles = [];
            $scope.end = false;
            initQueryPage($scope.query.findBy, null, null, $scope.query.filter);
            getAllArticle($scope.query);
        }

        // 清除过滤字符
        $scope.clearFilter = function() {
            $scope.query.filter = '';
            $scope.refreshArticleList();
        }

        var initialize = function() { // 初始化dashboard.html所需的数据
            initQueryPage(); //初始化
            getBooks();
            getAllArticle($scope.query);
        }
        // 初始化查询: 不传入值，则为初始值
        var initQueryPage = function(findBy, page, size, filter) {
            $scope.query = {
                findBy: findBy?findBy:'all', // 默认所有
                page: page?page:1, // 默认查询第一页
                size: size?size:pageSize, // 默认每次查询的数量
                filter: filter?filter:''
            };
        }
        initialize();
	})
	.controller('AttentionCtrl', function($scope, $timeout, $state, $mdSidenav, $log, $mdDialog){
	    console.log('AttentionCtrl');
	    var initialize = function() { // 初始化dashboard.html所需的数据
            console.log($scope.query);
            initQueryPage(); //初始化
//            getBooks();
//            getAllArticle($scope.query);
        }
        // 初始化查询: 不传入值，则为初始值
        var initQueryPage = function(findBy, page, size, filter) {
            $scope.query = {
                findBy: findBy?findBy:'0', // 默认所有
                page: page?page:1, // 默认查询第一页
                size: size?size:pageSize, // 默认每次查询的数量
                filter: filter?filter:''
            };
            console.log($scope.query);
        }
        initialize();
	})
	.controller('CollectionCtrl', function($scope, $timeout, $state, $mdSidenav, $log, $mdDialog){
	    console.log('CollectionCtrl');
	})
	.controller('MessageCtrl', function($scope, $timeout, $state, $mdSidenav, $log, $mdDialog){
	    console.log('MessageCtrl');
	})
