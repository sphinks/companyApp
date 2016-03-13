app.controller('CompanyCtrl', function($scope, Company) {

    var entries = Company.query(function() {
        console.log(entries);
    });
    $scope.companyList = entries;
});

app.controller('CompanyViewCtrl', function($scope, $stateParams, Company) {

    var company = Company.get({ id: $stateParams.id }, function() {
        console.log(company);
    });

    $scope.company = company;
});

app.controller('CompanyCreateCtrl', function($scope, $state, $stateParams, Company) {

    $scope.company = new Company();
    $scope.company.beneficials = [
        {name: ''}
    ];

    $scope.addBeneficial = function() {
        if ($scope.company.beneficials.length < 10) {
            $scope.company.beneficials.push({
                name: ''
            });
        }
    }

    $scope.deleteBeneficial = function(index) {
        if ($scope.company.beneficials.length > 0) {
            $scope.company.beneficials.splice(index, 1);
        }
    }

    $scope.addCompany = function() {
        $scope.company.$save(function() {
            $state.go('companies');
        });
    };
});

app.controller('CompanyEditCtrl', function($scope, $state, $stateParams, Company) {

    var company = Company.get({ id: $stateParams.id }, function() {
        console.log(company);
    });

    $scope.company = company;

    $scope.editCompany = function() {
        $scope.company.$update(function() {
          $state.go('companies');
        });
    };

    $scope.addBeneficial = function() {
        if ($scope.company.beneficials.length < 10) {
            $scope.company.beneficials.push({
                name: ''
            });
        }
    }

    $scope.deleteBeneficial = function(index) {
        if ($scope.company.beneficials.length > 0) {
            $scope.company.beneficials.splice(index, 1);
        }
    }
});