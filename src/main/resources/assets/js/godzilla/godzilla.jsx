define(function(require) {

  'use strict';

  var React = require('react');

  var Godzilla = React.createClass({

    getDefaultProps: function () {
      return {
        initialZoom: 5,
            mapCenterLat: 33.1955102,
            mapCenterLng: 136.9374724
        };
    },

    componentDidMount: function (rootNode) {
        var mapOptions = {
            center: this.mapCenterLatLng(),
            zoom: this.props.initialZoom
        },
        map = new google.maps.Map(this.getDOMNode(), mapOptions);
        this.setState({map: map});

      $.ajax({
        'type': 'GET',
        'url': '/heat',
        'contentType': 'application/json',
        'async': 'false',
        'success' : function(data) {
          if(this.isMounted()) {
            var plots = [];

            for (var i = 0; i < data.length; i++) {
              var p = data[i];
              plots.push(new google.maps.LatLng(p.latitude, p.longitude));
            }
            var pointArray = new google.maps.MVCArray(plots);

            var heatmap = new google.maps.visualization.HeatmapLayer({
              data: pointArray,
              radius: 30
            });

            heatmap.setMap(map);
          }
        }.bind(this),
        'error': function(data) {
          console.log("error");
        }.bind(this)
      });
      $.ajax({
        'type': 'GET',
        'url': '/locations',
        'contentType': 'application/json',
        'async': 'false',
        'success' : function(data) {
          if(this.isMounted()) {
            var plots =[];
            for (var i = 0; i < data.length; i++) {
              var p = data[i];
              plots.push(new google.maps.LatLng(p.latitude, p.longitude));
            }
            for(var i = 0; i < plots.length; i++) {
              var p = plots[i];
              new google.maps.Marker({
                position: p,
                map: map,
                title: 'Gojira!'
              });
            }

            var flightPath = new google.maps.Polyline({
              path: plots,
              geodesic: true,
              strokeColor: '#FF0000',
              strokeOpacity: 1.0,
              strokeWeight: 2
            });
            flightPath.setMap(map);
          }
        }.bind(this),
        'error': function(data) {
          console.log("error");
        }.bind(this)
      });

    },

    mapCenterLatLng: function () {
        var props = this.props;
        return new google.maps.LatLng(props.mapCenterLat, props.mapCenterLng);
    },

    render: function () {
        return (
          <div className='map-godzilla'></div>
        );
    }

  });

  return Godzilla;

});


