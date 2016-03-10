'use strict';

// Declare app level module which depends on views, and components
var app = angular.module('myApp', [
  'ngRoute',
  'ngResource',
  'ui.router',
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({redirectTo: '/view1'});
}]);

app.config(function($stateProvider) {
  $stateProvider.state('companies', { // state for showing all movies
    url: '/companies',
    templateUrl: 'html/companies.html',
    controller: 'CompanyCtrl'
  }).state('viewCompany', { //state for showing single movie
    url: '/company/:id/view',
    templateUrl: 'html/view-company.html',
    controller: 'CompanyViewCtrl'
  }).state('newCompany', {
    url: '/companies/new',
    templateUrl: 'html/add-company.html',
    controller: 'CompanyCreateCtrl'
  }).state('editCompany', { //state for updating a movie
    url: '/company/:id/edit',
    templateUrl: 'html/edit-company.html',
    controller: 'CompanyCtrl'
  });
}).run(function($state) {
  $state.go('companies');
});

app.factory('errorHandler', function ($injector) {
    return function(err){
//        if (err.status >= 400 && err.status < 500) {
//            var $rootScope = $injector.get("$rootScope");
//            $rootScope.authenticated = false;
//            return;
//        }
        console.log(err);
        var container = document.getElementsByClassName("error-container");
        container[0].innerHTML = '<div class="alert alert-danger" role="alert">' +
            ((typeof err.data != 'undefined' && err.data != null) ? err.data.error : err)
            + '</div>';
    }
});



app.controller('CompanyCtrl', function($scope, Company) {

    var entries = Company.query(function() {
        console.log(entries);
    });
    $scope.companyList = entries;

//    $scope.entry = new Company(); //You can instantiate resource class
//
//    $scope.entry.data = 'some data';
//
//    Entry.save($scope.entry, function() {
//    //data saved. do something here.
//    }); //saves an entry. Assuming $scope.entry is the Entry object
//
//    $scope.selectedModelIndex = 0;
//    $scope.selectedColorIndex = -1;
//    $scope.selectedTypeIndex = -1;
//    $scope.selectedMaterialIndex = -1;
//    $scope.n = 20;
//
//    $scope.searchResult = {value: "Test"};
//    $scope.searchItems = [];

    $scope.showCompanyInfo = function(id) {
        console.log(id)
        var entry = Company.get({ id: id }, function() {
            console.log(entry);
        });
    }
});

app.controller('CompanyViewCtrl', function($scope, $stateParams, Company) {

    var entry = Company.get({ id: $stateParams.id }, function() {
        console.log(entry);
    });

    $scope.entry = entry;
});

app.controller('CompanyCreateCtrl', function($scope, $state, $stateParams, Company) {

    $scope.company = new Company();
    $scope.company.beneficials = [
        {name: ''}
    ];
    $scope.beneficialNumber = 1;

    $scope.getNumber = function(num) {
        return new Array(num);
    }

    $scope.addBeneficial = function() {

        if ($scope.company.beneficials.length < 10) {
            $scope.company.beneficials.push({
                name: ''
            });
        }

    }

    $scope.addCompany = function() {
        $scope.company.$save(function() {
            $state.go('companies');
        });
    };
});
