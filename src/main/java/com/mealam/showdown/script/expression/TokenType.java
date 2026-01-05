/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package com.mealam.showdown.script.expression;

enum TokenType {
	EOF,
	IDENT,
	NUMBER,
	STRING,
	LPAREN, RPAREN,
	LBRACKET, RBRACKET,
	COMMA, DOT,
	PLUS, MINUS, STAR, SLASH,
	BANG,
	AND_AND, OR_OR,
	EQ_EQ, BANG_EQ,
	LT, LT_EQ, GT, GT_EQ
}
