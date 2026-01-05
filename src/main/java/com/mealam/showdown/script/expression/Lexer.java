/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.expression;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Tokenizes expression strings.
 */
// TODO: Extract to Utils
final class Lexer {

	@NotNull
	private final String source;
	private int pos;

	Lexer(@NotNull String pSource) {
		this.source = pSource;
		this.pos = 0;
	}

	@NotNull
	Token next() {
		skipWhitespace();
		if (pos >= source.length()) {
			return new Token(TokenType.EOF, "");
		}

		// Two-char operators
		Token twoChar = tryTwoCharOperator();
		if (twoChar != null) return twoChar;

		// Single-char tokens
		Token singleChar = trySingleCharToken();
		if (singleChar != null) return singleChar;

		// String literals
		char c = source.charAt(pos);
		if (c == '\'' || c == '"') {
			return readString();
		}

		// Numbers
		if (isDigit(c) || (c == '.' && hasDigitAt(pos + 1))) {
			return readNumber();
		}

		// Identifiers
		if (isIdentStart(c)) {
			return readIdentifier();
		}

		throw new IllegalArgumentException("Unexpected character at " + pos + ": " + c);
	}

	private @Nullable Token tryTwoCharOperator() {
		if (match("&&")) return new Token(TokenType.AND_AND, "&&");
		if (match("||")) return new Token(TokenType.OR_OR, "||");
		if (match("==")) return new Token(TokenType.EQ_EQ, "==");
		if (match("!=")) return new Token(TokenType.BANG_EQ, "!=");
		if (match("<=")) return new Token(TokenType.LT_EQ, "<=");
		if (match(">=")) return new Token(TokenType.GT_EQ, ">=");
		return null;
	}

	@Nullable
	private Token trySingleCharToken() {
		char c = source.charAt(pos);
		TokenType type = switch (c) {
			case '(' -> TokenType.LPAREN;
			case ')' -> TokenType.RPAREN;
			case '[' -> TokenType.LBRACKET;
			case ']' -> TokenType.RBRACKET;
			case ',' -> TokenType.COMMA;
			case '.' -> TokenType.DOT;
			case '+' -> TokenType.PLUS;
			case '-' -> TokenType.MINUS;
			case '*' -> TokenType.STAR;
			case '/' -> TokenType.SLASH;
			case '!' -> TokenType.BANG;
			case '<' -> TokenType.LT;
			case '>' -> TokenType.GT;
			default -> null;
		};
		if (type != null) {
			pos++;
			return new Token(type, String.valueOf(c));
		}
		return null;
	}

	@NotNull
	private Token readIdentifier() {
		int start = pos++;
		while (pos < source.length() && isIdentPart(source.charAt(pos))) {
			pos++;
		}
		return new Token(TokenType.IDENT, source.substring(start, pos));
	}

	@NotNull
	private Token readNumber() {
		int start = pos;
		boolean hasDot = false;

		while (pos < source.length()) {
			char c = source.charAt(pos);
			if (isDigit(c)) {
				pos++;
			} else if (c == '.' && !hasDot) {
				hasDot = true;
				pos++;
			} else {
				break;
			}
		}
		return new Token(TokenType.NUMBER, source.substring(start, pos));
	}

	@NotNull
	private Token readString() {
		char quote = source.charAt(pos++);
		StringBuilder sb = new StringBuilder();

		while (pos < source.length()) {
			char c = source.charAt(pos++);
			if (c == '\\' && pos < source.length()) {
				sb.append(unescape(source.charAt(pos++)));
			} else if (c == quote) {
				return new Token(TokenType.STRING, sb.toString());
			} else {
				sb.append(c);
			}
		}
		throw new IllegalArgumentException("Unterminated string");
	}

	private char unescape(char pChar) {
		return switch (pChar) {
			case 'n' -> '\n';
			case 'r' -> '\r';
			case 't' -> '\t';
			default -> pChar;
		};
	}

	private boolean match(@NotNull String pLiteral) {
		if (source.regionMatches(pos, pLiteral, 0, pLiteral.length())) {
			pos += pLiteral.length();
			return true;
		}
		return false;
	}

	private void skipWhitespace() {
		while (pos < source.length() && Character.isWhitespace(source.charAt(pos))) {
			pos++;
		}
	}

	private boolean hasDigitAt(int pInt) {
		return pInt < source.length() && isDigit(source.charAt(pInt));
	}

	private static boolean isDigit(char pChar) {
		return pChar >= '0' && pChar <= '9';
	}

	private static boolean isIdentStart(char pChar) {
		return (pChar >= 'a' && pChar <= 'z') || (pChar >= 'A' && pChar <= 'Z') || pChar == '_';
	}

	private static boolean isIdentPart(char pChar) {
		return isIdentStart(pChar) || isDigit(pChar);
	}
}
