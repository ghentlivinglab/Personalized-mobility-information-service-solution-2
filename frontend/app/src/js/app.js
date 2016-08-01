var app = angular.module("App", ['ngResource', 'ngMap', 'ngRoute', 'ui.bootstrap', 'google.places', 'LocalStorageModule']);


app.config(['$routeProvider', function($routeProvider){
	$routeProvider.
		when('/index',{
			templateUrl:'view/login/loginPage.html',
			controller: 'MainController'
		}).
		when('/userindex',{
			templateUrl:'view/user/userIndex.html',
			controller: 'UserIndexController'
		}).
		when('/verify',{
			templateUrl:'view/user/verification.html'
		}).
		when('/validation',{
			templateUrl:'view/user/validation.html',
			controller: 'ValidationController'
		}).
		when('/users/:userId/events', {
			templateUrl: 'view/event/eventList.html',
			controller: 'EventController'
		}).
		when('/registration',{
			templateUrl: 'view/user/register.html',
			controller: 'RegistrationController'
		}).
		when('/adminIndex', {
			templateUrl: 'view/admin/adminIndex.html',
			controller: 'AdminController'
		}).
		when('/dataDump', {
			templateUrl: 'view/admin/dataDump.html',
			controller: 'AdminController'
		}).
		otherwise({
			redirectTo: '/index'
		});

}]);
