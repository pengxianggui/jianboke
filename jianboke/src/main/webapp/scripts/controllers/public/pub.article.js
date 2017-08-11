'use strict';

angular.module('jianboke')
    .controller('PubReadBlogCtrl', function($scope, $state, entity, PubAccount) {
        console.log('PubReadBlogCtrl');

        $scope.article;
        $scope.authorNameArr = [];
        var getAuthorName = function(article) {
            PubAccount.getAuthorNameByArticleId({id: article.id}).$promise.then(function(result) {
                console.log(result);
                $scope.authorNameArr.push(result);
            }).catch(function(httpResponse){});
        }

        if (entity.code == '4001') { // 无权限
            $state.go('noAuth');
        } else {
            $scope.article = entity.data;
            getAuthorName($scope.article);
        }
    })