(function () {
  'use strict';

  function OptBuilder(base) {
    this._base = base;
    this._pair = [];
  }
  angular.extend(OptBuilder.prototype, {
    append: function (key, value) {
      this._pair.push([key, value]);
    },
    appendString: function (key, value) {
      value = value != null ? String(value) : '';
      var literal = '"' + value.replace(/([\\"])/g, function(w, $1) { return {'\\': '\\\\', '"': '\\"'}[$1] }) + '"';
      this.append(key, literal);
    },
    build: function () {
      var addition = this._pair.map(function (kv) {
        return kv[0] + ': ' + kv[1];
      }).join(', ');

      if (addition.length === 0) {
        return this._base != null ? this._base : '{}';
      }
      if (this._base == null || this._base === '' || this._base.match(/^\s*\{\s*\}\s*$/)) {
        return '{' + addition + '}';
      }
      return this._base.replace(/\{/, '{' + addition + ',');
    }
  });

  var calico =  angular.module('calico');
  calico.provider('ClcWithErrorsConfig', function() {
    this.$get = function () {
      return this;
    };

    this.$transporters = {};
    this.addTransporter = function(attrName, transporter) {
      this.$transporters[attrName] = this.$transporters[attrName] || [];
      this.$transporters[attrName].push(transporter);
    };
  });

  calico.directive('clcWithErrors', function(ClcWithErrorsConfig){
    return {
      restrict: 'A',
      priority: 2,
      compile: function(element, attrs){
        var elem = angular.element('<span></span>');
        elem.attr('clc-errors', '');
        elem.attr('errors-input-name', attrs.name);

        //errors-opts
        var builder = new OptBuilder(attrs.errorsOpts);
        angular.forEach(ClcWithErrorsConfig.$transporters, function(transporters, attrName) {
          if (attrs[attrName] != null) {
            angular.forEach(transporters, function(transporter) {
              transporter(element, attrs, builder);
            });
          }
        });
        elem.attr('errors-opts', builder.build());

        element.after(elem);

        element.removeAttr('clc-with-errors');
        delete attrs.clcWithErrors;
      }
    };
  });
  calico.config(function(ClcWithErrorsConfigProvider) {
    ClcWithErrorsConfigProvider.addTransporter('ngMinlength', function(elem, attrs, optBuilder) {
      optBuilder.append('minlength', attrs.ngMinlength);
    });
    ClcWithErrorsConfigProvider.addTransporter('ngMaxlength', function(elem, attrs, optBuilder) {
      optBuilder.append('maxlength', attrs.ngMaxlength);
    });
  });
}());
