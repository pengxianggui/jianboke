'use strict';

angular.module('jianboke')
	.controller('LoginCtrl', function($scope, $cookies, Auth, $state, $rootScope) {
		console.log('LoginCtrl');
		$scope.user = {};
	    $scope.errors = {};
	    $scope.rememberMe = true;
	    try {
	     // 读取cookie中保存密码按钮的状态,如果保存密码同时读取用户名和密码
	     if($cookies.get("jbk-rememberMeFlag") === 'true'){
	      $scope.rememberMe = true;
	      $scope.passwordType = 'password';
	      if($cookies.get("jbk-loginCookies")){
	        var jbkLoginCookies = atob(atob($cookies.get("jbk-loginCookies")));
	        var userNameLength = jbkLoginCookies.split(":")[0];
	        var content = jbkLoginCookies.substring(userNameLength.length+1);
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
	        	try {
                    // 在cookie中保存密码按钮的状态,如果保存状态为true，同时保存用户名和密码
                    var expireDate = new Date();
                    expireDate.setDate(expireDate.getDate() + 31);
                    $cookies.put("jbk-rememberMeFlag", $scope.rememberMe, {'expires': expireDate});
                    if($scope.rememberMe === true){
                        var jbkLoginCookies = btoa(btoa($scope.username.length+":"+$scope.username+$scope.password));//用户名密码存储格式：用户名长度:用户名+密码
                        $cookies.put("jbk-loginCookies", jbkLoginCookies, {'expires': expireDate});
                    } else {
                        $cookies.remove("jbk-loginCookies");
                    }
                } catch (e) {
                    console.log(e.name + ": " + e.message);
                }
	        }).catch(function() {
	        	$scope.authenticationError = true;
	        	$rootScope.popMessage("登录失败", false);
	        });
	      }
	    };
	})