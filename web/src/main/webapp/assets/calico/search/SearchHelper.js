(function () {
  'use strict';

  var SESSION_STORAGE_PREFIX = 'search-form-';

  angular.module('calico').provider('SearchHelper', function () {

    this.$get = function (SerializeService, SessionStorageService, $path, $hash, $route, PromiseService) {
      var service = {};

      service.SESSION_STORAGE_PREFIX = SESSION_STORAGE_PREFIX;
      service.EVENT_UPDATE_SEARCH_FORM = 'search_helper.update_search_form';

      /**
       * $scopeの検索実行関数を作成。
       *
       * コントローラを破棄せずに再検索する場合（モーダル上など）以外では引数なしで良い。
       * handlerは第一引数に新しい検索フォームオブジェクトが渡されるので検索ロジックを書く。
       * hanlerを指定する場合は$scope必須。
       */
      service.getSearchFunc = function(handler, $scope, searchFormProp){
        //handlerが指定されている場合のみscope上のsearchFormを監視
        if(handler){
          var searchForm = null;
          $scope.$watch(searchFormProp || 'form', function(val){
            searchForm = val;
          });
        }

        return function(form, ignorePageNoReset){
          if(!ignorePageNoReset && form._page){
            form._page.no = 1;
          }
          if(handler){
            service.updateSearchForm($scope, searchForm, form);
            return handler(form);
          }else{
            service.storeSearchForm(form);
            //clc-submit対応
            return PromiseService.resolved();
          }
        };
      };

      /**
       * searchFormにnewFormをマージしてpager,sortディレクティブに通知
       */
      service.updateSearchForm = function($scope, searchForm, newForm){
        angular.extend(searchForm, newForm);
        $scope.$broadcast(service.EVENT_UPDATE_SEARCH_FORM, newForm);
      };

      /**
       * 検索ページの制御
       * initは検索条件がURLハッシュに埋め込まれてない時（最初に来た時）
       * resumeはURLハッシュから検索条件を復元できた時(検索実行した時)
       *
       * SearchHelper.handle({
       *   init: function(){
       *     //formをAPIから取得など
       *   },
       *   resume: function(form){
       *     //formを使って検索実行など
       *   }
       * });
       */
      service.handle = function(handlers){
        var form = service.restoreSearchForm();
        if(form == null){
          handlers.init();
        }else{
          handlers.resume(form);
        }
      };

      service.restoreSearchForm = function(removeFromSessionStorageIfNoHash){
        if(removeFromSessionStorageIfNoHash === undefined){
          removeFromSessionStorageIfNoHash = true;
        }
        var hash = $hash();
        if (!hash) {
          if(removeFromSessionStorageIfNoHash){
            this.removeSearchFormFromSessionStorage();
          }
          return null;
        }
        return SerializeService.deserialize(hash);
      };

      service.restoreSearchFormFromSessionStorage = function(path){
        return SessionStorageService.restoreRawData(SESSION_STORAGE_PREFIX + path);
      };

      service.storeSearchForm = function(form){
        this.storeSearchFormToSessionStorage(form);
        this.storeSearchFormToUrlHash(form);
      };

      service.storeSearchFormToUrlHash = function(form){
        var oldHash = $hash();
        var newHash = SerializeService.serialize(form);
        $hash(newHash);
        if(oldHash == newHash){
          $route.reload();
        }
      };

      service.storeSearchFormToSessionStorage = function(form){
        SessionStorageService.store(SESSION_STORAGE_PREFIX + $path(), form);
      };

      service.removeSearchFormFromSessionStorage = function(){
        SessionStorageService.remove(SESSION_STORAGE_PREFIX + $path());
      };

      return service;
    };
  });
  //LocationService hash拡張
  angular.module('calico').config(function ($provide) {
    $provide.decorator('LocationService', function ($delegate, SessionStorageService) {
      $delegate.actualUrlOrg = $delegate.actualUrl;
      $delegate.actualUrl = function(path, param, hash){
        if(hash === true){
          var restore = SessionStorageService.restoreRawData(SESSION_STORAGE_PREFIX + '/' + $delegate.actualPath(path));
          hash = restore ? restore : null;
        }
        return $delegate.actualUrlOrg(path, param, hash);
      };
      return $delegate;
    });
  });

}());
