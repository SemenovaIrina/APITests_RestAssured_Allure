package edu.practicum;

import edu.practicum.client.OrderClient;
import edu.practicum.data.Colors;
import edu.practicum.general.PrepareForTests;
import edu.practicum.models.Order;
import edu.practicum.models.OrderAfterCreate;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static edu.practicum.data.UtilsForDataPrepare.*;
import static edu.practicum.general.Checks.checkCreateOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class OrderCreateTest extends PrepareForTests {

    private OrderAfterCreate orderAfterCreate;

    public static Stream<Arguments> getOrderData() {
        Random rnd = new Random();
        return Stream.of(
                arguments(new Order.Builder()
                        .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .lastName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .address(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .metroStation(Integer.toString(rnd.nextInt(1000) + 1))
                        .phone(phoneRandomGenerate(11))
                        .rentTime(rnd.nextInt(365) + 1)
                        .deliveryDate(dateRandomGenerate())
                        .comment(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .build()),
                arguments(new Order.Builder()
                        .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .lastName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .address(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .metroStation(Integer.toString(rnd.nextInt(1000) + 1))
                        .phone(phoneRandomGenerate(11))
                        .rentTime(rnd.nextInt(365) + 1)
                        .deliveryDate(dateRandomGenerate())
                        .comment(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .color(List.of(new String[]{Colors.BLACK.getColor()}))
                        .build()),
                arguments(new Order.Builder()
                        .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .lastName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .address(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .metroStation(Integer.toString(rnd.nextInt(1000) + 1))
                        .phone(phoneRandomGenerate(11))
                        .rentTime(rnd.nextInt(365) + 1)
                        .deliveryDate(dateRandomGenerate())
                        .comment(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .color(List.of(new String[]{Colors.GREY.getColor()}))
                        .build()),
                arguments(new Order.Builder()
                        .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .lastName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .address(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .metroStation(Integer.toString(rnd.nextInt(1000) + 1))
                        .phone(phoneRandomGenerate(11))
                        .rentTime(rnd.nextInt(365) + 1)
                        .deliveryDate(dateRandomGenerate())
                        .comment(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .color(List.of(new String[]{Colors.BLACK.getColor(), Colors.GREY.getColor()}))
                        .build())

        );
    }

    @ParameterizedTest
    @MethodSource("getOrderData")
    @Description("Checking the possibility of creating a new order")
    public void createNewOrder(Order order) {
        orderAfterCreate = checkCreateOrder(order);
        //для того чтобы окончательно убедиться, что заказ создан получим информацию о нем по трек-номеру и сравним с информацией, использованной при создании заказа
        Response responseGet = OrderClient.getInfo(orderAfterCreate);
        assertEquals(200, responseGet.statusCode(), "Получаемый статус код при получении информации о заказе не соответствует ожидаемому");
        //проверим соотвествие полей созданного заказа и полученной по трек-номеру информации
        Assertions.assertAll(
                () -> responseGet.then().assertThat().body("order.firstName", equalTo(order.getFirstName())),
                () -> responseGet.then().assertThat().body("order.lastName", equalTo(order.getLastName())),
                () -> responseGet.then().assertThat().body("order.address", equalTo(order.getAddress())),
                () -> responseGet.then().assertThat().body("order.metroStation", equalTo(order.getMetroStation())),
                () -> responseGet.then().assertThat().body("order.phone", equalTo(order.getPhone())),
                () -> responseGet.then().assertThat().body("order.rentTime", equalTo(order.getRentTime())),
                () -> responseGet.then().assertThat().body("order.deliveryDate", equalTo(order.getDeliveryDate())),
                () -> responseGet.then().assertThat().body("order.color", equalTo(order.getColor()))
        );
        //установлено, что ручка получения заказа по трек-номеру работает нестабильно: иногда выдает заказ, не соответствующий указанному номеру
    }

    @AfterEach
    public void tearDown() {
        //так как нет возможности удалить созданный для теста заказ, то хотя бы отменим его
        OrderClient.cancel(orderAfterCreate);
    }
}
