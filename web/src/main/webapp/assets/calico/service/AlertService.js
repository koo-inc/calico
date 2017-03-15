(function () {
  'use strict';

  angular.module('calico').provider('AlertService', function () {

    var SESSION_STORAGE_RESERVE_KEY = 'reserved-alert';

    this.$get = function ($alert, SessionStorageService) {
      var service = {};

      var DURATION_FALSE = {};

      service.show = function(opts){
        var alert = null;
        if(opts.duration === false && opts.placement){
          //duration:falseが重ならないように指定されたplacementに表示中のアラートを非表示にする
          if(DURATION_FALSE[opts.placement]){
            DURATION_FALSE[opts.placement].destroy();
          }
          alert = $alert(opts);
          DURATION_FALSE[opts.placement] = alert;
        }else{
          alert = $alert(opts);
        }
        return alert;
      };

      service.reserve = function(opts){
        SessionStorageService.store(SESSION_STORAGE_RESERVE_KEY, opts);
      };

      service.showReserved = function(){
        var opts = SessionStorageService.restore(SESSION_STORAGE_RESERVE_KEY);
        if(opts){
          service.show(opts);
          service.clearReserved();
        }
      };

      service.clearReserved = function(){
        SessionStorageService.remove(SESSION_STORAGE_RESERVE_KEY);
      };

      return service;
    };
  }).run(function($rootScope, AlertService){
    $rootScope.$on('$routeChangeSuccess', function(){
      AlertService.showReserved();
    });
  });

}());
