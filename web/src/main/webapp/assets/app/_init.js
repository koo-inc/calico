(function() {
  'use strict';

  var app = angular.module('app', [
    'ngRoute',
    'ngSanitize',
    'ngAnimate',
    'mgcrea.ngStrap',
    'calico',
    'calicoSample'
  ]);

  //ngRoute
  app.config(function($locationProvider){
    $locationProvider.html5Mode(true);
  }).config(function($routeProvider){
    //routeがないときはreloadさせて404にする
    $routeProvider.otherwise({
      controller: function($window){
        $window.location.reload();
      },
      template: '<div></div>'
    });
  }).run(function($rootScope){
    //$routeChangeSuccess route変更時にスクロール位置をトップに
    $rootScope.$on('$routeChangeSuccess', function(){
      angular.element('body').scrollTop(0);
    });
  });

  //ApiService
  app.config(function(ApiServiceProvider){
    ApiServiceProvider.setRootUrl('api/');
  });

  //date filter format
  app.run(function($locale){
    $locale.DATETIME_FORMATS['mediumDate'] = 'yyyy/MM/dd(EEE)';
  });

  //datepicker
  app.config(function ($datepickerProvider) {
    angular.extend($datepickerProvider.defaults, {
      monthTitleFormat: 'yyyy年 MM月',
      dateFormat: 'yyyy/MM/dd',
      modelDateFormat: 'yyyy-MM-ddTHH:mm:ss.sssZ',
      autoclose: true,
      startWeek: 1
    });
  });

  //timepicker
  app.config(function ($timepickerProvider) {
    angular.extend($timepickerProvider.defaults, {
      timeFormat: 'HH:mm',
      modelTimeFormat: 'yyyy-MM-ddTHH:mm:ss.sssZ',
      //autoclose: true
      minuteStep: 5
    });
  });

}());
