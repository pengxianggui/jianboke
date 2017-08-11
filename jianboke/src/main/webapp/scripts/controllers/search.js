'use strict';

angular.module('jianboke')
    .controller('SearchCtrl', function($scope, $rootScope, $state, articles, users, books, $stateParams, Article, Account) {
        console.log('SearchCtrl');
        $scope.queryUser = { // 搜索的条件
            keyWord: $stateParams.keyWord,
            page: 1,
            size: 10
        }
        $scope.queryArticle = angular.copy($scope.queryUser);
        console.log(articles);
        $scope.articles = articles;
        $scope.users = users;
        console.log($scope.users);
        $scope.books = books; // 不加入

        $scope.active = 'article'; // article(default)、user、book
        $scope.showItem = function (param) {
            $scope.active = param;
        }
        // articles翻页
        $scope.articlePageRefresh = function() {
            $scope.articlePromise = Article.queryAll({
                page: $scope.queryArticle.page - 1,
                size: $scope.queryArticle.size,
                keyWord: $stateParams.keyWord
            }, function(data, responseHeaders) {
                $scope.articles = data;
            }).$promise;
        }

        // users 翻页
        $scope.userPageRefresh = function() {
            $scope.userPromise = Account.queryAll({
                page: $scope.queryUser.page - 1,
                size: $scope.queryUser.size,
                keyWord: $stateParams.keyWord
            }, function(data, responseHeaders) {
                $scope.users = data;
                console.log($scope.users);
            }).$promise;
        }

        // 关注 or 取消关注
        $scope.follow = function(param) {
            console.log(param);
            Account.follow({userId: param.id}).$promise.then(function(resp) {
                if (resp.code == '0000') {
                    $rootScope.popMessage('操作成功', true);
                    param.attention = !param.attention;
                }
            }, function() {
                $rootScope.popMessage('操作失败, 请稍后重试', false);
            });
        }

        // 点赞
        $scope.praise = function() {

        }
     });