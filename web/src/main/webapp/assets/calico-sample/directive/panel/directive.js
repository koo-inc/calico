(function () {
  'use strict';

  angular.module('calicoSample').directive('panel', function(){
    return {
      restrict: 'A',
      templateUrl: 'assets/calico-sample/directive/panel/template.html',
      scope: true,
      transclude: true,
      compile: function(element){
        element.addClass('panel panel-default');

        return function(scope, element, attrs){
          scope.panelTitle = attrs.panel;
        }
      }
    };
  });
}());
