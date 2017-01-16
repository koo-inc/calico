(function () {
  'use strict';

  angular.module('calicoSample').directive('btn', function(){
    function prepareAttrs(btn){
      return btn.split(',').map(function(e){
        return e.trim();
      });
    }

    return {
      restrict: 'A',
      compile: function(element, attrs){
        element.addClass('btn');
        if(attrs.btn && !attrs.btn.isBlank()){
          angular.forEach(prepareAttrs(attrs.btn), function(btn){
            element.addClass('btn-' + btn);
          });
        }

        element.removeAttr('btn');
        delete attrs.btn;
      }
    };
  });
}());
