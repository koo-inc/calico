(function () {
  'use strict';

  angular.module('calico').directive('clcErrors', function(ValidationMessageService, $timeout){
    var DEFAULT_SHOW_AS = 'tooltip';

    return {
      restrict: 'A',
      templateUrl: 'assets/calico/form/errors/template.html',
      transclude: true,
      replace: true,
      scope: {
        input: '=?clcErrors',
        errorsOpts: '=?',
        errorsInputName: '@?',
        errorsShowAs: '@?' // 'tooltip' or 'embed'
      },
      require: '^form',
      link: function(scope, element, attrs, formCtrl){
        //errors-input-nameからinput取得
        if(scope.input === undefined && scope.errorsInputName){
          scope._form = formCtrl;
          var watch = scope.$watch('_form.' + scope.errorsInputName, function(val){
            if(val){
              scope.input = val;
              watch();
            }
          });
        }

        //エラーメッセージ
        scope.keys = ValidationMessageService.getKeys();
        scope.message = function(key){
          return ValidationMessageService.getMessage(key, scope.errorsOpts || {});
        };

        //showAs
        scope.showAsClass = function(){
          return 'show-as-' + (scope.errorsShowAs || DEFAULT_SHOW_AS);
        };

        //tooltip表示 座標計算
        if((scope.errorsShowAs || 'tooltip') == DEFAULT_SHOW_AS){
          var input = element.prev();
          scope.style = {};
          updateStyle();

          //エラー内容変更時に座標更新
          scope.$watch('input.$error', function(){
            $timeout(function(){
              updateStyle();
            });
          }, true);

          //focus&hover時に座標更新
          input.on('focus', function(){
            updateStyle();
          });
          input.hover(function(){
            updateStyle();
          });

          //textareaリサイズ対応
          if(input.prop('tagName') == 'TEXTAREA'){
            input.on('mouseup mousedown mousemove', function(){
              updateStyle();
            });
          }
        }

        //tooltip表示座標計算
        function updateStyle(){
          var input = getInputForStyle();
          var marginTop = element.outerHeight() + input.outerHeight(true) + 10;
          $timeout(function(){
            scope.style.marginTop = '-' + marginTop + 'px';
            scope.style.left = (input.position() || {}).left;
          });
        }
        //座標計算用input取得
        function getInputForStyle(){
          var ret = element.prev();
          if(!ret.hasClass('datepicker') && !ret.hasClass('timepicker')) return ret;
          return ret.prev();
        }
      }
    };
  });
}());
