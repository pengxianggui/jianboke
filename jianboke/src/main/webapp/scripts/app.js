'use strict';

angular.module('jianboke', [
     'ngAnimate',
     'ngAria',
     'chart.js',
     'ngCookies',
     'angular.filter',
     'angular-loading-bar',
     'LocalStorageModule',
     'ngMaterial',
     'ngMessages',
     'ngResource',
     'ngRoute',
     'ui.codemirror',
     'ui.router',
     'ui.validate',
     'ngFileUpload',
     "integralui",
     "mdPickers"
]).run(function($rootScope, $state, $templateCache, Auth, $window, $mdDialog, $mdToast) {
    console.log('run.js');
	$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams, options) { 
        $templateCache.remove(toState.templateUrl); //清除路由缓存
        $rootScope.toState = toState;
        $rootScope.toStateParams = toParams;
        Auth.authorize();
    });
	
    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        if (toState.name !== 'login' && $rootScope.previousStateName) {
            $rootScope.previousStateName = fromState.name;
            $rootScope.previousStateParams = fromParams;
        }
        if (toState.data && toState.data.title) {
          $window.document.title = '简博客 － ' + toState.data.title;
        }
    });

	$rootScope.goToDashboard = function() {
		$state.go('dashboard');
	}
	// 返回上一个路由
	$rootScope.back = function() {
		
	}
	// 登出
    $rootScope.logout = function() {
    	Auth.logout();
    };
    
    /**
     * 显示执行结果的方法。浮动提示图标
     * @param content 指定显示内容
     * @param isSuccess 成功或失败
     */
    $rootScope.popMessage = function(content, isSuccess){
      $mdDialog.cancel();
      isSuccess = isSuccess && true;
      var theme = isSuccess ? 'success':'error';
      $mdToast.show($mdToast.simple().content(content).hideDelay(3000).theme(theme));
    }
    
    /**
     * 操作提示弹框
     * @param title 显示的标题
     * @param content 正文内容
     * @param clickOutsideToClose 点击外面是否退出弹框
     * @param ev 触发的DOM对象
     */
    $rootScope.confirmMessage = function(title, content, clickOutsideToClose, ok_text, cancel_text, ev) {
		    // Appending dialog to document.body to cover sidenav in docs app
    		clickOutsideToClose = clickOutsideToClose && true; // 默认false
    		ok_text = ok_text ? ok_text : '确定';
    		cancel_text = cancel_text? cancel_text : '取消';
		    var confirm = $mdDialog.confirm()
		          .title(title)
		          .textContent(content)
//		          .ariaLabel('Lucky day')
		          .clickOutsideToClose(clickOutsideToClose)
		          .targetEvent(ev)
		          .ok(ok_text)
		          .cancel(cancel_text);
	
		    return $mdDialog.show(confirm);
    }
    
    /**
     * 提示信息
     */
    $rootScope.tipMessage = function(content, ev) {
	    var alert = $mdDialog.alert()
	        .textContent(content)
	        .clickOutsideToClose(false)
	        .targetEvent(ev)
	        .ok("知道了");
		  return $mdDialog.show(alert);
    }
})
.controller('HeaderCtrl', function($scope, $state) {
    console.log('HeaderCtrl');
    $scope.current = 'dashboard';
    $scope.go = function (router, param) {
        $state.go(router, param);
        $scope.current = router;
    }
});