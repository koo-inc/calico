(function () {
  'use strict';

  /**
   * バリデーションエラーメッセージを管理
   */
  angular.module('calico').provider('ValidationMessageService', function () {
    this.$get = function () {
      var messages = {};

      var service = {
        addMessages: function(hash){
          angular.extend(messages, hash);
        },
        getKeys: function(){
          return Object.keys(messages);
        },
        getMessage: function(key, opts){
          var ret = messages[key];
          if(angular.isFunction(ret)){
            return ret(opts);
          }
          return ret;
        }
      };
      return service;
    };
  }).run(function(ValidationMessageService){
    /**
     * デフォルトメッセージ設定
     * app/_init.jsなどで上書き可能
     */
    ValidationMessageService.addMessages({
      required: '必須項目です。',
      minlength: function(opts){
        if(opts.minlength != null){
          return '{1}文字以上で入力してください。'.assign(opts.minlength);
        }else{
          return '入力文字数が少なすぎます。';
        }
      },
      maxlength: function(opts){
        if(opts.maxlength != null){
          return '{1}文字以内で入力してください。'.assign(opts.maxlength);
        }else{
          return '入力文字数が多すぎます。';
        }
      },
      pattern: function(opts){
        if(opts.patternName != null){
          return '{1}で入力してください。'.assign(opts.patternName);
        }else{
          return '正しい形式ではありません。';
        }
      },
      date: '不正な日付です。'
    });
  });

}());
