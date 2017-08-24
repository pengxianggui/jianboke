'use strict';

angular.module('jianboke')
    .controller('PubReadBlogCtrl', function($scope, $rootScope, $state, entity, PubAccount, Comment, Reply, PubComment) {
    console.log('PubReadBlogCtrl');

    $scope.article;
    $scope.comment;
    $scope.authorNameArr = [];

    var initQuery = function(article) {
        var newQuery = { // 搜索的条件
           page: 1,
           size: 10,
           articleId: null,
           orderBy: null,
           fromUid: null
        }
        $scope.query = angular.copy(newQuery);
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
        // 分页获取，实现排序过滤等功能
        console.log("page-----:" + $scope.query.page);
        PubComment.pageQuery({
            page: $scope.query.page - 1,
            size: $scope.query.size,
            articleId: article.id,
            orderBy: null,
            fromUid: $scope.query.fromUid
        }).$promise.then(function(data) {
            console.log(data);
            $scope.commentsPage = data;
        });
    }

    // 只看作者评论
    $scope.filterCommentByFromUid = function() {
        $scope.query.page = 1;
        if (!$scope.query.fromUid) {
            $scope.query.fromUid = $scope.article.authorId;
        } else {
            $scope.query.fromUid = null;
        }
        getComments($scope.article);
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

    // 返回一个初始化的reply
    var initReply = function(comment) {
        var newReply = {
            id: null,
            commentId: comment.id,
            fromUid: null,
//            toUid: comment.fromUser.id,
            content: ''
        }
        return angular.copy(newReply);
    }

    if (entity.code == '4001') { // 无权限
        $state.go('noAuth');
    } else {
        $scope.article = entity.data;
        console.log($scope.article);
        initQuery($scope.article);
        initComment($scope.article);
        getAuthorName($scope.article);
        getComments($scope.article);
    }

    // 提交文章评论
    $scope.saveComment = function(ev) {
        console.log($scope.comment);
        Comment.save($scope.comment).$promise.then(function(data) {
            console.log('保存结果');
            console.log(data);
            initComment($scope.article);
            $scope.pageRefresh();
        });
    }

    // 删除评论
    $scope.deleteComment = function(comment) {
        var index = $scope.commentsPage.content.indexOf(comment);

        if (comment.replys.length > 0) {
            $rootScope.tipMessage("该评论存在回复, 不允许删除");
            return;
        }

        $rootScope.confirmMessage("提示", "确定要删除此评论吗？", true).then(function() {
            Comment.delete({id: comment.id}).$promise.then(function(data) {
                console.log(data);
                if (data && data.id) {
                    $scope.commentsPage.content.splice(index, 1);
                    $rootScope.popMessage("删除成功！", true);
                }
            })
        }, function() {
            console.log('你取消了');
        });
    }

    // 打开回复评论的表单
    $scope.openReply = function(comment, reply) {
        if (!($rootScope.account && $rootScope.account.id)) {
            $state.go('login');
            return;
        }
        console.log(comment);
        comment.isOpenReply = !comment.isOpenReply;
        comment.newReply = initReply(comment);
        if (reply) { // 回复别人的回复，需要加上 @username ,后端判断'@username',确定回复的对象
            comment.newReply.content += '@' + reply.fromUserModel.username + ' ';
        }
    }

    // 提交comment评论中的newReply回复
    $scope.saveReply = function(comment) {
        console.log(comment);
        if (!comment.newReply.commentId || !comment.newReply.content) {
            $rootScope.popMessage('提交数据不正确，请刷新后重试!', false);
            return;
        }
        Reply.save(comment.newReply).$promise.then(function(data) {
            console.log(data);
            comment.replys.push(data);
            $scope.openReply(comment);
            $rootScope.popMessage('回复成功！', true);
        }).catch(function(resp) {
            $rootScope.popMessage('回复失败！请刷新浏览器后重试', false);
        });
    }

    // 删除comment评论中的reply回复
    $scope.deleteReply = function(comment, reply) {
        var index = comment.replys.indexOf(reply);
        $rootScope.confirmMessage("提示", "确定要删除此回复吗？", true).then(function() {
            Reply.delete({id: reply.id}).$promise.then(function(data) {
                console.log(data);
                if (data && data.id) {
                    comment.replys.splice(index, 1);
                    $rootScope.popMessage("删除成功！", true);
                }
            })
        }, function() {
            console.log('你取消了');
        });
    }

})