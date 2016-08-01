// Karma configuration
// Generated on Thu Mar 17 2016 10:27:21 GMT+0100 (CET)

module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '',


    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine'],


    // list of files / patterns to load in the browser
    files: [
      // Angular dependencies
      'bower_components/angular/angular.js',
      'bower_components/angular-animate/angular-animate.js',
      'bower_components/angular-mocks/angular-mocks.js',
      'bower_components/angular-resource/angular-resource.js',
      'bower_components/angular-route/angular-route.js',
      'bower_components/jquery/dist/jquery.js',

      // Dependencies in /src
      'src/js/app.js',
      'src/js/autocomplete.js',
      'src/js/ng-map.min.js',
      'src/js/ui-bootstrap-tpls-1.2.2.min.js',
      'src/js/angular-local-storage.min.js',
      'src/bootstrap-clockpicker.min.js',

      // Include Google maps API
      "https://maps.googleapis.com/maps/api/js?key=AIzaSyBSEb7-qBPegmyEPD2poDx7MopVj0_LY2Q&extension=.js&libraries=places",

      // Service dependencies
      'src/js/services/*.js',

      // Directive dependencies
      'src/js/directives/*.js',

      // Controller dependencies
      'src/js/controllers/*.js',

      // Test module
      'test/app.js',

      // Test files
      'test/controllers/*.js',
      'test/service_mocks/*.js',
      'test/services/*.js'
    ],

    // list of files to exclude
    exclude: [
      'src/js/toggle.js'
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
      'src/js/controllers/*.js': ['coverage'],
      'src/js/directives/*.js': ['coverage'],
      'src/js/services/*.js': ['coverage']
    },


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress', 'dots', 'junit', 'coverage'],


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    // logLevel: config.LOG_INFO,
    logLevel: config.LOG_DISABLE,



    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: ['Chrome', 'PhantomJS'],


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: true,

    junitReporter: {
      outputFile: 'results/test-results.xml',
    },

    coverageReporter: {
      type : 'lcov',
      dir : 'coverage/',
	    subdir: '.'
    }
  });
};
