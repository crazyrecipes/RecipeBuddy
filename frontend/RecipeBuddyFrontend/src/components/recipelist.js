import React, {Component} from 'react';
import RecipeBuddyMap from '../recipebuddymap.js';
import RecipeEditor from './recipeeditor.js';

/* 
    RecipeList handles displaying a list of all stored recipes.
*/
class RecipeList extends Component {
    constructor() {
        super();
        this.state = {recipes: []}
    }

    /* Fetch and display recipes upon mount */
    async componentDidMount() {
        const response = await fetch(RecipeBuddyMap.API_URL + "/recipes")
                            .catch(error => {window.alert(error);});
        const body = await response.json();
        this.setState({recipes: body});
    }

    /* Display */
    render() {
        const {recipes} = this.state;
        return (
            <>
                {recipes.map(recipe =>
                    <div key={recipe.id}>
                        <RecipeEditor
                            newID = {recipe.id}
                            newName = {recipe.name}
                            newDesc = {recipe.desc}
                        />
                    </div>
                )}
            </>
        )
    }
}

export default RecipeList;