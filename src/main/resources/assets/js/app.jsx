define(function(require) {

  'use strict';

  var React = require('react');
  var Godzilla = require('jsx!godzilla/godzilla');
  var Router = require('react-router');
  var Route = Router.Route;
  var RouteHandler = Router.RouteHandler;
  var content = document.getElementById('godzilla');

  var App = React.createClass({

    render: function () {
      return (
        <div>
          <RouteHandler/>
        </div>
      );
    }

  });

  App.init = function () {
    var routes = (
      <Route path="/" handler={Godzilla}/>
    );

    Router.run(routes, function (Handler, state) {
      React.render(<Handler/>, document.getElementById('godzilla'));
    });

  };

  return App;

});
