'use strict';

angular.module('jianboke')
	.controller('DashBoardCtrl', function($scope, $timeout, $mdSidenav, $log, $state, $mdDialog, Book, Article, $rootScope) {
//		console.log('DashBoardCtrl');
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
	    $scope.popToAddBook = function(ev, book) {
	      $mdDialog.show({
	        controller: "BookAddCtrl",
	        templateUrl: 'views/book.add.html',
	        parent: angular.element(document.body),
	        targetEvent: ev,
	        locals: {
	        	Entity:  book ? book : {
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
	.controller('BlogListCtrl', function($scope, $timeout, $state, Article, Book, $mdSidenav, $log, $mdDialog, $rootScope, books) {
	    var bookmark; // 书签：保留页码

	    $scope.selected = [];
	    $scope.query = { // 搜索的条件
            filter: '',
            bookId: '0',
//            order: null,
            page: 1,
            size: 10
        }
        $scope.books = books;

        $scope.advancedSearchFlag = false; // 高级搜索区域默认隐藏
        $scope.advancedSearchToggle = function(flag) {
          $scope.advancedSearchFlag = !flag;
          $scope.query.advancedSearchIfPublic = null;
          $scope.query.advancedSearchIfAllowReprint = null;
          $scope.query.advancedSearchIfAllowComment = null;
          $scope.pageRefresh();
        }

        // 查询条件变更时触发搜索
        $scope.onChange = function() {
//          var sort = $scope.query.order;
//          if (sort && sort.length > 0) {
//            if (sort.charAt(0) === '-') {
//              sort = sort.substring(1) + ',desc'; //降序
//            } else {
//              sort = sort + ',asc'; // 升序
//            }
//          }
          return Article.get({
            page: $scope.query.page - 1,
            size: $scope.query.size,
            filter: $scope.query.filter,
            bookId: $scope.query.bookId
//            sort: sort
          }, function(value, responseHeaders) {
            $scope.data = value;
//            console.log($scope.data.content);
          }).$promise;
        }

        // 高级搜索
        $scope.advancedSearchSave = function(e) {
          $rootScope.showLoading();
          $scope.selected = [];
//          var sort = $scope.query.order;
//          if (sort && sort.length > 0) {
//            if (sort.charAt(0) === '-') {
//              sort = sort.substring(1) + ',desc';
//            } else {
//              sort = sort + ',asc';
//            }
//          }
          var params = {
              page: $scope.query.page - 1,
              size: $scope.query.size,
              bookId: $scope.query.bookId,
//              sort:sort,
              ifPublic: $scope.query.advancedSearchIfPublic,
              ifAllowReprint:$scope.query.advancedSearchIfAllowReprint,
              ifAllowComment:$scope.query.advancedSearchIfAllowComment
          };
          return Article.get(params, function(value, responseHeaders) {
            $scope.data = value;
            $rootScope.hideLoading();
          }).$promise;
        }

        // 高级搜索条件清除
        $scope.cleanAdvancedSearch = function() { //清除高级搜索条件
          $scope.query.advancedSearchIfPublic = null;
          $scope.query.advancedSearchIfAllowReprint = null;
          $scope.query.advancedSearchIfAllowComment = null;
          $scope.promise = $scope.onChange();
        }

        // 刷新数据
        $scope.refresh = function(event) {
          $scope.advancedSearchFlag = false;
          $scope.cleanAdvancedSearch();
        }

        // 翻页
        $scope.pageRefresh = function() {
          if ($scope.advancedSearchFlag == false) {//收起，调取所有数据
            $scope.promise = $scope.onChange();
          } else {//未收起，根据条件查询
            $scope.promise = $scope.advancedSearchSave();
          }
        }

        $scope.$watch('query.filter', function(newValue, oldValue) { //快速搜索过滤
          if (!oldValue) bookmark = $scope.query.page;
          if (newValue !== oldValue) $scope.query.page = 1;
          if (!newValue) $scope.query.page = bookmark;
          $scope.refresh();
        });

        // 清除过滤字符
        $scope.clearFilter = function() {
            $scope.query.filter = '';
            $scope.refresh();
        }

        // 删除文章
        $scope.remove = function(id) {
            $rootScope.confirmMessage("提示:", "删除文章会一并删除该文章的归档记录。确定要这样做吗？", true, "确定", "取消")
              .then(function() {
                Article.delete({id: id}).$promise.then(function(data) {
                    $rootScope.popMessage("删除成功！", true);
                    $scope.refresh();
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
                // templateUrl: 'views/blog.set.html',
                parent: angular.element(document.body),
                clickOutsideToClose: true
            }).then(function(){},function() {
                $scope.pageRefresh();
            });
        }

        // todo 归档变更
        $scope.addToBook = function(id) {
            $state.go('addToBook', {id: id});
        }

        // 批量删除文章
        $scope.deleteBatch = function() {
            $rootScope.confirmMessage("提示:", "删除这些文章会一并删除他们的归档记录。确定要这样做吗？", true, "确定", "取消")
            .then(function() {
                var ids = $scope.selected.map(function(item) {return item.id}).join(',');
                Article.deleteBatch({ids: ids}).$promise.then(function(data) {
                  $rootScope.popMessage("删除成功！", true);
                  $scope.refresh();
                  $scope.selected = [];
                }).catch(function(httpResponse) {
                  $rootScope.popMessage("删除失败！", false);
                });
            }, function() {});
        }

        // todo 批量设置文章的权限： 暂时弃用，改为在树中的组上操作
        $scope.updateArticleSetBatch = function() {

        }

        // todo 批量归档文章
        $scope.addToBookBatch = function() {

        }
	})
	.controller('AttentionCtrl', function($scope, $timeout, $state, $mdSidenav, $log, $mdDialog, Account, $rootScope){
	    var bookmark;
//	    console.log('AttentionCtrl');
        $scope.query = {
            findBy: 'attentions'
        }
        // 查询条件变更时触发搜索
        $scope.onChange = function() {
          return Account.query({
            findBy: $scope.query.findBy
          }, function(resp, responseHeaders) {
            $scope.users = resp.data;
//            console.log($scope.users);
          }).$promise;
        }

        // 刷新数据
        $scope.refresh = function(event) {
          $scope.promise = $scope.onChange();
        }
        $scope.onChange();

        // 关注 or 取消关注
        $scope.follow = function(param) {
//            console.log(param);
            Account.follow({userId: param.id}).$promise.then(function(resp) {
                if (resp.code == '0000') {
                    $rootScope.popMessage('操作成功', true);
                    param.attention = !param.attention;
                }
                $scope.onChange();
            }, function() {
                $rootScope.popMessage('操作失败, 请稍后重试', false);
            });
        }
	})
	.controller('CollectionCtrl', function($scope, $timeout, $state, $mdSidenav, $log, $mdDialog){
//	    console.log('CollectionCtrl');
	})
	.controller('MessageCtrl', function($scope, $timeout, $state, $mdSidenav, $log, $mdDialog){
//	    console.log('MessageCtrl');
	})
