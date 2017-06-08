'use strict';

angular.module('jianboke')
    .controller('UserSetCtrl', function($scope, $rootScope, $state, Auth, Account, Upload, entity, AccountDefaultSetting, $q, $timeout, $mdConstant) {
        console.log('UserSetCtrl');
        $scope.accountDefaultSetting = entity;
        console.log($scope.accountDefaultSetting);
        // 处理labels
        if ($scope.accountDefaultSetting == null || $scope.accountDefaultSetting.hobbyLabels == null ||  $scope.accountDefaultSetting.hobbyLabels == '') {
            $scope.hobbyLabels = [];
        } else {
            $scope.hobbyLabels = $scope.accountDefaultSetting.hobbyLabels.split(',');
        }
        $scope.keys = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA];

        $scope.selectImage = function(file) {
            $scope.picFile = file;
        }

        // 上传头像
        $scope.updateAvator = function () {
            if (!$scope.picFile) {
                $scope.popMessage('你没有做修改');
                return;
            }
            Upload.upload({
                url: 'api/account/avator',
                data: {file: $scope.picFile}
            }).then(function(resp) {
                console.log(resp);
                if (resp.data.code == '0000') {
                    $rootScope.popMessage('上传成功！', true);
                    $rootScope.account.avatarPath = resp.data.data;
                    $scope.picFile = null;
                } else {
                    $rootScope.popMessage(resp.data.detail, false);
                }
            }, function(resp) {
                $rootScope.popMessage('发生错误！', false);
            })
        }

        // 更改邮箱
        $scope.updateEmail = function() {
            // todo updateEmail setting
            $rootScope.tipMessage("暂未开通此功能，请谅解！");
        }

        $scope.accountName;
        $timeout(function() {
            $scope.accountName = $rootScope.account.username;
            console.log(JSON.stringify($rootScope.account));
        },0)
        // 验证用户名是否已被占用的方法
        $scope.usernameExist = function(value) {
            return true; // 暂不支持修改用户名，因此直接返回true，不做校验
            if (value == $rootScope.account.username) {
                return true; // 未做修改
            }
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
        // 更改用户名
        $scope.updateUsername = function() {
            $rootScope.tipMessage("暂未开通此功能，请谅解！"); // 暂不支持修改用户名
            return;

            if ($scope.accountName == $rootScope.account.username) {
                $rootScope.popMessage('你没有做修改');
                return;
            }
            Account.updateUsername({username: $scope.accountName}).$promise.then(function(resp) {
                if (resp.data.code == '0000') {
                    $rootScope.popMessage('更新成功！', true);
                    $rootScope.account.username = resp.data.data;
                } else {
                    $rootScope.popMessage(resp.data.detail, false);
                }
            }, function(resp) {
                $rootScope.popMessage('发生错误！', false);
            });
        }

        $scope.watchDefaultIfPublic = function() {
            if (!$scope.accountDefaultSetting) return;
            if (!$scope.accountDefaultSetting.defaultIfPublic) {
                $scope.accountDefaultSetting.defaultIfAllowReprint = false;
                $scope.accountDefaultSetting.defaultIfAllowComment = false;
                $scope.accountDefaultSetting.defaultIfAllowSecondAuthor = false;
            }
        }

        // 保存所有设置
        $scope.saveAccountDefaultSetting = function(param) {
            if (param == 'hobbyLabels') {
                var hobbyLabelsStr = $scope.hobbyLabels.join(',');
                if (hobbyLabelsStr == $scope.accountDefaultSetting.hobbyLabels) {
                    $rootScope.tipMessage("你没有做修改");
                    return;
                }
                if ($scope.hobbyLabels.length <= 0) {
                    $rootScope.tipMessage('兴趣标签不能为空');
                    return;
                }
                $scope.accountDefaultSetting.hobbyLabels = $scope.hobbyLabels.join(',');
            }
            AccountDefaultSetting.save($scope.accountDefaultSetting).$promise.then(function(resp) {
                console.log(resp);
                if (resp.code == '0000') {
                    $rootScope.popMessage('保存成功！', true);
                } else {
                    $rootScope.popMessage('保存失败, 请稍后再试', false);
                }
            }).catch(function(resp) {
                $rootScope.popMessage('保存失败, 请稍后再试', false);
            });
        }
    });