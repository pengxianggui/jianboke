'use strict';

angular.module('jianboke')
	.config(function($httpProvider) {
		$httpProvider.interceptors.push('authExpiredInterceptor'); //注册权限过期拦截器
	})
	.config(function($mdThemingProvider) {

//	    $mdThemingProvider.definePalette('amazingPaletteName', {
//            '50': 'e1f5fe',
//            '100': 'b3e5fc',
//            '200': '81d4fa',
//            '300': '4fc3f7',
//            '400': '29b6f6',
//            '500': '03a9f4',
//            '600': '039be5',
//            '700': '0288d1',
//            '800': '0277bd',
//            '900': '01579b',
//            'A100': '80d8ff',
//            'A200': '40c4ff',
//            'A400': '00b0ff',
//            'A700': '0091ea',
//            'contrastDefaultColor': 'light',    // whether, by default, text (contrast)
//                                                // on this palette should be dark or light
//
//            'contrastDarkColors': ['50', '100', //hues which contrast should be 'dark' by default
//             '200', '300', '400', 'A100'],
//            'contrastLightColors': undefined    // could also specify this if default was 'dark'
//          }
//        );

          $mdThemingProvider.theme('default')
            .primaryPalette('blue')
            .accentPalette('blue-grey')
            .warnPalette('deep-orange');

          $mdThemingProvider.theme('pxg-dark-theme')
            .primaryPalette('blue')
            .accentPalette('blue-grey')
            .warnPalette('deep-orange').dark();
          $mdThemingProvider.alwaysWatchTheme(true);


//        $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
//        $mdThemingProvider.theme('dark-orange').backgroundPalette('orange').dark();
//        $mdThemingProvider.theme('dark-purple').backgroundPalette('deep-purple').dark();
//        $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();

//          $mdThemingProvider.disableTheming(); // 禁用主题
	});