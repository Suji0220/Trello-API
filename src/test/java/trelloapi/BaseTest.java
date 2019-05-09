package trelloapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class BaseTest {
    String keyID = "0697ace29da135af1009cc535346c753";
    String tokenID = "7e4ee4a40eb3a5b893248b00aa289b4943578d7a687ac7569f3ea1a8e849db6f";
    String boardId;
    String listID;
    String cardId;


    @BeforeSuite
    public void setUp()
    {
        RestAssured.baseURI = "https://api.trello.com/1/";
        String boardId = createBoard();
    }

    public String createBoard() {
        RequestSpecification requestSpecification = given()
                .queryParam("key", keyID)
                .queryParam("token", tokenID)
                .queryParam("name", "RetroUpdated123").log().all()
                .contentType(ContentType.JSON);

        Response response = requestSpecification.when().
                post("boards/");
        System.out.println(response.body());

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("createBoard.json"))
                .body("name", equalTo("RetroUpdated123"))

                .extract()
                .response()
                .jsonPath()
                .getMap("$");
        Map<Object, Object> map = response.jsonPath().getMap("$");
        System.out.println("Board ID: " + map.get("id"));
        System.out.println("Board Name: " + map.get("name"));
        boardId = map.get("id").toString();
        System.out.println(boardId);
        Assert.assertEquals(map.get("name").toString(),"RetroUpdated123");
        return boardId;
    }
    @AfterSuite
    public void tearDown()
    {
        deleteBoard();
        System.out.println("Deleting board successfully");

    }

       /*@AfterClass
    public void tearDown(){

        Response response =  given()
                .pathParam("boardID",boardId)
                .queryParam("key", keyID)
                .queryParam("token", tokenID).
                        when().
                        delete("1/boards/{boardId}");


        response.then();
               // .statusCode(200);

    }
*/


       public void deleteBoard()
       {
           RequestSpecification requestSpecification = given()
                   .queryParam("key", keyID)
                   .queryParam("token", tokenID)
                   .pathParam("boardID",boardId)
                   .contentType(ContentType.JSON);

           Response response = requestSpecification.when().
                   delete("boards/{boardID}");

           response.then()
                   .statusCode(200).log();
       }
}
