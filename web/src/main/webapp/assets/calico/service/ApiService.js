(function () {
  'use strict';

  var calico = angular.module('calico');
  calico.provider('ApiService', function () {
    this.rootUrl = '';
    this.setRootUrl = function (url) {
      this.rootUrl = url;
    };

    var errorHandlers = [];
    this.addErrorHandler = function(matcher, callback, weight) {
      if (angular.isString(matcher) || angular.isNumber(matcher)) {
        var _status = matcher;
        matcher = function(status) {
          return status == _status;
        };
      }
      if (angular.isArray(matcher)) {
        var _pair = matcher;
        matcher = function(status, type) {
          return status == _pair[0] && type == _pair[1];
        }
      }
      if (weight == null) {
        weight = 1;
      }
      errorHandlers.push({index: errorHandlers.length, matcher: matcher, weight: weight, callback: callback});
    };

    function getErrorHandler(status, type) {
      var handlers = errorHandlers.filter(function(handler) {
        return handler.matcher(status, type);
      });

      handlers.sort(function(a, b) {
        if (a.weight < b.weight) return -1;
        if (a.weight > b.weight) return 1;
        return a.index - b.index;
      });
      return handlers[0];
    }

    this.$get = function ($http, $q, $injector, AlertService, $href) {
      var rootUrl = this.rootUrl;
      var service = {};

      //service-exceptionアラート表示
      var VIOLATIONS_ALERT_OPTS = {
        title: '処理中にエラーが起きました。',
        type: 'danger',
        placement: 'top-left',
        animation: 'am-fade-and-slide-left',
        duration: false
      };
      function genViolationsAlert(showError) {
        var errorCount = 20;
        return function showViolationsAlert(violations){
          if (!showError) return;
          var list = [];
          var ul = angular.element('<ul>');
          angular.forEach(violations, function (messages, key) {
            list.add(messages);
          });
          list.slice(0, errorCount).forEach(function (message) {
            ul.append(angular.element('<li>').text(message));
          });
          if (list.length > errorCount) {
            ul.append('<li>...</li>');
            ul.append('<li><i>その他 <b>' + (list.length - errorCount) + '</b> 件数のエラー</i></li>');
          }
          AlertService.show(angular.extend(angular.copy(VIOLATIONS_ALERT_OPTS), {content: ul.html()}));
        }
      }

      //service-exception以外のアラート表示
      var ERROR_ALERT_OPTS = {
        title: '',
        content: '',
        type: 'danger',
        placement: 'top-left',
        animation: 'am-fade-and-slide-left',
        duration: false
      };
      function genErrorAlert(showError) {
        return function showErrorAlert(message){
          if (!showError) return;
          AlertService.show(angular.extend(angular.copy(ERROR_ALERT_OPTS), {title: message}));
        }
      }

      // 空文字をnullに
      var sanitizeData = function (data) {
        if (angular.isObject(data) || angular.isArray(data)) {
          angular.forEach(data, function (v, k) {
            data[k] = sanitizeData(v);
          });
        } else if (angular.isString(data)) {
          if (data === '') {
            data = null;
          }
        }
        return data;
      };

      var wrapPromise = function (promise, showError) {
        if (showError !== false) {
          showError = true;
        }
        var deferred = $q.defer();
        promise.then(function (res) {
          deferred.resolve(res.data);
        }, function (res) {
          var handler = getErrorHandler(res.status, angular.isObject(res.data) ? res.data.type : null);
          var locals = {
            $violationsAlert: genViolationsAlert(showError),
            $errorAlert: genErrorAlert(showError),
            $response: res,
            $deferred: deferred,
            $status: res.status,
            $type: angular.isObject(res.data) ? res.data.type : ''
          };

          $injector.invoke(handler.callback, this, locals);
          deferred.reject();
        });
        return deferred.promise;
      };

      service.get = function (path, showError) {
        return wrapPromise($http.get(rootUrl + $href(path)), showError);
      };
      service.post = function (path, data, showError) {
        if(data === undefined) data = null; //IE対応
        return wrapPromise($http.post(rootUrl + $href(path), sanitizeData(data)), showError);
      };
      service.put = function (path, data, showError) {
        if(data === undefined) data = null; //IE対応
        return wrapPromise($http.put(rootUrl + $href(path), sanitizeData(data)), showError);
      };
      service.delete = function (path, showError) {
        return wrapPromise($http.delete(rootUrl + $href(path)), showError);
      };

      return service;
    };
  });

  calico.config(function(ApiServiceProvider) {
    ApiServiceProvider.addErrorHandler(function($status, $type) {
      return $status / 100 == 5 && $type == 'unknown-exception';
    }, function($response, $errorAlert, $type, $status, ENV_NAME) {
      if (ENV_NAME === 'production') {
        console.warn($type, $status, $response);
        $errorAlert($status + " Server Error");
      }
      else {
        $errorAlert($response.data.message);
      }
    }, -1);

    ApiServiceProvider.addErrorHandler(function($status) {
      return $status / 100 == 5;
    }, function($response, $errorAlert, $status) {
      $errorAlert($status + ' Server Error');
    }, -1);

    ApiServiceProvider.addErrorHandler('404', function($errorAlert) {
      $errorAlert('404 Not Found');
    });

    ApiServiceProvider.addErrorHandler(['400', 'authentication-required'], function($transit) {
      $transit('');
    });

    ApiServiceProvider.addErrorHandler(['400', 'csrf-token-invalid'], function($errorAlert) {
      $errorAlert('400 Bad Request');
    });

    ApiServiceProvider.addErrorHandler(['400', 'service-exception'], function($response, $violationsAlert, $deferred) {
      var violations = $response.data.violations;
      $violationsAlert(violations);
      $deferred.reject(violations);
    }, 100);

    ApiServiceProvider.addErrorHandler(function(){ return true }, function($status, $type, $response, $errorAlert) {
      $errorAlert($status + ' Error');
      console.warn($type, $status, $response);
    }, Infinity);
  });

}());
