# Project Name

## Team Information

### Team Members
- Yaxuan Yu
- Xinyi Lu
- Yudong Zhang
- Chong lyu

---

## Project Description

### Domain

Smart Kitchen Assistant is a Java desktop application designed to help users manage their kitchen ingredients, plan meals, and organize grocery shopping. 

Users can add, edit, and remove ingredients from their pantry, including quantities and expiration dates. The application uses an external recipe API to suggest meals based on available ingredients, helping users decide what to cook.

Users can also generate shopping lists based on missing ingredients required for their planned meals. Additionally, the application highlights ingredients that are close to expiring to help reduce food waste.
 
The goal of Smart Kitchen Assistant is to make everyday cooking and grocery shopping easier, more organized, and more efficient.


---

## User Stories

### User Story 1: Manage Pantry Inventory

As a user, I want to add, edit, and remove ingredients from my pantry, including their quantities and expiration dates, so that I can keep track of what food I have at home.

### User Story 2: Find Recipes Based on Available Ingredients

As a user, I want to search for recipes based on the ingredients I already have, so that I can decide what to cook without buying unnecessary groceries.

### User Story 3: Generate a Shopping List

As a user, I want to automatically generate a shopping list based on the ingredients missing from my planned meals, so that I can buy only what I need.

### User Story 4: Track Ingredient Expiration Dates

As a user, I want to see which ingredients are close to expiring, so that I can use them before they go bad and reduce food waste.

---

## Use Cases

### Use Case 1
| Field | Description |
|---|---|
| **Name** | Manage Pantry Inventory |
| **Primary Actor** | User |
| **Preconditions** | The application is running. |
| **Trigger** | The user chooses to add, edit, or remove an ingredient. |
| **Success Outcome** | The pantry inventory is updated. |

**Main Success Scenario**

1. The user opens the pantry management interface.
2. The system displays the current inventory.
3. The user selects an action to add, edit, or remove an ingredient.
4. The system displays the appropriate input fields or confirmation option.
5. The user enters or confirms the ingredient information.
6. The system updates the pantry inventory.
7. The system displays the updated inventory.

**Extensions**

- **5a.** The user cancels → The system returns without changes.
- **6a.** The entered information is invalid → The system displays an error message and allows the user to correct it.
- **6b.** The ingredient cannot be found → The system informs the user and leaves the inventory unchanged.

---


### Use Case 2
| Field | Description |
|---|---|
| **Name** | Find Recipes Based on Available Ingredients |
| **Primary Actor** | User |
| **Supporting Actor** | External Recipe API |
| **Preconditions** | The application is running, and the pantry contains at least one ingredient. |
| **Trigger** | The user requests recipe suggestions. |
| **Success Outcome** | The system displays matching recipes and their details. |

**Main Success Scenario**

1. The user opens the recipe search interface.
2. The system displays the available pantry ingredients.
3. The user requests recipe suggestions.
4. The system sends the available ingredients to the external recipe API.
5. The API returns matching recipes.
6. The system displays the recipe suggestions.
7. The user selects a recipe.
8. The system displays the recipe's ingredients and cooking instructions.

**Extensions**

- **5a.** The API is unavailable → The system displays an error message.
- **5b.** No matching recipes are found → The system informs the user.
- **7a.** The user does not select a recipe → The system continues displaying the suggestions.

---

### Use Case 3

| Field | Description |
|---|---|
| **Name** | Generate a Shopping List |
| **Primary Actor** | User |
| **Preconditions** | The application is running, and at least one recipe has been selected for a planned meal. |
| **Trigger** | The user requests a shopping list. |
| **Success Outcome** | A shopping list containing missing ingredients and required quantities is generated and displayed. |

**Main Success Scenario**

1. The user opens the shopping list interface.
2. The system displays the selected recipes.
3. The user requests a shopping list.
4. The system retrieves the required ingredients and quantities.
5. The system compares them with the current pantry inventory.
6. The system identifies missing or insufficient ingredients.
7. The system generates a shopping list.
8. The system displays the shopping list.

**Extensions**

- **4a.** Recipe ingredient information is unavailable → The system informs the user that the shopping list cannot be fully generated.
- **6a.** All required ingredients are available → The system displays an empty shopping list and informs the user that no additional ingredients are needed.

---

### Use Case 4: Track Ingredient Expiration Dates

| Field | Description |
|---|---|
| **Name** | Track Ingredient Expiration Dates |
| **Primary Actor** | User |
| **Preconditions** | The application is running, and the pantry contains ingredients with recorded expiration dates. |
| **Trigger** | The user opens the expiration tracking interface. |
| **Success Outcome** | Ingredients are displayed with their expiration status, highlighting those that are expired or close to expiring. |

**Main Success Scenario**

1. The user opens the expiration tracking interface.
2. The system retrieves the ingredients and expiration dates.
3. The system compares the expiration dates with the current date.
4. The system identifies expired or nearly expired ingredients.
5. The system displays the ingredients with their expiration status.
6. The user reviews the expiration information.

**Extensions**

- **2a.** No expiration information is available → The system informs the user.
- **4a.** No ingredients are expired or close to expiring → The system displays a message indicating that there are no expiration warnings.

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Yudong Zhang | Manage Pantry Inventory |
| Xinyi Lu | Find Recipes Based on Available Ingredients |
| Yaxuan Yu | Generate a Shopping List |
| Chong Lyu | Track Ingredient Expiration Dates |

---

## Entities

### Entity 1

### Entity 2

### Entity 3

### Entity 4

---

## API Usage

**API Name:**
TheMealDB API

**Documentation:** [Filter by main ingredient
themealdb.com/api/json/v1/1/filter.php?i=chicken_breast
Filter meals that include a single ingredient.
themealdb.com/api/json/v1/1/search.php?s=Arrabiata]


**How it is used in the program:**
Filter by Main Ingredient:
The system sends a selected pantry ingredient to the API and retrieves a list of matching recipes. The results are displayed to the user, allowing them to decide what to cook based on their available ingredients.

Search Meal by Name:
When the user selects a recipe, the system retrieves detailed recipe information, including required ingredients, measurements, and cooking instructions. The system displays the recipe details and compares the required ingredients with the current pantry inventory to automatically generate a shopping list of missing or insufficient ingredients.

---

## Design Diagrams

### UML Class Diagram


### Sequence Diagrams

---

## How to Run
**How to Run the App**
1. Install Java JDK 17 or later and IntelliJ IDEA.
 2. Clone the project repository from GitHub using 
    `git clone <repository-url>`.
 3. Open the cloned project folder in IntelliJ IDEA.
 4. Ensure the correct Java SDK (JDK 17 or later) is selected in **Project Structure**.
 5. Locate the main class (e.g., `Main.java`) and click **Run** to launch the refrigerator app.
 
**Prerequisites**
- Java JDK 17 or later
 - IntelliJ IDEA (Community or Ultimate Edition)
 - Git installed to clone the repository
 
**Getting Started**
1. Make sure you have all the prerequisites installed (see **Prerequisites** above).
 2. Clone the repository using `git clone <repository-url>`.
 3. Open the project in IntelliJ IDEA.
 4. Configure the project to use Java JDK 17 or later.
 5. Run the main class (e.g., `Main.java`) to start the refrigerator app.



---

## Notes

[Any additional project information.]
