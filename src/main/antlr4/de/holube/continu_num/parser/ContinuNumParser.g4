parser grammar ContinuNumParser;

options {
    tokenVocab=ContinuNumLexer;
}

compilationUnit : statement* EOF;

statement
    : symbolDefinition SEMI
    | assignment SEMI
    | staticMethodCall SEMI
    | whileStatement
    ;

symbolDefinition
    : MUT? typeIdentifier SymbolIdentifier ASSIGN expression
    ;

expression
    : literalExpression
    | symbolIdentifierExpression
    | staticMethodCall
    | expression binaryOperator expression
    | LPAREN expression RPAREN
    | expression LBRACK expression RBRACK // collection access
    | ARRAY LBRACE (expression (COMMA expression)*)? RBRACE // 1D array initializer
    ;

binaryOperator
    : ADD
    | SUB
    | MUL
    | DIV
    | LT
    | GT
    | LE
    | GE
    | EQUAL
    | NOTEQUAL
    ;

typeIdentifier
    : TypeIdentifier
    | typeIdentifier LBRACK RBRACK
    ;

literalExpression
    : IntegerLiteral
    | StringLiteral
    | BooleanLiteral
    ;

symbolIdentifierExpression
    : SymbolIdentifier
    ;

assignment
    : SymbolIdentifier ASSIGN expression
    ;

staticMethodCall
    : (typeIdentifier | expression) DOT SymbolIdentifier LPAREN (expression (COMMA expression)*)? RPAREN
    ;

whileStatement
    : WHILE expression LBRACE statement* RBRACE
    ;
