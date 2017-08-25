'use strict';

angular.module('jianboke')
    .controller('CommentCtrl', function($scope, $rootScope, $state, PubAccount, Article, Comment, Reply, PubComment, PubArticle) { // 文章归档

//    $scope.article;
    console.log($scope.article);
    $scope.authorNameArr = [];
    $scope.articleLikes = [];
    $scope.isAuthenticated = $rootScope.isAuthenticated;
    $scope.account = $rootScope.account;

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

    // 获取文章所有的喜欢
    var getArticleLike = function(article) {
        PubArticle.queryAllLikesByArticleId({ articleId: article.id }).$promise.then(function(data) {
            console.log(data);
            $scope.articleLikes = data;
        })
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

    // 提交文章评论
    $scope.saveComment = function(ev) {
        if (!($rootScope.account && $rootScope.account.id)) {
            $state.go('login');
            return;
        }
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
        if (!($rootScope.account && $rootScope.account.id)) {
            $state.go('login');
            return;
        }
        var index = $scope.commentsPage.content.indexOf(comment);

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
        if (!($rootScope.account && $rootScope.account.id)) {
            $state.go('login');
            return;
        }
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
        if (!($rootScope.account && $rootScope.account.id)) {
            $state.go('login');
            return;
        }
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

    $scope.hasMyLike = function(resource) {
        if (!$rootScope.account || !$rootScope.account.id) return false;
        var result = false;
        resource.forEach(function(value, index) {
            if (value.fromUid == $rootScope.account.id) result = true;
        });
        return result;
    }

    // 喜欢文章
    $scope.likeArticle = function(article) {
        if (!($rootScope.account && $rootScope.account.id)) {
            $state.go('login');
            return;
        }
        Article.like({articleId: article.id}).$promise.then(function(resp) {
            if (resp.data.liked) {
                $scope.articleLikes.push(resp.data);
//                $scope.myLikeToArticle = true;
            } else {
                // todo 删除对应的like
                $scope.articleLikes.forEach(function(value, index) {
                    if (value.id == resp.data.id) {
                        $scope.articleLikes.splice(index, 1);
//                        $scope.myLikeToArticle = false;
                    }
                })
            }
        });
    }

    // todo 喜欢评论
    $scope.likeComment = function(comment) {
        if (!($rootScope.account && $rootScope.account.id)) {
            $state.go('login');
            return;
        }
        Comment.like({commentId: comment.id}).$promise.then(function(resp) {
            if (resp.data.liked) {
                comment.likes.push(resp.data);
            } else {
                // todo 删除对应的like
                comment.likes.forEach(function(value, index) {
                    if (value.id == resp.data.id) {
                        comment.likes.splice(index, 1);
                    }
                })
            }
        });
    }

    // 更改文章是否评论属性
    $scope.modifyArticleCommentAuth = function(param) {
        $scope.article.ifAllowComment = param;
        Article.update($scope.article).$promise.then(function(resp) {
            if (resp.code === '0000') {
                $rootScope.popMessage('开启成功', true);
            } else {
                $rootScope.popMessage(resp.data, false);
                $scope.article.ifAllowComment = !param;
            }
        }, function(resp, header) {
            $scope.article.ifAllowComment = !param;
        });
    }

    initQuery($scope.article);
    initComment($scope.article);
    getComments($scope.article);
    getArticleLike($scope.article);
});