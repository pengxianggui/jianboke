'use strict';

angular.module('jianbo')
	.factory('Principal', function($q, Account) {
		var _identity, //账户对象
			_authenticated = false; // 是否已经认证，默认为false。
		
		return {
			/**
			 * 验证身份。
			 * @returns
			 */
			identity: function(force) {
		        var deferred = $q.defer(); //作用？？？？
		        if (force) {
		        	_identity = undefined;
		        }
		        if (angular.isDefined(_identity)) { //如果_identity已经被定义过，无需再去验证
		        	deferred.resolve(_identity);
		        	return deferred.promise;
		        }
		        // 否则，向后端查询
		        Account.get().$promise.then(function(account) { //查询
		        	_identity = account;
		        	_authenticated = true;// 已认证
		        	deferred.resolve(_identity);
		        	console.log('认证成功');
		        }).catch(function() { //查询账户失败
		        	_identity = null;
		        	_authenticated = false;
		        	deferred.resolve(_identity);
		        	console.log('认证失败');
		        });
		        return deferred.promise;
			},
			/**
			 * 判断是否是某个权限，传入权限代号，如“ROLE_USER”
			 */
			isAuthority: function(authority) { 
				if (!_authenticated) { //如果没有认证
					return $q.when(false); //------------------？？
				}
				return this.identity().then(function(account) {
					return account.authorities && account.authorities == authority;
				}, function(error) {
					// wrong
					return false;
				});
			},
			/**
			 * 返回是否已经认证
			 */
			isAuthenticated: function() {
				return _authenticated;
			},
			// 设置认证状态和账户对象,用作登出时的制空处理
			setAuthenticate: function(identity) {
				_identity = identity;
				_authenticated = (_identity !== null);
			}
		}
	})