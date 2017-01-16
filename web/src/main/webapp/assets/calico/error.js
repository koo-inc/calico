(function() {
  'use strict';

  function sendErrorLog(exception) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'api/system/js_log', false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({location: location.href, userAgent: navigator.userAgent, exception: exception}));
  }

  var onerror = window.onerror;
  window.onerror = function(message, file, line, column, e) {
    var exception = e != null && e.stack;
    if (!exception) {
      exception = message + "\n\tat " + file + ":" + line + ":" + column
    }

    sendErrorLog(exception);
    if ('console' in window) {
      window.console.error(exception);
    }
    if (onerror) {
      return onerror.apply(this, arguments);
    }
  };

  if ('console' in window) {
    var warn = console.warn;
    console.warn = function() {
      var e = new Error(angular.toJson({'warn': arguments}));
      sendErrorLog(e.stack);
      return warn.apply(this, arguments);
    }
  }

  angular.module('calico').config(function($provide) {
    $provide.decorator('$exceptionHandler', function($delegate) {
      return function(exception, cause) {
        sendErrorLog(exception.stack + "\n\tat " + cause);
        return $delegate(exception, cause);
      }
    });
  });
}());
