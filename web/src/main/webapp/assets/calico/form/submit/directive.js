(function () {
  'use strict';

  // submitボタン
  // バリデーションエラー制御とボタン無効化＆最有効化を制御。
  // callbackに指定する関数はPromiseオブヘクトをreturnしなければならない。
  //
  // formCtrl.$invalidがtrue ならエラーアラートを表示して実行を中止。
  // falseならボタンをdisabledし、ディレクティブに指定された処理を実行。
  //
  // autoDisabled : booleanまたはhash
  // auto-disabled="{underPending: true}" : $http.pendingRequestsが空になったら(全てのAJAXリクエストが完了したら)disabled解除。
  // auto-disabled="{duration: 3}" : 3秒後にdisabled解除
  angular.module('calico').directive('clcSubmit', function(ClcSubmitService, $timeout, $http){
    return {
      restrict: 'A',
      priority: 3,
      scope: {
        callback: '&clcSubmit',
        confirm: '=?',
        autoDisabled: '=?',
        invalidAlertOpts: '=',
        ignoreValidation: "=",
        beforeValidation: "&?"
      },
      require: '^form',
      compile: function(element){
        if (element.attr('type') == null) {
          element.attr('type', 'submit');
        }

        //link
        return function(scope, element, attrs, formCtrl){
          $timeout(function() {
            function isInPending() {
              return $http.pendingRequests.length > 0;
            }

            if(angular.isObject(scope.autoDisabled) && scope.autoDisabled.underPending){
              if (isInPending()) {
                element.prop('disabled', true);
              }
              scope.$watch(isInPending, function(disabled) {
                element.prop('disabled', disabled);
              });
            }
          });

          element.on('click', function(e){
            if (scope.beforeValidation != null) {
              scope.beforeValidation({$event: e});
              scope.$apply();
              if (e.isDefaultPrevented()) return;
            }
            formCtrl.$setSubmitted(true);
            scope.$apply(function(){
              if(formCtrl.$pending){
                return;
              }
              if(formCtrl.$invalid && attrs.ignoreValidation === undefined){
                ClcSubmitService.showInvalidAlert(scope.invalidAlertOpts);
                return;
              }

              if(scope.confirm){
                if(!confirm(scope.confirm)) return;
              }

              var disabled = element.prop('disabled');
              if(scope.autoDisabled === undefined || scope.autoDisabled){
                element.prop('disabled', true);
              }

              var promise = scope.callback();
              if(promise == null || promise.then == null){
                alert('clc-submit: callback must return Promise object');
              }
              promise.catch(function(){
                element.prop('disabled', disabled);
              });

              if(angular.isObject(scope.autoDisabled) && scope.autoDisabled.duration){
                $timeout(function(){
                  element.prop('disabled', disabled);
                }, parseInt(scope.autoDisabled.duration, 10) * 1000);
              }
            });
          });
        }
      }
    };
  }).provider('ClcSubmitService', function(){
    var invalidAlertOpts = {
      title: '入力値に問題がありました。',
      content:'',
      type: 'danger',
      placement: 'top-left',
      animation: 'am-fade-and-slide-left',
      duration: 3
    };
    this.setInvalidAlertOpts = function (opts) {
      invalidAlertOpts = opts;
    };

    this.$get = function (AlertService) {
      var service = {
        showInvalidAlert: function(opts){
          if(opts){
            opts = angular.extend(angular.copy(invalidAlertOpts), opts);
          }
          AlertService.show(opts || invalidAlertOpts);
        }
      };
      return service;
    };
  });
}());
