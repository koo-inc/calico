(function () {
  'use strict';

  angular.module('calico').provider('SessionStorageService', function () {
    this.prefix = '';
    this.setPrefix = function (prefix) {
      this.prefix = prefix;
    };

    this.$get = function (SerializeService, DEPLOYED_AT) {
      var prefix = this.prefix;

      var service = {};

      service.keyPrefix = function(withDeployedAt){
        return withDeployedAt === true ? prefix + DEPLOYED_AT + '-' : prefix;
      };

      service.createKey = function(key, withDeployedAt){
        return service.keyPrefix(withDeployedAt) + key;
      };

      service.store = function (key, value, withDeployedAt) {
        key = service.createKey(key, withDeployedAt);
        sessionStorage.setItem(key, SerializeService.serialize(value))
      };

      service.restore = function (key, withDeployedAt) {
        key = service.createKey(key, withDeployedAt);
        var item = sessionStorage.getItem(key);
        if (item == null) {
          return null;
        }
        return SerializeService.deserialize(item);
      };

      service.restoreRawData = function (key, withDeployedAt) {
        key = service.createKey(key, withDeployedAt);
        var item = sessionStorage.getItem(key);
        if (item == null) {
          return null;
        }
        return item;
      };

      service.remove = function (key, withDeployedAt) {
        key = service.createKey(key, withDeployedAt);
        sessionStorage.removeItem(key)
      };

      service.clear = function (matchKey, withDeployedAt) {
        matchKey = service.createKey(matchKey == null ? '' : matchKey, withDeployedAt);
        removeMatched(matchKey);
      };

      service.clearByDeployedAt = function(deployedAt){
        removeMatched(prefix + deployedAt + '-');
      };

      function removeMatched(matchKey){
        var i;
        var keys = [];
        for (i = 0; i < sessionStorage.length; i++) {
          keys.push(sessionStorage.key(i));
        }
        angular.forEach(keys, function (key) {
          if (key.startsWith(matchKey)) {
            sessionStorage.removeItem(key);
          }
        });
      }

      return service;
    };
  }).run(function(SessionStorageService, DEPLOYED_AT){
    var key = 'session-storage-deployed-at';
    var last = SessionStorageService.restore(key);
    if(last != null && last !== DEPLOYED_AT){
      SessionStorageService.clearByDeployedAt(last);
    }
    if(last == null || last !== DEPLOYED_AT){
      SessionStorageService.store(key, DEPLOYED_AT);
    }
  });

}());
