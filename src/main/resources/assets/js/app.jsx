define(function(require) {

  'use strict';

  var React = require('react');
  var Toolbar = require('jsx!godzilla/toolbar');
  var Map = require('jsx!godzilla/map');

  var App = React.createClass({

    getInitialState: function() {
      return {
        heatPlots: [],
        sampleRate: 30,
        locations: [],
        deviation: 16
      };
    },

    getHeat: function(sampleRate) {
      if (typeof sampleRate === "undefined") {
        sampleRate = this.state.sampleRate
      }
      $.ajax({
        'type': 'GET',
        'url': '/heat/' + sampleRate,
        'contentType': 'application/json',
        'async': 'false',
        'success' : function(data) {
          if(this.isMounted()) {
            this.setState({
              heatPlots: data
            });
          }
        }.bind(this),
        'error': function(data) {
          console.log("error");
        }.bind(this)
      });
    },

    getLocations: function(deviation) {
      if (typeof deviation === "undefined") {
        deviation = this.state.deviation
      }

      $.ajax({
        'type': 'GET',
        'url': '/locations/' + deviation,
        'contentType': 'application/json',
        'async': 'false',
        'success' : function(data) {
          if(this.isMounted()) {
            this.setState({
              locations: data
            });
          }
        }.bind(this),
        'error': function(data) {
          console.log("error");
        }.bind(this)
      });
    },

    componentDidMount: function() {
      this.getHeat();
      this.getLocations();
    },

    heat: function(sampleRate) {
      this.setState({sampleRate: sampleRate});
      this.getHeat(sampleRate);
    },

    filter: function(deviation) {
      this.setState({deviation: deviation});
      this.getLocations(deviation);
    },

    render: function () {
      return (
        <div>
          <Toolbar
            heat={this.heat}
            filter={this.filter}/>
          <Map
            heatPlots={this.state.heatPlots}
            locations={this.state.locations}
            {...this.props}/>
        </div>
      );
    }

  });

  App.init = function () {

      React.render(<App/>, document.getElementById('godzilla'));

  };

  return App;

});
