package test.api;

import data.SQLHelper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import data.ApiHelper;
import data.DataHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiTest {
    DataHelper.CardInfo declinedCardInfo = DataHelper.getDeclinedCard();

    @BeforeAll
    static void setUp() {
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured());
    }

    @DisplayName("Запрос на покупку по карте со статусом APPROVED")
    @Test
    void shouldApprovePayment() {
        var cardInfo = DataHelper.getApprovedCard();
        ApiHelper.payDebitCard(cardInfo);
        var paymentCardData = SQLHelper.getPaymentInfo();
        assertEquals(200, paymentCardData.getStatus());
    }

    @DisplayName("Запрос на кредит по карте со статусом APPROVED")
    @Test
    void shouldApproveCredit() {
        var cardInfo = DataHelper.getApprovedCard();
        ApiHelper.payCreditCard(cardInfo);
        var paymentCardData = SQLHelper.getCreditRequestInfo();
        assertEquals("APPROVED", paymentCardData.getStatus());

    }

    @DisplayName("Запрос на покупку по карте со статусом DECLINED")
    @Test
    void shouldDeclinePayment() {
        var cardInfo = DataHelper.getDeclinedCard();
        ApiHelper.payDebitCard(cardInfo);
        var paymentCardData = SQLHelper.getPaymentInfo();
        assertEquals("DECLINED", paymentCardData.getStatus());
    }

    @DisplayName("Запрос на кредит по карте со статусом DECLINED")
    @Test
    void shouldDeclineCredit() {
        var cardInfo = DataHelper.getDeclinedCard();
        ApiHelper.payCreditCard(cardInfo);
        var paymentCardData = SQLHelper.getCreditRequestInfo();
        assertEquals("DECLINED", paymentCardData.getStatus());
    }

}
