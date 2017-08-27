'use strict';

angular.module('jianboke')
// 导航栏上的账户菜单
.directive('pxgAccountMenu', function(ACCOUNT_MENU, $state, $rootScope) {
    return {
        restrict: 'E',
        templateUrl: 'views/template/account_menu.html',
        replace: true,
        link: function(scope, iElement, iAttr) {

        },
        controller: function($scope) {
            $scope.showMenu = false;
            $scope.accountMenu = ACCOUNT_MENU.list;
            $scope.toggleMenu = function(showMenu) {
                $scope.showMenu = !showMenu;
            };
            $scope.closeMenu = function() {
                $scope.showMenu = false;
            };
            $scope.goState = function(item) {
                $scope.showMenu = false;
                if (item.state === undefined && item.title == 'exit') {
//                    console.log('exit');
                    var title = '确定要退出？',
                        content = '';
                    $rootScope.confirmMessage(title, content).then(function() {
                        $rootScope.logout();
                    }, function() {});
                } else {
                    $state.go(item.state, item.param);
                }
            }
        }
    }
})
.directive('pxgBlogSet', function() {
    return {
        restrict: 'E',
        scope: {
            article: '='
        },
        replace: true,
        templateUrl: 'views/template/blogSet.html',
        controller: 'BlogSetCtrl'
    }
})
.directive('pxgComment', function() {
    return {
        restrict: 'AE',
        scope: {
            article: '=pxgData'
        },
        replace: true,
        templateUrl: 'views/template/comment.html',
        controller: 'CommentCtrl'
    }
})