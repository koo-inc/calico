(function () {
  'use strict';

  /**
   * orgFormに検索条件フォームのコピーをキープしているのはUI上のフォームがいじられていても最後の検索条件で再検索するため
   */
  angular.module('calico').directive('clcPager', function(SearchHelper){
    var OPTIONS = [
      {id: 10, name: "10件"},
      {id: 50, name: "50件"},
      {id: 100, name: "100件"}
    ];
    var RANGE = 3;

    return {
      restrict: 'A',
      templateUrl: 'assets/calico/search/pager/template.html',
      transclude: true,
      scope: {
        result: '=?',
        form: '=?',
        callback: '=?'

      },
      link: function(scope, element, attrs){
        if(attrs.result == null){
          scope.$parent.$watch('result', function(val){
            scope.result = val;
          });
        }
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
        $scope.options = OPTIONS;

        $scope.getRecordCount = function(){
          return $scope.result._count;
        };
        $scope.getCurrentPageNo = function(){
          return $scope.form._page.no;
        };
        $scope.getPerPage = function(){
          return $scope.form._page.perPage;
        };
        $scope.getMinPageNo = function(){
          return 1;
        };
        $scope.getMaxPageNo = function(){
          if($scope.getRecordCount() == 0) return 1;
          return Math.floor($scope.getRecordCount() / $scope.getPerPage()) + ($scope.getRecordCount() % $scope.getPerPage() == 0 ? 0 : 1);
        };
        $scope.getPageNos = function(){
          return createPageNos($scope.getCurrentPageNo(), $scope.getMinPageNo(), $scope.getMaxPageNo());
        };
        $scope.hasPrevPage = function(){
          return $scope.getCurrentPageNo() > $scope.getMinPageNo();
        };
        $scope.hasNextPage = function(){
          return $scope.getCurrentPageNo() < $scope.getMaxPageNo();
        };

        $scope.moveTo = function(no){
          if($scope.callback){
            //orgFormを使って検索
            var form = angular.copy($scope.orgForm);
            if(form._page){
              form._page.no = no;
              $scope.callback(form, true);
            }
          }
        };
        $scope.moveToPrev = function(){
          if($scope.hasPrevPage()){
            $scope.moveTo($scope.getCurrentPageNo() - 1);
          }
        };
        $scope.moveToNext = function(){
          if($scope.hasNextPage()){
            $scope.moveTo($scope.getCurrentPageNo() + 1);
          }
        };
        $scope.onchangePerPage = function(){
          if($scope.callback){
            //orgFormを使って検索
            var form = angular.copy($scope.orgForm);
            if(form._page){
              form._page.perPage = $scope.form._page.perPage;
              $scope.callback(form);
            }
          }
        };
      }
    };

    function createPageNos(current, min, max){
      var ret = [];
      var i;
      for(i = current - RANGE;i <= current + RANGE;i++){
        if(min <= i && i <= max) ret.push(i);
      }
      if(ret.indexOf(min) === -1){
        if(ret.first() >= 3){
          ret.unshift(null);
        }
        ret.unshift(min);
      }
      if(ret.indexOf(max) === -1){
        if(ret.last() <= max -2){
          ret.push(null);
        }
        ret.push(max);
      }
      return ret;
    }
  });
}());
