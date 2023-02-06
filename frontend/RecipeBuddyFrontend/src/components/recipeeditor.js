import React, { Component } from 'react';
import RecipeBuddyMap from '../recipebuddymap.js';

/* 
  RecipeEditor is a single editable entry in the list of recipes. 
*/
class RecipeEditor extends Component {
  constructor({newID, newName, newDesc}) {
    super();
    
    /* Initialize state */
    this.state={id: newID, name: newName, desc: newDesc}

    /* Bind input handlers */
    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleDescChange = this.handleDescChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleRemove = this.handleRemove.bind(this);
  }

  /* Update state when this recipe changes name */
  handleNameChange(event) {
    this.setState({name: event.target.value});
  }

  /* Update state when this recipe changes desc */
  handleDescChange(event) {
    this.setState({balance: event.target.value});
  }

  /* Update this Recipe with this RecipeEditor's current state */
  async handleSubmit(event) {
    event.preventDefault();
    await fetch(RecipeBuddyMap.API_URL + "/recipes/" + this.state.id, {
        method: 'PUT',
        headers: {
            'Accept':'application/json',
            'Content-Type':'application/json',
        },
        body: JSON.stringify(this.state),
    }).catch(error => {
      window.alert(error);
    });
    window.location.reload();
  }

  /* Remove this Recipe */
  async handleRemove(event) {
    event.preventDefault();
    if(window.confirm("Are you sure you want to delete this recipe?")) {
      await fetch(RecipeBuddyMap.API_URL + "/recipes/" + this.state.id, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
      }).catch(error => {
          window.alert(error);
      });
      window.location.reload();
    }
  }

  /* Display */
  render() {
    return (
        <>
        <div>
        <form onSubmit={this.handleSubmit}>
            <label>
                <input type="text" value={this.state.name} onChange={this.handleNameChange} />
                <input type="text" value={this.state.desc} onChange={this.handleDescChange} />
            </label>
            <button type="submit">Save</button>
            <button onClick={this.handleRemove}>Remove</button>
        </form>
        </div>
        </>
    );
  }
}

export default RecipeEditor;
