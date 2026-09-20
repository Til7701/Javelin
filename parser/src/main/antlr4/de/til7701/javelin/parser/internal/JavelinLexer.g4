lexer grammar JavelinLexer;

NEXT: 'next';
PREVIOUS: 'previous';
DO: 'do';
WHEN: 'when';
LET: 'let';
NEW: 'new';
STATIC: 'static';
CLASS: 'class';
RETURN: 'return';
BREAK: 'break';
CONTINUE: 'continue';
PUB: 'pub';
GET: 'get';
SET: 'set';
ELSE: 'else';
AS: 'as';
EXTENDS: 'extends';
IMPORT: 'import';

LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
SEMI   : ';';
COMMA  : ',';
DOT    : '.';
COLON      : ':';

ASSIGN     : '=';
GT         : '>';
LT         : '<';
BANG       : '!';
TILDE      : '~';
QUESTION   : '?';
EQUAL      : '==';
LE         : '<=';
GE         : '>=';
NOTEQUAL   : '!=';
AND        : '&&';
OR         : '||';
IMPL       : '==>';
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
AT         : '@';

ADD_ASSIGN     : '+=';
SUB_ASSIGN     : '-=';
MUL_ASSIGN     : '*=';
DIV_ASSIGN     : '/=';
AND_ASSIGN     : '&=';
OR_ASSIGN      : '|=';
XOR_ASSIGN     : '^=';
MOD_ASSIGN     : '%=';

INTEGER : [0-9]+;
I : 'I';
U : 'U';

UnsignedIntegerTypeIdentifier : U INTEGER;
SignedIntegerTypeIdentifier : I INTEGER;
SignedIntegerLiteral : SUB? INTEGER I INTEGER;
UnsignedIntegerLiteral : INTEGER U INTEGER;

StringLiteral : '"' ( ~["\\] | '\\' . )* '"';
BooleanLiteral : 'true' | 'false';

TypeIdentifier : [A-Z][a-zA-Z0-9]*;
SymbolIdentifier : [a-z][a-zA-Z_0-9]*;
FullyQualifiedTypeIdentifier : (SymbolIdentifier DOT)+ TypeIdentifier;

WS: [ \t\r\n\u000C]+ -> skip;
MULTI_LINE_COMMENT: '/*' .*? '*/' -> skip;
DOC_COMMENT_LINE: '///' ~[\r\n]* -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;
