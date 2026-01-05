/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Recursive descent parser for expressions.
 */
final class Parser {

	@NotNull
	private final Lexer lexer;
	@NotNull
	private final Map<String, Object> vars;
	@NotNull
	private Token current;

	Parser(@NotNull Lexer pLexer, @NotNull Map<String, Object> pVariables) {
		this.lexer = pLexer;
		this.vars = pVariables;
		this.current = pLexer.next();
	}

	@NotNull
	Object parse() {
		Object result = parseOr();
		expect(TokenType.EOF);
		return result;
	}

	@NotNull
	private Object parseOr() {
		Object left = parseAnd();
		while (current.type() == TokenType.OR_OR) {
			advance();
			Object right = parseAnd();
			left = Coerce.toBool(left) || Coerce.toBool(right);
		}
		return left;
	}

	@NotNull
	private Object parseAnd() {
		Object left = parseEquality();
		while (current.type() == TokenType.AND_AND) {
			advance();
			Object right = parseEquality();
			left = Coerce.toBool(left) && Coerce.toBool(right);
		}
		return left;
	}

	@NotNull
	private Object parseEquality() {
		Object left = parseComparison();
		while (current.type() == TokenType.EQ_EQ || current.type() == TokenType.BANG_EQ) {
			TokenType op = current.type();
			advance();
			Object right = parseComparison();
			boolean eq = Coerce.equalsLoose(left, right);
			left = (op == TokenType.EQ_EQ) == eq;
		}
		return left;
	}

	@NotNull
	private Object parseComparison() {
		Object left = parseAdditive();
		while (isComparison(current.type())) {
			TokenType op = current.type();
			advance();
			double a = Coerce.toNumber(left);
			double b = Coerce.toNumber(parseAdditive());
			left = compare(op, a, b);
		}
		return left;
	}

	@NotNull
	private Object parseAdditive() {
		Object left = parseMultiplicative();
		while (current.type() == TokenType.PLUS || current.type() == TokenType.MINUS) {
			TokenType op = current.type();
			advance();
			double a = Coerce.toNumber(left);
			double b = Coerce.toNumber(parseMultiplicative());
			left = op == TokenType.PLUS ? a + b : a - b;
		}
		return left;
	}

	@NotNull
	private Object parseMultiplicative() {
		Object left = parseUnary();
		while (current.type() == TokenType.STAR || current.type() == TokenType.SLASH) {
			TokenType op = current.type();
			advance();
			double a = Coerce.toNumber(left);
			double b = Coerce.toNumber(parseUnary());
			left = op == TokenType.STAR ? a * b : a / b;
		}
		return left;
	}

	@NotNull
	private Object parseUnary() {
		if (current.type() == TokenType.BANG) {
			advance();
			return !Coerce.toBool(parseUnary());
		}
		if (current.type() == TokenType.MINUS) {
			advance();
			return -Coerce.toNumber(parseUnary());
		}
		return parsePrimary();
	}

	@NotNull
	private Object parsePrimary() {
		return switch (current.type()) {
			case NUMBER -> parseNumber();
			case STRING -> parseString();
			case LPAREN -> parseGrouped();
			case LBRACKET -> parseArray();
			case IDENT -> parseIdentifier();
			default -> throw new IllegalArgumentException("Unexpected: " + current);
		};
	}

	@NotNull
	private Object parseNumber() {
		String text = current.text();
		advance();
		return Double.parseDouble(text);
	}

	@NotNull
	private Object parseString() {
		String text = current.text();
		advance();
		return text;
	}

	@NotNull
	private Object parseGrouped() {
		advance(); // (
		Object value = parseOr();
		consume(TokenType.RPAREN);
		return value;
	}

	@NotNull
	private List<Object> parseArray() {
		advance(); // [
		List<Object> items = new ArrayList<>();
		if (current.type() != TokenType.RBRACKET) {
			items.add(parseOr());
			while (current.type() == TokenType.COMMA) {
				advance();
				items.add(parseOr());
			}
		}
		consume(TokenType.RBRACKET);
		return items;
	}

	@NotNull
	private Object parseIdentifier() {
		String name = current.text();
		advance();

		// Function call
		if (current.type() == TokenType.LPAREN) {
			return parseFunctionCall(name);
		}

		// Variable with optional property access
		Object value = vars.get(name);
		while (current.type() == TokenType.DOT) {
			advance();
			String prop = current.text();
			consume(TokenType.IDENT);
			value = Coerce.getProperty(value, prop);
		}
		return value;
	}

	@NotNull
	private Object parseFunctionCall(@NotNull String pName) {
		advance(); // (
		List<Object> args = new ArrayList<>();
		if (current.type() != TokenType.RPAREN) {
			args.add(parseOr());
			while (current.type() == TokenType.COMMA) {
				advance();
				args.add(parseOr());
			}
		}
		consume(TokenType.RPAREN);
		return ExpressionFunctions.call(pName, args);
	}

	private boolean isComparison(@NotNull TokenType pType) {
		return pType == TokenType.LT || pType == TokenType.LT_EQ
				|| pType == TokenType.GT || pType == TokenType.GT_EQ;
	}

	private boolean compare(@NotNull TokenType pType, double pA, double pB) {
		return switch (pType) {
			case LT -> pA < pB;
			case LT_EQ -> pA <= pB;
			case GT -> pA > pB;
			case GT_EQ -> pA >= pB;
			default -> throw new IllegalStateException("Invalid comparison: " + pType);
		};
	}

	private void expect(@NotNull TokenType pType) {
		if (current.type() != pType) {
			throw new IllegalArgumentException("Expected " + pType + " but got " + current);
		}
	}

	private void consume(@NotNull TokenType pType) {
		expect(pType);
		advance();
	}

	private void advance() {
		current = lexer.next();
	}
}
