**GPA Calculator – JavaFX Application**

A clean, fast, and user-friendly GPA Calculator built using JavaFX.
This application allows students to enter course details, view them in a table, and automatically calculate their GPA.
A formatted Result Window is also available to show the GPA summary and full course list.

✨ **Features**

📘 Course Entry Form

Course Name

Course Code

Course Credit **(validated: only numeric/float allowed)**

Teacher 1 Name

Teacher 2 Name

Grade selection (dropdown)

🎯 **Credit Tracking**

Enter Target Credits (e.g., 15.0)

Live Current Credits counter updates as courses are added or removed

GPA calculation enabled only when credits match target

**🗂️ Courses Table (Interactive)**

Displays all entered courses with:

Course Name

Code

Credit

Teacher 1

Teacher 2

Grade

Shows “No content in table” when empty

**Supports delete using:**

Remove Selected button

Delete key (keyboard)

Supports Update Selected to modify a course entry

**🧮 Automatic GPA Calculation**

Converts Letter Grade → Grade Points

Computes cumulative GPA

Calculation button remains disabled until credit requirement is met

Displays summary in a separate Result Window

**📤 Export & Reset**

Export to Text… button generates a text file containing:

All course entries

GPA summary

Reset button clears:

All form input fields

Table data

Credits display

**🏠 Navigation**

Back to Home link returns to the main view

Clean scene switching powered by JavaFX FXML + Controllers

**🎨 Clean Modern JavaFX UI**

FXML-based view design

Custom CSS styling (gpa-style.css)

Fully responsive and neatly organized layout:

Left: Form input

Right: Table + GPA summary

Top: Credit tracker bar

Bottom: GPA calculation button

**🛠️ Technologies Used**

Java 17+

JavaFX 17+

Maven

Scene Builder (for FXML design)

🚀 **How to Run the Application**
▶️ Run from IDE (IntelliJ IDEA / Eclipse)

Clone the repository:

git clone https://github.com/aaddiibb/Raian_2207020_GPAcalculator


Open the project in your IDE.

Import as a Maven Project.

Run:

HelloApplication.java


The JavaFX application will launch.



📁** Project Structure**

src/
 └── main/
 
     ├── java/com.example.gpa2207020/
     │     ├── Course.java
     │     ├── GpaFormController.java
     │     ├── HelloApplication.java
     │     ├── HelloController.java
     │     └── ResultController.java
     │
     
     └── resources/com.example.gpa2207020/
           ├── gpa-form-view.fxml
           ├── gpa-style.css
           ├── hello-view.fxml
           └── result-view.fxml
