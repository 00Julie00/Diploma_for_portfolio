package test.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import data.DataHelper;
import data.SQLHelper;
import page.MainPage;
import page.PaymentPage;

import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static data.DataHelper.*;
import static data.SQLHelper.*;

public class PayCreditCard {
    MainPage page = open("http://localhost:8080/", MainPage.class);
    PaymentPage paymentPage = new PaymentPage();
    MainPage mainPage = new MainPage();

    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
    }

    @AfterEach
    void tearDown() {
        closeWindow();
    }

    @AfterEach
    void cleanDB() {
        SQLHelper.databaseCleanUp();
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @SneakyThrows
    @DisplayName("Покупка кредитной картой")
    void shouldApproveCreditCard() {
      mainPage.buyCreditCard();
        var info = getApprovedCard();
        paymentPage.sendingData(info);
        paymentPage.bankApproved();
        var expected = DataHelper.getStatusFirstCard();
        var creditRequest = getCreditRequestInfo();
        var orderInfo = getOrderInfo();
        assertEquals(expected, getCreditRequestInfo().getStatus());
        assertEquals(orderInfo.getPayment_id(), creditRequest.getBank_id());
    }

    @Test
    @SneakyThrows
    @DisplayName("Покупка кредитной невалидной картой")
    void shouldPayCreditDeclinedCard() {
        mainPage.buyCreditCard();
        var info = DataHelper.getDeclinedCard();
        paymentPage.sendingData(info);
        paymentPage.bankDeclined();
        var expected = getStatusSecondCard();
        var paymentInfo = getPaymentInfo().getStatus();
        assertEquals(expected, paymentInfo);
    }

    @Test
    @DisplayName("Покупка кредитной картой: пустое поле")
    void shouldEmptyFormWithCredit() {
        mainPage.buyCreditCard();
        paymentPage.pressButtonForContinue();
        paymentPage.emptyForm();

    }

    @Test
    @DisplayName("Покупка кредитной картой без заполнения поля карты, остальные поля - валидные данные")
    public void shouldEmptyFieldCardWithCredit() {
        mainPage.buyCreditCard();
        var info = getEmptyCardNumber();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля карты одной цифрой, остальные поля - валидные данные")
    public void shouldOneNumberInFieldCardNumberWithCredit() {
        mainPage.buyCreditCard();
        var info = getOneNumberCardNumber();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля карты 15 цифрами, остальные поля - валидные данные")
    public void shouldFifteenNumberInFieldCardNumberWithCredit() {
        mainPage.buyCreditCard();
        var info = getFifteenNumberCardNumber();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    @Test
    @DisplayName("Покупка кредитной картой неизвестной картой при заполнения поля карты, остальные поля - валидные данные")
    public void shouldFakerCardInFieldCardNumberWithCredit() {
        mainPage.buyCreditCard();
        var info = getFakerNumberCardNumber();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFakerCardNumber();
    }

    @Test
    @DisplayName("Покупка кредитной картой без заполнения поля месяц, остальные поля - валидные данные")
    public void shouldEmptyFieldMonthWithCredit() {
        mainPage.buyCreditCard();
        var info = getEmptyMonth();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: поле месяц одной цифрой, остальные поля - валидные данные")
    public void shouldOneNumberInFieldMonthWithCredit() {
        mainPage.buyCreditCard();
        var info = getOneNumberMonth();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: в поле месяц предыдущий от текущего, остальные поля -валидные данные")
    public void shouldFieldWithPreviousMonthWithCredit() {
        mainPage.buyCreditCard();
        var info = getPreviousMonthInField();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: в поле месяц нулевой (не существующий) месяц" +
            " остальные поля -валидные данные")
    public void shouldFieldWithZeroMonthWithCredit() {
        mainPage.buyCreditCard();
        var info = getZeroMonthInField();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    @Test
    @DisplayName("Покупка кредитной картой:  в поле месяц в верном формате тринадцатый (не существующий) месяц" +
            " остальные поля -валидные данные")
    public void shouldFieldWithThirteenMonthWithCredit() {
        mainPage.buyCreditCard();
        var info = getThirteenMonthInField();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    @Test
    @DisplayName("Покупка кредитной картой без заполнения поля год, остальные поля -валидные данные")
    public void shouldEmptyFieldYearWithCredit() {
        mainPage.buyCreditCard();
        var info = getEmptyYear();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля год, предыдущим годом от текущего" +
            " остальные поля -валидные данные")
    public void shouldPreviousYearFieldYearWithCredit() {
        mainPage.buyCreditCard();
        var info = getPreviousYearInField();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля год, на шесть лет больше чем текущий" +
            " остальные поля -валидные данные")
    public void shouldPlusSixYearFieldYearWithCredit() {
        mainPage.buyCreditCard();
        var info = getPlusSixYearInField();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: поле владелец пустое, остальные поля -валидные данные")
    public void shouldEmptyFieldNameWithCredit() {
        mainPage.buyCreditCard();
        var info = getApprovedCard();
        paymentPage.sendingEmptyNameValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }


    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля владелец спец. символами" +
            " остальные поля -валидные данные")
    public void shouldSpecialSymbolInFieldNameWithCredit() {
        mainPage.buyCreditCard();
        var info = getSpecialSymbolInFieldName();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля владелец цифрами" +
            " остальные поля -валидные данные")
    public void shouldNumberInFieldNameWithCredit() {
        mainPage.buyCreditCard();
        var info = getNumberInFieldName();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поле владелец кириллицей" +
            " остальные поля -валидные данные")
    public void shouldRussianNameInFieldNameWithCredit() {
        mainPage.buyCreditCard();
        var info = getRusName();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: заполнение поле владелец только фамилией" +
            " остальные поля -валидные данные")
    public void shouldOnlySurnameInFieldNameWithCredit() {
        mainPage.buyCreditCard();
        var info = getOnlySurnameInFieldName();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }


    @Test
    @DisplayName("Покупка кредитной картой: поле CVV пустое" +
            " остальные поля -валидные данные")
    public void shouldEmptyCVVInFieldCVVWithCredit() {
        mainPage.buyCreditCard();
        var info = getEmptyCVVInFieldCVV();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: поле CVV одним числом" +
            " остальные поля -валидные данные")
    public void shouldOneNumberInFieldCVVWithCredit() {
        mainPage.buyCreditCard();
        var info = getOneNumberInFieldCVV();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    @Test
    @DisplayName("Покупка кредитной картой: поле CVV двумя числами" +
            " остальные поля -валидные данные")
    public void shouldTwoNumberInFieldCVVWithCredit() {
        mainPage.buyCreditCard();
        var info = getOTwoNumberInFieldCVV();
        paymentPage.sendingData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

}





