**Personal Budget Management System**


**About the Project**
This project is a Java EE/Jakarta EE-based web application designed to help users track and manage their personal finances. 
Built as a practical implementation of software architecture principles for the SYSB24 course (IT Architecture and Software Systems), 
the application provides a centralized and intuitive interface for monitoring financial history, categorized incomes, and expenses.


**Key Features**


**Financial Dashboard:** A centralized homepage offering an immediate overview of total incomes, expenses, and transaction history.


**Transaction Management (CRUD):** Users can seamlessly create, read, update, and delete transactions. 
Each entry tracks the transaction type, category, and date.


**Demo Account Integration:** Features a streamlined, single-user demo environment ("BudgetBea") 
designed to efficiently demonstrate the core transaction logic without the overhead of complex authentication layers.


**Dynamic UI Interactions:** Utilizes AJAX and RESTful communication to allow client-side updates and form validation without 
requiring full page reloads.


**System Architecture**
The application is built on a strict, layered MVC (Model-View-Controller) architecture to ensure high cohesion, low coupling, 
and scalable "separation of concerns" between the presentation layer, business logic, and database management.
Backend (Java EE / Jakarta EE)


**Container-Managed Persistence:** Utilizes Enterprise JavaBeans (EJB) and JPA Entities to abstract database communications, 
handle relational mapping, and manage object lifecycles.


**Enterprise Access Objects (EAO):** Dedicated EAO classes encapsulate all database logic and EntityManager queries, 
ensuring that database operations are isolated from the business logic.


**Facade Pattern:** A central Facade interface acts as the sole access point between the frontend and the underlying backend subsystems, 
hiding database complexity from the client.
Frontend & Web Architecture


**Server-Side Rendering:** JavaServer Pages (JSP) generate dynamic HTML layouts, 
while a central Servlet acts as the primary Controller directing HTTP requests and coordinating data flow.


**Client-Side Logic:** JavaScript handles AJAX-based REST communications and form validations to reduce server load and improve 
UI responsiveness. CSS is strictly separated from HTML/JSP to ensure maintainable design modifications.


**Quality Assurance & Testing**
The backend architecture is designed for high testability. By utilizing separated EAO classes and a Facade pattern, 
individual system components and methods can be isolated and verified through automated unit tests without requiring the entire 
application or database to run concurrently.


**Development Team**

Nicole Daxberg

Molly Hantosi

Felix Jönsson

Malte Lindblad

Måns Wiberg

Instructor: Mats Svensson
