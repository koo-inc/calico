(function () {
  'use strict';

  //$routeProvider拡張
  //optionsオプションで指定したoptionsをプリロード
  //例： options: ['grade', 'sex']
  angular.module('calico').config(function ($routeProvider, OptionsServiceProvider) {
    var when = $routeProvider.when;
    $routeProvider.when = function (path, route) {
      var options = OptionsServiceProvider.defaultOptions.concat(route.options ? route.options : []);
      if (route.resolve === undefined) route.resolve = {};
      route.resolve.options = function (OptionsService) {
        return OptionsService.values.apply(null, options);
      };
      return when.call($routeProvider, path, route);
    };
  });

  //$routeProvider拡張に問題があるときはこちらを使う
  //resolve: preload('grade', 'sex')
  //angular.module('calico').constant('preload', function(){
  //  var ARGS = arguments;
  //  return {
  //    options: function(OptionsService){
  //      return OptionsService.values.apply(null, ARGS);
  //    }
  //  }
  //});

  //routeChangeごとにキャッシュ削除 (400ミリ秒以内に複数回走る場合は間引く)
  angular.module('calico').run(function($rootScope, OptionsService){
    $rootScope.$on('$routeChangeStart', OptionsService.clearCache.throttle(400));
  });

  angular.module('calico').provider('OptionsService', function () {
    var LOCAL_STORAGE_PREFIX = 'options-';

    this.defaultOptions = [];
    this.addDefaultOptions = function(/*options*/) {
      this.defaultOptions = this.defaultOptions.add(Array.create(arguments).flatten());
    };

    this.$get = function (ApiService, LocalStorageService, $q, PromiseService) {
      var CACHE = {};

      var service = {};

      service.values = function () {
        var keys = Array.create(arguments).flatten();
        var promiseMap = {};
        var apiArgs = [];
        var modifyCheckingMap = {};

        angular.forEach(keys, function(key){
          // from CACHE
          if(CACHE[key] !== undefined){
            promiseMap[key] = CACHE[key];
            return;
          }

          // from LocalStorage
          var storageData = LocalStorageService.restore(localStorageKey(key), true);
          if(storageData != null){
            //lastModifiedAtがある場合はapiに変更チェックしにいく
            if(storageData.lastModifiedAt == null){
              var promise = PromiseService.resolved(storageData.data);
              promiseMap[key] = promise;
              CACHE[key] = promise;
            }else{
              apiArgs.push({
                key: key,
                lastModifiedAt: storageData.lastModifiedAt
              });
              modifyCheckingMap[key] = storageData.data;
            }
            return;
          }

          // from api
          apiArgs.push({
            key: key,
            lastModifiedAt: null
          });

          apiArgs = apiArgs.unique('key');
        });

        // APIアクセス
        if(apiArgs.length != 0){
          var deferredMap = {};
          angular.forEach(apiArgs, function(arg){
            var key = arg.key;
            var deferred = $q.defer();
            deferredMap[key] = deferred;
            promiseMap[key] = deferred.promise;
            CACHE[key] = deferred.promise;
          });
          ApiService.post('options', apiArgs).then(function(res){
            angular.forEach(apiArgs, function(arg, i){
              var key = arg.key;
              if(res[i].modified === false){
                deferredMap[key].resolve(modifyCheckingMap[key]);
              }else{
                deferredMap[key].resolve(res[i].data);
              }
              // store to LocalStorage
              if(res[i].cacheable === true){
                LocalStorageService.store(localStorageKey(key), {
                  data: res[i].data,
                  lastModifiedAt: res[i].modifiedAt
                }, true);
              }
            });
          });
        }

        var promises = [];
        angular.forEach(keys, function(key){
          promises.push(promiseMap[key]);
        });
        return $q.all(promises).then(function(data){
          var ret = {};
          angular.forEach(keys, function(key, i){
            ret[key] = data[i];
          });
          return ret;
        });
      };

      service.of = function (key, id) {
        return service.values(key).then(function(data){
          return data[key].find(function(value){
            return value.id == id;
          });
        });
      };

      service.valueOf = function (key, NAME) {
        return service.values(key).then(function(data){
          return data[key].find(function(value){
            return value.NAME == NAME;
          });
        });
      };

      service.clearCache = function(){
        CACHE = {};
      };

      service.assignTo = function(target/*, keys */) {
        var keys = Array.create(arguments).slice(1).flatten();
        return service.values(keys).then(function(data) {
          if (angular.isFunction(target)) {
            target = target.call();
          }
          keys.forEach(function(key) {
            target[key] = data[key];
          });
          return data;
        });
      };

      function localStorageKey(key){
        return LOCAL_STORAGE_PREFIX + key;
      }

      return service;
    };
  });
}());
