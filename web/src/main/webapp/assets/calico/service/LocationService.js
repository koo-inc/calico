(function () {
  'use strict';

  angular.module('calico').provider('LocationService', function () {
    /*
     パスをRESOURCE_ROOTによって変換
     相対パス(頭に./)の場合はRESOURCE_ROOTを頭につける。
     戻り値の頭の/は削除される。

     例：
     現在のURL: /hoge/index
     $href('./show') => hoge/show
     $href('./show', {id: 1}) => hoge/show?id=1
     $href('./show', {id: 1, a: 'あ'}) => hoge/show?id=1&a=%E3%81%82
     $href('foo/bar/show') => foo/bar/show
     $href('/foo/bar/show') => foo/bar/show
     */

    this.$get = function ($location, $window, RESOURCE_ROOT, $browser) {
      var service = {};

      service.href = function(path, params, hash){
        return service.actualUrl(path, params, hash);
      };

      service.transit = function(path, params, hash){
        $window.location.href = $browser.baseHref() + service.actualUrl(path, params, hash);
      };

      service.url = function(path, params, hash){
        if(arguments.length == 0){
          return $location.url();
        }else{
          return $location.url(service.actualUrl(path, params, hash));
        }
      };

      service.path = function(path){
        if(arguments.length == 0){
          return $location.path();
        }else{
          return $location.path(service.actualPath(path, RESOURCE_ROOT));
        }
      };

      service.search = function(search, paramValue){
        return $location.search.apply($location, arguments);
      };

      service.hash = function(hash){
        return $location.hash.apply($location, arguments);
      };

      service.actualPath = function(path){
        var ret = '';
        if (path.match(/^\.\//)) {
          ret = path.replace(/^\./, RESOURCE_ROOT);
        } else {
          ret = path;
        }
        return ret.replace(/^\/+/, '');
      };

      service.searchQuery = function(params){
        if(params == null || Object.isEmpty(params)) return null;

        var keys = Object.keys(params).filter(function(k){
          return params[k] != null;
        });
        if(keys == null || keys.length == 0) return null;

        return keys.map(function (k) {
          return new String(k).escapeURL(true) + '=' + new String(params[k]).escapeURL(true);
        }).join('&');
      };

      service.actualUrl = function(path, params, hash){
        var aPath = service.actualPath(path);
        var search = service.searchQuery(params);
        var ret = aPath;
        if(search && !search.isBlank()) ret += '?' + search;
        if(hash && !hash.isBlank()) ret += '#' + hash;
        return ret;
      };

      return service;
    };
  }).factory('$href', function(LocationService){
    return LocationService.href;
  }).factory('$transit', function(LocationService){
    return LocationService.transit;
  }).factory('$url', function(LocationService){
    return LocationService.url;
  }).factory('$path', function(LocationService){
    return LocationService.path;
  }).factory('$search', function(LocationService){
    return LocationService.search;
  }).factory('$hash', function(LocationService){
    return LocationService.hash;
  }).run(function(LocationService, RootScopeUtil){
    RootScopeUtil.addCommonProp('$href', LocationService.href);
    RootScopeUtil.addCommonProp('$transit', LocationService.transit);
    RootScopeUtil.addCommonProp('$url', LocationService.url);
    RootScopeUtil.addCommonProp('$path', LocationService.path);
    RootScopeUtil.addCommonProp('$search', LocationService.search);
    RootScopeUtil.addCommonProp('$hash', LocationService.hash);
  });

}());
