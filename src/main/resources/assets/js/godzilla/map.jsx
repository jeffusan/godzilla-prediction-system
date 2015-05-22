define(function(require) {

  'use strict';

  var React = require('react');

  var Map = React.createClass({

    getDefaultProps: function () {
      return {
        mapOptions: {
          center: new google.maps.LatLng(33.1955102, 136.9374724),
          zoom: 5
        },
        flightPath: new google.maps.Polyline({
          path: [],
          geodesic: true,
          strokeColor: '#FF0000',
          strokeOpacity: 1.0,
          strokeWeight: 2
        }),
        heatMap: new google.maps.visualization.HeatmapLayer({
          data: [],
          radius: 10
        }),
        markers: []
      };
    },

    getInitialState: function() {
      return {
        map: {}
      };
    },

    componentDidMount: function () {
      console.log("mount");
      this.setState({
        map: new google.maps.Map(this.getDOMNode(), this.props.mapOptions)
      })
    },

    plotToLatLng: function(plots) {
      var latlng = [];
      for(var i=0; i < plots.length; i++) {
        latlng.push(
          new google.maps.LatLng(
            plots[i].latitude,
            plots[i].longitude
          ));
      }
      return latlng;
    },

    latLngToMarker: function(latlngs) {
      for(var i=0; i <latlngs.length; i++) {
        this.props.markers.push(
          new google.maps.Marker({
            position: latlngs[i],
            map: this.state.map,
            title: 'Gojira!'
          }));
      }
    },

    clearMarkers: function() {
      for(var i=0; i < this.props.markers.length; i++) {
        this.props.markers[i].setMap(null);
      }
    },

    updateHeatMap: function() {
      this.props.heatMap.setMap(null);
      this.props.heatMap = new google.maps.visualization.HeatmapLayer({
        data: new google.maps.MVCArray(this.plotToLatLng(this.props.heatPlots)),
        radius: 10
      });
      this.props.heatMap.setMap(this.state.map);
    },

    updateLocations: function() {
      this.clearMarkers();
      this.props.flightPath.setMap(null);
      var ll = this.plotToLatLng(this.props.locations);
      this.props.flightPath.path = ll;
      this.latLngToMarker(ll);
    },

    componentDidUpdate: function() {
      this.updateHeatMap();
      this.updateLocations();
    },

    render: function () {

      return (
          <div className='map-godzilla'>
          </div>
      );
    }

  });

  return Map;

});


