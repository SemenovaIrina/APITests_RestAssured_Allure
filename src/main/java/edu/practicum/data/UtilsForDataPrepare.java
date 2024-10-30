package edu.practicum.data;

import edu.practicum.models.CourierAfterLogin;
import edu.practicum.models.Order;
import edu.practicum.models.OrderInfoGetInList;
import edu.practicum.models.OrderList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UtilsForDataPrepare {

    public static String stringRandomGenerate(int length) {
        Random random = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            //используем символы с кодами в диапазоне от 32 до 175 (пробел, знаки операций и препинания, английские и русские буквы, цифры)
            //если появятся ограничения на используемые символы, то можно описать массив с этими символами и выбирать случайные значения из него
            text[i] = (char) (random.nextInt(150) + 32); //150=175-32+1
        }
        return new String(text);
    }

    public static String substringRandom(String str, int substringLength) {
        Random random = new Random();
        int strLength = str.length();
        int beginSubstr = random.nextInt(strLength - substringLength);
        return str.substring(beginSubstr, beginSubstr + substringLength - 1);
    }

    public static String phoneRandomGenerate(int length) {
        Random random = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            //используем символы с кодами в диапазоне от 48 до 57 (цифры)
            text[i] = (char) (random.nextInt(10) + 48); //10=57-48+1
        }
        return new String(text);
    }

    public static String dateRandomGenerate() {
        //генерируем случайную дату от текущей
        Random rnd = new Random();
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime futureLocalDateTime = localDateTime.plusDays(rnd.nextInt(30));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedFutureLocalDateTime = futureLocalDateTime.format(dateTimeFormatter);
        return formattedFutureLocalDateTime;

    }

    public static ArrayList<String> metroStationsListGenerate(int length) {
        Random random = new Random();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(Integer.toString(random.nextInt(100) + 1));
        }
        return list;
    }

    public static ArrayList<String> metroStationsListFromOrders(OrderList orderList) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < orderList.getOrders().size(); i++) {
            OrderInfoGetInList order = orderList.getOrders().get(i);
            list.add(order.getMetroStation());
        }
        return list;
    }

    public static ArrayList<Order> orderListWithFixedMetroStationsForCourier(ArrayList<String> metroStations, CourierAfterLogin courierAfterLogin) {
        Random rnd = new Random();
        ArrayList<Order> list = new ArrayList<>();
        for (int i = 0; i < metroStations.size(); i++) {
            Order order = new Order.Builder()
                    .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                    .lastName(stringRandomGenerate(rnd.nextInt(254) + 1))
                    .address(stringRandomGenerate(rnd.nextInt(254) + 1))
                    .metroStation(metroStations.get(i))
                    .phone(phoneRandomGenerate(11))
                    .rentTime(rnd.nextInt(365) + 1)
                    .deliveryDate(dateRandomGenerate())
                    .comment(stringRandomGenerate(rnd.nextInt(254) + 1))
                    .color(List.of(new String[]{Colors.BLACK.getColor(), Colors.GREY.getColor()}))
                    .build();
            list.add(order);
        }
        //добавим заказ со станцией, которой точно нет в списке ближайших станций
        Order order = new Order.Builder()
                .firstName(stringRandomGenerate(rnd.nextInt(254) + 1))
                .lastName(stringRandomGenerate(rnd.nextInt(254) + 1))
                .address(stringRandomGenerate(rnd.nextInt(254) + 1))
                .metroStation("234")
                .phone(phoneRandomGenerate(11))
                .rentTime(rnd.nextInt(365) + 1)
                .deliveryDate(dateRandomGenerate())
                .comment(stringRandomGenerate(rnd.nextInt(254) + 1))
                .color(List.of(new String[]{Colors.BLACK.getColor(), Colors.GREY.getColor()}))
                .build();
        list.add(order);
        return list;
    }
}
