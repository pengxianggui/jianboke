'use strict';

angular.module('jianbo')
	.controller('DashBoardCtrl', function($scope, $timeout, $mdSidenav, $log, $state, $mdDialog, Book, Article) {
		console.log('DashBoardCtrl');
		$scope.books;
		$scope.articles;
		$scope.filter;
		$scope.showDarkTheme = false;
		$scope.toggleLeft = buildDelayedToggler('left');
//	    $scope.toggleRight = buildToggler('right');
	    $scope.isOpenRight = function(){
	      return $mdSidenav('right').isOpen();
	    };
	    
	    $scope.goToWrite = function() {
	    	$state.go('blog.newblog');
	    }

	    /**
	     * Supplies a function that will continue to operate until the
	     * time is up.
	     */
	    function debounce(func, wait, context) {
	      var timer;

	      return function debounced() {
	        var context = $scope,
	            args = Array.prototype.slice.call(arguments);
	        $timeout.cancel(timer);
	        timer = $timeout(function() {
	          timer = undefined;
	          func.apply(context, args);
	        }, wait || 10);
	      };
	    }

	    /**
	     * Build handler to open/close a SideNav; when animation finishes
	     * report completion in console
	     */
	    function buildDelayedToggler(navID) {
	      return debounce(function() {
	        // Component lookup should always be available since we are not using `ng-if`
	        $mdSidenav(navID)
	          .toggle()
	          .then(function () {
	            $log.debug("toggle " + navID + " is done");
	          });
	      }, 200);
	    }

//	    function buildToggler(navID) {
//	      return function() {
//	        // Component lookup should always be available since we are not using `ng-if`
//	        $mdSidenav(navID)
//	          .toggle()
//	          .then(function () {
//	            $log.debug("toggle " + navID + " is done");
//	          });
//	      }
//	    }

	    var getBooks = function() {
	    	Book.query().$promise.then(function(data){
    			$scope.books = data;
	    	});
	    };
	    
	    var getAllArticle = function() {
	    	Article.query({filter : $scope.filter || -1}, function(data){
	    		$scope.articles = data;
	    	});
	    };
	    
	    $scope.tiles = [
	         {'icon': '', 'title': 'title1', 'background': 'md-bg-green'},
	         {'icon': '', 'title': 'title2', 'background': 'md-bg-gray'},
	         {'icon': '', 'title': 'title3', 'background': 'md-bg-purple'},
	         {'icon': '', 'title': 'title4', 'background': 'md-bg-blue'},
	         {'icon': '', 'title': 'title5', 'background': 'md-bg-black'},
	         {'icon': '', 'title': 'title6', 'background': 'md-bg-green'}
	    ];
	    
	    /**
	     * 弹框添加一本book
	     */
	    $scope.popToAddBook = function(ev) {
	      $mdDialog.show({
	        controller: "BookAddCtrl",
	        templateUrl: 'views/book.add.html',
	        parent: angular.element(document.body),
	        targetEvent: ev,
	        locals: {
	        	Entity:  {
	        		bookName: null,
	        		bookIntro: null,
	        		authorId: null,
	        		bookCoverPath: null,
	        	}
	        },
	        clickOutsideToClose: false,
	      });
	    }
	    
	    var initialize = function() { // 初始化dashboard.html所需的数据
	    	getBooks();
	    	getAllArticle();
	    }
	    initialize();
	});