package com.epam.training.calculator.impl;

import com.epam.training.calculator.Calculator;
import com.epam.training.calculator.exception.UnexpectedSymbolException;
import com.epam.training.calculator.impl.bean.Symbol;
import com.epam.training.calculator.impl.bean.SymbolBuffer;
import com.epam.training.calculator.impl.bean.SymbolType;

import java.util.ArrayList;
import java.util.List;

public class CalculatorImpl implements Calculator {

    private static final char NINE = '9';
    private static final char ZERO = '0';

    @Override
    public double calculate(String expression) {
        List<Symbol> symbols = convertToSymbolList(expression);
        SymbolBuffer symbolBuffer = new SymbolBuffer(symbols);
        return calculate(symbolBuffer);
    }

    private double calculate(SymbolBuffer symbols) {
        Symbol symbol = symbols.next();
        if (symbol.getType() == SymbolType.END_OF_LINE) {
            return 0;
        } else {
            symbols.back();
            return plusMinus(symbols);
        }
    }

    private List<Symbol> convertToSymbolList(String expression) {
        ArrayList<Symbol> symbols = new ArrayList<>();
        int currentPosition = 0;
        while (currentPosition < expression.length()) {
            char currentChar = expression.charAt(currentPosition);
            switch (currentChar) {
                case '(':
                    symbols.add(new Symbol(SymbolType.LEFT_BRACKET, currentChar));
                    currentPosition++;
                    continue;
                case ')':
                    symbols.add(new Symbol(SymbolType.RIGHT_BRACKET, currentChar));
                    currentPosition++;
                    continue;
                case '+':
                    symbols.add(new Symbol(SymbolType.PLUS, currentChar));
                    currentPosition++;
                    continue;
                case '-':
                    symbols.add(new Symbol(SymbolType.MINUS, currentChar));
                    currentPosition++;
                    continue;
                case '*':
                    symbols.add(new Symbol(SymbolType.MULTIPLY, currentChar));
                    currentPosition++;
                    continue;
                case '/':
                    symbols.add(new Symbol(SymbolType.DIVIDE, currentChar));
                    currentPosition++;
                    continue;
                default:
                    if (currentChar <= NINE && currentChar >= ZERO) {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(currentChar);
                            currentPosition++;
                            if (currentPosition >= expression.length()) {
                                break;
                            }
                            currentChar = expression.charAt(currentPosition);
                        } while (currentChar <= NINE && currentChar >= ZERO);
                        symbols.add(new Symbol(SymbolType.NUMBER, sb.toString()));
                    } else {
                        if (currentChar != ' ') {
                            throw new UnexpectedSymbolException("Unexpected character: " + currentChar);
                        }
                        currentPosition++;
                    }
            }
        }
        symbols.add(new Symbol(SymbolType.END_OF_LINE, ""));
        return symbols;
    }

    private double plusMinus(SymbolBuffer symbols) {
        double value = multiplyDivide(symbols);
        while (true) {
            Symbol symbol = symbols.next();
            switch (symbol.getType()) {
                case PLUS -> value += multiplyDivide(symbols);
                case MINUS -> value -= multiplyDivide(symbols);
                case END_OF_LINE, RIGHT_BRACKET -> {
                    symbols.back();
                    return value;
                }
                default -> throw getUnexpectedSymbolException(symbol, symbols);
            }
        }
    }

    private double multiplyDivide(SymbolBuffer symbols) {
        double value = function(symbols);
        while (true) {
            Symbol symbol = symbols.next();
            switch (symbol.getType()) {
                case MULTIPLY -> value *= function(symbols);
                case DIVIDE -> value /= function(symbols);
                case END_OF_LINE, RIGHT_BRACKET, PLUS, MINUS -> {
                    symbols.back();
                    return value;
                }
                default -> throw getUnexpectedSymbolException(symbol, symbols);
            }
        }
    }

    private double function(SymbolBuffer symbols) {
        Symbol symbol = symbols.next();
        switch (symbol.getType()) {
            case NUMBER:
                return Integer.parseInt(symbol.getValue());
            case LEFT_BRACKET:
                double value = plusMinus(symbols);
                symbol = symbols.next();
                if (symbol.getType() != SymbolType.RIGHT_BRACKET) {
                    throw getUnexpectedSymbolException(symbol, symbols);
                }
                return value;
            default:
                throw getUnexpectedSymbolException(symbol, symbols);
        }
    }
    
    private UnexpectedSymbolException getUnexpectedSymbolException(Symbol symbol, SymbolBuffer symbols) {
        return new UnexpectedSymbolException("Unexpected symbol: " + symbol.getValue()
                + " at position: " + symbols.getPosition());
    }

}
