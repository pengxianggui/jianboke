'use strict';

angular.module('jianboke')
    .controller('PubReadBlogCtrl', function($scope, $rootScope, $state, entity, PubAccount, Comment, Reply, PubComment) {
    console.log('PubReadBlogCtrl');

    $scope.article;
    $scope.comment;
    $scope.authorNameArr = [];

    $scope.query = { // 搜索的条件
        page: 1,
        size: 10,
        articleId: null,
        orderBy: null,
        fromUid: null
    }

    // 获取文章所有的作者
    var getAuthorName = function(article) {
        PubAccount.getAuthorNameByArticleId({id: article.id}).$promise.then(function(result) {
            console.log(result);
            $scope.authorNameArr.push(result);
        }).catch(function(httpResponse){});
    }

    // 获取文章所有的评论, 包含回复
    var getComments = function(article) {
//        PubComment.query({articleId: article.id}).$promise.then(function(data) {
//           $scope.comments = data;
//           console.log($scope.comments);
//        });
        // 分页获取，实现排序过滤等功能
        PubComment.pageQuery({
            page: $scope.query.page - 1,
            size: $scope.query.size,
            articleId: article.id,
            orderBy: null,
            fromUid: null
        }).$promise.then(function(data) {
            console.log(data);
            $scope.commentsPage = data;
        });
    }

    // 评论翻页
    $scope.pageRefresh = function() {
        console.log("page:");
        console.log($scope.query.page);
        getComments($scope.article);
    }

    // 初始化评论表单
    var initComment = function(article) {
        var newComment = {
            id: null,
            articleId: article.id,
            fromUserId: null,
            content: ''
        }
        $scope.comment = angular.copy(newComment);
    }

    if (entity.code == '4001') { // 无权限
        $state.go('noAuth');
    } else {
        $scope.article = entity.data;
        console.log($scope.article);
        getAuthorName($scope.article);
        getComments($scope.article);
        initComment($scope.article);
    }

    // 提交文章评论
    $scope.saveComment = function(ev) {
        console.log($scope.comment);
        Comment.save($scope.comment).$promise.then(function(data) {
            console.log('保存结果');
            console.log(data);
            initComment($scope.article);
            $scope.commentsPage.content.unshift(data);
        });
    }

    // 提交评论回复
    $scope.saveReply = function(ev) {
        console.log(ev);
    }

})