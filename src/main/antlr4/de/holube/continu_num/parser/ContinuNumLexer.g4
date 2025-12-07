lexer grammar ContinuNumLexer;

MUT: 'mut';
MEW: 'mew';
ARRAY: 'array';
WHILE: 'while';

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

IntegerLiteral : [0-9]+;
StringLiteral : '"' ( ~["\\] | '\\' . )* '"';
BooleanLiteral : 'true' | 'false';

TypeIdentifier: [A-Z][a-zA-Z_0-9]* | PRIMITIVE;
PRIMITIVE: 'i8' | 'i16' | 'i32' | 'i64' | 'f32' | 'f64' | 'bool' | 'str' | 'char';
SymbolIdentifier : [a-z][a-zA-Z_0-9]*;

WS: [ \t\r\n\u000C]+ -> skip;
COMMENT: '/*' .*? '*/' -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;
