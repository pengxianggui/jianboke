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

/**
 * 存在bug: 通过顶端工具栏改变textarea内容时，无法将最新变动传递给conent，需要在点击、keyup等途径触发传递。
 * 如何监听 textarea value的改变(包括通过js改变)成为解决bug的思路之一！ 网上优选方案是input 和 propertychange 事件结合
 * 前者无法监听通过js改变的情况，后者虽然可以监听js改变的情况，但却是IE独有。
 */
.directive('pxgMarkDown', function($timeout,$rootScope) {
	return {
        restrict: 'E',
        template: '<md-content flex style="z-index: 1; overflow: hidden" data-ng-init="setValue()">' +
//			            '<md-button ng-click="setValue()"></md-button>' +
                    '<div id="editormd" flex style="z-index: 89;"></div>' +
                    '<textarea ng-model="value" style="display:none"></textarea>' +
                  '</md-content>',
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
                tex             : true,
                onload: function() {
                    this.setMarkdown(scope.content);
                    if ($rootScope.showDarkTheme) { // 黑暗主题
                        this.setTheme('dark');
                        this.setPreviewTheme('dark');
                        this.setEditorTheme('pastel-on-dark');
                    }
                },
                onchange: function() {
                    scope.$apply(scope.getValue);
                },
                imageUpload    : true,
                imageFormats   : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
                imageUploadURL : "./api/article/img",
            });

            scope.getValue = function() {
                scope.content = scope.editor.getMarkdown();
            }
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
    template: '<md-content flex id="{{mdId}}" style="width: auto;">' +
                ' <textarea id="append-test" style="display:none;">{{mdData}}</textarea>' +
              '</md-content>',
    replace: true,
    transclude: true,
    link: function($scope, iElm, iAttrs) {
      $scope.getMarkHtml = function() {
        var id = $scope.mdId; //有多个需要解析的markdown内容(多个场景)，需要一一对应
        var testEditormdView = editormd.markdownToHTML(id, {
            htmlDecode      : true,  // 开启 HTML 标签解析，为了安全性，默认不开启
            flowChart       : true,  // 默认不解析，流程图
            tex             : true,
//            theme : "dark",
//            previewTheme    : "dark"
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
        restrict: 'AE',
        scope: {
            chipsData: '=',
            chipsStyle: '='
        },
        replace: true,
        template: '<span>' +
                        '<span ng-repeat="item in data" ng-bind="item" ng-style="style"></span>' +
                  '</span>',
        controller: function($scope) {
            if(!Array.isArray($scope.chipsData) && $scope.chipsData != null && $scope.chipsData != '') {
                $scope.data = $scope.chipsData.split(",");
            }
            $scope.style = $scope.style || {
                "font-size": "12px",
                "border": "1px solid rgba(120,120,120,.2)",
                "border-radius": "8px",
//                "background-color": "#fafafa",
                "margin": "2px 4px"
            }
        }
    }
})

// 倒计时按钮，用于发送验证码类似功能，可配置点击触发传入的操作。操作被执行后，倒计指定时间time，期间，按钮不点击，倒计时结束后，按钮可点击可重新触发该操作。
.directive('pxgCountDownButton', function($interval) {
    return {
        restrict: 'AE',
        scope: {
            pxgAction: '&', // 传递引用
            pxgTime: '@', // 传递字符串
            pxgDisabled: '=' // 绑定
        },
        template: '<md-button ng-click="action()" class="md-primary md-raised" ng-disabled="pxgDisabled || countDowning">' +
                    '<span>发送验证码<span ng-if="countDowning">({{timeLong}})</span></span>' +
                  '</md-button>',
        link: function(scope, ele, attrs) {
            scope.countDowning = false; // 是否触发action操作
            scope.pxgTime -= 0;
            var countDownTimer; // 定时器
            scope.action = function() {
                scope.countDowning = true;
                scope.timeLong = scope.pxgTime;
                countDownTimer = $interval(function() {
                    countDown();
                }, 1000);
                scope.pxgAction(); // 执行action
            }
            var countDown = function() {
                if (scope.timeLong <= 0) {
                scope.countDowning = false;
                    $interval.cancel(countDownTimer);
                } else {
                    scope.timeLong--;
                }
            }
        }
    }
})
// 滑动效果
.directive('pxgSlideEffect', function() {
    return {
        restrict: 'A',
        link: function(scope, ele, attrs) {
            var buttonArr = $(ele).children("button.nav");
            for (var i = 0; i < buttonArr.length; i++) {
//                var button = buttonArr[i];
//                $(button).mouseenter(function() {
//                    console.log('鼠标进入 button');
//                    console.log(this);
//                });
//                $(button).mouseleave(function() {
//                    console.log('鼠标离开 button');
//                })
            }
        }
    }
})
.directive('pxgHoverAction', function() {
    return {
        restrict: 'A',
        link: function(scope, ele, attrs) {
            var bookArea = $(ele).find(".book-area")[0];
            $(ele).mouseenter(function() {
                $(bookArea).height($(ele).innerHeight() + 'px');
            });
            $(ele).mouseleave(function() {
                $(bookArea).height(0);
            })
        }
    }
})
.directive('pxgScrollBottomLoading', function() {
    return {
        restrict: 'AE',
        scope: {
            pxgLoadAction: '&'
        },
        link: function(scope, ele, attrs) {
            $(ele).scroll(function() {
                var _this = $(this);
//                console.log('可见高度:' + _this.height());
//                console.log('内容高度:' + _this.get(0).scrollHeight);
//                console.log('滚动高度:' + _this.scrollTop());
                if (_this.scrollTop() / (_this.get(0).scrollHeight -_this.height()) > 0.99999) { // 滚动条逼近底部时，加载
                    scope.pxgLoadAction();
                }
            });
        }
    }
})
;