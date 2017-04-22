'use strict';

angular.module('jianboke')
	.controller('LoginCtrl', function($scope, $cookies, Auth, $state, $rootScope) {
		console.log('LoginCtrl');
		$scope.user = {};
	    $scope.errors = {};
	    try {
	     // 读取cookie中保存密码按钮的状态,如果保存密码同时读取用户名和密码
	     if($cookies.get("rememberMeFlag") === 'true'){
	      $scope.rememberMe = true;
	      $scope.passwordType = 'password';
	      if($cookies.get("sSerial")){
	        var serial = atob(atob($cookies.get("sSerial")));
	        var userNameLength = serial.split(":")[0];
	        var content = serial.substring(userNameLength.length+1);
	        $scope.email = content.substring(0,userNameLength);
	        $scope.password = content.substring(userNameLength);
	      }
	     }else{
	      $scope.rememberMe = false;
	      $scope.passwordType = 'text';
	     }
	    } catch (e) {
	      console.log(e.name + ": " + e.message);
	    }

	    $scope.login = function(event) {
	      event.preventDefault();

	      angular.forEach($scope.form.$error.required, function(field) {
	        field.$setTouched();
	      });
	      if ($scope.form.$valid) {
	        Auth.login({
	          email: $scope.email,
	          password: $scope.password,
	          rememberMe: $scope.rememberMe
	        }).then(function(data) {
	          //提示登录成功，并定向到dashboard路由；
	        	console.log(data);
	        	$rootScope.popMessage("登录成功", true);
	        	$state.go('dashboard');
	        }).catch(function() {
	        	$scope.authenticationError = true;
	        	$rootScope.popMessage("登录失败", false);
	        });
	      }
	    };
	})