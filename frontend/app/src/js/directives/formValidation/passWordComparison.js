app.directive('pwCheck', [function () {
    return {
      require: 'ngModel',
      link: function (scope, elem, attrs, ctrl) {
        var firstPassword = '#' + attrs.pwCheck;
        elem.add(firstPassword).on('keyup', function () {
          scope.$apply(function () {
            var password = scope.registration.pwd.$modelValue;
            var v = elem.val() === password;
            console.log(password);
            console.log(firstPassword);
            ctrl.$setValidity('confirmPwd', v);
            ctrl.$setValidity('pwmatch', v);
          });
        });
      }
    };
  }]);
