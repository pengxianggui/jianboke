'use strict';

angular.module('jianboke')
    .controller('RegisterCtrl', function($scope, $rootScope, $state, Auth, Account, $q) {
        console.log('RegisterCtrl');
        $scope.emailValide = false; // 邮箱已通过验证
        $scope.sendEmailed = false; // 已发送邮件

        // 验证邮箱是否已被注册的方法
        $scope.emailExist = function(value) {
            var obj = {}; // 构造请求参数
            obj.id = null;
            obj.value = value;
            return $q(function (resolve, reject) {
                Account.emailUniqueValid(obj).$promise.then(
                  function (result, responseHeaders) {
                    $scope.emailValide = result.valid;
                    if (result.valid) {
                      resolve();
                    } else {
                      reject();
                    }
                  });

            });
        }

        // 验证用户名是否已被占用的方法
        $scope.usernameExist = function(value) {
            var obj = {}; // 构造请求参数
            obj.id = null;
            obj.value = value;
            return $q(function (resolve, reject) {
                Account.usernameUniqueValid(obj).$promise.then(
                  function (result, responseHeaders) {
                    if (result.valid) {
                      resolve();
                    } else {
                      reject();
                    }
                  });

            });
        }

        // 发送邮箱验证码
        $scope.sendEmailVerificationCode = function() {
            console.log('sendEmailVerificationCode');
            var obj = {};
            obj.username = $scope.username;
            obj.email = $scope.email;
            Account.sendEmailValidCode(obj).$promise.then(function(result) {
                console.log(result);
                if (result.valid)
                    $rootScope.popMessage("验证码发送成功", true);
                else
                    $rootScope.popMessage("验证码发送失败", false);
            }, function(resp) {
               $rootScope.popMessage("验证码发送失败", false);
            });
        }

        // 注册
        $scope.register = function(event) {
            event.preventDefault();
            angular.forEach($scope.form.$error.required, function(field) {
                field.$setTouched();
            });
            if ($scope.form.$valid) {
                console.log($scope.validCode);
                Auth.register({
                    'username': $scope.username,
                    'email': $scope.email,
                    'validCode': $scope.validCode,
                    'password': $scope.password
                }).then(function(result) {
                    console.log(result);
                    if (result.code == '0000') { // 成功
                        Auth.login({
                          email: result.data.email,
                          password: result.data.password,
                          rememberMe: false
                        }).then(function(data) {
                            //提示登录成功，并定向到dashboard路由；
                            console.log(data);
                            $rootScope.popMessage("登录成功", true);
                            $state.go('dashboard');
                        }).catch(function() {
                            $scope.authenticationError = true;
                            $rootScope.popMessage("登录失败", false);
                        });
                    } else {
                        $rootScope.popMessage(result.detail, false);
                    }
                }, function(resp) {});
            }
        }
    })