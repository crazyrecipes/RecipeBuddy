import React, { Component } from 'react';
import RecipeBuddyLogo from './logo.png';

/*
    About contains the version information of the frontend
*/
class About extends Component {
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
                        <p><a href="https://github.com/crazyrecipes/recipebuddy">https://github.com/crazyrecipes/recipebuddy</a></p>
                        <p>Version 0.0.0</p>
                    </div>
                </div>
            </>
        );
    }
}

export default About;