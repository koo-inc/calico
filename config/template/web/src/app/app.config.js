"use strict";
exports.AppConfig = {
    appName: "calico-sample",
    envName: "${envName}",
    deployedAt: "${generated.format('yyyyMMddHHmmss')}",
    messages: {}
        <  % message.each };
;
