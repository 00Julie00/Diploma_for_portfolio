package test.ui;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import data.DataHelper;
import data.SQLHelper;
import page.PaymentPage;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static data.DataHelper.*;
import static data.SQLHelper.*;

public class PayDebitCard {
    PaymentPage paymentPage = new PaymentPage();

    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {

        open("http://localhost:8080");
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
    @DisplayName("Покупка дебетовой картой")
    void shouldApproveDebitCard() {
        paymentPage.payDebitCard();
        var info = getApprovedCard();
        paymentPage.sendingValidData(info);
        paymentPage.bankApproved();
        var expected = DataHelper.getStatusFirstCard();
        var creditRequest = getCreditRequestInfo();
        var orderInfo = getOrderInfo();
        assertEquals(expected, getCreditRequestInfo().getStatus());
        assertEquals(orderInfo.getPayment_id(), creditRequest.getBank_id());
    }

    @Test
    @SneakyThrows
    @DisplayName("Покупка дебетовой невалидной картой")
    void shouldPayDebitDeclinedCard() {
        paymentPage.payDebitCard();
        var info = DataHelper.getDeclinedCard();
        paymentPage.sendingNotValidData(info);
        paymentPage.bankDeclined();
        var expected = getStatusSecondCard();
        var paymentInfo = getPaymentInfo().getStatus();
        assertEquals(expected, paymentInfo);
    }

    @Test
    @DisplayName("Покупка дебетовой картой: пустое поле")
    void shouldEmptyFormWithDebit() {
        paymentPage.payDebitCard();
        paymentPage.pressButtonForContinue();
        paymentPage.emptyForm();

    }

    @Test
    @DisplayName("Покупка дебетовой картой без заполнения поля карты, остальные поля - валидные данные")
    public void shouldEmptyFieldCardWithDebit() {
        paymentPage.payDebitCard();
        var info = getEmptyCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля карты одной цифрой, остальные поля - валидные данные")
    public void shouldOneNumberInFieldCardNumberWithDebit() {
        paymentPage.payDebitCard();
        var info = getOneNumberCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля карты 15 цифрами, остальные поля - валидные данные")
    public void shouldFifteenNumberInFieldCardNumberWithDebit() {
        paymentPage.payDebitCard();
        var info = getFifteenNumberCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой неизвестной картой при заполнения поля карты, остальные поля - валидные данные")
    public void shouldFakerCardInFieldCardNumberWithDebit() {
        paymentPage.payDebitCard();
        var info = getFakerNumberCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFakerCardNumber();
    }

    @Test
    @DisplayName("Покупка дебетовой картой без заполнения поля месяц, остальные поля - валидные данные")
    public void shouldEmptyFieldMonthWithDebit() {
        paymentPage.buyCreditCard();
        var info = getEmptyMonth();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: поле месяц одной цифрой, остальные поля - валидные данные")
    public void shouldOneNumberInFieldMonthWithDebit() {
        paymentPage.payDebitCard();
        var info = getOneNumberMonth();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: в поле месяц предыдущий от текущего, остальные поля -валидные данные")
    public void shouldFieldWithPreviousMonthWithDebit() {
        paymentPage.payDebitCard();
        var info = getPreviousMonthInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: в поле месяц нулевой (не существующий) месяц" +
            " остальные поля -валидные данные")
    public void shouldFieldWithZeroMonthWithDebit() {
        paymentPage.payDebitCard();
        var info = getZeroMonthInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой:  в поле месяц в верном формате тринадцатый (не существующий) месяц" +
            " остальные поля -валидные данные")
    public void shouldFieldWithThirteenMonthWithDebit() {
        paymentPage.payDebitCard();
        var info = getThirteenMonthInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой без заполнения поля год, остальные поля -валидные данные")
    public void shouldEmptyFieldYearWithDebit() {
        paymentPage.payDebitCard();
        var info = getEmptyYear();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля год, предыдущим годом от текущего" +
            " остальные поля -валидные данные")
    public void shouldPreviousYearFieldYearWithDebit() {
        paymentPage.payDebitCard();
        var info = getPreviousYearInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля год, на шесть лет больше чем текущий" +
            " остальные поля -валидные данные")
    public void shouldPlusSixYearFieldYearWithDebit() {
        paymentPage.payDebitCard();
        var info = getPlusSixYearInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: поле владелец пустое, остальные поля -валидные данные")
    public void shouldEmptyFieldNameWithDebit() {
        paymentPage.payDebitCard();
        var info = getApprovedCard();
        paymentPage.sendingEmptyNameValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля владелец спец. символами" +
            " остальные поля -валидные данные")
    public void shouldSpecialSymbolInFieldNameWithDebit() {
        paymentPage.payDebitCard();
        var info = getSpecialSymbolInFieldName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля владелец цифрами" +
            " остальные поля -валидные данные")
    public void shouldNumberInFieldNameWithDebit() {
        paymentPage.payDebitCard();
        var info = getNumberInFieldName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поле владелец русскими буквами" +
            " остальные поля -валидные данные")
    public void shouldRussianNameInFieldNameWithDebit() {
        paymentPage.payDebitCard();
        var info = getRusName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поле владелец только фамилией" +
            " остальные поля -валидные данные")
    public void shouldOnlySurnameInFieldNameWithDebit() {
        paymentPage.payDebitCard();
        var info = getOnlySurnameInFieldName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }


    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV пустое" +
            " остальные поля -валидные данные")
    public void shouldEmptyCVVInFieldCVVWithDebit() {
        paymentPage.payDebitCard();
        var info = getEmptyCVVInFieldCVV();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV одним числом" +
            " остальные поля -валидные данные")
    public void shouldOneNumberInFieldCVVWithDebit() {
        paymentPage.payDebitCard();
        var info = getOneNumberInFieldCVV();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV двумя числами" +
            " остальные поля -валидные данные")
    public void shouldTwoNumberInFieldCVVWithDebit() {
        paymentPage.payDebitCard();
        var info = getOTwoNumberInFieldCVV();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

}

