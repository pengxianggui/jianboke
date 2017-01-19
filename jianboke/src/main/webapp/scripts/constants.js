'use strict';

angular.module('jianbo')
	.constant('ACCESS_LEVELS', {
		user: 'ROLE_USER',
		admin: 'ROLE_ADMIN',
		pub: 'PUB'
	})
	.constant('ACCOUNT_MENU', {
		list: [
		       	{'title': 'home', 'chi_title': '主页',  'state': 'dashboard', 'icon': 'home'},
		       	{'title': 'write', 'chi_title': '写博客', 'state': 'blog.newblog', 'icon': 'edit'},
		       	{'title': 'mycollection', 'chi_title': '我的收藏', 'state': 'mycollection', 'icon': 'bookmark'},
		       	{'title': 'setting', 'chi_title': '设置', 'state': 'userset', 'icon': 'settings'},
		       	{'title': 'exit', 'chi_title': '退出', 'icon': 'exit_to_app'},
		      ]
	});