package com.epam.training.calculator.impl.bean;

import com.epam.training.calculator.impl.bean.Symbol;

import java.util.List;

public class SymbolBuffer {

    private int position;
    public List<Symbol> symbols;

    public SymbolBuffer(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    public Symbol next() {
        return symbols.get(position++);
    }

    public void back() {
        position--;
    }

    public int getPosition() {
        return position;
    }
}

