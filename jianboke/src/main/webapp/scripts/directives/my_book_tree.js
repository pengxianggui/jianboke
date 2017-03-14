'use strict';

angular.module('jianboke')
	// book章节树Block
	.directive('myBookTree', function(Book, IntegralUITreeViewService, $rootScope, Chapter) {
		return {
			restrict: 'AE',
			templateUrl: 'views/template/treeOfBooks.html',
			replace: true,
			controller: function($scope, $mdDialog) {
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
			          templateUrl: 'views/template/chapter-add-dialog.html',
			          controller: DialogController,
			          locals: {
			            group: {
			              groupName: obj.groupName,
			              bookId: obj.bookId,
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
				$scope.refreshTree = function () {
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

                // 展开所有节点
                $scope.expandAll = function() {
                    IntegralUITreeViewService.expand($scope.treeName);
                }

                // 收起所有节点
                $scope.collapseAll = function() {
                    IntegralUITreeViewService.collapse($scope.treeName);
                }

                // 添加一个空的章节
                var showAddDialog = function (obj, sortNum) {
                      console.log(obj);
                      console.log(sortNum);
                      $mdDialog.show({
                        templateUrl: 'views/template/chapter-add-dialog.html',
                        controller: DialogController,
                        //parent: angular.element(document.body),
                        locals: {
                          group: {
                            groupName: null,
                            bookId: obj.bookId,
                            parentName: obj.groupName,
                            parentId: obj.id,
                            description: null,
                            sortNum: sortNum
                          }
                        }
                      }).then(function (result) {
                        console.log(result);
                        var item = {
                          id: result.id,
                          text: result.groupName,
                          groupName: result.groupName,
                          cpId: result.cpId,
                          description: result.description,
                          parentId: result.parentId,
                          parentName: result.parentName,
                          sortNum: result.sortNum,
                          templateObj: {
                            id: result.id,
                            text: result.groupName,
                            groupName: result.groupName,
                            cpId: result.cpId,
                            description: result.description,
                            parentId: result.parentId,
                            parentName: result.parentName,
                            sortNum: result.sortNum,
                            events: objEvents
                          },
                          expanded: true
                        };
                        var selItem = IntegralUITreeViewService.findItemById($scope.treeName, obj.id);
                        IntegralUITreeViewService.addItem($scope.treeName, item, selItem);
                      }).catch(function (e) {
                        console.log(e);
                      })
                    };

			    $scope.$watch('selectedBook', function(newValue, oldValue) {
			    	if (newValue != null) {
			    		$scope.refreshTree();
			    	}
			    });


			    // 添加/编辑节点时的弹出框对应的控制器
                function DialogController($stateParams, $scope, $rootScope, $mdToast, $mdDialog, group) {
                    $scope.group = group;
                    $scope.hide = function () {
                        $mdDialog.hide();
                    };
                    $scope.cancel = function () {
                        $mdDialog.cancel();
                    };
                    $scope.save = function (event) {
                        event.preventDefault();
                        angular.forEach($scope.form.$error.required, function (field) {
                          field.$setTouched();
                        });
                        if ($scope.form.$valid) {
                            Chapter.save($scope.group).$promise.then(
                              function (result, responseHeaders) {
                                $mdToast.show(
                                  $mdToast.simple()
                                    .content('保存成功。')
                                    .theme('success')
                                );
                                $mdDialog.hide(result);
                              }
                            ).catch(function (httpResponse) {
                              $rootScope.showResult('失败', '保存失败', false, false, httpResponse);
                              //$scope.hide();
                            });
                        }
                    }
                }
			}
		}
	})