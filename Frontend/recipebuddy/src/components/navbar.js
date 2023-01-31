import React from 'react';
import {Link} from 'react-router-dom';
import '../App.css';

function Navbar() {
    return (
      <div class="inline_menu">
        <div class="inline_text">Juno's React Test App</div>
        <div class="inline_button">
            <Link to="/">Home</Link>
        </div>
        <div class="inline_button">
            <Link to="/about">About</Link>
        </div>
        
      </div>
    );
  }
  
export default Navbar;