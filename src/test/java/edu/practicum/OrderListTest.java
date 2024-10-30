package edu.practicum;

import com.google.gson.Gson;
import edu.practicum.client.OrderClient;
import edu.practicum.data.Colors;
import edu.practicum.general.PrepareForTests;
import edu.practicum.models.*;
import io.qameta.allure.Description;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.core.json.JsonWriteFeature;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;

import static edu.practicum.data.UtilsForDataPrepare.*;
import static edu.practicum.general.Checks.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class OrderListTest extends PrepareForTests {
    @Test
    @Description("Checking the possibility of receiving a list of all orders. Checking the status code and response structure")
    public void getListWithAllOrders() {
        Response responseList = OrderClient.getListWithAllOrders();
        //проверяем статус код ответа
        assertEquals(200, responseList.statusCode(), "Получаемый статус код при получении списка заказов не соответствует ожидаемому");
        //проверяем тело ответа
        responseList.then().assertThat().body("orders", notNullValue());
        assertEquals(responseList.as(OrderList.class).getClass(), OrderList.class, "Структура ответа не соответствует ожидаемому");
    }

    @Test
    @Description("Checking the possibility of receiving a list of orders from the specified courier. Checking the status code and response structure")
    public void getListOrdersForCourier() {
        Random rnd = new Random();
        //создаем курьера и пытаемся залогинить его в системе
        Courier courier = new Courier.Builder()
                .login(stringRandomGenerate(rnd.nextInt(254) + 1))
                .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                .build();
        CourierAfterLogin courierAfterLogin = checksCorrectCreateAndLoginCourier(courier).as(CourierAfterLogin.class);
        //создаем заказ
        Order order = new Order.Builder()
                .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                .lastName(stringRandomGenerate(rnd.nextInt(254) + 1))
                .address(stringRandomGenerate(rnd.nextInt(254) + 1))
                .metroStation(Integer.toString(rnd.nextInt(1000) + 1))
                .phone(phoneRandomGenerate(11))
                .rentTime(rnd.nextInt(365) + 1)
                .deliveryDate(dateRandomGenerate())
                .comment(stringRandomGenerate(rnd.nextInt(254) + 1))
                .color(List.of(new String[]{Colors.BLACK.getColor(), Colors.GREY.getColor()}))
                .build();
        OrderAfterCreate orderAfterCreate = checkCreateOrder(order);
        //проверяем возможность принять заказ курьером
        checkAcceptOrder(orderAfterCreate, courierAfterLogin);
        //получаем список заказов тестового курьера
        Response response = OrderClient.getListForCourier(courierAfterLogin.getId());
        //проверяем статус код ответа
        assertEquals(200, response.statusCode(), "Получаемый статус код при получении списка заказов курьера не соответствует ожидаемому");
        //проверяем структуру ответа
        assertEquals(response.as(OrderList.class).getClass(), OrderList.class, "Ответ на запрос о получнии списка заказов для курьера не соответствует ожидаемому");
        assertEquals(response.as(OrderList.class).getOrders().size(), 1, "Количество заказов для курьера не соответствует ожидаемому");

        //установлено, что в момент обновления информации о заказе создается еще один идентичный заказ, но с другим id
        //таким образом, в получаемом списке заказов для курьера всегда минимум 2 заказа
        //в связи с недостаточностью информации не совсем понятно на сколько это корректное поведение
    }

    @Test
    @Description("Checking the possibility of receiving a list of orders from the specified courier, taking into account the nearest metro stations.")
    public void getListOrdersForCourierWithNearestStation() throws JsonProcessingException {
        Random rnd = new Random();
        //создаем курьера и пытаемся залогинить его в системе
        Courier courier = new Courier.Builder()
                .login(stringRandomGenerate(rnd.nextInt(254) + 1))
                .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                .build();
        CourierAfterLogin courierAfterLogin = checksCorrectCreateAndLoginCourier(courier).as(CourierAfterLogin.class);
        //генерируем список ближайших станций
        ArrayList<String> stations = metroStationsListGenerate(rnd.nextInt(10) + 1);
        //преобразуем список станций в json
        Gson gson = new Gson();
        NearestMetroStations nearestStations = new NearestMetroStations();
        nearestStations.setNearestStation(stations);
        String nearestStationsJson = gson.toJson(nearestStations);
        //убираем кавычки у поля json (согласно примеру в документации их нет)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature());
        JsonNode jsonNode = objectMapper.readTree(nearestStationsJson);
        String jsonStringWithoutQuotes = objectMapper.writeValueAsString(jsonNode);
        //уберем внешние скобки у json (для корректной подстановки json в качестве параметра)
        nearestStationsJson = jsonStringWithoutQuotes.substring(1, jsonStringWithoutQuotes.length() - 1);
        //заменим : на = (для корректной подстановки json в качестве параметра)
        nearestStationsJson = nearestStationsJson.replace(':', '=');
        //создаем заказы с соответствующими станциями и назначаем их курьеру
        ArrayList<Order> list = orderListWithFixedMetroStationsForCourier(stations, courierAfterLogin);
        //проверяем возможность принять заказы курьером
        checkCreateAndAcceptOrders(list, courierAfterLogin);
        //получаем список заказов тестового курьера c учетом ближайших станций
        Response response = OrderClient.getListForCourierWithNearestStations(courierAfterLogin.getId(), nearestStationsJson);
        //проверяем статус код ответа
        assertEquals(200, response.statusCode(), "Получаемый статус код при получении списка заказов курьера не соответствует ожидаемому");
        //проверяем структуру ответа
        assertEquals(response.as(OrderList.class).getClass(), OrderList.class, "Структура ответа на запрос о получнии списка заказов для курьера не соответствует ожидаемой");
        //сформируем список станций метро из списка полученных заказов
        ArrayList<String> stationsFromListOrders = metroStationsListFromOrders(response.as(OrderList.class));

        //из-за того, что в момент обновления информации о заказе создается еще один идентичный заказ, но с другим id
        //в списке stationsFromListOrders будут храниться дублирующиеся значения
        //также может отличаться порядок элементов в списках
        Set<String> stationsSet = new HashSet<String>(stations);
        Set<String> stationsFromListOrdersSet = new HashSet<String>(stationsFromListOrders);
        assertEquals(stationsSet, stationsFromListOrdersSet, "Список заказов сформирован без учета ближайших станций метро");
    }
}
