// `main.js` is the file that sbt-web will use as an entry point
(function (requirejs) {
  'use strict';

  // -- RequireJS config --
  requirejs.config({
    // Packages = top-level folders; loads a contained file named 'main.js"
    packages: ['godzilla', 'maps'],
    paths: {
      'react': ['/webjars/react/0.13.1/react-with-addons'],
      'JSXTransformer': ['/webjars/jsx-requirejs-plugin/0.6.0/js/JSXTransformer'],
      'jsx': ['/webjars/jsx-requirejs-plugin/0.6.0/js/jsx'],
      'jquery': ['/webjars/jquery/2.1.4/jquery'],
      'react-router' : ['/webjars/react-router/0.13.2/ReactRouter'],
      'react-router-shim': 'react-router-shim',
      'jsRoutes': ['/jsroutes'],
      'text': ['/webjars/requirejs-text/2.0.10-3/text']
    },
    jsx: {
      fileExtension: '.jsx'
    },
    shim : {
      'jsRoutes': {
        deps: [],
        // it's not a RequireJS module, so we have to tell it what var is returned
        exports: 'jsRoutes'
      },
      'react': {
        deps: ['jquery'],
        exports: 'react'
      },
      'react-router': {
        deps:    ['react'],
        exports: 'Router'
      }
    }
  });

  requirejs.onError = function (err) {
    console.log(err);
  };

  require(['jsx!app'], function(App){
    console.log('initializing app');
    App.init();

  });

})(requirejs);
