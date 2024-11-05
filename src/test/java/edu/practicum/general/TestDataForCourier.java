package edu.practicum.general;

import edu.practicum.models.Courier;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Random;
import java.util.stream.Stream;

import static edu.practicum.data.UtilsForDataPrepare.stringRandomGenerate;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TestDataForCourier {
    public static Stream<Arguments> getCorrectCourierData() {
        Random rnd = new Random();
        return Stream.of(
                arguments(new Courier.Builder()
                        .login(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .build()),
                arguments(new Courier.Builder()
                        .login(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .build())
        );
    }

    public static Stream<Arguments> getCourierDataWithoutRequiredFild() {
        Random rnd = new Random();
        return Stream.of(
                arguments(new Courier.Builder()
                        .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .build()),
                arguments(new Courier.Builder()
                        .login(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .build()),
                arguments(new Courier.Builder()
                        .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .build()),
                arguments(new Courier.Builder()
                        .build())
        );
    }

    public static Stream<Arguments> getEqualCourierData() {
        Random rnd = new Random();
        Courier courier1 = new Courier.Builder()
                .login(stringRandomGenerate(rnd.nextInt(254) + 1))
                .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                .build();
        Courier courier2 = new Courier.Builder()
                .login(courier1.getLogin())
                .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                .build();
        return Stream.of(
                arguments(courier1, courier1),
                arguments(courier1, courier2)
        );
    }
}
