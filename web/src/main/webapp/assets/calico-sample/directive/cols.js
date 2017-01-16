(function () {
  'use strict';

  angular.module('calicoSample').directive('cols', function(){
    return {
      restrict: 'A',
      compile: function(element, attrs){
        var cols = angular.fromJson(attrs.cols.replace(/'/g, '"'));
        if(cols == null || !angular.isArray(cols)) return;

        angular.forEach(cols, function(col){
          var e = angular.element('<col>');
          if(col === null){
            element.append(e);
            return;
          }
          if(angular.isNumber(col)){
            col = '' + col + '%';
          }
          e.css('width', col);
          element.append(e);
        });

        element.removeAttr('cols');
        delete attrs.cols;
      }
    };
  });
}());
