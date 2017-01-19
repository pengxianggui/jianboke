'use strict';

angular.module('jianbo')
	.factory('authExpiredInterceptor', function($rootScope, $injector, $q) { // 权限过期拦截器
		return {
			responseError: function(response) {
				/*
				 * 状态为401无权限，
				 * 并且响应的请求路径不为undifined，
				 * 并且响应的请求路径不包含'/api/account'----------------？
				 * 且不包含'/api/authenentication'-------------------？
				 * 才执行拦截器的定向到login页面的功能
				 */
				if (response.status === 401 && response.data.path !== undefined 
						&& response.data.path.indexOf('/api/account') === -1 
						&& response.data.path.indexOf('/api/authentication') === -1){
					var Auth = $injector.get('Auth'), //显式获取Auth服务
			          	$state = $injector.get('$state');
					var to = $rootScope.toState,
						params = $rootScope.toStateParams;

					$rootScope.previousStateName = to.name;
					$rootScope.previousStateNameParams = params;
			        console.log('interceptor will redirect to login state');
			        $state.go('login');
				}
				return $q.reject(response); //---关闭响应？？?
			}
		}
	})