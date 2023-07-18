
package data;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiHelper {
    private static DataHelper.CardInfo cardInfo;
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void payDebitCard(DataHelper.CardInfo cardInfo) {
        cardInfo = DataHelper.getApprovedCard();
        given()
                .spec(requestSpec)
                .body(cardInfo)
                .when()
                .post("/payment")
                .then()
                .statusCode(200);
    }

    public static void payCreditCard(DataHelper.CardInfo cardInfo) {
        cardInfo = DataHelper.getApprovedCard();
        given()
                .spec(requestSpec)
                .body(cardInfo)
                .when()
                .post("/credit")
                .then()
                .statusCode(200);

    }
}

