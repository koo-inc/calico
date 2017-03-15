(function () {
  'use strict';

  angular.module('calico').directive('clcRadios', function($timeout){
    return {
      restrict: 'A',
      templateUrl: 'assets/calico/form/radios/template.html',
      scope: {
        options: '=',
        valueProp: '@?',
        labelProp: '@?',
        ngDisabled: '=?',
        nullLabel: '@?'
      },
      require: 'ngModel',
      compile: function(element){
        element.addClass('btn-group');
        element.attr('data-toggle', 'buttons');

        return function(scope, element, attrs, ngModelCtrl){
          function valueProp(){
            return scope.valueProp !== undefined ? scope.valueProp : 'id';
          }
          function labelProp(){
            return scope.labelProp !== undefined ? scope.labelProp : 'name';
          }
          function value(option){
            if (option == null) return null;
            return option[valueProp()];
          }
          function label(option){
            if (option == null) return scope.nullLabel;
            return option[labelProp()];
          }

          scope.isActive = function(option){
            return ngModelCtrl.$modelValue == value(option);
          };
          scope.onClick = function(option, $event){
            $event.stopPropagation();
            ngModelCtrl.$setViewValue(value(option));
          };
          scope.getLabel = function(option){
            return label(option);
          };

          //focus
          $timeout(function(){
            element.on('focus', 'button', function(){
              element.addClass('focused')
            });
            element.on('blur', 'button', function(){
              element.removeClass('focused')
            });
          });
        }
      }
    };
  });
}());
