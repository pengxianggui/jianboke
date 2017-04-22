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
            Account.sendEmailValidCode().$promise.then(function(result) {

            }, function(resp) {});
        }
    })