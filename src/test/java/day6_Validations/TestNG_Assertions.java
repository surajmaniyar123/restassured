package day6_Validations;

import static io.restassured.RestAssured.*;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class TestNG_Assertions {

	  /*  
    ===========================================================
      FULL JSON RESPONSE (for reference & explanation mapping)
    ===========================================================

    {
  "status": "success",
  "data": {
    "userDetails": {
      "id": 12345,
      "name": "John Doe",
      "email": "john.doe@example.com",
      "phoneNumbers": [
        {
          "type": "home",
          "number": "123-456-7890"
        },
        {
          "type": "work",
          "number": "987-654-3210"
        }
      ],
      "address": {
        "street": "123 Main St",
        "city": "Springfield",
        "state": "IL",
        "postalCode": "62704",
        "geo": {
          "latitude": 39.7817,
          "longitude": -89.6501
        }
      },
      "preferences": {
        "notifications": true,
        "theme": "dark",
        "languages": ["English", "Spanish", "French"]
      }
    },
    "recentOrders": [
      {
        "orderId": 101,
        "orderDate": "2025-01-15",
        "items": [
          {
            "itemId": 1,
            "name": "Laptop",
            "quantity": 1,
            "price": 1200.99
          },
          {
            "itemId": 2,
            "name": "Mouse",
            "quantity": 1,
            "price": 25.50
          }
        ],
        "totalAmount": 1226.49
      },
      {
        "orderId": 102,
        "orderDate": "2025-01-20",
        "items": [
          {
            "itemId": 3,
            "name": "Smartphone",
            "quantity": 1,
            "price": 799.99
          }
        ],
        "totalAmount": 799.99
      }
    ]
  },
  "meta": {
    "requestId": "abc123xyz",
    "timestamp": "2025-01-26T14:30:00Z",
    "responseTimeMs": 250
  }
}

    ===========================================================
    */

   @Test
   void getUsers_JSONPathEnhancedValidation() {

       // Send GET request
       Response response =
               given()
                       .header("Content-Type", "application/json")
               .when()
                       .get("https://reqres.in/api/users?page=2");

       // ===========================================================
       // BASIC RESPONSE VALIDATION
       // ===========================================================

       Assert.assertEquals(response.getStatusCode(), 200);  
       // Validates API returned HTTP 200 ✓

       Assert.assertEquals(response.getHeader("Content-Type"),
               "application/json; charset=utf-8");
       // Validates response body is JSON ✓


       JsonPath json = response.jsonPath();  // Parse JSON response


       // ===========================================================
       // TOP-LEVEL FIELDS (These map directly to JSON root)
       // ===========================================================

       Assert.assertEquals(json.getInt("page"), 2);
       // Corresponds to: "page": 2

       Assert.assertEquals(json.getInt("per_page"), 6);
       // Corresponds to: "per_page": 6

       Assert.assertEquals(json.getInt("total"), 12);
       // Corresponds to: "total": 12

       Assert.assertEquals(json.getInt("total_pages"), 2);
       // Corresponds to: "total_pages": 2


       // ===========================================================
       // VALIDATE data[] ARRAY
       // ===========================================================

       List<Map<String, Object>> users = json.getList("data");
       Assert.assertEquals(users.size(), 6);
       // Based on: "per_page": 6 → data array MUST contain 6 users


       // Validate each user inside data[]
       for (Map<String, Object> user : users) {

           Assert.assertTrue(user.containsKey("id"));
           // From user object: "id": 7

           Assert.assertTrue(user.containsKey("email"));
           // From user object: "email": "michael.lawson@reqres.in"

           Assert.assertTrue(user.containsKey("first_name"));
           // From user object: "first_name": "Michael"

           Assert.assertTrue(user.containsKey("last_name"));
           // From user object: "last_name": "Lawson"

           Assert.assertTrue(user.containsKey("avatar"));
           // From user object: "avatar": "https://reqres.in/img/faces/7-image.jpg"
       }


       // ===========================================================
       // VALIDATE FIRST USER DETAILS
       // ===========================================================

       Assert.assertEquals(json.getInt("data[0].id"), 7);
       // Mapping: data[0].id = 7

       Assert.assertEquals(json.getString("data[0].email"),
               "michael.lawson@reqres.in");
       // Mapping: data[0].email = "michael.lawson@reqres.in"

       Assert.assertEquals(json.getString("data[0].first_name"), "Michael");
       // Mapping: data[0].first_name = "Michael"

       Assert.assertTrue(json.getString("data[0].avatar").contains("7-image"));
       // Mapping: data[0].avatar contains "7-image.jpg"


       // ===========================================================
       // VALIDATE ALL EMAILS USE SAME DOMAIN
       // ===========================================================

       List<String> emails = json.getList("data.email");
       for (String email : emails) {
           Assert.assertTrue(email.contains("@reqres.in"));
       }
       // All emails in JSON end with "@reqres.in"


       // ===========================================================
       // VALIDATE support OBJECT
       // ===========================================================

       Assert.assertTrue(json.getString("support.url").startsWith("https://"));
       // Mapping: "url": "https://contentcaddy.io..."

       Assert.assertTrue(json.getString("support.text").length() > 10);
       // Mapping: long support message text


       // ===========================================================
       // VALIDATE _meta OBJECT
       // ===========================================================

       Assert.assertEquals(
               json.getString("_meta.powered_by"),
               "🚀 ReqRes - Deploy backends in 30 seconds"
       );
       // Mapping: "_meta.powered_by": "🚀 ReqRes - Deploy..."

       Assert.assertTrue(json.getString("_meta.docs_url").contains("https://reqres.in"));
       // Mapping: "_meta.docs_url": "https://reqres.in"


       // ===========================================================
       // VALIDATE _meta.features[] ARRAY
       // ===========================================================

       List<String> features = json.getList("_meta.features");

       Assert.assertTrue(features.size() >= 4);
       // Based on JSON: 4 features expected

       Assert.assertTrue(features.contains("Custom API Endpoints"));
       // This item appears in _meta.features[]

       Assert.assertTrue(features.contains("Real-time Analytics"));
       // This item also appears in _meta.features[]


       // ===========================================================
       // VALIDATE upgrade_cta FIELD
       // ===========================================================

       Assert.assertTrue(json.getString("_meta.upgrade_cta")
               .contains("Upgrade to Pro"));
       // Mapping: "_meta.upgrade_cta": "Upgrade to Pro for unlimited requests..."


       // ===========================================================
       // VALIDATE ALL AVATAR URLs FORMAT
       // ===========================================================

       List<String> avatars = json.getList("data.avatar");

       for (String avatar : avatars) {
           Assert.assertTrue(avatar.startsWith("https://reqres.in/img/faces/"));
       }
       // Mapping: each user → avatar starts with this URL path


       // Print full response
       response.prettyPrint();
   }
}
