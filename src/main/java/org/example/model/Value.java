package org.example.model;

import lombok.Data;

import java.util.Arrays;

@Data
public class Value implements Comparable<Value> {
    private  String value;
    private  Integer weight;

    public Value(String value) {
        this.value = value;
        String[] valuesArr = {"6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        weight = Arrays.asList(valuesArr).indexOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Value objValue = (Value) o;
        return weight.equals(objValue.weight);
    }



    @Override
    public String toString() {
        return value;
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
    }}
