package edu.practicum;

import edu.practicum.client.CourierClient;
import edu.practicum.general.PrepareForTests;
import edu.practicum.models.Courier;
import edu.practicum.models.CourierAfterLogin;
import edu.practicum.models.CourierForLogin;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static edu.practicum.data.UtilsForDataPrepare.stringRandomGenerate;
import static edu.practicum.data.UtilsForDataPrepare.substringRandom;
import static edu.practicum.general.Checks.checkLoginCourierWithNotCorrectLoginOrPassword;
import static edu.practicum.general.Checks.checksCorrectCreateAndLoginCourier;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CourierLoginTest extends PrepareForTests {
    private static Courier courier = new Courier.Builder()
            .login(stringRandomGenerate(new Random().nextInt(254) + 1))
            .password(stringRandomGenerate(new Random().nextInt(254) + 1))
            .firstName(stringRandomGenerate(new Random().nextInt(254) + 1))
            .build();
    private CourierAfterLogin courierAfterLogin;

    @BeforeEach
    public void setUp() {
        super.setUp();
        //чтобы проверять логин курьера нужно чтобы он был успешно создан и мог залогиниться
        courierAfterLogin = checksCorrectCreateAndLoginCourier(courier).as(CourierAfterLogin.class);
    }

    public static Stream<Arguments> getCourierDataForLoginWithoutRequiredField() {
        return Stream.of(
                arguments(new CourierForLogin.Builder()
                        .password(courier.getPassword())
                        .build()),
                arguments(new CourierForLogin.Builder()
                        .login(courier.getLogin())
                        .build()),
                arguments(new CourierForLogin.Builder()
                        .build())
        );
    }

    public static Stream<Arguments> getCourierDataForLoginWithError() {
        //по условию ошибка в логине ИЛИ пароле (случай, когда ошибка и в логине, и в пароле посути покрывается тестом на логин несуществующего курьера)
        //имитируем ошибку путем выделения случайной подстроки в существующем логине или пароле
        //случай, когда длина логина или пароля не менялась, а была допущена ошибка в каком-либо символе
        // покрывается тестами для попытки залогиниться с несуществующими данными или данными, которые уже существуют
        Random rnd = new Random();
        return Stream.of(
                arguments(new CourierForLogin.Builder()
                        .password(substringRandom(courier.getPassword(), rnd.nextInt(courier.getPassword().length() + 1)))
                        .login(courier.getLogin())
                        .build()),
                arguments(new CourierForLogin.Builder()
                        .password(courier.getPassword())
                        .login(substringRandom(courier.getLogin(), rnd.nextInt(courier.getLogin().length() + 1)))
                        .build())
        );
    }

    @ParameterizedTest
    @MethodSource("getCourierDataForLoginWithoutRequiredField")
    @Description("Checking the impossibility of the courier's login without specifying the required field")
    public void loginCourierWithoutRequiredFieldNotPossible(CourierForLogin courier) {
        //пытаемся залогинить курьера без указания обязательного поля
        Response response = CourierClient.login(courier);
        //проверяем статус код ответа
        Assertions.assertEquals(400, response.statusCode(), "Получаемый статус код при логине курьера не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));

        //установлено, что если при попытке логина не указать логин, то сообщение об ошибке не выдается, срабатывает time out
        //если не указать пароль поведение системы соответствует ожидаемому
    }

    @ParameterizedTest
    @MethodSource("getCourierDataForLoginWithError")
    @Description("Checking the impossibility of the courier's login with erroneous data")
    public void loginCourierWithErrorInDataNotPossible(CourierForLogin courier) {
        //пытаемся залогинить курьера с ошибочными данными
        checkLoginCourierWithNotCorrectLoginOrPassword(courier);
    }

    @Test
    @Description("Checking the impossibility of the login of a non-existent courier")
    public void loginNotExistingCourierNotPossible() {
        Random rnd = new Random();
        CourierForLogin courier = new CourierForLogin.Builder()
                .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                .login(stringRandomGenerate(rnd.nextInt(254) + 1))
                .build();
        checkLoginCourierWithNotCorrectLoginOrPassword(courier);
    }

    @AfterEach
    public void tearDown() {
        CourierClient.delete(courierAfterLogin);
    }
}
