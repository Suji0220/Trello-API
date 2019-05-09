package trelloapi;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.*;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class TrelloAPITest extends BaseTest{

    String listName;

    
   
        @Test(priority =  1)
        public void testGetListInformation(){

            Response response =  given()
                    .pathParam("boardID",boardId)
                    .queryParam("key", keyID)
                    .queryParam("token", tokenID).
                            when().
                            get("boards/{boardID}/lists/");

            response.then()
                             .statusCode(200)
                             .contentType(ContentType.JSON)
                             .extract();

            System.out.println(response.statusCode());

                List<Map<String, ?>> listInfo = response.jsonPath().getList("$");
                for(int i = 0; i < listInfo.size(); i++) {
                    System.out.println(listInfo.get(i).get("id").toString());
                    System.out.println(listInfo.get(i).get("name").toString());
                }
                listID = listInfo.get(0).get("id").toString();
                listName = listInfo.get(0).get("name").toString();
            System.out.println("List ID: " + listID);
            System.out.println("List Name: " + listName);

    }

    @Test(priority = 2)
    public void testCreateCard(){
        RequestSpecification requestSpecification = given()
                .queryParam("key", keyID)
                .queryParam("token", tokenID)
                .queryParam("name", "CardName")
                .queryParam("idList", listID)
                .contentType(ContentType.JSON)
                .log().all();

        Response response = requestSpecification.when().
                post("cards");
        System.out.println("Response Body: " + response.body());
        System.out.println("Response Status Code: " + response.statusCode());


        response.then()
                .statusCode(200);
            /*    .contentType(ContentType.JSON)
                .extract()
                 .response()
                .jsonPath()
                .getMap("$");*/
        Map<Object, Object> map = response.jsonPath().getMap("$");

        cardId = map.get("id").toString();
        System.out.println(cardId);

    }

   @Test(priority =  3)
    public void testUpdateCard(){
       System.out.println("Checking whether cardid is displaying under testUpdateCard: " + cardId);
        RequestSpecification requestSpecification = given()
                .queryParam("key", keyID)
                .queryParam("token", tokenID)
                .queryParam("name", "UpdatedCardName")
                .pathParam("id", cardId)
                .contentType(ContentType.JSON)
                .log().all();

        Response response = requestSpecification.when().
                put("cards/{id}");
       System.out.println("Response body after updating the card --> "+ response.body());
       System.out.println("Response code after updating the card --> "+ response.statusCode());
        response.then()
                .statusCode(200);

        Map<Object, Object> map = response.jsonPath().getMap("$");

        cardId = map.get("id").toString();
        System.out.println("Card ID: " + cardId);

    }
   @Test(priority = 4)
    public void testDeleteCard() {
       System.out.println("Checking for Card ID in Delete Card method: " + cardId);
       RequestSpecification requestSpecification = given()
               .queryParam("key", keyID)
               .queryParam("token", tokenID)
               .pathParam("id", cardId)
               .contentType(ContentType.JSON)
               .log().all();

       Response response = requestSpecification.when().
               delete("cards/{id}");

       response.then()
               .statusCode(200).log();

       Assert.assertEquals(response.getStatusCode(),200);

       System.out.println("Response body after deleting the card--> "+ response.body());

       System.out.println("Response code from delete card -->" + response.statusCode());

   }


    }





