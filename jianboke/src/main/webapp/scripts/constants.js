'use strict';

angular.module('jianboke')
	.constant('ACCESS_LEVELS', {
		user: 'ROLE_USER',
		pub: 'PUB'
	})
	.constant('ACCOUNT_MENU', {
		list: [
		       	{'title': 'home', 'chi_title': '个人中心',  'state': 'dashboard.blogs', 'icon': 'home'},
		       	{'title': 'write', 'chi_title': '写博客', 'state': 'blog.edit', 'param': {id: 'new'}, 'icon': 'edit'},
		       	{'title': 'mycollection', 'chi_title': '我的收藏', 'state': 'dashboard.collection', 'icon': 'bookmark'},
		       	{'title': 'setting', 'chi_title': '设置', 'state': 'userset', 'icon': 'settings'},
		       	{'title': 'exit', 'chi_title': '退出', 'icon': 'exit_to_app'},
		      ]
	});