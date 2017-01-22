'use strict';

angular.module('jianboke')
	.factory('Chapter', function($resource) {
		return $resource('api/chapter', {}, {
			getTree: {
				url: 'api/chapter/listChapterTreeWithoutArticle/:bookId',
				method: 'GET'
			}
		});
	});