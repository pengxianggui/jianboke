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
	});