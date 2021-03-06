//'use strict';
//
//angular.module('jianboke')
//	/**
//	 * 存在bug: 通过顶端工具栏改变textarea内容时，无法将最新变动传递给conent，需要在点击、keyup等途径触发传递。
//	 * 如何监听 textarea value的改变(包括通过js改变)成为解决bug的思路之一！ 网上优选方案是input 和 propertychange 事件结合
//	 * 前者无法监听通过js改变的情况，后者虽然可以监听js改变的情况，但却是IE独有。
//	 */
//	.directive('myMarkDown', function() {
//		return {
//			restrict: 'E',
//			template: '<div flex style="z-index: 1" data-ng-init="setValue()">' +
////			            '<md-button ng-click="setValue()"></md-button>' +
//						'<div id="editormd" flex style="z-index: 89;"></div>' +
//						'<textarea ng-model="value" style="display:none"></textarea>' +
//					  '</div>',
//			replace: true,
//			scope: {
//				content: '=ngModel'
//			},
//			link: function(scope, iElement, iAttrs) {
//				scope.editor = editormd({
//		            id   : "editormd",
//		            path : "bower_components/editor.md/lib/",
//		            htmlDecode      : true,
//		            htmlDecode      : "script,iframe",  // you can filter tags decode
//		            flowChart       : true,
//		            tex             : true
//		        });
//				scope.getValue = function() {
//					scope.content = scope.editor.getMarkdown();
//				}
//				iElement.on('change keyup blur input propertychange click', function() {
//					scope.$apply(scope.getValue);
//				});
//		        var setValue = function(){
//                    scope.editor.setMarkdown(scope.content);
//                };
//                // TODO 如何改进，让页面完全渲染完毕后去执行此方法？好几种方法都尝试过，失败：
//                // 1. scope.$on('$viewContentLoaded',functon(){})
//                // 2. scope.$watch('$viewContentLoaded', function(){})
//                // 3. ng-init / data-ng-init都失败。
//                window.setTimeout(setValue, 1000);
//			}
//		}
//	})
//	.directive('myAccountMenu', function(ACCOUNT_MENU, $state, $rootScope) {
//		return {
//			restrict: 'E',
//			templateUrl: 'views/account_menu.html',
//			replace: true,
//			link: function(scope, iElement, iAttr) {
//
//			},
//			controller: function($scope) {
//				$scope.showMenu = false;
//				$scope.accountMenu = ACCOUNT_MENU.list;
//				$scope.toggleMenu = function(showMenu) {
//					$scope.showMenu = !showMenu;
//				};
//				$scope.closeMenu = function() {
//					$scope.showMenu = false;
//				};
//				$scope.goState = function(item) {
//					$scope.showMenu = false;
//					if (item.state === undefined && item.title == 'exit') {
//						console.log('exit');
//						var title = '确定要退出？',
//							content = '';
//						$rootScope.confirmMessage(title, content).then(function() {
//							$rootScope.logout();
//						}, function() {});
//					} else {
//						$state.go(item.state);
//					}
//				}
//			}
//		}
//	})
//	//展示chips的指令，传入的chips-data可以使以逗号分隔的字符串或者数组，chipsStyle是css样式对象，不传入则使用默认值
//	.directive('myChipsShow', function() {
//		return {
//			restrice: 'AE',
//			scope: {
//				chipsData: '=',
//				chipsStyle: '='
//			},
//			template: '<span>' +
//							'<span ng-repeat="item in chipsData" ng-bind="item" ng-style="chipsStyle"></span>' +
//					  '</span>',
//			controller: function($scope) {
//				if(!Array.isArray($scope.chipsData) && $scope.chipsData != '') {
//					$scope.chipsData = $scope.chipsData.split(",");
//				}
//				$scope.chipsStyle = $scope.chipStyle || {
//					"font-size": "12px",
//				    "border": "1px solid rgba(0,0,0,.2)",
//				    "border-radius": "8px",
//				    "background-color": "#fafafa",
//				    "margin": "2px 4px"
//				}
//			}
//		}
//	});