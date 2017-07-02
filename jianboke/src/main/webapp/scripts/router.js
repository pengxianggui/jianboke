'use strict';

angular.module('jianboke')
	.config(function($stateProvider, $urlRouterProvider, ACCESS_LEVELS) {
	    console.log('router.js');
		$urlRouterProvider.otherwise('/mine/dashboard');
		$stateProvider
		.state('index', {
		    url: '/index/{userId}',
			data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
				title: '首页',
				belong: 'index'
			},
		    controller: 'IndexCtrl',
		    templateUrl: 'views/index_center.html'
		})
		.state('dashboard', {
			url: '/mine',
			data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
				title: '个人中心',
				belong: 'dashboard'
			},
			controller: 'DashBoardCtrl',
			templateUrl: 'views/dashboard.html'
		})
		.state('dashboard.blogs', {
		    url: '/dashboard',
		    data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
				belong: 'dashboard'
		    },
		    controller: 'BlogListCtrl',
		    templateUrl: 'views/blogList.html'
		})
		.state('dashboard.attention', {
			url: '/attention',
			data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
				title: '我的关注',
				belong: 'dashboard'
			},
			controller: 'AttentionCtrl',
			templateUrl: 'views/attention.html'
		})
		.state('dashboard.collection', {
			url: '/collection',
			data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
				title: '我的收藏',
				belong: 'dashboard'
			},
			controller: 'CollectionCtrl',
			templateUrl: 'views/collection.html'
		})
		.state('dashboard.message', {
			url: '/message',
			data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
				title: '我的消息',
				belong: 'dashboard'
			},
			controller: 'MessageCtrl',
			templateUrl: 'views/message.html'
		})
		.state('login', {
			url: '/login',
			data: {
				access_level: [ACCESS_LEVELS.pub],
				title: '登录'
			},
			controller: 'LoginCtrl',
			templateUrl: 'views/login.html'
		})
		.state('register', {
			url: '/register',
			data: {
				access_level: [ACCESS_LEVELS.pub],
				title: '注册'
			},
			controller: 'RegisterCtrl',
			templateUrl: 'views/register.html'
		})
		.state('blog', {
			url: '/blog',
			abstract: true,
			data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
				title: '博客',
				belong: 'dashboard'
			},
			templateUrl: 'views/blog.html',
			controller: 'BlogCtrl'
		})
		.state('blog.edit', {
			url: '/{id}',
			data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
				title: '新建Blog',
				belong: 'dashboard'
			},
			controller: 'NewBlogCtrl',
			templateUrl: 'views/newBlog.html',
			resolve: {
			    entity: function($stateParams, Article) {
			        var obj;
			        if($stateParams.id == 'new') {
                      obj = {
                        id: null,
                        title: '',
                        content: '',
                        labels: '',
                        authorId: null,
                        bookId: null
                      }
			        } else {
			          obj = Article.get({id: $stateParams.id}).$promise;
			        }
			        return obj;
			    }
			}
		})
		.state('blog.readBlog', {
			url: '/readblog/{id}',
			data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
				title: '阅读Blog',
                belong: 'dashboard'
			},
			controller: 'ReadBlogCtrl',
			templateUrl: 'views/readBlog.html',
			resolve: {
			    entity: function($stateParams, Article) {
			        return Article.get({id: $stateParams.id}).$promise;
			    }
			}
		})
		.state('blog.addToBook', {
		    url: '/addToBook/{id}',
		    data: {
		        access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
		        title: '文章归档',
                belong: 'dashboard'
		    },
		    controller: 'AddToBookCtrl',
		    templateUrl: 'views/addToBook.html',
		    resolve: {
		        entity: function($stateParams, Article) {
		            return Article.get({id: $stateParams.id}).$promise;
		        }
		    }
		})
		.state('book', {
		    url: '/book/{bookId}',
		    data: {
		        access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
		        title: '书',
                belong: 'dashboard'
		    },
		    controller: 'BookCtrl',
		    templateUrl: 'views/book.html',
		    resolve: {
		        entity: function($stateParams, Book) {
		            return Book.get({id: $stateParams.bookId}).$promise;
		        }
		    }
		})
		.state('book.content', {
		    url: '/{type}/{resourceId}', // type: 'chapter' or 'article'; resourceId : 'chapter.id' or 'article.id'
		    data: {
		        access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
		        title: '书',
                belong: 'dashboard'
		    },
		    controller: 'BookContentCtrl',
		    templateUrl: 'views/book.content.html',
		    resolve: {
		        entity: function($stateParams, Article, Chapter) {
		            console.log($stateParams.type);
		            if ($stateParams.type === 'article') {
		                return Article.get({id: $stateParams.resourceId}).$promise;
		            } else {
		                return Chapter.get({id: $stateParams.resourceId}).$promise;
		            }
		        }
		    }
		})

//		.state('mycollection', {
//			url: '/mycollection',
//			data: {
//				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
//				title: '我的收藏',
//                belong: 'dashboard'
//			},
//			controller: 'MyCollectionCtrl',
//			templateUrl: 'views/myCollection.html'
//		})
		.state('userset', {
			url: '/userset',
			data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.admin],
				title: '用户设置',
                belong: 'dashboard'
			},
			controller: 'UserSetCtrl',
			templateUrl: 'views/userSet.html',
		    resolve: {
		        entity: function(AccountDefaultSetting) {
		            return AccountDefaultSetting.get().$promise;
		        }
		    }
		})
	});