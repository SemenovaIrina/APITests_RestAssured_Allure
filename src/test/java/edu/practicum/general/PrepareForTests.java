package edu.practicum.general;

import edu.practicum.data.Constants;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

public abstract class PrepareForTests {
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }
}
