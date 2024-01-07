package org.example.EntityLayer;


import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class Value implements Comparable<Value> {
    String value;
    Integer weight;

    public Value(String value) {
        this.value = value;
        String[] valuesArr = {"6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        this.weight = Arrays.asList(valuesArr).indexOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        } else return weight.equals(((Value) o).getWeight());
    }


    @Override
    public String toString() {
        if(value.equals("J")) {
            return " J";
        } else return value;
    }

    @Override
    public int compareTo(Value o) {
        if (weight.equals(o.weight)) {
            return 0;
        } else if (weight > o.weight) {
            return 1;
        } else return -1;
    }

    public int getWeight() {
        return weight;
    }
}
