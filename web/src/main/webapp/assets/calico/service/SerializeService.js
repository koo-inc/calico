(function () {
  'use strict';

  /*
   オブジェクトのシリアライズ＆格納サービス
   json化&base64エンコードでシリアライズする。
   */
  angular.module('calico').provider('SerializeService', function () {

    this.$get = function () {
      var service = {};

      //シリアライズ
      service.serialize = function(obj){
        return angular.toJson(obj).encodeBase64();
      };

      //デシリアライズ
      service.deserialize = function(str){
        try {
          return angular.fromJson(str.decodeBase64());
        } catch (e) {
          return null;
        }
      };

      return service;
    };
  });

}());
