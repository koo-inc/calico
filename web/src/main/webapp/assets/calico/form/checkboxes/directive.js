(function () {
  'use strict';

  angular.module('calico').directive('clcCheckboxes', function($timeout){
    return {
      restrict: 'A',
      templateUrl: 'assets/calico/form/checkboxes/template.html',
      scope: {
        options: '=',
        valueProp: '@?',
        labelProp: '@?',
        ngDisabled: '=?'
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
            return option[valueProp()];
          }
          function label(option){
            return option[labelProp()];
          }
          function hasValue(modelValue, val){
            return modelValue && modelValue.indexOf(val) > -1;
          }
          function addedValue(modelValue, val){
            return (modelValue || []).add(val).sortBy(function(e){return e;});
          }
          function removedValue(modelValue, val){
            return modelValue.remove(function(v){
              return v == val;
            }).sortBy(function(e){return e;});
          }

          scope.isActive = function(option){
            var modelVal = ngModelCtrl.$modelValue;
            var val = value(option);
            return hasValue(modelVal, val);
          };
          scope.onClick = function(option, $event){
            $event.stopPropagation;
            var modelVal = ngModelCtrl.$modelValue;
            var val = value(option);
            if(hasValue(modelVal, val)){
              ngModelCtrl.$setViewValue(removedValue(modelVal, val));
            }else{
              ngModelCtrl.$setViewValue(addedValue(modelVal, val));
            }
          };
          scope.getLabel = function(option){
            return label(option);
          };

          //空配列はrequiredチェックにかかるように
          ngModelCtrl.$isEmpty = function(value){
            if(value == null) return true;
            return value.length == 0;
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
