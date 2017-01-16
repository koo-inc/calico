(function () {
  'use strict';

  angular.module('calico').directive('clcRadio', function($timeout){
    return {
      restrict: 'A',
      templateUrl: 'assets/calico/form/radio/template.html',
      scope: {
        label: '@?',
        ngValue: '=',
        ngDisabled: '=?'
      },
      require: 'ngModel',
      compile: function (element) {
        element.addClass('btn-group');
        element.attr('data-toggle', 'buttons');

        return function (scope, element, attrs, ngModelCtrl) {
          scope.isActive = function () {
            return  ngModelCtrl.$modelValue == scope.ngValue;
          };
          scope.onClick = function ($event) {
            $event.stopPropagation();
            ngModelCtrl.$setViewValue(scope.ngValue);
          };

          //focus
          $timeout(function () {
            element.on('focus', 'button', function () {
              element.addClass('focused')
            });
            element.on('blur', 'button', function () {
              element.removeClass('focused')
            });
          });
        }
      }
    }
  });
}());
