(function () {
  'use strict';

  var calico =  angular.module('calico')

  calico.directive('clcFile', function($ArrayBufferEncoder) {
    return {
      restrict: 'EA',
      replace: true,
      priority: 1,
      require: 'ngModel',
      templateUrl: 'assets/calico/form/file/template.html',
      scope: {},
      link: function(scope, elem, attrs, ngModel) {
        scope.clearModel = function() {
          ngModel.$setViewValue(null);
        };

        scope.loading = false;
        scope.info = {
          status: function() {
            if (scope.loading) return 'loading';
            if (ngModel.$modelValue != null) return 'input';
            return 'acceptable';
          },
          fileName: function() {
            return ngModel.$modelValue != null ? ngModel.$modelValue.meta.name : null;
          }
        };

        elem.bind('change', 'input[type=file]', function(e) {
          var files = e.target.files;
          if (files.length == 0) return;

          var file = files.item(0);

          var reader = new FileReader();

          reader.onload = function() {
            scope.$apply(function() {
              ngModel.$setViewValue({
                meta: {
                  name: file.name != '' ? file.name : '新規ファイル',
                  size: file.size > 0 ? file.size : 0,
                  type: file.type != '' ? file.type : 'application/octet-stream'
                },
                payload: $ArrayBufferEncoder.encode(reader.result)
              });
              scope.loading = false;
            });
          };

          scope.$apply(function() {
            scope.loading = true;
            reader.readAsArrayBuffer(file);
          });
        });

        scope.$watch(function() { return ngModel.$modelValue }, function(value) {
          if (value == null) {
            elem.find('input[type=file]').val(null);
          }
        });
      },
      controller: function() {}
    };
  });

  calico.directive('fileExt', function() {
    var alias = {
      jpg: ['jpeg'],
      jpeg: ['jpg']
    };

    return {
      restrict: 'A',
      priority: 2,
      require: ['ngModel', 'clcFile'],
      link: function(scope, elem, attrs, ctrls) {
        var ngModel = ctrls[0];
        var exts = attrs.fileExt.split(/[^a-zA-Z0-9]+/).reduce(function(array, value) {
          array.push(value);
          if (alias[value.toLowerCase()]) {
            array.add(alias[value.toLowerCase()]);
          }
          return array;
        }, []);
        var regexp = new RegExp('[.](' + exts.join('|') + ')$', 'i');
        ngModel.$parsers.push(function(viewValue) {
          var valid = viewValue == null || regexp.test(viewValue.meta.name);
          ngModel.$setValidity('fileExt', valid);
          return valid ? viewValue : null;
        });
      }
    }
  });
  calico.config(function(ClcWithErrorsConfigProvider) {
    ClcWithErrorsConfigProvider.addTransporter("fileExt", function (elem, attrs, optBuilder) {
      var msg = attrs.fileExt.split(/[^a-zA-Z0-9]+/).join(",");
      optBuilder.appendString('fileExt', msg)
    });
  });
  calico.run(function(ValidationMessageService) {
    ValidationMessageService.addMessages({
      fileExt: function(opts){
        if(opts.fileExt){
          return '拡張子が{1}のファイルをアップロードしてください。'.assign(opts.fileExt);
        }else{
          return '許されない拡張子のファイルです。';
        }
      }
    });
  });

  calico.directive('fileSize', function() {
    return {
      restrict: 'A',
      priority: 2,
      require: ['ngModel', 'clcFile'],
      link: function(scope, elem, attrs, ctrls) {
        scope.$watch(attrs.fileSize, function(value){
          scope.opts = value;
        }, true);

        var ngModel = ctrls[0];
        ngModel.$parsers.push(function(viewValue) {
          var valid = viewValue == null || scope.opts.max == null || viewValue.meta.size <= scope.opts.max;
          ngModel.$setValidity('fileSizeMax', valid);
          return valid ? viewValue : null;
        });
        ngModel.$parsers.push(function(viewValue) {
          var valid = viewValue == null || scope.opts.min == null || viewValue.meta.size >= scope.opts.min;
          ngModel.$setValidity('fileSizeMin', valid);
          return valid ? viewValue : null;
        });
      }
    }
  });

  calico.config(function(ClcWithErrorsConfigProvider) {
    ClcWithErrorsConfigProvider.addTransporter("fileSize", function (elem, attrs, optBuilder) {
      var value = attrs.fileSize;

      // TODO きちんとパーサー書く
      var max = value.match(/\bmax\s*:\s*([^,}]+)/);
      if (max) {
        optBuilder.append('fileSizeMax', max[1]);
      }

      var min = value.match(/\bmin\s*:\s*([^,}]+)/);
      if (min) {
        optBuilder.append('fileSizeMin', min[1]);
      }
    });
  });
  calico.run(function(ValidationMessageService) {
    ValidationMessageService.addMessages({
      fileSizeMax: function (opts) {
        if (opts.fileSizeMax != null) {
          return '{1}バイト以下のファイルをアップロードしてください。'.assign(opts.fileSizeMax);
        }
        else{
          return 'ファイルサイズが大きすぎます。';
        }
      },
      fileSizeMin: function (opts) {
        if (opts.fileSizeMin != null) {
          return '{1}バイト以上のファイルをアップロードしてください。'.assign(opts.fileSizeMin);
        }
        else{
          return 'ファイルサイズが小さすぎます。';
        }
      }
    });
  });

}());
