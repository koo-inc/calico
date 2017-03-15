(function () {
  'use strict';

  angular.module('calico').provider('ModalService', function () {

    this.$get = function ($modal, $rootScope, RootScopeUtil) {
      var service = {};

      service.open = function(title, template, scopeValues, opts){
        var sc = $rootScope.$new(true);
        RootScopeUtil.setCommonProps(sc);
        angular.extend(sc, scopeValues);

        var options = {
          templateUrl: BASE_TEMPLATE_ID,
          contentTemplate: template,
          title: title,
          scope: sc,
          animation: 'am-fade',
          show: false
        };
        if(opts){
          angular.extend(options, opts);
        }

        var modal = $modal(options);
        modal.$promise.then(modal.show);
        return modal;
      };

      return service;
    };
  });

  var BASE_TEMPLATE_ID = 'calico/modal/modal.tpl.html';
  var BASE_TEMPLATE =
    '<div class="modal">' +
    '  <div class="modal-dialog modal-lg">' +
    '    <div class="modal-content">' +
    '      <div class="modal-header">' +
    '        <button type="button" class="close" ng-click="$hide()">&times;</button>' +
    '        <h4 class="modal-title"><span class="title" ng-bind="title"></span></h4>' +
    '      </div>' +
    '      <div class="modal-body" ng-bind="content"></div>' +
    '    </div>' +
    '  </div>' +
    '</div>';
  angular.module('calico').run(['$templateCache', function($templateCache) {
    $templateCache.put(BASE_TEMPLATE_ID, BASE_TEMPLATE);
  }]);

}());
