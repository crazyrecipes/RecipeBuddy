import React from "react";
import RecipeBuddyMap from "../recipebuddymap.js";

/* 
  UserCreator handles creating new recipes.
*/
class RecipeCreator extends React.Component {
  constructor() {
    super();
    /* Bind input handlers */
    this.handleCreate = this.handleCreate.bind(this);
  }

  /* Create a new recipe */
  async handleCreate(event) {
    event.preventDefault();
    await fetch(RecipeBuddyMap.API_URL + "/recipes", {
        method: 'POST',
        headers: {
            'Accept':'application/json',
            'Content-Type':'application/json',
        },
        body: "New Recipe",
    }).catch(error => {
      window.alert(error);
    });
    window.location.reload();
  }

  /* Display */
  render() {
    return (
      <>
        <button onClick={this.handleCreate}>Create Recipe</button>
      </>
    );
  }
}

export default RecipeCreator;