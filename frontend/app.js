'use strict';

// Declare app level module which depends on views, and components
var app = angular.module('myApp', [
  'ngResource',
  'ui.router',
]);

app.config(function($stateProvider) {
  $stateProvider.state('companies', {
    url: '/companies',
    templateUrl: 'html/companies.html',
    controller: 'CompanyCtrl'
  }).state('viewCompany', {
    url: '/company/:id/view',
    templateUrl: 'html/view-company.html',
    controller: 'CompanyViewCtrl'
  }).state('newCompany', {
    url: '/companies/new',
    templateUrl: 'html/add-company.html',
    controller: 'CompanyCreateCtrl'
  }).state('editCompany', {
    url: '/company/:id/edit',
    templateUrl: 'html/edit-company.html',
    controller: 'CompanyEditCtrl'
  });
}).run(function($state) {
  $state.go('companies');
});

app.factory('errorHandler', function ($injector) {
    return function(err){
        console.log(err);
        var container = document.getElementsByClassName("error-container");
        container[0].innerHTML = '<div class="alert alert-danger" role="alert">' +
            ((typeof err.data != 'undefined' && err.data != null) ? err.data.error : err)
            + '</div>';
    }
});
