'use strict';

angular.module('jianboke')
/**
 * 伸缩版块
 */
.directive('myToggleBlock',
  function() { // 面板收缩/显示属性
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
/**
 * 嵌入markdown的指令
 */
.directive('myMarkDown', function() {
	return {
		restrict: 'E',
		template: '<div flex layout="column">' +
					'<div id="editormd" flex></div>' +
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
		}
	}
})
/**
 * markdown解析指令
 */
.directive('myMarkdownParse', function($timeout){
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
});