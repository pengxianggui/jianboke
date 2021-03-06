'use strict';

angular.module('jianboke')
    .factory('BookChapterArticle', function($resource) {
    return $resource('api/bookChapterArticle/:id', {}, {
        listByParentId: {
            url: 'api/bookChapterArticle/list/:parentId',
            method: 'GET',
            isArray: true
        },
        listByChapterIdDeeply: {
            url: 'api/bookChapterArticle/listdeeply/:chapterId',
            method: 'GET',
            isArray: true
        },
        updateSortNum: {
            url: 'api/bookChapterArticle/updateSortNum/:id/:newSortNum',
            method: 'GET'
        },
        getTree: {
            url: 'api/bookChapterArticle/listTreeWithArticle/:bookId/:articleId',
            method: 'GET'
        }
    })
}).factory('PubBookChapterArticle', function($resource) {
    return $resource('pub/bookChapterArticle/:id', {}, {
        getTree: {
            url: 'pub/bookChapterArticle/listTreeWithArticle/:bookId/:articleId',
            method: 'GET'
        }
    })
});