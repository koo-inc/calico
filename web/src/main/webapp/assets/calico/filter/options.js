(function() {
  'use strict';

  angular.module('calico').filter('options', function(OptionsFilterModel){
    var f = function f(id, key, prop, defaultValue){
      if(id === undefined){
        return id;
      }

      var valueMap = OptionsFilterModel.valueMap(key);
      if(valueMap === undefined) return undefined;

      var e = valueMap[id];
      if(!e){
        return defaultValue != null ? defaultValue : '';
      }
      return e[prop != null ? prop : 'name'];
    };
    f.$stateful = true;
    return f;
  }).factory('OptionsFilterModel', function(OptionsService){
    var CACHE = {};
    return {
      values: function(key){
        if(CACHE[key] !== undefined) return CACHE[key];
        OptionsService.values(key).then(function(data){
          CACHE[key] = data[key];
        });
        return undefined;
      },
      valueMap: function(key) {
        var map = CACHE['_' + key];
        if (map == null) {
          OptionsService.values(key).then(function(data){
            if (CACHE['_' + key] != null) return;
            CACHE['_' + key] = data[key].reduce(function(map, d) {
              map[d.id] = d;
              return map;
            }, {});
          });
        }
        return map;
      },
      clearCache: function(){
        CACHE = {};
      }
    }
  }).run(function($rootScope, OptionsFilterModel){
    $rootScope.$on('$routeChangeSuccess', function(){
      OptionsFilterModel.clearCache();
    });
  });

}());
