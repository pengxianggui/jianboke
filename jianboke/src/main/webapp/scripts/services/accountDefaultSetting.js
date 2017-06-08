'use strict';

angular.module('jianboke')
    .factory('AccountDefaultSetting', function($resource) {
        return $resource('api/accountDefaultSetting/:id', {}, {
            get: {
                url: 'api/accountDefaultSetting',
                method: 'GET'
            }
        })
    })