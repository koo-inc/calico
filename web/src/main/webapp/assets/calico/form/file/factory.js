(function() {
  'use strict';

  var calico = angular.module('calico');

  calico.factory('$ArrayBufferEncoder', function() {
    // String#fromCharCode に渡す配列のサイズを制限する必要あり。数値は Google closure library のパフォーマンステストに使われた数値より小さく 8 と 6 の公倍数。
    // http://stackoverflow.com/questions/22747068/is-there-a-max-number-of-arguments-javascript-functions-can-accept
    var CHUNK_SIZE = 8184;

    return {
      encode: function(arrayBuffer) {
        var result = '';
        for (var i = 0, len = arrayBuffer.byteLength; i < len; i += CHUNK_SIZE) {
          result += btoa(String.fromCharCode.apply(null, new Uint8Array(arrayBuffer, i, Math.min(CHUNK_SIZE, len - i))));
        }
        return result;
      },
      decode: function(encoded) {
        var str = atob(encoded);
        var bytes = new Uint8Array(str.length);
        for (var i = 0, len = str.length; i < len; i++) {
          bytes[i] = str.charCodeAt(i);
        }
        return bytes.buffer;
      }
    };
  });

  calico.factory('$DownloadServiceInner', function($window, $document) {
    if ($window.navigator.saveBlob) {
      return function(blob, name) {
        $window.navigator.saveBlob(blob, name);
      };
    }
    if ($window.navigator.msSaveBlob) {
      return function(blob, name) {
        $window.navigator.msSaveBlob(blob, name);
      };
    }

    var document = $document.get(0);
    return function(blob, name) {
      var url = URL.createObjectURL(blob);
      var a = document.createElement('a');
      a.href = url;
      a.setAttribute('download', name);
      var e = document.createEvent("MouseEvent");
      e.initEvent("click", true, false);
      a.dispatchEvent(e);
      // うまく破棄してくれない。。。
      // URL.revokeObjectURL(url);
    }
  });

  calico.factory('DownloadService', function($DownloadServiceInner, $ArrayBufferEncoder) {
    var DownloadService = {
      downloadAt: function(paramName) {
        return function(data) {
          DownloadService.download(data[paramName]);
          return data;
        }
      },
      download: function(media) {
        var blob = new Blob([$ArrayBufferEncoder.decode(media.payload)]);
        $DownloadServiceInner(blob, media.meta.name);
        return media;
      }
    };
    return DownloadService;
  });
}());
