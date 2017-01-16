(function () {
  'use strict';

  angular.module('calico').provider('LocalStorageService', function () {
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
        localStorage.setItem(key, SerializeService.serialize(value))
      };

      service.restore = function (key, withDeployedAt) {
        key = service.createKey(key, withDeployedAt);
        var item = localStorage.getItem(key);
        if (item == null) {
          return null;
        }
        return SerializeService.deserialize(item);
      };

      service.restoreRawData = function (key, withDeployedAt) {
        key = service.createKey(key, withDeployedAt);
        var item = localStorage.getItem(key);
        if (item == null) {
          return null;
        }
        return item;
      };

      service.remove = function (key, withDeployedAt) {
        key = service.createKey(key, withDeployedAt);
        localStorage.removeItem(key)
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
        for (i = 0; i < localStorage.length; i++) {
          keys.push(localStorage.key(i));
        }
        angular.forEach(keys, function (key) {
          if (key.startsWith(matchKey)) {
            localStorage.removeItem(key);
          }
        });
      }

      return service;
    };
  }).run(function(LocalStorageService, DEPLOYED_AT){
    var key = 'local-storage-deployed-at';
    var last = LocalStorageService.restore(key);
    if(last != null && last !== DEPLOYED_AT){
      LocalStorageService.clearByDeployedAt(last);
    }
    if(last == null || last !== DEPLOYED_AT){
      LocalStorageService.store(key, DEPLOYED_AT);
    }
  });

}());
