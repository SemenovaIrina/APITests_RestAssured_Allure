package edu.practicum;

import edu.practicum.client.CourierClient;
import edu.practicum.general.PrepareForTests;
import edu.practicum.models.Courier;
import edu.practicum.models.CourierAfterLogin;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static edu.practicum.general.Checks.checksCorrectCreateAndLoginCourier;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest extends PrepareForTests {
    private CourierAfterLogin courierAfterLogin;

    @ParameterizedTest
    @MethodSource("edu.practicum.general.TestDataForCourier#getCorrectCourierData")
    @Description("Checking the creation of a courier with correct data")
    public void createNewCourierWithCorrectData(Courier courier) {
        courierAfterLogin = checksCorrectCreateAndLoginCourier(courier).as(CourierAfterLogin.class);
    }

    @ParameterizedTest
    @MethodSource("edu.practicum.general.TestDataForCourier#getEqualCourierData")
    @Description("Checking if it is impossible to create two identical couriers")
    public void createTwoEqualCourierNotPossible(Courier courier1, Courier courier2) {
        courierAfterLogin = checksCorrectCreateAndLoginCourier(courier1).as(CourierAfterLogin.class);
        //выполняем запрос на создание эквивалентного курьера
        Response response = CourierClient.create(courier2);
        //проверяем статус код ответа
        Assertions.assertEquals(409, response.statusCode(), "Получаемый статус код при повторном создании курьера не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @ParameterizedTest
    @MethodSource("edu.practicum.general.TestDataForCourier#getCourierDataWithoutRequiredFild")
    @Description("Checking if it is impossible to create a courier without a required field")
    public void createNewCourierWithoutRequiredFildNotPossible(Courier courier) {
        //выполняем запрос на создание курьера
        Response response = CourierClient.create(courier);
        //сохраним ответ, для того, чтобы не было проблем с методом tearDown()
        courierAfterLogin = response.as(CourierAfterLogin.class);
        //проверяем статус код ответа
        Assertions.assertEquals(400, response.statusCode(), "Получаемый статус код при создании курьера не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @AfterEach
    public void tearDown() {
        CourierClient.delete(courierAfterLogin);
    }
}