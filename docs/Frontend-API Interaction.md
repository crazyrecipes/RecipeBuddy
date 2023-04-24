Open in VScode or Github for the best experience.
### The CrazyRecipes Team - Juno Meifert - 3/2/2023
# Frontend-API Interaction
This document details what API calls are available and how they are formatted.

API calls must follow same-origin policy. Cross-origin calls will be rejected by the API.

No more than 100 API calls per second may be made, this will cause a HTTP 429 and block further calls.

Almost every POST or PUT call will return a copy of the data you just sent to make displaying it easier. This does not happen with photos in order to save bandwidth.

# API Map
```
api/
    recipes -- GET, POST
    recipe/ -- GET, PUT, DELETE
        0
        1
        ...
    
    ingredients -- GET, POST
    
    allergens -- GET, POST

    utensils -- GET, POST
    
    search -- POST
    
    backup -- GET
    
    restore -- POST
    
    photo/ -- GET, PUT
        0
        1
        ...
    
    
```

# Response Structures
## recipe/x
```json
{
    "id": "0",
    "name": "The Collins Burger",
    "desc": "A delicious burger made to honor the man himself.",
    "rating": 4.3,
    "cooked": 6,
    "ingredients": [
        "Bun",
        "Ground Beef",
        "Onion",
        "Garlic",
        "Tomato",
        "Lettuce",
        "Butter",
        "Swiss Cheese"
    ],
    "utensils": [
        "Frying pan",
        "Spatula"
    ],
    "steps": [
        "Make patty with ground beef, diced onion, and diced garlic.",
        "Cook patty medium with butter to taste.",
        "Crisp bun in pan with remaining butter and juice.",
        "Put patty, lettuce, cheese, and tomato on bun. Serve."
    ],
    "allergens": [
        "Gluten",
        "Dairy"
    ],
    "tags": [
        "quick",
        "burger",
        "simple"
    ]
}
```

## recipes
```json
[
    {
       (Recipe parameters here)
    },
    {
       (Recipe parameters here)
    }
]

```

## ingredients
```json
[
    "Tortilla",
    "Onion"
]
```

## allergens
```json
[
    "Gluten",
    "Soy"
]
```

## utensils
```json
[
    "Frying Pan",
    "Spatula"
]
```

## search
```json
[
    {
        (Recipe parameters here)
    },
    {
        (Recipe parameters here)
    }
]
```

## photo
Base64 encoded image.

## backup
```json
[
    {
        (Recipe parameters here)
    },
    {
        (Recipe parameters here)
    }
]
```

# Request Structures
## search
```json
{
    "query": "burrito",
    "ingredients": "SOME",
    "utensils": "SOME",
    "allergens": "BLOCK"
}
```
```
search parameter values:
ingredients, utensils: NONE, SOME, ALL
allergens: SHOW, BLOCK
```
## photo
Image file.

## restore
```json
[
    {
        (Recipe parameters here)
    },
    {
        (Recipe parameters here)
    }
]
```

