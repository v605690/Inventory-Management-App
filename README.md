***

```markdown
# Inventory Analytics Dashboard

A web application designed to visualize and analyze product inventory data. This application utilizes Spring Boot for the backend and Thymeleaf with Google Charts for the frontend visualization.

## 📋 Features

### Category Price Analysis
The core feature currently implemented is the **Category Price Comparison** dashboard.
*   **Visual Charting:** Displays a Bar/Column chart comparing the Total Retail Price vs. Total Cost Price for each product category.
*   **Data Grid:** Provides a detailed tabular view of category statistics, including:
    *   Category Name
    *   Total Retail Price
    *   Total Cost Price
    *   Product Count per Category
*   **Dynamic Currency Parsing:** Robust client-side parsing handles currency formats (e.g., `$1,200.00`) to render accurate numerical charts.
*   **Responsive Design:** Uses W3.CSS for basic layout and Google Charts for responsive visualization.

## 🛠 Tech Stack

### Backend
*   **Language:** Java 17 SDK
*   **Framework:** Spring Boot (Spring MVC)
*   **Database Abstraction:** Spring Data JPA
*   **Persistence:** Jakarta EE (JPA)
*   **Tooling:** Lombok (for boilerplate reduction)

### Frontend
*   **Template Engine:** Thymeleaf
*   **Visualization:** Google Charts API (CoreChart, Bar/Column Charts)
*   **Styling:** W3.CSS, Inline CSS
*   **Scripting:** JavaScript (ES5/ES6 compatible)

## 🚀 Setup & Installation

### Prerequisites
*   Java Development Kit (JDK) 17
*   Maven or Gradle (depending on your build tool)
*   A configured database (e.g., H2, MySQL, PostgreSQL) defined in `application.properties`.

### Running the Application
1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    ```
2.  **Navigate to project directory:**
    ```bash
    cd inventory-analytics
    ```
3.  **Run via Maven Wrapper:**
    ```bash
    ./mvnw spring-boot:run
    ```
4.  **Access the Application:**
    Open your browser and go to:
    `http://localhost:8080` (or the configured port).

## 📂 Application Structure

### Controller Integration
The view expects a `model` attribute named `tableData` to be passed from the Spring Controller.

**Expected Data Structure (DTO):**
The `tableData` list should contain objects with the following fields:
*   `categoryName` (String)
*   `totalVbrp` (String/Money format, e.g., "$1,200.00") - *Retail Price*
*   `totalVbcp` (String/Money format, e.g., "$800.00") - *Cost Price*
*   `productCount` (Integer/Long)

### Frontend Implementation Details
*   **Google Charts Integration:**
    The application asynchronously loads `google.charts.loader.js`. The chart logic is encapsulated in the `drawChart()` function, which parses Thymeleaf model attributes directly into JavaScript variables:
    ```javascript
    var serverData = /*[[${tableData}]]*/ [];
    ```
*   **Error Handling:**
    Includes safety checks for empty datasets and a `parseMoney` helper function to strip currency symbols and commas before charting.

## 🏗 Architecture & Design

This project follows a standard **Monolithic MVC (Model-View-Controller)** architecture, ensuring separation of concerns while maintaining simplicity in deployment.

### 1. Backend Layer (Spring Ecosystem)
*   **Controller Layer (Spring MVC):** Handles HTTP requests. It fetches data from the service layer, wraps it in DTOs (Data Transfer Objects), and passes them to Thymeleaf templates via the `Model`.
*   **Service Layer:** Contains business logic. It handles calculations (e.g., aggregating category totals) and transforms raw Entity data into View-optimized DTOs.
*   **Persistence Layer (Spring Data JPA & Jakarta EE):**
    *   Uses `JpaRepository` interfaces for standard CRUD operations.
    *   Entities are defined using Jakarta Persistence annotations (`@Entity`, `@Table`, `@Id`).
    *   **Lombok** is utilized to reduce boilerplate code (Getters, Setters, Constructors) in Entities and DTOs.

### 2. Frontend Layer (Thymeleaf & Google Charts)
*   **Hybrid Rendering:** The application uses a "Server-First" approach. The initial HTML is rendered server-side by Thymeleaf.
*   **Data Injection:** JSON data for charts is injected directly into the JavaScript context using Thymeleaf inlining (`th:inline="javascript"`), eliminating the need for a separate REST API call for this specific view.
*   **Visualization:** Google Charts (CoreChart package) renders interactive data visualizations based on the injected data.

---

## 📊 Feature Deep Dive: Category Price Analysis

The **Category Prices** module (`/category-prices` endpoint) is a key analytical tool providing a comparative view of retail versus cost pricing.

### Data Visualization Logic
The chart renders a **Column Chart** comparing two specific metrics per category:
1.  **Total VBRP:** The sum of the *Retail Price* of all products in a category.
2.  **Total VBCP:** The sum of the *Cost Price* of all products in a category.

**Client-Side Data Processing:**
The application handles data sanitization on the client side. Since the server transmits currency as formatted strings (e.g., `"$1,200.50"`), the frontend includes a dedicated parser:
*   **Currency Stripping:** Removes `$` symbols, commas, and whitespace.
*   **Safety Checks:** Converts `NaN` or `null` values to `0` to prevent chart rendering errors.

### Tabular Data View
Below the visualization, a detailed data grid provides exact figures, including a `Product Count` column to give context to the pricing totals (e.g., distinct high-value items vs. bulk low-value items).

---

## 💻 Technical Specifications

### Tech Stack
| Component | Technology | Usage |
| :--- | :--- | :--- |
| **Language** | Java 17 SDK | Core application logic |
| **Framework** | Spring Boot 3.x | Application context & dependency injection |
| **Web MVC** | Spring MVC | Routing and HTTP handling |
| **ORM** | Spring Data JPA / Hibernate | Database interaction |
| **Imports** | Jakarta EE 9/10 | `jakarta.persistence.*` namespace |
| **Utils** | Project Lombok | Code generation (annotations) |
| **Template Engine** | Thymeleaf | Server-side HTML rendering |
| **Charting** | Google Charts API | Client-side SVG charts |
| **CSS Framework** | W3.CSS | Responsive layout and grid system |

### Data Model (DTO Structure)
The View layer expects a specific Data Transfer Object structure passed as a list named `tableData`.

```
