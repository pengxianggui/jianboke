'use strict';

angular.module('jianboke')
	// book章节树Block
	.directive('myBookTree', function(Book, IntegralUITreeViewService, $rootScope, Chapter) {
		return {
			restrict: 'AE',
			templateUrl: 'views/treeOfBooks.html',
			replace: true,
			controller: function($scope) {
				$scope.treeName = 'categoryTree';
				$scope.selectedBook;
				$scope.nodes = [];
				Book.query().$promise.then(function(data) {
					$scope.books = data;
				});
				var objEvents = {
			      addClick: function (obj) {
			        return addClick(obj);
			      },
			      insertClick: function (obj) {
			        return insertClick(obj);
			      },
			      appendClick: function (obj) {
			        return appendClick(obj);
			      },
			      editClick: function (obj) {
			        return editClick(obj);
			      },
			      deleteClick: function (obj) {
			        return deleteClick(obj);
			      },
			      listClick: function (obj) {
			        return listClick(obj);
			      }
			    };
				/**
				 * 添加一个子栏目
				 */
				var addClick = function (obj) {
			      if (obj) {
			        var selItem = IntegralUITreeViewService.findItemById($scope.treeName, obj.id);
			        var sortNum = 0;
			        if (selItem.items && selItem.items.length > 0) {
			          sortNum = selItem.items.length;
			          showAddDialog(obj, sortNum);
			        } else {
			          GroupItem.groupItemsList({
			            groupId: obj.id
			          }).$promise.then(function (result) {
			            if (result == null || result.length == 0) {
			              showAddDialog(obj, sortNum);
			            } else {
			              $rootScope.showResult('添加失败', '该产品组已有表或资源，不能添加子产品。', false, false);
			            }
			          });
			        }
			      }
			    };
			    /**
			     * 编辑当前栏目
			     */
			    var editClick = function (obj) {
			      if (obj) {
			        console.log(obj);
			        $mdDialog.show({
			          templateUrl: 'group_edit.html',
			          controller: DialogController,
			          locals: {
			            group: {
			              groupName: obj.groupName,
			              cpId: obj.cpId,
			              id: obj.id,
			              parentName: obj.parentName,
			              description: obj.description,
			              sortNum: obj.sortNum,
			              parentId: obj.parentId
			            }
			          }
			        }).then(function (result) {
			          console.log(result)
			          var item = IntegralUITreeViewService.findItemById($scope.treeName, obj.id);
			          item.groupName = result.groupName;
			          item.templateObj.groupName = result.groupName;
			          item.description = result.description;
			          item.templateObj.description = result.description;
			          IntegralUITreeViewService.refresh($scope.treeName, obj.id);
			        }, function (e) {
			          console.log(e);
			        });
			      }
			    };
			    /**
			     * 删除当前栏目
			     */
			    var deleteClick = function (obj) {
			      if (obj) {
			        var item = IntegralUITreeViewService.findItemById($scope.treeName, obj.id);
			        console.log(item);
			        if (item.items && item.items.length > 0) {
			          $rootScope.showResult('删除失败', '【' + item.groupName + '】下面还有产品，不能删除。', false, false);
			          return;
			        }
			        GroupItem.groupItemsList({
			          groupId: obj.id
			        }).$promise.then(function (value) {
			          if (value.length != 0) {
			            $rootScope.showResult('删除失败', '产品【' + obj.groupName + '】包含表或资源，删除失败。', false, false);
			          }
			          else {
			            //删除组
			            ProductGroup.remove({
			              id: obj.id
			            }).$promise.then(function () {
			              $rootScope.showResult('删除成功', '产品【' + obj.groupName + '】删除成功。', true, false);
			              var item = IntegralUITreeViewService.findItemById($scope.treeName, obj.id);
			              if (item) {
			                IntegralUITreeViewService.removeItem($scope.treeName, item);
			                IntegralUITreeViewService.refresh($scope.treeName, obj.id);
			              }
			            }).catch(function (httpResponse) {
			              $rootScope.showResult('删除失败', '【' + obj.groupName + '】删除失败。', false, false);
			            });
			          }
			        });
			      }
			    };
			    /**
			     * 列出当前栏目下所有的表
			     */
			    var listClick = function (obj) {

			      console.log(obj);
			      var node = {};
			      node.id = obj.id;
			      node.cpId = obj.cpId;
			      node.groupName = obj.name;
			      node.sortNum = obj.sortNum;
			      node.parentId = obj.parentId;
			      node.parentName = obj.parentName;
			      $scope.listTable(node);
			    };
			    var clearList = function () {
			    	IntegralUITreeViewService.clearItems($scope.treeName);
			    };
			    $scope.customItemTemplate = {url: '\'item-template.html\''};
			    var addItem = function (parent, chapterGroup) {
			        var node = {
			          groupName: chapterGroup.groupName,
			          text: chapterGroup.groupName,
			          id: chapterGroup.id,
			          bookId: chapterGroup.bookId,
			          description: chapterGroup.description,
			          parentId: chapterGroup.parentId,
//			          parentName: chapterGroup.parentName,
			          //items:category.nodes,
			          sortNum: chapterGroup.sortNum,
			          templateObj: {
			            id: chapterGroup.id,
			            text: chapterGroup.groupName,
			            groupName: chapterGroup.groupName,
			            bookId: chapterGroup.bookId,
			            parentId: chapterGroup.parentId,
//			            parentName: chapterGroup.parentName,
			            description: chapterGroup.description,
			            //items:category.nodes,
			            sortNum: chapterGroup.sortNum,
			            events: objEvents
			          },
			          expanded: true,
			        };
			        IntegralUITreeViewService.addItem($scope.treeName, node, parent);
			        if (chapterGroup.items) {
			        	chapterGroup.items.forEach(function (t) {
			            addItem(node, t); //迭代
			          });
			        }
			      };
				// 加载树
				$scope.refreshTree = function (event) {
			      IntegralUITreeViewService.beginLoad($scope.treeName, null, {type: 'linear', speed: 'fast', opacity: 0.25});
			      Chapter.getTree({"bookId": $scope.selectedBook.id}).$promise.then(function (value, responseHeaders) {
			        $scope.chaptersTree = value;
			        console.log($scope.chaptersTree);
			        IntegralUITreeViewService.suspendLayout($scope.treeName);
			        clearList();
			        addItem(null, $scope.chaptersTree);
			        IntegralUITreeViewService.resumeLayout($scope.treeName);
//				        $scope.selectedCategory = null;
			      }).catch(function (httpResponse) {
			        $scope.loading = false;
			        $rootScope.popMessage('加载失败', false);
			      });
			    };
			    $scope.$watch('selectedBook', function(newValue, oldValue) {
			    	if (newValue != null) {
			    		$scope.refreshTree();
			    	}
			    });
			}
		}
	})