'use strict';

angular.module('jianboke')
	.config(function($stateProvider, $urlRouterProvider, ACCESS_LEVELS) {
	    console.log('router.js');
//		$urlRouterProvider.otherwise('/mine/dashboard');
		$urlRouterProvider.otherwise('/');
		$stateProvider
		.state('dashboard', {
			url: '/mine',
			data: {
				access_level: [ACCESS_LEVELS.user],
				title: '个人中心',
				belong: 'dashboard'
			},
			controller: 'DashBoardCtrl',
			templateUrl: 'views/dashboard.html'
		})
		.state('dashboard.blogs', {
		    url: '/dashboard',
		    data: {
				access_level: [ACCESS_LEVELS.user],
				belong: 'dashboard'
		    },
		    controller: 'BlogListCtrl',
		    templateUrl: 'views/blogList.html',
		    resolve: {
		        books: function(Book) {
		            return Book.query().$promise;
		        }
		    }
		})
		.state('dashboard.attention', {
			url: '/attention',
			data: {
				access_level: [ACCESS_LEVELS.user],
				title: '我的关注',
				belong: 'dashboard'
			},
			controller: 'AttentionCtrl',
			templateUrl: 'views/attention.html'
		})
		.state('dashboard.collection', {
			url: '/collection',
			data: {
				access_level: [ACCESS_LEVELS.user],
				title: '我的收藏',
				belong: 'dashboard'
			},
			controller: 'CollectionCtrl',
			templateUrl: 'views/collection.html'
		})
		.state('dashboard.message', {
			url: '/message',
			data: {
				access_level: [ACCESS_LEVELS.user],
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
			url: '/blog/{id}',
			abstract: true,
			data: {
				access_level: [ACCESS_LEVELS.user],
				title: '博客',
				belong: 'dashboard'
			},
			templateUrl: 'views/blog.html',
			controller: 'BlogCtrl',
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
		.state('blog.edit', {
			url: '/',
			data: {
				access_level: [ACCESS_LEVELS.user],
				title: '新建Blog',
				belong: 'dashboard'
			},
			controller: 'NewBlogCtrl',
			templateUrl: 'views/newBlog.html',
//			resolve: {
//			    entity: function($stateParams, Article) {
//			        var obj;
//			        if($stateParams.id == 'new') {
//                      obj = {
//                        id: null,
//                        title: '',
//                        content: '',
//                        labels: '',
//                        authorId: null,
//                        bookId: null
//                      }
//			        } else {
//			          obj = Article.get({id: $stateParams.id}).$promise;
//			        }
//			        return obj;
//			    }
//			}
		})
		.state('blog.readBlog', {
			url: '/readBlog',
			data: {
				access_level: [ACCESS_LEVELS.user],
				title: '阅读Blog',
                belong: 'dashboard'
			},
			controller: 'ReadBlogCtrl',
			templateUrl: 'views/readBlog.html',
//			resolve: {
//			    entity: function($stateParams, Article) {
//			        return Article.get({id: $stateParams.id}).$promise;
//			    }
//			}
		})
		.state('addToBook', {
		    url: '/addToBook/{id}',
		    data: {
		        access_level: [ACCESS_LEVELS.user],
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
		        access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.pub],
		        title: '书'
		    },
		    controller: 'BookCtrl',
		    templateUrl: 'views/book.html',
		    resolve: {
		        entity: function($stateParams, PubBook) {
		            return PubBook.get({id: $stateParams.bookId}).$promise;
		        }
		    }
		})
		.state('book.content', {
		    url: '/{type}/{resourceId}', // type: 'chapter' or 'article'; resourceId : 'chapter.id' or 'article.id'
		    data: {
		        access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.pub],
		        title: '书',
                belong: 'dashboard'
		    },
		    controller: 'BookContentCtrl',
		    templateUrl: 'views/book.content.html',
		    resolve: {
		        entity: function($stateParams, PubArticle, PubChapter) {
		            console.log($stateParams.type);
		            if ($stateParams.type === 'article') {
		                return PubArticle.get({id: $stateParams.resourceId}).$promise;
		            } else {
		                return PubChapter.get({id: $stateParams.resourceId}).$promise;
		            }
		        }
		    }
		})

//		.state('mycollection', {
//			url: '/mycollection',
//			data: {
//				access_level: [ACCESS_LEVELS.user],
//				title: '我的收藏',
//                belong: 'dashboard'
//			},
//			controller: 'MyCollectionCtrl',
//			templateUrl: 'views/myCollection.html'
//		})
		.state('userset', {
			url: '/userset',
			data: {
				access_level: [ACCESS_LEVELS.user],
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
		.state('searchAll', {
		    url: '/searchResult/{keyWord}',
		    data: {
				access_level: [ACCESS_LEVELS.pub],
				title: '搜索结果',
                belong: 'dashboard'
		    },
		    controller: 'SearchCtrl',
		    templateUrl: 'views/searchResult.html',
		    resolve: {
		        articles: function(PubArticle, $stateParams) {
                    return PubArticle.queryAll({keyWord: $stateParams.keyWord}).$promise;
		        },
		        users: function(PubAccount, $stateParams) {
                    return PubAccount.queryAll({keyWord: $stateParams.keyWord}).$promise;
		        },
		        books: function(Book, $stateParams) {
                    return $stateParams.keyWord;
		        }
		    }
		})
		.state('index', {
		    url: '/',
			data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.pub],
				title: '首页',
				belong: 'index'
			},
		    controller: 'IndexCtrl',
		    templateUrl: 'views/index_center.html'
		})
		.state('accountCenter', {
		    url: '/u/{username}',
		    data: {
		        access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.pub],
		        title: '主页-',
		        belong: 'accountCenter'
		    },
		    controller: 'AccountCenterCtrl',
		    templateUrl: 'views/public/account.center.html',
		    resolve: {
		        userEntity: function(PubAccount, $stateParams) {
                    return PubAccount.getByUsername({username: $stateParams.username}).$promise;
		        },
		        articlesEntity: function(PubArticle, $stateParams) {
                    return PubArticle.queryAllByUsername({username: $stateParams.username}).$promise;
		        },
		        booksEntity: function(PubBook, $stateParams) {
                    return PubBook.queryAllByUsername({username: $stateParams.username}).$promise;
		        }
		    }
		})
		.state('pubReadBlog', {
			url: '/article/{id}',
			data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.pub],
				title: '阅读Blog',
                belong: 'dashboard'
			},
			controller: 'PubReadBlogCtrl',
			templateUrl: 'views/public/blog.read.html',
			resolve: {
			    entity: function($stateParams, PubArticle) {
			        return PubArticle.get({id: $stateParams.id}).$promise;
			    }
			}
		})
		.state('noAuth', {
		    url: '/wrong',
		    data: {
				access_level: [ACCESS_LEVELS.user, ACCESS_LEVELS.pub],
				title: '无权限'
		    },
		    templateUrl: 'views/public/noAuth.html'
		})
	});