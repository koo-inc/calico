(function () {
  'use strict';

  angular.module('calico').provider('PopoverService', function () {

    this.$get = function ($popover, $rootScope, RootScopeUtil) {
      var service = {};

      service.open = function(title, template, element, scopeValues, opts){
        var sc = $rootScope.$new(true);
        RootScopeUtil.setCommonProps(sc);
        angular.extend(sc, scopeValues);

        var options = {
          templateUrl: BASE_TEMPLATE_ID,
          contentTemplate: template,
          title: title,
          scope: sc,
          placement: 'bottom-left',
          autoClose: true,
          animation: 'am-fade'
        };
        if(opts){
          angular.extend(options, opts);
        }

        var popover = $popover(element, options);
        popover.$promise.then(popover.show);
        return popover;
      };

      return service;
    };
  });

  var BASE_TEMPLATE_ID = 'calico/popover/popover.tpl.html';
  var BASE_TEMPLATE =
    '<div class="popover">' +
      '<div class="arrow"></div>' +
      '<h3 class="popover-title">' +
        '<span ng-bind="title"></span>' +
        '<button type="button" ng-click="$hide()" glyphicon="remove" class="close"></button>' +
      '</h3>' +
      '<div class="popover-content" ng-bind="content"></div>' +
    '</div>';
  angular.module('calico').run(['$templateCache', function($templateCache) {
    $templateCache.put(BASE_TEMPLATE_ID, BASE_TEMPLATE);
  }]);

}());
