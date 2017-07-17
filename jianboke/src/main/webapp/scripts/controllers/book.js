'use strict';

angular.module('jianboke')
	.controller('AddToBookCtrl', function($scope, entity, IntegralUITreeViewService) { // 文章归档
	    console.log('AddToBookCtrl');
	    $scope.type = 'EDIT';
	    $scope.article = entity;
	    console.log(entity);
	    $scope.book = entity.books[0];
	    $scope.articleId = entity.id;
	})
	.controller('BookAddCtrl', function($scope, $mdDialog, Upload, $timeout, Entity, Book, $rootScope, $state) { // 添加一本书
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
	.controller('BookCtrl', function($scope, entity, Book, $timeout, $mdSidenav, $log, $state, $stateParams, $location, Chapter, Article, $mdMedia) {
	    $scope.type = 'READ'; // markdown指令模式
	    var dataType; // markdown解析的是chapter还是article
	    $scope.book = entity.data;
	    console.log('BookCtrl');
        $scope.toggleLeft = buildToggler('left');
        $scope.toggleRight = buildToggler('right');
        $scope.treeName = 'bookTree';
        $scope.nodes = [];
        $scope.articleId;
        $scope.toggleValue = $mdMedia('gt-md'); // boolean
        $scope.currentchapOrArt = entity.data; //当前选中的chapter或article, 默认是当前书本

        $scope.isOpenLeft = function(){
          return $mdSidenav('left').isOpen();
        };
        if ($state.params.type === 'article' && $state.params.resourceId) {
            $scope.articleId = $state.params.resourceId;
//            Book.getFirstBookByArticleId({articleId: $scope.articleId}).$promise.then(function(value) {
//                console.log(value);
//                $scope.book = value;
//            });
        }
        $scope.treeEvents = {
          itemClick: function (e) {
            return $scope.onItemClick(e);
          }
        };

        // 单击树list时
        $scope.onItemClick = function(e) {
            if (!e.item) {
                return;
            }
            var resourceId;
            if (e.item.isArticle) {
                dataType = 'article';
                $scope.articleId = e.item.id.split('-')[0];
                $scope.currentchapOrArt = Article.get({id: $scope.articleId});
            } else {
                dataType = 'chapter';
                $scope.currentchapOrArt = Chapter.get({id: e.item.id.split('-')[0]});;
            }
            resourceId = e.item.id.split('-')[0];
            $state.go('book.content', {type: dataType, resourceId: resourceId});
        }

        function buildToggler(navID) {
          return function() {
            if (navID == 'left') {
                $scope.toggleValue = !$scope.toggleValue;
            }
            $mdSidenav(navID).toggle().then(function() {
                console.log('toggle over');
            });
          };
        }

//        var showContent = function() {
//            console.log('$state.params.type: ' + $state.params.type);
//            console.log('$state.params.resourceId: ' + $state.params.resourceId);
//            $state.go('book.content', {
//                type: $state.params.type?$state.params.type:'chapter',
//                resourceId: $state.params.resourceId?$state.params.resourceId:$scope.book.id
//            });
//        }
//        $timeout(function() {
//            showContent();
//        }, 1000);
      })
    .controller('LeftCtrl', function ($scope, $timeout, $mdSidenav, $log) {
        $scope.close = function () {
          // Component lookup should always be available since we are not using `ng-if`
          $mdSidenav('left').close()
            .then(function () {
              $log.debug("close LEFT is done");
            });
        };
    })
    .controller('RightCtrl', function ($scope, $timeout, $mdSidenav, $log) {
        $scope.close = function () {
          // Component lookup should always be available since we are not using `ng-if`
          $mdSidenav('right').close()
            .then(function () {
              $log.debug("close RIGHT is done");
            });
        };
    })
    .controller('BookContentCtrl', function ($scope, entity, $stateParams, $state) {
        console.log('BookContentCtrl');
        $scope.datType = $stateParams.type;
        if ($scope.datType === 'chapter') {
            $scope.content = entity.description;
        } else {
            $scope.content = entity.content;
        }
    })