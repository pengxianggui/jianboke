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
        }
    })
});