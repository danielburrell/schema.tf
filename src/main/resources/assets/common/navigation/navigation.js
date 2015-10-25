/*
 * Define a navigation module. every 'page' in my app has it's own module. each module should register it's navigation info here for display purposes.
 * To do this. Navigation service was created. every module should call 'push' passing a piece of info in. somebody somewhere should call
 * getAll to get all the items
 */
angular.module('navigation', ['apiservice']).directive('navbar', function() {
    return {
        restrict: 'A',
        templateUrl: '/assets/common/navigation/navbar.html.tpl',
        replace: true,
        controller: 'NavigationController',
    };
}).controller('NavigationController',
		[ '$scope', '$location', 'hatf2api', function($scope, $location, hatf2api) {
			$scope.pages = [{"url":"/apiguide", "name":"API" }, {"url":"/history", "name":"Schema History" }, {"url":"/current", "name":"Current Schema" }, {"url":"/notifications", "name":"Notifications" }];
			
			$scope.logout = function() {
				var xhr = new XMLHttpRequest();
				xhr.open('GET', '/api/logout');
				xhr.onload = function() {
					$location.url('/');
					$scope.$apply();
				};
				xhr.send();
			};
			
			function getUserInfo() {
				hatf2api.getUserInfo().then(function(data) {
					$scope.user = data.data;
				});
			}
			
			getUserInfo();
		} ]);