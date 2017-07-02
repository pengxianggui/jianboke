'use strict';

angular.module('jianboke')
	.factory('Auth', function(AuthServerProvider, $state, $q, Principal, $rootScope, ACCESS_LEVELS, localStorageService, Account) {
		return {
			login: function(credentials) { //登录方法
				var deferred = $q.defer();
				AuthServerProvider.login(credentials).then(function(data) {
					Principal.identity(true).then(function(account) {
						console.log(account);
						deferred.resolve(data);
					})
				}).catch(function(error) {
				    this.logout();
					deferred.reject(error);
				}.bind(this));
				return deferred.promise;
			},
			logout: function() { //登出方法
				AuthServerProvider.logout().then(function(response) {
					localStorageService.clearAll();
					Principal.setAuthenticate(null);
					// Reset state memory
					$rootScope.previousStateName = undefined;
					$rootScope.previousStateNameParams = undefined;
					// 提示
					$rootScope.popMessage('登出成功!', true);
					$state.go('login');
				}).catch(function(response) {
					$rootScope.popMessage('登出失败！', false);
				});
			},
			register: function(account, callback) {
				return Account.register(account).$promise;
			},
			authorize: function() { //授权验证，判断是否认证(登录)。每次路由改变都会触发验证
				return Principal.identity().then(function(account) {
					$rootScope.account = account; //将账号信息保存到$rootScope中
		            $rootScope.isAuthenticated = Principal.isAuthenticated; //相当于把获取“是否已经认证的get方法”放到$rootScope下，方便使用
		            var isAuthenticated = Principal.isAuthenticated(); //获取是否已经认证的boolean值
		            if (isAuthenticated && $rootScope.toState.name === 'login') { // 已经有认证(登录)的不能到登录页面
		            	$state.go('dashboard');
		            }
		            console.log($rootScope.toState);
		            console.log(isAuthenticated);
		            if ($rootScope.toState.data.access_level.indexOf(ACCESS_LEVELS.pub) === -1) { // 不含有pub权限级别
			            if (!isAuthenticated) { //如果没有认证(登录)，则定向到登录路由；
			            	 $rootScope.previousStateName = $rootScope.toState.name;
			                 $rootScope.previousStateNameParams = $rootScope.toStateParams;
			            	console.log('定向到login');
			            	$state.go('login');
			            }
		            }
				});
			}
		}
	}).factory('AuthServerProvider', function($http) {
		return {
			login: function(credentials) {
				var data = 'jbk_username=' + encodeURIComponent(credentials.username) +
		          '&jbk_password=' + encodeURIComponent(credentials.password) +
		          '&remember-me=' + credentials.rememberMe + '&submit=Login';
				return $http.post('api/authentication', data, { // api/authentication 这个路径由spring security配置并处理
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
					},
					ignoreLoadingBar: true
		        }).success(function(response) {
                    return response;
                });
			},
			logout: function() {
				return $http.post('api/logout');
			}
		}
	})