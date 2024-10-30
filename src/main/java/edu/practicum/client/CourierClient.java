package edu.practicum.client;

import edu.practicum.data.Constants;
import edu.practicum.models.Courier;
import edu.practicum.models.CourierAfterLogin;
import edu.practicum.models.CourierForLogin;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class CourierClient {

    @Step("Creating a courier with POST request to /api/v1/courier")
    public static Response create(Courier courier) {
        return
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(Constants.COURIER_CREATE_REQUEST);
    }

    @Step("Delete a courier with DELETE request to /api/v1/courier")
    public static void delete(CourierAfterLogin courier) {
        given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete(Constants.COURIER_DELETE_REQUEST + "/" + courier.getId());
    }

    @Step("Login a courier with POST request to /api/v1/courier/login")
    public static Response login(CourierForLogin courier) {
        return
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(Constants.COURIER_LOGIN_REQUEST);
    }
}
