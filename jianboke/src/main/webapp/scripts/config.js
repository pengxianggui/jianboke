'use strict';

angular.module('jianbo')
	.config(function($httpProvider) {
		$httpProvider.interceptors.push('authExpiredInterceptor'); //注册权限过期拦截器
	});