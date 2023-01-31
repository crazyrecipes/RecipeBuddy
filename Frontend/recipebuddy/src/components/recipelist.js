import React, {Component} from 'react';
import API_URL from '../RecipeBuddyMap.js';

class RecipeList extends Component {
    constructor(props) {
        super(props);
        this.state = {recipes: []}
    }

    async componentDidMount() {
        const response = await fetch("http://localhost:8080/recipes");
        const body = await response.json();
        this.setState({recipes: body});
    }

    render() {
        const {recipes} = this.state;
        return (
            <div>
                <p>Recipes:</p>
                {recipes.map(recipe =>
                    <div key={recipe.id}>
                        {recipe.name} ({recipe.desc})
                    </div>
                )}
            </div>
        )
    }
}
export default RecipeList;