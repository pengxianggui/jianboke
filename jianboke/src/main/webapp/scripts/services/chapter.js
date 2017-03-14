'use strict';

angular.module('jianboke')
	.factory('Chapter', function($resource) {
		return $resource('api/chapter/:id', {}, {
			getTree: {
				url: 'api/chapter/listChapterTreeWithoutArticle/:bookId',
				method: 'GET'
			}
		});
	});