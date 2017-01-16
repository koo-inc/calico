(function () {
  'use strict';

  /**
   * orgFormに検索条件フォームのコピーをキープしているのはUI上のフォームがいじられていても最後の検索条件で再検索するため
   */
  angular.module('calico').directive('clcSort', function(SearchHelper){
    function isTarget(form, prop){
      return form && form._sort && form._sort.prop == prop;
    }
    function isDesc(form){
      return form && form._sort && form._sort.type == 'DESC';
    }
    function getNewSort(form, prop){
      if(isTarget(form, prop)){
        return [prop, isDesc(form) ? 'ASC' : 'DESC'];
      }else{
        return [prop, 'ASC'];
      }
    }

    return {
      restrict: 'A',
      templateUrl: 'assets/calico/search/sort/template.html',
      transclude: true,
      scope: {
        prop: '@clcSort',
        form: '=?',
        callback: '=?'

      },
      link: function(scope, element, attrs){
        if(attrs.form == null){
          scope.$parent.$watch('form', function(val){
            scope.form = val;
          });
        }
        //初回のみorgFormとしてコピーをキープ
        var w = scope.$watch('form', function(val){
          if(scope.orgForm == null){
            scope.orgForm = angular.copy(val);
            w();
          }
        });
        if(attrs.callback == null){
          scope.callback = scope.$parent.search;
        }

        //検索フォーム変更通知が来たらorgFormを更新
        scope.$on(SearchHelper.EVENT_UPDATE_SEARCH_FORM, function(event, newForm){
          scope.orgForm = angular.copy(newForm);
        });
      },
      controller: function($scope){
        $scope.isAsc = function(){
          return isTarget($scope.form, $scope.prop) && !isDesc($scope.form);
        };
        $scope.isDesc = function(){
          return isTarget($scope.form, $scope.prop) && isDesc($scope.form);
        };
        $scope.sort = function(){
          if($scope.callback){
            //orgFormを使って検索
            var form = angular.copy($scope.orgForm);
            if(form._sort){
              var sort = getNewSort($scope.form, $scope.prop);
              form._sort.prop = sort[0];
              form._sort.type = sort[1];
              $scope.callback(form);
            }
          }
        };
      }
    };
  });
}());
