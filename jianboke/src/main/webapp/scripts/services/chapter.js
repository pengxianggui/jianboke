'use strict';

angular.module('jianboke')
	.factory('Chapter', function($resource) {
		return $resource('api/chapter/:id', {}, {
			getTree: {
				url: 'api/chapter/listChapterTreeWithoutArticle/:bookId/:articleId',
				method: 'GET'
			},
			getArticlesById: {
			    url: 'api/chapter/listArticlesById/:id',
			    method: 'GET',
			    isArray: true
			}
		});
	})
	.factory('PubChapter', function($resource) {
		return $resource('pub/chapter/:id', {}, {
//			getTree: {
//				url: 'pub/chapter/listChapterTreeWithoutArticle/:bookId/:articleId',
//				method: 'GET'
//			},
//			getArticlesById: {
//			    url: 'pub/chapter/listArticlesById/:id',
//			    method: 'GET',
//			    isArray: true
//			}
		});
	});