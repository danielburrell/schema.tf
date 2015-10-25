//define a new module called MyHatF2, then define a controller on that module
angular.module('home', [ 'ngRoute','apiservice', 'ngCookies' ]).config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : '/assets/app/home/home.html.tpl',
		controller : 'HomeCtrl',
	});
}).controller(
		'HomeCtrl', [ '$scope', '$window', '$cookies', '$location', 'hatf2api',
		function($scope, $window, $cookies, $location, hatf2api) {
			
			
			
			$scope.login = function() {
				//OH DEAR TODO !! must swap this client id out for one taken from the backend
				//step one, build an endpoint for this (in session)
				//step two, build a service to fetch and cache this
				
				var xhr = new XMLHttpRequest();
				xhr.open('GET', '/api/getClientId');
				xhr.onload = function() {
					var clientId = xhr.responseText
					var random = Math.random();
					//set {domain:}
					var host = $location.host();
					if (host != 'localhost') {
						host = host.replace("www.","");
						host = "."+host;
						console.log(host);
						$cookies.putObject("key", random, {domain:host});
					} else {
						$cookies.putObject("key", random);
					}
					
					$window.location.href = 'https://github.com/login/oauth/authorize?client_id='+clientId+'&scope=user:email&state='+random;
				};
				xhr.send();
				
			};
			
			$scope.logout = function() {
				var xhr = new XMLHttpRequest();
				xhr.open('GET', '/api/logout');
				xhr.onload = function() {
					getUserInfo();
				};
				xhr.send();
			};
			
			function getUserInfo() {
				hatf2api.getUserInfo().then(function(data) {
					$scope.user = data.data;
				});
			}
			
			getUserInfo();
			
		}]);
