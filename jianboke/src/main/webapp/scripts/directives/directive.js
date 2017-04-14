'use strict';

angular.module('jianboke')
/**
 * 伸缩版块
 */
.directive('pxgToggleBlock', function() { // 面板收缩/显示属性
  return {
    restrict: 'EA',
    scope: {
      myLabel: '@',
      defaultExpanded: '='
    },
    transclude: true,
    template: '<div>'+
              '    <h3 layout="row" class="my-block-toggle-h3" ng-click="toggle()">' +
              '        <span ng-bind="myLabel"></span>' +
              '        <span flex></span>' +
              '        <md-icon>{{flag ? "expand_more" : "chevron_left"}}</md-icon>' +
              '    </h3>' +
              '    <div class="toggle-content" ng-transclude ng-hide="!flag"></div>' + 
              '</div>',
    link: function(scope, element, attrs) {
      scope.flag = scope.defaultExpanded;
      scope.toggle = function() {
        scope.flag = !scope.flag;
      };
    }
  };
})

.directive('pxgAccountMenu', function(ACCOUNT_MENU, $state, $rootScope) {
    return {
        restrict: 'E',
        templateUrl: 'views/account_menu.html',
        replace: true,
        link: function(scope, iElement, iAttr) {

        },
        controller: function($scope) {
            $scope.showMenu = false;
            $scope.accountMenu = ACCOUNT_MENU.list;
            $scope.toggleMenu = function(showMenu) {
                $scope.showMenu = !showMenu;
            };
            $scope.closeMenu = function() {
                $scope.showMenu = false;
            };
            $scope.goState = function(item) {
                $scope.showMenu = false;
                if (item.state === undefined && item.title == 'exit') {
                    console.log('exit');
                    var title = '确定要退出？',
                        content = '';
                    $rootScope.confirmMessage(title, content).then(function() {
                        $rootScope.logout();
                    }, function() {});
                } else {
                    $state.go(item.state);
                }
            }
        }
    }
})

/**
 * 存在bug: 通过顶端工具栏改变textarea内容时，无法将最新变动传递给conent，需要在点击、keyup等途径触发传递。
 * 如何监听 textarea value的改变(包括通过js改变)成为解决bug的思路之一！ 网上优选方案是input 和 propertychange 事件结合
 * 前者无法监听通过js改变的情况，后者虽然可以监听js改变的情况，但却是IE独有。
 */
.directive('pxgMarkDown', function($timeout) {
	return {
        restrict: 'E',
        template: '<div flex style="z-index: 1" data-ng-init="setValue()">' +
//			            '<md-button ng-click="setValue()"></md-button>' +
                    '<div id="editormd" flex style="z-index: 89;"></div>' +
                    '<textarea ng-model="value" style="display:none"></textarea>' +
                  '</div>',
        replace: true,
        scope: {
            content: '=ngModel'
        },
        link: function(scope, iElement, iAttrs) {
            scope.editor = editormd({
                id   : "editormd",
                path : "bower_components/editor.md/lib/",
                htmlDecode      : true,
                htmlDecode      : "script,iframe",  // you can filter tags decode
                flowChart       : true,
                tex             : true
            });
            scope.getValue = function() {
                scope.content = scope.editor.getMarkdown();
            }
            iElement.on('change keyup blur input propertychange click', function() {
                scope.$apply(scope.getValue);
            });
            var setValue = function(){
                scope.editor.setMarkdown(scope.content);
            };
            // TODO 如何改进，让页面完全渲染完毕后去执行此方法？好几种方法都尝试过，失败：
            // 1. scope.$on('$viewContentLoaded',functon(){})
            // 2. scope.$watch('$viewContentLoaded', function(){})
            // 3. ng-init / data-ng-init都失败。
//            window.setTimeout(setValue, 1000);
            $timeout(function() {
                setValue();
            }, 1000);
        }
    }
})
/**
 * markdown解析指令
 */
.directive('pxgMarkdownParse', function($timeout){
  return {
    scope: {
      mdId: '@', // 一个需要解析的md文件对应一个id
      mdData: '=' //  需要解析的md文本字符
    }, 
    restrict: 'AE', // E = Element, A = Attribute, C = Class, M = Comment
    template: '<div id="{{mdId}}" style="width: auto;">' +
                ' <textarea id="append-test" style="display:none;">{{mdData}}</textarea>' +
              '</div>',
    replace: true,
    transclude: true,
    link: function($scope, iElm, iAttrs) {
      $scope.getMarkHtml = function() {
        var id = $scope.mdId; //有多个需要解析的markdown内容(多个场景)，需要一一对应
        var testEditormdView = editormd.markdownToHTML(id, {
            htmlDecode      : true,  // 开启 HTML 标签解析，为了安全性，默认不开启 
            flowChart       : true,  // 默认不解析，流程图
            tex             : true
          });
      };
      // 设置timeout可以让其中的代码跳出digest循环外。从而实现“先渲染，后解析”的效果
      $timeout(function() {
        $scope.getMarkHtml();
      }, 0)
    }
  };
})

//展示chips的指令，传入的chips-data可以使以逗号分隔的字符串或者数组，chipsStyle是css样式对象，不传入则使用默认值
.directive('pxgChipsShow', function() {
    return {
        restrice: 'AE',
        scope: {
            chipsData: '=',
            chipsStyle: '='
        },
        template: '<span>' +
                        '<span ng-repeat="item in chipsData" ng-bind="item" ng-style="chipsStyle"></span>' +
                  '</span>',
        controller: function($scope) {
            if(!Array.isArray($scope.chipsData) && $scope.chipsData != '') {
                $scope.chipsData = $scope.chipsData.split(",");
            }
            $scope.chipsStyle = $scope.chipStyle || {
                "font-size": "12px",
                "border": "1px solid rgba(0,0,0,.2)",
                "border-radius": "8px",
                "background-color": "#fafafa",
                "margin": "2px 4px"
            }
        }
    }
});