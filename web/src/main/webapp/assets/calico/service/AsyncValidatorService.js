(function () {
  'use strict';

  angular.module('calico').provider('AsyncValidatorService', function () {

    this.$get = function ($q, $timeout) {
      var service = {};

      /**
       * ngModelCtrl.$asyncValidators.XXX に設定するようの関数を生成。
       *
       * ajaxで問合せ中は2重にリクエストしないようにする。
       * ajax問合せ中に値が変更されていれば問合せ後に再度validateする。
       *
       * 引数:fnc booleanを返すpromiseを返す関数を渡す。
       */
      service.createValidator = function(ngModelCtrl, fnc){
        var prevValue = undefined;
        var promise = undefined;
        var recheck = false;

        return function(modelValue, viewValue){
          //値が変わってなければ前回のpromise
          if(viewValue === prevValue){
            return promise === undefined ? true : promise;
          }
          //結果待ち状態なら前回のpromiseを返してrecheckフラグ立てる
          if(ngModelCtrl.$pending !== undefined && promise !== undefined){
            recheck = true;
            return promise;
          }else{
            prevValue = viewValue;
          }
          promise = fnc.apply(this, [modelValue, viewValue]).then(function(bool){
            //recheckフラグ立ってたらもう一度validate
            if(recheck){
              recheck = false;
              $timeout(function(){
                ngModelCtrl.$validate();
              });
            }
            return bool ? true : $q.reject();
          });
          return promise;
        };
      };

      return service;
    };
  });

}());
