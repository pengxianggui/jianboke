'use strict';

angular.module('jianboke')
    .controller('UserSetCtrl', function($scope, $rootScope, $state, Auth, Account) {
        console.log('UserSetCtrl');
        $scope.date = null;
        $scope.time = null;
        $scope.show = function() {
            console.log($scope.date);
            console.log(new Date($scope.time));
        }
    })