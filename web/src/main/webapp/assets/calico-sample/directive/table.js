(function () {
  'use strict';

  angular.module('calicoSample').directive('simpleTable', function(){
    return {
      restrict: 'A',
      compile: function(element, attrs){
        element.addClass('table table-simple');

        element.removeAttr('simple-table');
        delete attrs.simpleTable;
      }
    };
  });

  angular.module('calicoSample').directive('listTable', function(){
    return {
      restrict: 'A',
      compile: function(element, attrs){
        element.addClass('table table-bordered table-striped table-list');

        element.removeAttr('list-table');
        delete attrs.listTable;
      }
    };
  });

  angular.module('calicoSample').directive('infoTable', function(){
    return {
      restrict: 'A',
      compile: function(element, attrs){
        element.addClass('table table-bordered table-striped table-info');

        element.removeAttr('info-table');
        delete attrs.infoTable;
      }
    };
  });

  angular.module('calicoSample').directive('searchTable', function(){
    return {
      restrict: 'A',
      compile: function(element, attrs){
        element.addClass('table table-bordered table-search');

        element.removeAttr('search-table');
        delete attrs.infoTable;
      }
    };
  });
}());
