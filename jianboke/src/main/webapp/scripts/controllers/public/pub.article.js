'use strict';

angular.module('jianboke')
    .controller('PubReadBlogCtrl', function($scope, $rootScope, $state, entity, PubAccount, Article, Comment, Reply, PubComment, PubArticle) {
    console.log('PubReadBlogCtrl');

    $scope.article;
    $scope.authorNameArr = [];

    // 获取文章所有的作者
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