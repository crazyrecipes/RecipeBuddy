import React, { Component } from 'react';
import EchoForm from '../components/echoform';

class Home extends Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
        <>
          <div class="spacer"></div>
            <div class="content_outer box">
              <div class="content_inner">
              <p>Home</p>
              <EchoForm />
            </div>
          </div>
        </>
    );
  }
}

export default Home;