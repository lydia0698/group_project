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

As a user, I want to add, edit, and remove ingredients from my pantry, so that I can keep track of what food I have at home.

### User Story 2: Find Recipes Based on Available Ingredients

As a user, I want to search for recipes based on the ingredients I already have, so that I can decide what to cook without buying unnecessary groceries.

### User Story 3: Generate a Shopping List

As a user, I want to see which ingredients for my selected recipe are missing from my pantry, so that I know what to look for when I shop.

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
| **Preconditions** | The application is running, and the user has selected a recipe. |
| **Trigger** | The user requests to view missing ingredients for the selected recipe. |
| **Success Outcome** | The system displays a list of ingredients required by the selected recipe that are missing from the user's pantry. |

**Main Success Scenario**

1. The user selects a recipe.
2. The user requests to view the missing ingredients.
3. The system retrieves the required ingredients for the selected recipe from the external recipe API or previously retrieved recipe data.
4. The system retrieves the current pantry inventory.
5. The system compares the required ingredients with the available pantry ingredients.
6. The system identifies the ingredients missing from the pantry.
7. The system displays the list of missing ingredients.

**Extensions**

- **3a.** The API is unavailable and recipe information is not already stored → The system displays an error message.
- **3b.** Recipe ingredient information is unavailable → The system informs the user that missing ingredients cannot be identified.
- **4a.** The pantry is empty → The system identifies all required ingredients as missing.
- **6a.** All required ingredients are available → The system informs the user that no ingredients are missing.

---

### Use Case 4: Track Ingredient Expiration Dates

| **Field** | **Description** |
|---|---|
| **Name** | Track Ingredient Expiration Dates |
| **Primary Actor** | User |
| **Supporting Actor** | Food Shelf-Life Data Source |
| **Preconditions** | The application is running, and the pantry contains ingredients with recorded purchase dates and storage conditions. |
| **Trigger** | The user opens the expiration tracking interface. |
| **Success Outcome** | The system displays ingredients with estimated expiration dates and highlights those that may be close to expiring. |

**Main Success Scenario**

1. The user opens the expiration tracking interface.
2. The system retrieves the pantry ingredients, purchase dates, and storage conditions.
3. The system retrieves estimated shelf-life information for the ingredients from the food shelf-life data source.
4. The system calculates estimated expiration dates based on the purchase dates and shelf-life information.
5. The system compares the estimated expiration dates with the current date.
6. The system identifies ingredients that may be expired or close to expiring.
7. The system displays the ingredients with their estimated expiration status.

**Extensions**

- **3a.** Shelf-life information is unavailable → The system informs the user that an estimated expiration date cannot be calculated.
- **3b.** An ingredient is not found in the data source → The system allows the user to enter an expiration date manually.
- **6a.** No ingredients are close to expiring → The system displays a message indicating that there are no expiration warnings.

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Yudong Zhang | Manage Pantry Inventory |
| Chong Lyu | Find Recipes Based on Available Ingredients |
| Yaxuan Yu | Generate a Shopping List |
| Xinyi Lu | Track Ingredient Expiration Dates |

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
USDA FoodKeeper

**Documentation:** 
### Filter by main ingredient
themealdb.com/api/json/v1/1/filter.php?i=chicken_breast
### Filter meals that include a single ingredient.
themealdb.com/api/json/v1/1/search.php?s=Arrabiata
### Find expiration date:
https://www.fsis.usda.gov/shared/data/EN/foodkeeper.json


**How it is used in the program:**
### Filter by Main Ingredient:
The system sends a selected pantry ingredient to the API and retrieves a list of matching recipes. The results are displayed to the user, allowing them to decide what to cook based on their available ingredients.

### Search Meal by Name:
When the user selects a recipe, the system retrieves detailed recipe information, including required ingredients, measurements, and cooking instructions. The system displays the recipe details and compares the required ingredients with the current pantry inventory to automatically generate a shopping list of missing or insufficient ingredients.

### Find expiration date:
The system retrieves estimated shelf-life information for different food items, including fruits and vegetables, based on their types and storage conditions. It uses this information along with the user's recorded purchase dates to calculate estimated expiration dates.
The system then compares these dates with the current date and displays ingredients that are expired or close to expiring, helping users reduce food waste.

---

## Design Diagrams

### UML Class Diagram


### Sequence Diagrams

---

## How to Run

### Prerequisites
- Java JDK 27
- IntelliJ IDEA (Community or Ultimate Edition)
- Git
- Internet connection for accessing TheMealDB API

### Getting Started
1. Make sure Java JDK 27, IntelliJ IDEA, and Git are installed.
2. Clone the repository using:
   ```bash
   git clone https://github.com/lydia0698/group_project.git
   ```
3. Open the cloned `group_project` folder in IntelliJ IDEA.
4. Go to **File → Project Structure → Project**.
   - Set **Project SDK** to JDK 27.
   - Set **Language level** to 27 or SDK default.
5. Under **Project Structure → Modules → Dependencies**, ensure the Module SDK is set to the Project SDK.
6. Ensure that `lib/json-20260814.jar` is added to the project's dependencies.
7. Make sure `resources/foodkeeper.json` is present in the project.
8. Open `src/app/Main.java` and click **Run** to launch the Smart Kitchen Assistant.

### Notes
- The application uses TheMealDB API to retrieve recipes, so an internet connection is required for recipe-related features.
- FoodKeeper data is stored locally in `resources/foodkeeper.json`.
- Pantry inventory is stored in memory and will reset when the application is restarted.

---

## Notes

[Any additional project information.]
