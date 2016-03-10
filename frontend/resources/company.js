app.factory('Company', function ($resource, errorHandler) {

    var requestUri = 'http://localhost:8080/companyApp/companies/:id';

    return $resource(requestUri,
        { id: '@id' },
        {
            query: {
                method: 'GET',
                interceptor: {
                    responseError: errorHandler
                },
                isArray: true
            },
            get: {
                method: 'GET',
                interceptor: {
                    responseError: errorHandler
                }
            },
            update: {
                method: 'PUT',
                interceptor: {
                    responseError: errorHandler
                }
            },
            save: {
                method: 'POST',
                interceptor: {
                    responseError: errorHandler
                }
            }
        }
    );
});