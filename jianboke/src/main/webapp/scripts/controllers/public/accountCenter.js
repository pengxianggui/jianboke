'use strict';

angular.module('jianboke')
    .controller('AccountCenterCtrl', function($scope, $stateParams, $state, PubAccount, PubArticle,
            PubBook, userEntity, booksEntity, articlesEntity, Account, $rootScope, $mdSidenav) {
        console.log('AccountCenterCtrl');

        console.log(userEntity);
        console.log(booksEntity);
        console.log(articlesEntity);

        $scope.selectItem; // 选中第几个tab
        $scope.user = userEntity.data;
        $scope.books = booksEntity.data;
        $scope.articles = articlesEntity.content;
        $scope.showBottomTip = false; // 显示到底提示
        $scope.showLoadGif = false;
        $scope.query = { // 搜索的条件
            page: 1,
            size: 10
        }
        var initQuery = angular.copy($scope.query);

        // 查关注
        PubAccount.queryAttentions({username: $scope.user.username}).$promise.then(function(resp) {
            console.log(resp);
            $scope.attentions = resp.data;
        });

        // 查粉丝
        PubAccount.queryFans({username: $scope.user.username}).$promise.then(function(resp) {
            console.log(resp);
            $scope.fans = resp.data;
        });

        PubAccount.getNumOfAttentions({id: $scope.user.id}).$promise.then(function(resp) {
            console.log(resp);
            $scope.attentionsNum = resp.data;
        });
        PubAccount.getNumOfFans({id: $scope.user.id}).$promise.then(function(resp) {
           console.log(resp);
           $scope.fansNum = resp.data;
        });
        PubAccount.getNumOfArticles({id: $scope.user.id}).$promise.then(function(resp) {
           console.log(resp);
           $scope.articlesNum = resp.data;
        });
//        $scope.likesNum = PubAccount.getNumOfLikes({id: $scope.user.id}).$promise;

        // 关注 or 取消关注
        $scope.follow = function(param) {
            console.log(param);
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

        $scope.toggleRight = buildToggler('right');
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

        var addArticles = function() {
            if ($scope.query.page >= articlesEntity.totalPages) { // 当前页码大于等于总页数
                $scope.showBottomTip = true;
                $scope.showLoadGif = false;
                console.log('别扯了，到底了...');
                return;
            }
            // 否则，追加
            $scope.query.page++;
            console.log('query...');
            PubArticle.queryAllByUsername({
                page: $scope.query.page - 1,
                size: $scope.query.size,
                username: $stateParams.username
            }).$promise.then(function(resp) {
                console.log(resp.content);
                $scope.articles = $scope.articles.concat(resp.content);
                $scope.showLoadGif = false;
            })
        }

        // todo attentions是一次性加载的，未分页
        var addAttentions = function() { }
        // todo fans是一次性加载的，未分页
        var addFans = function() { }

        // 加载数据
        $scope.loadData = function() {
            if ($scope.showLoadGif) return; // 防止加载中狂拖，导致第一次未加载完，就去请求后面所有页的数据
            $scope.showLoadGif = true;
            switch($scope.selectItem) {
                case 0:
                    addArticles();
                    break;
                case 1:
                    addAttentions();
                    bread;
                case 2:
                    addFans();
                    break;
            }
        }

        $scope.$watch('selectItem', function(newValue, oldValue) {
            console.log('init $scope.query...');
            $scope.query = angular.copy(initQuery);
            $scope.showBottomTip = false;
        })
    });