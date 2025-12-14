parser grammar JavelinParser;

options {
    tokenVocab=JavelinLexer;
}

compilationUnit : (statement* | typeDefinition) EOF;

statement
    : LBRACE statement* RBRACE #statementList
    | MUT? typeIdentifier SymbolIdentifier ASSIGN expression SEMI #variableInitialization
    | expression ASSIGN expression SEMI #assignment
    | expression SEMI #expressionStatement
    | WHILE expression statement #whileStatement
    | FOR typeIdentifier SymbolIdentifier IN expression statement #foreachStatement
    | RETURN expression? SEMI #returnStatement
    | BREAK SEMI #breakStatement
    | CONTINUE SEMI #continueStatement
    | IF expression statement (ELSE statement)? #ifStatement
    ;

expression
    : IntegerLiteral #integerLiteralExpression
    | StringLiteral #stringLiteralExpression
    | BooleanLiteral #booleanLiteralExpression
    | SymbolIdentifier #symbolIdentifierExpression
    | typeIdentifier DOT SymbolIdentifier LPAREN (expression (COMMA expression)*)? RPAREN #staticMethodCall
    | expression DOT SymbolIdentifier LPAREN (expression (COMMA expression)*)? RPAREN #instanceMethodCall
    | typeIdentifier LPAREN (expression (COMMA expression)*)? RPAREN #constructorCall
    | leftUnaryOperator expression #leftUnaryOperationExpression
    | expression rightUnaryOperator #rightUnaryOperationExpression
    | expression binaryOperator expression #binaryOperationExpression
    | LPAREN expression RPAREN #parenExpression
    | expression LBRACK expression RBRACK #collectionAccess
    | TypeIdentifier DOT EnumValueIdentifier #enumValueExpression
    | expression AS typeIdentifier #typeCastExpression
    ;

leftUnaryOperator
    : INC
    | DEC
    | SUB
    | BANG
    | HASH
    ;

rightUnaryOperator
    : INC
    | DEC
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
    : TypeIdentifier #simpleTypeIdentifier
    | typeIdentifier LBRACK typeIdentifier RBRACK #collectionTypeIdentifier
    | typeIdentifier genericTypeList #genericTypeIdentifier
    | ITypeIdentifier #iTypeIdentifier
    ;

typeDefinition
    : annotationTypeDefinition
    | enumTypeDefinition
    | classTypeDefinition
    ;

typeModifier
    : NATIVE
    | PUB
    ;

classTypeDefinition
    : typeModifier* CLASS genericTypeList? SEMI (EXTENDS typeIdentifier (COMMA typeIdentifier)*)? fieldDefinition* constructorDefinition* methodDefinition*
    ;

genericTypeList
    : LT (typeIdentifier (COMMA typeIdentifier)*) GT
    ;

fieldDefinition
    : annotation* fieldModifier* typeIdentifier SymbolIdentifier SEMI #uninitializedFieldDefinition
    | annotation* fieldModifier* typeIdentifier SymbolIdentifier ASSIGN expression SEMI #initializedFieldDefinition
    ;

fieldModifier
    : STATIC
    | MUT
    ;

constructorDefinition
    : annotation* methodModifier* LPAREN parametherList? RPAREN statement
    ;

methodDefinition
    : annotation* methodModifier* typeIdentifier? SymbolIdentifier LPAREN parametherList? RPAREN statement
    ;

methodModifier
    : STATIC
    | NATIVE
    | PUB
    ;

parameter
    : MUT? typeIdentifier SymbolIdentifier
    ;

parametherList
    : parameter (COMMA parameter)*
    ;

annotation
    : AT TypeIdentifier (LPAREN (elementValuePair (COMMA elementValuePair)*)? RPAREN)?
    ;

elementValuePair
    : SymbolIdentifier ASSIGN expression
    ;

annotationTypeDefinition
    : typeModifier* ANNOTATION SEMI annotationFieldDefinition*
    ;

annotationFieldDefinition
    : typeIdentifier SymbolIdentifier SEMI
    ;

enumTypeDefinition
    : typeModifier* ENUM SEMI EnumValueIdentifier (COMMA EnumValueIdentifier)* COMMA?
    ;
