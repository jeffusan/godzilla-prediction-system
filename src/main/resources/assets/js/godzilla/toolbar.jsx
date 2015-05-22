define(function(require) {

  'use strict';

  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var ButtonToolbar = ReactBootstrap.ButtonToolbar;
  var Button = ReactBootstrap.Button;

  var GodzillaToolbar = React.createClass({

    heatLowClicked: function() {
      this.props.heat(5);
    },
    heatMediumClicked: function() {
      this.props.heat(30);
    },
    heatHighClicked: function() {
      this.props.heat(100);
    },
    filterLowClick: function() {
      this.props.filter(18);
    },
    filterMediumClick: function() {
      this.props.filter(16);
    },
    filterHighClick: function() {
      this.props.filter(13);
    },

    render: function() {
      return (
        <div id="panel">
          <ButtonToolbar>
            <Button bsSize='small' disabled>Heat</Button>
            <Button
              bsStyle='info'
              bsSize='small'
              onClick={this.heatLowClicked}>Low</Button>

            <Button
              bsStyle='warning'
              bsSize='small'
              onClick={this.heatMediumClicked}>Med</Button>

            <Button
              bsStyle='danger'
              bsSize='small'
              onClick={this.heatHighClicked}>High</Button>
            <Button bsSize='small' disabled>Refine</Button>

            <Button
              bsStyle='info'
              bsSize='small'
              onClick={this.filterLowClick}>Low</Button>

            <Button
              bsStyle='warning'
              bsSize='small'
              onClick={this.filterMediumClick}>Med</Button>

            <Button
              bsStyle='danger'
              bsSize='small'
              onClick={this.filterHighClick}>High</Button>
          </ButtonToolbar>
        </div>
      );
    }
  });

  return GodzillaToolbar;
});
