'use strict';

angular.module('jianboke')
	.factory('Article', function($resource) {
		return $resource('api/article/:id', {}, {
			save: {
				url: 'api/article',
				method: 'POST'
			},
			update: {
			    url: 'api/article',
			    method: 'PUT'
			},
			deleteBatch: {
			    url: 'api/article/deleteBatch/:ids',
			    method: 'DELETE'
			},
            updateBlogSet: {
                url: 'api/article/updateBlogSet',
                method: 'POST',
                params: {
                    ids: "@ids",
                    ifPublic: "@ifPublic",
                    ifAllowReprint: "@ifAllowReprint",
                    ifAllowComment: "@ifAllowComment",
                    ifAllowSecondAuthor: "@ifAllowSecondAuthor",
                    ifSetTop: "@ifSetTop"
                }
            },
            queryAll: {
                url: 'api/article/queryAll',
                method: 'GET'
            }
		})
	})