import React, { Component } from 'react';
import RecipeList from '../components/recipelist.js';
import RecipeCreator from '../components/recipecreator.js';
import RecipeBuddyLogo from './logo.png';

/*
  Home is the primary page with all entries.
*/
class Home extends Component {
  /*
  constructor() {
    super();
  }
  */
  render() {
    return (
        <>
          <div className="spacer"></div>
            <div className="content_outer box">
              <div className="content_inner">
                <div className="centered_container">
                  <img src={RecipeBuddyLogo} alt="RecipeBuddy" width="100%"></img>
                </div>
                <div className="spacer"></div>
                <p><i>Recipes:</i></p>
                <RecipeList />
                <RecipeCreator />
            </div>
          </div>
        </>
    );
  }
}

export default Home;