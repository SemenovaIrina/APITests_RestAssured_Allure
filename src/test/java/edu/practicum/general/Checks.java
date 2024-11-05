package edu.practicum.general;

import edu.practicum.client.CourierClient;
import edu.practicum.client.OrderClient;
import edu.practicum.models.*;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Checks {
    @Step("Checking the possibility of creating a new courier and his login in the system")
    public static Response checksCorrectCreateAndLoginCourier(Courier courier) {
        //выполняем запрос на создание курьера
        Response response = CourierClient.create(courier);
        //проверяем статус код ответа
        Assertions.assertEquals(201, response.statusCode(), "Получаемый статус код при создании курьера не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body("ok", equalTo(true));
        //чтобы убедиться, что курьер действительно создался попытаемся залогинить его
        response = CourierClient.login(new CourierForLogin(courier.getLogin(), courier.getPassword()));
        //проверяем статус код, полученный при логине
        Assertions.assertEquals(200, response.statusCode(), "Получаемый статус код при логине нового курьера не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body("id", notNullValue());
        //возвращаем тело ответа после логина, чтобы можно было в последствии удалить созданного курьера
        return response;
    }

    @Step("Checking the impossibility of the courier's login with an error in the username or password")
    public static void checkLoginCourierWithNotCorrectLoginOrPassword(CourierForLogin courier) {
        //пытаемся залогинить курьера
        Response response = CourierClient.login(courier);
        //проверяем статус код ответа
        Assertions.assertEquals(404, response.statusCode(), "Получаемый статус код при логине курьера не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Checking the possibility of creating an order. Upon successful creation, the track number of the order is returned")
    public static OrderAfterCreate checkCreateOrder(Order order) {
        Response responseCreate = OrderClient.create(order);
        //проверяем статус код ответа
        assertEquals(201, responseCreate.statusCode(), "Получаемый статус код при создании заказа не соответствует ожидаемому");
        //проверяем тело ответа
        responseCreate.then().assertThat().body("track", notNullValue());
        //получим и вернем трек-номер созданного заказа
        return responseCreate.as(OrderAfterCreate.class);
    }

    @Step("Checking the courier's ability to accept an order")
    public static void checkAcceptOrder(OrderAfterCreate orderAfterCreate, CourierAfterLogin courierAfterLogin) {
        //получим id заказа
        Response responseGet = OrderClient.getInfo(orderAfterCreate);
        assertEquals(200, responseGet.statusCode(), "Получаемый статус код при получении информации о заказе не соответствует ожидаемому");
        InfoAboutOneOrder orderInfo = responseGet.as(InfoAboutOneOrder.class);
        int orderId = orderInfo.getOrder().getId();
        //курьер принимает заказ
        Response response = OrderClient.acceptOrder(courierAfterLogin.getId(), orderId);
        //проверяем статус код ответа
        assertEquals(200, response.statusCode(), "Получаемый статус код при назначении заказа курьеру не соответствует ожидаемому");

    }

    @Step("Checking the courier's ability to accept the list of orders")
    public static ArrayList<OrderAfterCreate> checkCreateAndAcceptOrders(ArrayList<Order> orders, CourierAfterLogin courierAfterLogin) {
        ArrayList<OrderAfterCreate> orderList = new ArrayList<OrderAfterCreate>();
        for (int i = 0; i < orders.size(); i++) {
            OrderAfterCreate orderAfterCreate = checkCreateOrder(orders.get(i));
            orderList.add(orderAfterCreate);
            //получим id заказа
            Response responseGet = OrderClient.getInfo(orderAfterCreate);
            assertEquals(200, responseGet.statusCode(), "Получаемый статус код при получении информации о заказе не соответствует ожидаемому");
            InfoAboutOneOrder orderInfo = responseGet.as(InfoAboutOneOrder.class);
            int orderId = orderInfo.getOrder().getId();
            //курьер принимает заказ
            Response response = OrderClient.acceptOrder(courierAfterLogin.getId(), orderId);
            //проверяем статус код ответа
            assertEquals(200, response.statusCode(), "Получаемый статус код при назначении заказа курьеру не соответствует ожидаемому");
        }
        return orderList;
    }
}
