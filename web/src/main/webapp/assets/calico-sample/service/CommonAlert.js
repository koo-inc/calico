(function () {
  'use strict';

  angular.module('calicoSample').provider('CommonAlert', function () {

    this.$get = function (AlertService) {
      var service = {};

      //保存しました
      var OPTS_SAVED = {
        title: '保存しました。',
        content:'',
        type: 'success',
        placement: 'top-right',
        animation: 'am-fade-and-slide-right',
        duration: 3
      };
      service.showSavedMessage = function(){
        AlertService.show(OPTS_SAVED);
      };
      service.reserveSavedMessage = function(){
        AlertService.reserve(OPTS_SAVED);
      };

      //削除しました
      var OPTS_DELETED = {
        title: '削除しました。',
        content:'',
        type: 'success',
        placement: 'top-right',
        animation: 'am-fade-and-slide-right',
        duration: 3
      };
      service.showDeletedMessage = function(){
        AlertService.show(OPTS_DELETED);
      };
      service.reserveDeletedMessage = function(){
        AlertService.reserve(OPTS_DELETED);
      };

      //取込処理を完了しました
      var OPTS_IMPORTED = {
        title: '取込処理を完了しました。',
        content:'',
        type: 'success',
        placement: 'top-right',
        animation: 'am-fade-and-slide-right',
        duration: 3
      };
      service.showImportedMessage = function(){
        AlertService.show(OPTS_IMPORTED);
      };
      service.reserveImportedMessage = function(){
        AlertService.reserve(OPTS_IMPORTED);
      };

      //処理を完了しました
      var OPTS_FINISHED = {
        title: '処理を完了しました。',
        content:'',
        type: 'success',
        placement: 'top-right',
        animation: 'am-fade-and-slide-right',
        duration: 3
      };
      service.showFinishedMessage = function(){
        AlertService.show(OPTS_FINISHED);
      };
      service.reserveFinishedMessage = function(){
        AlertService.reserve(OPTS_FINISHED);
      };

      return service;
    };
  });

}());
