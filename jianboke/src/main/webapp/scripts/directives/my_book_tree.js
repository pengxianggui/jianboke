'use strict';

angular.module('jianboke')
	// book章节树Block
	.directive('myBookTree', function(Book, IntegralUITreeViewService, $rootScope, Chapter, BookChapterArticle, $state) {
		return {
			restrict: 'AE',
			templateUrl: 'views/template/treeOfBooks.html',
			replace: true,
//			scope: {
//			    type: '@', // type 有两个值: READ(阅读模式) 和 EDIT(编辑模式)
//			    pxgData: '=',
//			    pxgAction: '&'
//			},
			controller: function($scope, $mdDialog) {
				$scope.treeName = 'categoryTree';
				$scope.selectedBook;
				if ($scope.book) {
				    $scope.selectedBook = $scope.book;
				    console.log($scope.selectedBook);
				}
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
			          // 判断当前节点下是否存在资源(文章)，若存在，则提示不能添加
			          BookChapterArticle.listByParentId({
			            parentId: obj.id.split('-')[0]
			          }).$promise.then(function (result) {
			            if (result == null || result.length == 0) {
			              showAddDialog(obj, sortNum);
			            } else {
			              $rootScope.tipMessage('该产品组已有表或资源，不能添加子产品。', null);
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
			        $mdDialog.show({
			          templateUrl: 'views/template/chapter-add-dialog.html',
			          controller: DialogController,
			          locals: {
			            group: {
			              groupName: obj.groupName,
			              bookId: obj.bookId,
			              id: obj.id.split('-')[0],
			              parentName: obj.parentName,
			              description: obj.description,
			              sortNum: obj.sortNum,
			              parentId: obj.parentId
			            }
			          }
			        }).then(function (result) {
			          var item = IntegralUITreeViewService.findItemById($scope.treeName, obj.id);
			          item.groupName = result.groupName;
			          item.templateObj.groupName = result.groupName;
			          item.description = result.description;
			          item.templateObj.description = result.description;
			          IntegralUITreeViewService.refresh($scope.treeName, obj.id);
			        }, function (e) {});
			      }
			    };
			    /**
			     * 删除当前章节
			     */
			    var deleteClick = function (obj) {
			      if (obj) {
			        // 根节点
			        if (obj.parentId == null || obj.parentId == undefined) {
                        $rootScope.tipMessage('根节点不允许删除。', null);
                        return;
			        }
			        var item = IntegralUITreeViewService.findItemById($scope.treeName, obj.id);
			        // 删除操作的具体执行逻辑
			        var deleteAction = function(title, content) {
                        $rootScope.confirmMessage(title, content, false, '确定', '取消', null).then(function() {
                            Chapter.remove({id: obj.id.split('-')[0]}).$promise.then(function(result) {
                                $rootScope.popMessage('删除成功', true);
                                var item = IntegralUITreeViewService.findItemById($scope.treeName, obj.id);
                                if (item) {
                                    IntegralUITreeViewService.removeItem($scope.treeName, item);
                                    IntegralUITreeViewService.refresh($scope.treeName, obj.id);
                                }
                            })
                        }, function() {}).catch(function(httpResponse) {
                            $rootScope.popMessage('删除失败', false);
                        });
			        }

			        BookChapterArticle.listByChapterIdDeeply({chapterId: obj.id.split('-')[0]}).$promise.then(function(result) {
                        var title, content;
                        if ((item.items && item.items.length > 0)) { // 章节下存在子章节
                            title = '警告！';
                            content = '【' + obj.groupName + '】章节下包含了子章节，删除会导致子章节也全部删除！并且无法撤销。';
                            if (result.length > 0) {
                                content += '另外这些子章节下分布了' + result.length + '篇文章，此操作不会删除这些文章，但是会导致这些文章撤销归档且无法依靠程序恢复，您确定此操作吗？';
                            }
                        } else if (result.length > 0) { // result.length>0表明章节下存在文章
                            title = '警告';
                            content = '【' + obj.groupName + '】章节下包含了' + result.length + '篇文章，此操作不会删除这些文章，但是会导致这些文章撤销归档且无法依靠程序恢复，您确定此操作吗？'
                        } else {
                            title = '提示:';
                            content = '此操作无法恢复，您确定要删除【' + obj.groupName + '】章节吗?';
                        }
                        deleteAction(title, content);
                    })
			      }
			    };

			    /**
			     * 列出当前栏目下所有的表
			     */
			    var listClick = function (obj) {
			      var node = {};
			      node.id = obj.id;
			      node.bookId = obj.bookId;
			      node.groupName = obj.groupName;
			      node.parentId = obj.parentId;
			      node.sortNum = obj.sortNum;
			      node.parentName = obj.parentName;
			      node.description = obj.description;
//			      console.log($scope.pxgAction);
//			      if ($scope.pxgAction) {
//			        console.log(node);
//			        $scope.pxgAction(node, $scope.treeName);
//			      }
                  $scope.listTable(node);
			    };

			    var clearList = function () {
			    	IntegralUITreeViewService.clearItems($scope.treeName);
			    };
			    if ($scope.type == 'EDIT') { // 选取模板
			        $scope.customItemTemplate = {url: '\'item-template-EDIT.html\''};
			    } else {
			        $scope.customItemTemplate = {url: '\'item-template-READ.html\''};
			    }

			    var addItem = function (parent, chapterGroup) {
			        var node = {
			          groupName: chapterGroup.groupName,
			          text: chapterGroup.groupName,
			          id: chapterGroup.id + '-' + chapterGroup.parentId,
			          bookId: chapterGroup.bookId,
			          description: chapterGroup.description,
			          parentId: chapterGroup.parentId,
			          parentName: chapterGroup.parentName,
			          //items:category.nodes,
			          sortNum: chapterGroup.sortNum,
			          isArticle: chapterGroup.isArticle,
//			          icon: 'icon-book',
			          templateObj: {
			            id: chapterGroup.id + '-' + chapterGroup.parentId,
			            text: chapterGroup.groupName,
			            groupName: chapterGroup.groupName,
			            bookId: chapterGroup.bookId,
			            parentId: chapterGroup.parentId,
			            parentName: chapterGroup.parentName,
			            description: chapterGroup.description,
			            //items:category.nodes,
			            sortNum: chapterGroup.sortNum,
//			            icon: 'icon-book',
			            events: objEvents
			          },
			          expanded: chapterGroup.expanded,
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
                  var promise;
                  if ($scope.type == 'EDIT') {
                    promise = Chapter.getTree({
                        "bookId": $scope.selectedBook.id,
                        "articleId": $scope.articleId?$scope.articleId:-1
                    }).$promise;
                  } else {
                    promise = BookChapterArticle.getTree({
                        "bookId": $scope.selectedBook.id,
                        "articleId": $scope.articleId?$scope.articleId:-1
                    }).$promise;
                  }
			      promise.then(function (value, responseHeaders) {
			        console.log(value);
			        $scope.nodes = value;
			        IntegralUITreeViewService.suspendLayout($scope.treeName);
			        clearList();
			        addItem(null, $scope.nodes);
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
                //
                $scope.onLoadComplete = function () {
                  IntegralUITreeViewService.endLoad($scope.treeName);
                };

                // 添加一个空的章节
                var showAddDialog = function (obj, sortNum) {
                    $mdDialog.show({
                        templateUrl: 'views/template/chapter-add-dialog.html',
                        controller: DialogController,
                        //parent: angular.element(document.body),
                        locals: {
                            group: {
                                groupName: null,
                                bookId: obj.bookId,
                                parentName: obj.groupName,
                                parentId: obj.id.split('-')[0],
                                description: null,
                                sortNum: sortNum
                            }
                        }
                    }).then(function (result) {
                        var item = {
			              id: result.id + '-' + result.parentId,
                          text: result.groupName,
                          groupName: result.groupName,
                          bookId: result.bookId,
                          description: result.description,
                          parentId: result.parentId,
                          parentName: result.parentName,
                          sortNum: result.sortNum,
                          templateObj: {
			                id: result.id + '-' + result.parentId,
                            text: result.groupName,
                            groupName: result.groupName,
                            bookId: result.bookId,
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
                    }).catch(function (e) {})
                };
                // 列出当前node节点下所有的表
                $scope.listTable = function (node) {
                    $scope.articleModels = [];
                    $scope.currentChapter = IntegralUITreeViewService.findItemById($scope.treeName, node.id);
                    Chapter.getArticlesById({id: node.id.split('-')[0]}).$promise.then(function (value) {
                        console.log(value);
                        $scope.articleModels = value;
                    })
                };

                // 撤销某个文章的归档。即将文章移出该书
                $scope.deleteArticle = function (article) {
                  var articleTitle = article.articleTitle;
                  var title = '确认操作';
                  var content = '你确定将【' + articleTitle + '】从本书中移出吗？';
                  $rootScope.confirmMessage(title, content, false, '确定', '取消', null).then(function() {
                    BookChapterArticle.remove({ id: article.id }).$promise.then(function (value) {
                      $rootScope.popMessage('《' + articleTitle + '》移除成功。', true);
                      $scope.listTable($scope.currentChapter);
                    }).catch(function (httpResponse) {
                      $rootScope.popMessage('《' + articleTitle + '》移除失败。', false);
                    });
                  });
                };

                // 更改排序
                $scope.sortNum = function (articleModel) {
                  $mdDialog.show({
                    controller: SortNumController,
                    templateUrl: 'views/template/sort_num.html',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false,
                    locals: {
                      articleModel: articleModel
                    }
                  }).then(function (result) {
                    $scope.listTable($scope.currentChapter);
                  }).catch(function (data) {
                    $scope.listTable($scope.currentChapter);
                  });
                };

			    $scope.$watch('selectedBook', function(newValue, oldValue) {
			    	if (newValue != null) {
			    	    if($scope.type === 'READ') {
			    	        $state.go('book', {bookId: newValue.id});
			    	    }
			    		$scope.refreshTree();
			    	}
			    });

//			    $scope.refreshBook = function(book) {
//                    $state.go('book', {bookId: book.id});
//                }

                // 归档文章
                $scope.publishArticle = function(article, currentChapter) {
                    var articleModel = {
                        bookId : $scope.selectedBook.id,
                        parentId : currentChapter.id.split('-')[0],
                        articleId : article.id,
                        articleTitle : article.title,
                        sortNum : $scope.articleModels.length
                    };
                    BookChapterArticle.save(articleModel).$promise.then(function(result) {
                        if (result.id) {
                            $rootScope.popMessage('归档成功！', true);
                            $scope.listTable({id: currentChapter.id});
                        } else {
                            $rootScope.tipMessage('当前文章已经存在于该章节下，不能重复添加', null);
                        }
                    }).catch(function(httpResponse) {
                        $rootScope.popMessage('归档失败！', false);
                        $scope.listTable({id: currentChapter.id});
                    })
                }



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

                // 排序
                function SortNumController($stateParams, $scope, $rootScope, $mdToast, $mdDialog, BookChapterArticle, articleModel) {
                  $scope.articleModel = articleModel;
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
                      BookChapterArticle.updateSortNum({
                        id: articleModel.id,
                        newSortNum: $scope.index
                      }).$promise.then(function (result) {
                        if (result) {
                            $rootScope.popMessage('保存成功', true);
                        } else {
                            $rootScope.popMessage('保存失败', false);
                        }
                      }).catch(function () {
                        $rootScope.popMessage('保存失败', false);
                      })
                    }
                  }
                }
			}
		}
	})