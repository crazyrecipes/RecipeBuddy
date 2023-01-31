import React, { Component } from 'react';
import RecipeList from '../components/recipelist';

class About extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <>
            <div class="spacer"></div>
                <div class="content_outer box">
                <div class="content_inner">
                <p>About</p>
                <RecipeList />
                <p>Test2</p>
                </div>
            </div>
            </>
        );
    }
}

export default About;