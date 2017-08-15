'use strict';

angular.module('jianboke')
    .controller('AccountCenterCtrl', function($scope, $stateParams, $state, PubAccount, PubArticle,
            PubBook, userEntity, booksEntity, articlesEntity, Account, $rootScope, $mdSidenav) {
        console.log('AccountCenterCtrl');

        console.log(userEntity);
        console.log(booksEntity);
        console.log(articlesEntity);

        $scope.user = userEntity.data;
        $scope.books = booksEntity.data;
        $scope.articles = articlesEntity.content;

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
    });