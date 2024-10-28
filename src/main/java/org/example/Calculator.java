package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class parses and calculates an expression which a user types in.
 */
public class Calculator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, Double> variables = new HashMap<>();


    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.run();
    }

    /**
     * Starts activity of the class and caculates a given expression
     */
    public void run() {
        System.out.println("Введите выражение (например, 3 + 5 * (2 - 8) или x + y):");
        String expression = scanner.nextLine();

        try {
            double result = evaluateExpression(expression);
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private double evaluateExpression(String expression) throws Exception {
        expression = expression.replaceAll("\\s+", ""); // Удаляем пробелы
        return parseExpression(new StringBuilder(expression));
    }

    private double parseExpression(StringBuilder expression) throws Exception {
        double result = parseTerm(expression);

        while (expression.length() > 0) {
            char operator = expression.charAt(0);
            if (operator == '+' || operator == '-') {
                expression.deleteCharAt(0); // Убираем оператор
                double term = parseTerm(expression);
                result = applyOperation(result, term, operator);
            } else {
                break; // Останавливаемся, если оператор не + или -
            }
        }
        return result;
    }

    private double parseTerm(StringBuilder expression) throws Exception {
        double result = parseFactor(expression);

        while (expression.length() > 0) {
            char operator = expression.charAt(0);
            if (operator == '*' || operator == '/') {
                expression.deleteCharAt(0); // Убираем оператор
                double factor = parseFactor(expression);
                result = applyOperation(result, factor, operator);
            } else {
                break; // Останавливаемся, если оператор не * или /
            }
        }
        return result;
    }

    private double parseFactor(StringBuilder expression) throws Exception {
        if (expression.length() == 0) {
            throw new Exception("Неожиданный конец выражения.");
        }

        char firstChar = expression.charAt(0);
        if (Character.isDigit(firstChar) || firstChar == '-') {
            // Обработка чисел
            int index = 1;
            while (index < expression.length() &&
                    (Character.isDigit(expression.charAt(index)) || expression.charAt(index) == '.')) {
                index++;
            }
            String number = expression.substring(0, index);
            expression.delete(0, index); // Убираем обработанное число
            return Double.parseDouble(number);
        } else if (Character.isAlphabetic(firstChar)) {
            // Обработка переменной
            StringBuilder varName = new StringBuilder();
            int index = 0;
            while (index < expression.length() &&
                    (Character.isAlphabetic(expression.charAt(index)) || Character.isDigit(expression.charAt(index)))) {
                varName.append(expression.charAt(index));
                index++;
            }
            String variable = varName.toString();
            expression.delete(0, index); // Убираем обработанное имя переменной
            if (!variables.containsKey(variable)) {
                System.out.print("Введите значение для переменной '" + variable + "': ");
                double value = Double.parseDouble(scanner.nextLine());
                variables.put(variable, value); // Сохраняем значение переменной
            }
            return variables.get(variable); // Возвращаем значение переменной
        } else if (firstChar == '(') {
            // Обработка скобок
            expression.deleteCharAt(0); // Убираем открывающую скобку
            double result = parseExpression(expression);
            if (expression.length() == 0 || expression.charAt(0) != ')') {
                throw new Exception("Несоответствующая скобка.");
            }
            expression.deleteCharAt(0); // Убираем закрывающую скобку
            return result;
        } else {
            throw new Exception("Неизвестный символ: " + firstChar);
        }
    }


    private double applyOperation(double left, double right, char operator) throws Exception {
        switch (operator) {
            case '+':
                return left + right;
            case '-':
                return left - right;
            case '*':
                return left * right;
            case '/':
                if (right == 0) {
                    throw new Exception("Деление на ноль.");
                }
                return left / right;
            default:
                throw new Exception("Неизвестный оператор: " + operator);
        }
    }
}
