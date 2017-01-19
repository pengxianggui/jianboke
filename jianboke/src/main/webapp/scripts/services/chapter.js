'use strict';

angular.module('jianbo')
	.factory('Chapter', function($resource) {
		return $resource('api/chapter', {}, {
			getTree: {
				url: 'api/chapter/listChapterTreeWithoutArticle/:bookId',
				method: 'GET'
			}
		});
	});