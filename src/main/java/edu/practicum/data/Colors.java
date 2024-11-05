package edu.practicum.data;

public enum Colors {
    BLACK("BLACK"),
    GREY("GREY");
    private final String color;

    Colors(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
