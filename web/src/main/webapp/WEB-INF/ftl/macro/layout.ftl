<#macro main>
<!DOCTYPE html>
<html lang="ja" ng-app="app">
  <@head />
  <body ng-cloak>
    <@header />
    <div class="container-fluid">
      <div class="row">
        <div class="main">
          <h1 class="page-header<#if menuIcon??> menu-icon-${menuIcon}</#if> "><span class="title">${(menuName!'')?html}</span></h1>
          <div class="container">
            <#nested >
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
</#macro>

<#macro login>
<!DOCTYPE html>
<html lang="ja" ng-app="app">
  <@head />
  <body ng-cloak>
    <@header />
    <div class="container" role="main">
      <#nested >
    </div>
  </body>
</html>
</#macro>

<#macro head>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <base href="${context.appPath}/">
  <title>calico-sample</title>
  <#list _styles as style>
    <link href="${style}" rel="stylesheet" media="all">
  </#list>
  <!--[if lt IE 9]>
  <script src="assets/vendor/html5shiv-3.7.2/dist/html5shiv.min.js"></script>
  <script src="assets/vendor/respond-1.4.2/dest/respond.min.js"></script>
  <![endif]-->
  <#list _scripts as script>
    <script src="${script}"></script>
  </#list>
  <script>
    angular.module('app')
      .constant('ENV', '${ENV}')
      .constant('DEPLOYED_AT', '${DEPLOYED_AT}')
      .constant('RESOURCE_ROOT', '${RESOURCE_ROOT}')
      .constant('PARTIAL_PATH', ${toJson(_partials)})
      .constant('AUTH_INFO', ${toJson(AUTH_INFO)})
      .run(['$rootScope', 'PARTIAL_PATH', function($rootScope, PARTIAL_PATH) {
        $rootScope.PARTIAL_PATH = PARTIAL_PATH;
      }]);
  </script>
</head>
</#macro>

<#macro header>
<header ng-controller="HeaderCtrl">
  <div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
      <div class="navbar-header">
        <a class="navbar-brand" href="./" target="_self">Sample</a>
      </div>
      <#if AUTH_INFO.authenticated>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="customer/index" target="_self">顧客</a></li>
            <li class="dropdown">
              <a bs-dropdown data-animation="">マスタ <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="user_info/index" target="_self">ユーザ</a></li>
              </ul>
            </li>
            <li class="dropdown">
              <a bs-dropdown data-animation="">Sample <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="sample/form/text" target="_self">FormUI</a></li>
                <li><a href="sample/mail/index" target="_self">Mail送信</a></li>
              </ul>
            </li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li>
              <p class="navbar-text">
                <span class="glyphicon glyphicon-user"></span>
              ${AUTH_INFO.loginId?html}
              </p>
            </li>
            <li>
              <a ng-click="logout()" href="">
                <span class="glyphicon glyphicon-log-out"></span> logout
              </a>
            </li>
          </ul>
        </div>
      </#if>
    </div>
  </div>
</header>
</#macro>
