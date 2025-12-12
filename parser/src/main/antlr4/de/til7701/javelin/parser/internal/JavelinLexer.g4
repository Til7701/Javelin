lexer grammar JavelinLexer;

MUT: 'mut';
WHILE: 'while';
STATIC: 'static';
NATIVE: 'native';
CLASS: 'class';
ENUM: 'enum';
ANNOTATION: 'annotation';
RETURN: 'return';
BREAK: 'break';
CONTINUE: 'continue';
PUB: 'pub';
GET: 'get';
SET: 'set';
IF: 'if';
ELSE: 'else';
FOR: 'for';
AS: 'as';
EXTENDS: 'extends';
IN: 'in';

LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
SEMI   : ';';
COMMA  : ',';
DOT    : '.';

ASSIGN     : '=';
GT         : '>';
LT         : '<';
BANG       : '!';
TILDE      : '~';
QUESTION   : '?';
COLON      : ':';
EQUAL      : '==';
LE         : '<=';
GE         : '>=';
NOTEQUAL   : '!=';
AND        : '&&';
OR         : '||';
HASH       : '#';
INC        : '++';
DEC        : '--';
ADD        : '+';
SUB        : '-';
MUL        : '*';
DIV        : '/';
BITAND     : '&';
BITOR      : '|';
CARET      : '^';
MOD        : '%';
ARROW      : '->';
COLONCOLON : '::';

ADD_ASSIGN     : '+=';
SUB_ASSIGN     : '-=';
MUL_ASSIGN     : '*=';
DIV_ASSIGN     : '/=';
AND_ASSIGN     : '&=';
OR_ASSIGN      : '|=';
XOR_ASSIGN     : '^=';
MOD_ASSIGN     : '%=';

AT : '@';

IntegerLiteral : [0-9]+;
StringLiteral : '"' ( ~["\\] | '\\' . )* '"';
BooleanLiteral : 'true' | 'false';

EnumValueIdentifier : [A-Z][A-Z_]*;
TypeIdentifier: [A-Z][a-zA-Z0-9]*;
SymbolIdentifier : [a-z][a-zA-Z_0-9]*;

WS: [ \t\r\n\u000C]+ -> skip;
COMMENT: '/*' .*? '*/' -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;
