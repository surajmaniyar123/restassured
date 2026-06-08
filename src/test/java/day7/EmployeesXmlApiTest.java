package day7;
import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class EmployeesXmlApiTest {

    @BeforeClass
    public void setup() {
        // Base URI for all API requests
        RestAssured.baseURI = "https://www.example.com/api"; // Replace with actual API
    }

    // ------------------------------
    // 1. GET - Retrieve all employees
    // ------------------------------
    @Test
    public void getEmployeesTest() {
        Response response = given()
                                .header("Accept", "application/xml")
                            .when()
                                .get("/employees")
                            .then()
                                .statusCode(200)
                                .extract().response();

        String xml = response.asString();
        System.out.println("GET Response:\n" + xml);

        XmlPath xmlPath = new XmlPath(xml);

        // Validate all fields for first employee
        String id1 = xmlPath.getString("employees.employee[0].id");
        String name1 = xmlPath.getString("employees.employee[0].name");
        String role1 = xmlPath.getString("employees.employee[0].role");
        String department1 = xmlPath.getString("employees.employee[0].department");
        List<String> projects1 = xmlPath.getList("employees.employee[0].projects.project");

        System.out.println("Employee 1 Details:");
        System.out.println("ID: " + id1);
        System.out.println("Name: " + name1);
        System.out.println("Role: " + role1);
        System.out.println("Department: " + department1);
        System.out.println("Projects: " + projects1);

        // Assertions for employee 1
        response.then()
                .body("employees.employee[0].id", equalTo("1"))
                .body("employees.employee[0].name", equalTo("John Doe"))
                .body("employees.employee[0].role", equalTo("Developer"))
                .body("employees.employee[0].department", equalTo("IT"))
                .body("employees.employee[0].projects.project[0]", equalTo("Project A"))
                .body("employees.employee[0].projects.project[1]", equalTo("Project B"));

        // Validate second employee
        String id2 = xmlPath.getString("employees.employee[1].id");
        String name2 = xmlPath.getString("employees.employee[1].name");
        String role2 = xmlPath.getString("employees.employee[1].role");
        String department2 = xmlPath.getString("employees.employee[1].department");
        List<String> projects2 = xmlPath.getList("employees.employee[1].projects.project");

        // Assertions for employee 2
        response.then()
                .body("employees.employee[1].id", equalTo("2"))
                .body("employees.employee[1].name", equalTo("Jane Doe"))
                .body("employees.employee[1].role", equalTo("Manager"))
                .body("employees.employee[1].department", equalTo("Sales"))
                .body("employees.employee[1].projects.project[0]", equalTo("Project C"));
    }

    // ------------------------------
    // 2. POST - Create a new employee
    // ------------------------------
    @Test
    public void createEmployeeTest() {
        String xmlBody = "<employee>" +
                         "<id>3</id>" +
                         "<name>Bob Smith</name>" +
                         "<role>Tester</role>" +
                         "<department>QA</department>" +
                         "<projects>" +
                             "<project>Project X</project>" +
                             "<project>Project Y</project>" +
                         "</projects>" +
                         "</employee>";

        Response response = given()
                                .header("Content-Type", "application/xml")
                                .body(xmlBody)
                            .when()
                                .post("/employees")
                            .then()
                                .statusCode(201) // Created
                                .extract().response();

        XmlPath xmlPath = new XmlPath(response.asString());

        // Validate all fields of the created employee
        response.then()
                .body("employee.id", equalTo("3"))
                .body("employee.name", equalTo("Bob Smith"))
                .body("employee.role", equalTo("Tester"))
                .body("employee.department", equalTo("QA"))
                .body("employee.projects.project[0]", equalTo("Project X"))
                .body("employee.projects.project[1]", equalTo("Project Y"));
    }

    // ------------------------------
    // 3. PUT - Update an existing employee
    // ------------------------------
    @Test
    public void updateEmployeeTest() {
        String xmlBody = "<employee>" +
                         "<name>Jane Smith</name>" +
                         "<role>Senior Manager</role>" +
                         "<department>Sales</department>" +
                         "<projects>" +
                             "<project>Project C</project>" +
                             "<project>Project D</project>" +
                         "</projects>" +
                         "</employee>";

        given()
            .header("Content-Type", "application/xml")
            .body(xmlBody)
        .when()
            .put("/employees/2") // Update employee with ID 2
        .then()
            .statusCode(200)
            .body("employee.name", equalTo("Jane Smith"))
            .body("employee.role", equalTo("Senior Manager"))
            .body("employee.department", equalTo("Sales"))
            .body("employee.projects.project[0]", equalTo("Project C"))
            .body("employee.projects.project[1]", equalTo("Project D"));
    }

    // ------------------------------
    // 4. DELETE - Remove an employee
    // ------------------------------
    @Test
    public void deleteEmployeeTest() {
        given()
        .when()
            .delete("/employees/3") // Delete employee with ID 3
        .then()
            .statusCode(204); // No Content
    }
}
