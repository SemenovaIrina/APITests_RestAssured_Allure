package edu.practicum.client;

import com.google.gson.Gson;
import edu.practicum.data.Constants;
import edu.practicum.models.Order;
import edu.practicum.models.OrderAfterCreate;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class OrderClient {
    @Step("Create order with POST request to /api/v1/orders")
    public static Response create(Order order) {
        return
                given()
                        .header("Content-type", "application/json; charset=utf-8")
                        .and()
                        .body(order)
                        .when()
                        .post(Constants.ORDER_CREATE_REQUEST);
    }

    @Step("Getting information about a single order by track number with GET request to api/v1/orders/track")
    public static Response getInfo(OrderAfterCreate order) {
        return
                given()
                        .header("Content-type", "application/json; charset=utf-8")
                        .queryParam("t", order.getTrack())
                        .get(Constants.GET_ORDER_INFO_REQUEST);
    }

    @Step("Getting information about all orders with GET request to /api/v1/orders")
    public static Response getListWithAllOrders() {
        return
                given()
                        .header("Content-type", "application/json; charset=utf-8")
                        .get(Constants.GET_ORDER_LIST_REQUEST);
    }

    @Step("Getting information about courier orders with GET request to /api/v1/orders")
    public static Response getListForCourier(int courierId) {
        return
                given()
                        .header("Content-type", "application/json; charset=utf-8")
                        .queryParam("courierId", courierId)
                        .get(Constants.GET_ORDER_LIST_REQUEST);
    }

    @Step("Getting information about courier orders, taking into account the nearest metro stations with GET request to /api/v1/orders")
    public static Response getListForCourierWithNearestStations(int courierId, ArrayList stations) {
        return
                given().log().all()
                        .header("Content-type", "application/json")
                        .queryParam("courierId", courierId)
                        .queryParam("nearestStation", new Gson().toJson(stations))
                        //.queryParam(stations)
                        .get(Constants.GET_ORDER_LIST_REQUEST);
    }

    @Step("Cancellation of the order with PUT request to /api/v1/orders/cancel")
    public static void cancel(OrderAfterCreate order) {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .put(Constants.ORDER_CANCEL_REQUEST);
    }

    @Step("The courier accepts the order with PUT request to /api/v1/orders/accept")
    public static Response acceptOrder(int courierId, int orderId) {
        return
                given()
                        .header("Content-type", "application/json")
                        .queryParam("courierId", courierId)
                        .when()
                        .put(Constants.COURIER_ACCEPT_ORDER_REQUEST + "/" + orderId);
    }

    @Step("Cancellation the list of orders")
    public static void cancelListOfOrders(ArrayList<OrderAfterCreate> orders) {
        for (int i = 0; i < orders.size(); i++) {
            cancel(orders.get(i));
        }
    }
}
