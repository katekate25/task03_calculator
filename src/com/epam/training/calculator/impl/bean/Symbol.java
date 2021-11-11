package com.epam.training.calculator.impl.bean;


public class Symbol {

    private final SymbolType type;
    private final String value;

    public Symbol(SymbolType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Symbol(SymbolType type, Character value) {
        this.type = type;
        this.value = value.toString();
    }

    public SymbolType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}