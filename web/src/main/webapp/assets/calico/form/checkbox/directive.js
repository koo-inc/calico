(function () {
  'use strict';

  angular.module('calico').directive('clcCheckbox', function($timeout){
    return {
      restrict: 'A',
      templateUrl: 'assets/calico/form/checkbox/template.html',
      scope: {
        label: '@?',
        ngTrueValue: '=?',
        ngFalseValue: '=?',
        ngDisabled: '=?',
        realValue: '=ngModel'
      },
      require: 'ngModel',
      compile: function(element){
        element.addClass('btn-group');
        element.attr('data-toggle', 'buttons');

        return function (scope, element, attrs, ngModelCtrl) {
          function trueValue(){
            return scope.ngTrueValue !== undefined ? scope.ngTrueValue : true;
          }
          function falseValue(){
            if(scope.ngFalseValue !== undefined){
              return scope.ngFalseValue;
            }
            return scope.ngTrueValue !== undefined ? null : false;
          }

          scope.isActive = function () {
            return ngModelCtrl.$modelValue == trueValue();
          };
          scope.onClick = function ($event) {
            $event.stopPropagation();
            ngModelCtrl.$setViewValue(ngModelCtrl.$modelValue != trueValue() ? trueValue() : falseValue());
          };

          $timeout(function() {
            // init value
            scope.realValue = scope.realValue == trueValue() ? trueValue() : falseValue();

            //focus
            element.on('focus', 'button', function () {
              element.addClass('focused')
            });
            element.on('blur', 'button', function () {
              element.removeClass('focused')
            });
          });
        }
      }
    };
  });
}());
