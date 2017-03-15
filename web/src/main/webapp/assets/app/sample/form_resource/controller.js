(function () {
  'use strict';

  //text
  var TextCtrl = function ($scope){
    $scope.obj = {
      val3: '&'
    };
    $scope.onchange = function(prop){
      alert($scope.obj[prop]);
    };
  };

  //password
  var PasswordCtrl = function ($scope){
    $scope.obj = {
      val3: '&'
    };
    $scope.onchange = function(prop){
      alert($scope.obj[prop]);
    };
  };

  //textarea
  var TextareaCtrl = function ($scope){
    $scope.obj = {
      val3: 'あいうえお\nかきくけこ'
    };
    $scope.onchange = function(prop){
      alert($scope.obj[prop]);
    };
  };

  //radio
  var RadioCtrl = function($scope, OptionsService){
    $scope.obj = {
      val4: 1
    };
    $scope.options1 = [
      {id: 1, name: 'イヌ'},
      {id: 2, name: 'ネコ'},
      {id: 3, name: 'ネズミ'}
    ];
    OptionsService.values('sex').then(function(data){
      $scope.sexes = data.sex;
      $scope.options2 = data.sex.include({id: null, name: '不明'}, 0);
    });
    $scope.onchange = function(){
      alert($scope.obj.val4);
    };
  };

  //radios
  var RadiosCtrl = function($scope, OptionsService){
    $scope.obj = {
      val5: 1
    };
    $scope.options1 = [
      {id: 1, name: 'イヌ', en: 'dog', cry: 'ワンワン'},
      {id: 2, name: 'ネコ', en: 'cat', cry: 'ニャンニャン'},
      {id: 3, name: 'ネズミ', en: 'rat', cry: 'チューチュー'}
    ];
    OptionsService.values('sex').then(function(data){
      $scope.sexes = data.sex;
      $scope.options2 = data.sex.include({id: null, name: '不明'}, 0);
    });
    $scope.onchange = function(){
      alert($scope.obj.val5);
    };
  };

  //checkbox
  var CheckboxCtrl = function($scope){
    $scope.obj = {
      val4: true
    };
    $scope.onchange = function(){
      alert($scope.obj.val4);
    };
  };

  //checkboxes
  var CheckboxesCtrl = function($scope){
    $scope.obj = {
      val2: ["cat", "rat"]
    };
    $scope.options1 = [
      {id: 1, name: 'イヌ', en: 'dog', cry: 'ワンワン'},
      {id: 2, name: 'ネコ', en: 'cat', cry: 'ニャンニャン'},
      {id: 3, name: 'ネズミ', en: 'rat', cry: 'チューチュー'}
    ];
    $scope.onchange = function(){
      alert($scope.obj.val4);
    };
  };

  //select
  var SelectCtrl = function($scope, OptionsService){
    $scope.obj = {
      val4: 2
    };
    $scope.options1 = [
      {id: 1, name: 'イヌ'},
      {id: 2, name: 'ネコ'},
      {id: 3, name: 'ネズミ'}
    ];
    OptionsService.values('sex').then(function(data){
      $scope.options2 = data.sex;
    });
    $scope.onchange = function(prop){
      alert($scope.obj[prop]);
    };
  };

  //datepicker
  var DatepickerCtrl = function($scope){
    $scope.obj = {
      val3: Date.create('2015-01-08')
    };
    $scope.onchange = function(prop){
      alert($scope.obj[prop]);
    };
  };

  //timepicker
  var TimepickerCtrl = function($scope){
    $scope.obj = {
      val3: Date.create('19:15:00')
    };
    $scope.onchange = function(prop){
      alert($scope.obj[prop]);
    };
  };

  //file
  var FileCtrl = function($scope){
    $scope.obj = {
      val2: {
        id: "xxxxx",
        meta: {
          name: "hoge.jpg",
          size: 10000,
          type: "image/jpg"
        }
      }
    };
    $scope.onchange = function(){
      alert('change');
    };
  };

  //submit
  var SubmitCtrl = function($scope, ApiService, PromiseService){
    //clc-submitに指定する関数はPromiseを返さないとダメ
    $scope.alert = function(){
      alert('submitted');
      return PromiseService.resolved();
    };
    $scope.none = function(){
      return PromiseService.resolved();
    };
    $scope.sleep5Api = function(){
      return ApiService.post('./sleep5', {}).then(function(){
        console.log('done');
      });
    };
    $scope.obj = {};
  };

  //TABS
  angular.module('app').constant('TABS', [
    {title: "text", controller: TextCtrl},
    {title: "password", controller: PasswordCtrl},
    {title: "textarea", controller: TextareaCtrl},
    {title: "radio", controller: RadioCtrl},
    {title: "radios", controller: RadiosCtrl},
    {title: "checkbox", controller: CheckboxCtrl},
    {title: "checkboxes", controller: CheckboxesCtrl},
    {title: "select", controller: SelectCtrl},
    {title: "datepicker", controller: DatepickerCtrl},
    {title: "timepicker", controller: TimepickerCtrl},
    {title: "file", controller: FileCtrl},
    {title: "submit", controller: SubmitCtrl},
    {title: "errors"}
  ]);

  //route
  angular.module('app').config(function ($routeProvider, RESOURCE_ROOT, PARTIAL_PATH, TABS) {
    angular.forEach(TABS, function(tab){
      $routeProvider.when('/' + RESOURCE_ROOT + '/' + tab.title, {
        templateUrl: PARTIAL_PATH.APP[tab.title.toUpperCase()],
        controller: tab.controller
      })
    });
  });

  //tabs directive
  angular.module('app').directive('tabs', function(TABS, $path, RootScopeUtil){
    return {
      template: '<ul class="nav nav-tabs">' +
                  '<li ng-repeat="tab in tabs" ng-class="{active: $index == active}">' +
                    '<a href="{{$href(\'./\' + tab.title)}}">{{tab.title}}</a>' +
                  '</li>' +
                '</ul>',
      replace: true,
      scope: {},
      link: function(scope){
        RootScopeUtil.setCommonProps(scope);
        scope.tabs = TABS;
        scope.active = TABS.findIndex(function(tab){
          return tab.title == $path().split('/').last();
        });
        scope.$path = scope.$parent.$path;
      }
    };
  });

  //controllerSrc directive
  angular.module('app').directive('controllerSrc', function($route, $location){
    return {
      priority: 10,
      compile: function(element){
        var path = $location.path();
        if($route.routes[path] && $route.routes[path]['controller']){
          var controller = $route.routes[path]['controller'];
          var src = controller.toString();
          src = src.replace(/^\s{2}/mg, '');
          element.append(angular.element('<h3>Controller</h3>'));
          element.append(angular.element('<pre class="controller-src"></pre>').text(src));
        }
      }
    };
  });

  //sample directive
  angular.module('app').directive('sample', function(){
    return {
      priority: 11,
      compile: function(element, attrs){
        attrs.id = attrs.sample;
        element.attr('id', attrs.sample);
        element.after('<div sample-content="' + attrs.sample + '"></div>');
      }
    }
  }).directive('sampleContent', function($templateCache){
    var prepareSrc = function(tpl){
      return ('' + tpl).trim().replace(/^\s{4}/mg, '');
    };
    return {
      priority: 10,
      compile: function(element, attrs){
        var tpl = $templateCache.get(attrs.sampleContent);
        element.prepend(angular.element(tpl));
        element.prepend(angular.element('<pre ng-non-bindable></pre>').text(prepareSrc(tpl)));
      }
    }
  });

}());
