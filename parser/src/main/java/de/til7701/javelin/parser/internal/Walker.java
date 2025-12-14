package de.til7701.javelin.parser.internal;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Script;
import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.expression.*;
import de.til7701.javelin.ast.statement.*;
import de.til7701.javelin.ast.type.CollectionType;
import de.til7701.javelin.ast.type.GenericType;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.ast.type_definition.TypeModifier;
import de.til7701.javelin.ast.type_definition.TypeModifierValue;
import de.til7701.javelin.ast.type_definition.annotations.AnnotationFieldDefinition;
import de.til7701.javelin.ast.type_definition.annotations.AnnotationTypeDefinition;
import de.til7701.javelin.ast.type_definition.enums.EnumTypeDefinition;
import de.til7701.javelin.ast.type_definition.enums.EnumValueDefinition;
import de.til7701.javelin.parser.ParserException;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@NullMarked
public class Walker extends JavelinParserBaseVisitor<Node> {

    @Override
    public Node visitCompilationUnit(JavelinParser.CompilationUnitContext ctx) {
        List<JavelinParser.StatementContext> statementContexts = ctx.statement();
        if (statementContexts != null && !statementContexts.isEmpty()) {
            log.debug("Visiting compilation unit with {} statements", statementContexts.size());
            List<Statement> statements = ctx.statement().stream()
                    .map(statementContext -> (Statement) visit(statementContext))
                    .toList();

            return new Script(
                    createSpan(ctx),
                    statements
            );
        } else {
            log.debug("Visiting type definition compilation unit");
            return visit(ctx.typeDefinition());
        }
    }

    @Override
    public Node visitStatementList(JavelinParser.StatementListContext ctx) {
        return new StatementList(
                createSpan(ctx),
                ctx.statement().stream()
                        .map(tree -> (Statement) visit(tree))
                        .toList()
        );
    }

    @Override
    public Node visitVariableInitialization(JavelinParser.VariableInitializationContext ctx) {
        return new VariableInitialization(
                createSpan(ctx),
                ctx.MUT() != null,
                (Type) visit(ctx.typeIdentifier()),
                ctx.SymbolIdentifier().getText(),
                (Expression) visit(ctx.expression())
        );
    }

    @Override
    public Node visitAssignment(JavelinParser.AssignmentContext ctx) {
        return new Assignment(
                createSpan(ctx),
                (Expression) visit(ctx.expression(0)),
                (Expression) visit(ctx.expression(1))
        );
    }

    @Override
    public Node visitExpressionStatement(JavelinParser.ExpressionStatementContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Node visitWhileStatement(JavelinParser.WhileStatementContext ctx) {
        return new WhileStatement(
                createSpan(ctx),
                (Expression) visit(ctx.expression()),
                (Statement) visit(ctx.statement())
        );
    }

    @Override
    public Node visitForeachStatement(JavelinParser.ForeachStatementContext ctx) {
        return new ForeachStatement(
                createSpan(ctx),
                (Type) visit(ctx.typeIdentifier()),
                ctx.SymbolIdentifier().getText(),
                (Expression) visit(ctx.expression()),
                (Statement) visit(ctx.statement())
        );
    }

    @Override
    public Node visitReturnStatement(JavelinParser.ReturnStatementContext ctx) {
        return new ReturnStatement(
                createSpan(ctx),
                (Expression) visit(ctx.expression())
        );
    }

    @Override
    public Node visitBreakStatement(JavelinParser.BreakStatementContext ctx) {
        return new BreakStatement(
                createSpan(ctx)
        );
    }

    @Override
    public Node visitContinueStatement(JavelinParser.ContinueStatementContext ctx) {
        return new ContinueStatement(
                createSpan(ctx)
        );
    }

    @Override
    public Node visitIfStatement(JavelinParser.IfStatementContext ctx) {
        return new IfStatement(
                createSpan(ctx),
                (Expression) visit(ctx.expression()),
                (Statement) visit(ctx.statement(0)),
                ctx.ELSE() != null ? Optional.of((Statement) visit(ctx.statement(1))) : Optional.empty()
        );
    }

    @Override
    public Node visitIntegerLiteralExpression(JavelinParser.IntegerLiteralExpressionContext ctx) {
        return new IntegerLiteralExpression(
                createSpan(ctx),
                ctx.IntegerLiteral().getText()
        );
    }

    @Override
    public Node visitStringLiteralExpression(JavelinParser.StringLiteralExpressionContext ctx) {
        return new StringLiteralExpression(
                createSpan(ctx),
                ctx.StringLiteral().getText()
        );
    }

    @Override
    public Node visitBooleanLiteralExpression(JavelinParser.BooleanLiteralExpressionContext ctx) {
        Span span = createSpan(ctx);
        boolean value = switch (ctx.BooleanLiteral().getText()) {
            case "true" -> true;
            case "false" -> false;
            default -> throw new ParserException(span, "Invalid boolean literal: " + ctx.BooleanLiteral().getText());
        };
        return new BooleanLiteralExpression(
                span,
                value
        );
    }

    @Override
    public Node visitSymbolIdentifierExpression(JavelinParser.SymbolIdentifierExpressionContext ctx) {
        return new SymbolExpression(
                createSpan(ctx),
                ctx.SymbolIdentifier().getText()
        );
    }

    @Override
    public Node visitStaticMethodCall(JavelinParser.StaticMethodCallContext ctx) {
        List<Expression> args = ctx.expression().stream()
                .map(c -> (Expression) visit(c))
                .toList();
        Optional<Type> type = Optional.ofNullable(ctx.typeIdentifier())
                .map(t -> (Type) visit(t));
        return new StaticMethodCall(
                createSpan(ctx),
                type,
                ctx.SymbolIdentifier().getText(),
                args
        );
    }

    @Override
    public Node visitInstanceMethodCall(JavelinParser.InstanceMethodCallContext ctx) {
        List<Expression> args = ctx.expression().subList(1, ctx.expression().size()).stream()
                .map(c -> (Expression) visit(c))
                .toList();
        return new InstanceMethodCall(
                createSpan(ctx),
                (Expression) visit(ctx.expression(0)),
                ctx.SymbolIdentifier().getText(),
                args
        );
    }

    @Override
    public Node visitConstructorCall(JavelinParser.ConstructorCallContext ctx) {
        List<Expression> args = ctx.expression().subList(1, ctx.expression().size()).stream()
                .map(c -> (Expression) visit(c))
                .toList();
        return new ConstructorCall(
                createSpan(ctx),
                (Type) visit(ctx.typeIdentifier()),
                args
        );
    }

    @Override
    public Node visitLeftUnaryOperationExpression(JavelinParser.LeftUnaryOperationExpressionContext ctx) {
        Span span = createSpan(ctx);
        LeftUnaryOperator operator = switch (ctx.leftUnaryOperator().getText()) {
            case "++" -> LeftUnaryOperator.INC;
            case "--" -> LeftUnaryOperator.DEC;
            case "-" -> LeftUnaryOperator.SUB;
            case "!" -> LeftUnaryOperator.NOT;
            case "#" -> LeftUnaryOperator.HASH;
            default -> throw new ParserException(span, "Unexpected value: " + ctx.leftUnaryOperator().getText());
        };
        Expression expression = (Expression) visit(ctx.expression());
        return new LeftUnaryExpression(
                span,
                operator,
                expression
        );
    }

    @Override
    public Node visitRightUnaryOperationExpression(JavelinParser.RightUnaryOperationExpressionContext ctx) {
        Span span = createSpan(ctx);
        RightUnaryOperator operator = switch (ctx.rightUnaryOperator().getText()) {
            case "++" -> RightUnaryOperator.INC;
            case "--" -> RightUnaryOperator.DEC;
            default -> throw new ParserException(span, "Unexpected value: " + ctx.rightUnaryOperator().getText());
        };
        Expression expression = (Expression) visit(ctx.expression());
        return new RightUnaryExpression(
                span,
                expression,
                operator
        );
    }

    @Override
    public Node visitBinaryOperationExpression(JavelinParser.BinaryOperationExpressionContext ctx) {
        Span span = createSpan(ctx);
        BinaryOperator operator = switch (ctx.binaryOperator().getText()) {
            case "+" -> BinaryOperator.ADD;
            case "-" -> BinaryOperator.SUBTRACT;
            case "*" -> BinaryOperator.MULTIPLY;
            case "/" -> BinaryOperator.DIVIDE;
            case "==" -> BinaryOperator.EQ;
            case "!=" -> BinaryOperator.NEQ;
            case "<" -> BinaryOperator.LT;
            case "<=" -> BinaryOperator.LTE;
            case ">" -> BinaryOperator.GT;
            case ">=" -> BinaryOperator.GTE;
            default -> throw new ParserException(span, "Unexpected value: " + ctx.binaryOperator().getText());
        };
        Expression left = (Expression) visit(ctx.expression(0));
        Expression right = (Expression) visit(ctx.expression(1));
        return new BinaryExpression(
                span,
                left,
                operator,
                right
        );
    }

    @Override
    public Node visitParenExpression(JavelinParser.ParenExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Node visitCollectionAccess(JavelinParser.CollectionAccessContext ctx) {
        return new BinaryExpression(
                createSpan(ctx),
                (Expression) visit(ctx.expression(0)),
                BinaryOperator.COLLECTION_ACCESS,
                (Expression) visit(ctx.expression(1))
        );
    }

    @Override
    public Node visitEnumValueExpression(JavelinParser.EnumValueExpressionContext ctx) {
        return new EnumValueAccess(
                createSpan(ctx),
                new SimpleType(
                        createSpan(ctx),
                        ctx.TypeIdentifier().getText()
                ),
                ctx.EnumValueIdentifier().getText()
        );
    }

    @Override
    public Node visitTypeCastExpression(JavelinParser.TypeCastExpressionContext ctx) {
        return new TypeCastExpression(
                createSpan(ctx),
                (Type) visit(ctx.typeIdentifier()),
                (Expression) visit(ctx.expression())
        );
    }

    @Override
    public Node visitSimpleTypeIdentifier(JavelinParser.SimpleTypeIdentifierContext ctx) {
        return new SimpleType(
                createSpan(ctx),
                ctx.getText()
        );
    }

    @Override
    public Node visitCollectionTypeIdentifier(JavelinParser.CollectionTypeIdentifierContext ctx) {
        return new CollectionType(
                createSpan(ctx),
                (Type) visit(ctx.typeIdentifier(1)),
                (Type) visit(ctx.typeIdentifier(0))
        );
    }

    @Override
    public Node visitGenericTypeIdentifier(JavelinParser.GenericTypeIdentifierContext ctx) {
        List<Type> typeArguments = new ArrayList<>();
        for (int i = 1; i < ctx.typeIdentifier().size(); i++)
            typeArguments.add((Type) visit(ctx.typeIdentifier(i)));
        return new GenericType(
                createSpan(ctx),
                (Type) visit(ctx.typeIdentifier(0)),
                Collections.unmodifiableList(typeArguments)
        );
    }

    @Override
    public Node visitTypeDefinition(JavelinParser.TypeDefinitionContext ctx) {
        if (ctx.classTypeDefinition() != null)
            return visit(ctx.classTypeDefinition());
        if (ctx.annotationTypeDefinition() != null)
            return visit(ctx.annotationTypeDefinition());
        if (ctx.enumTypeDefinition() != null)
            return visit(ctx.enumTypeDefinition());
        throw new ParserException(createSpan(ctx), "Unknown type definition: " + ctx.getText());
    }

    @Override
    public Node visitTypeModifier(JavelinParser.TypeModifierContext ctx) {
        if (ctx.PUB() != null)
            return new TypeModifier(createSpan(ctx), TypeModifierValue.PUB);
        if (ctx.NATIVE() != null)
            return new TypeModifier(createSpan(ctx), TypeModifierValue.NATIVE);
        throw new ParserException(createSpan(ctx), "Unknown type modifier: " + ctx.getText());
    }

    @Override
    public Node visitAnnotationTypeDefinition(JavelinParser.AnnotationTypeDefinitionContext ctx) {
        List<TypeModifier> typeModifiers = ctx.typeModifier().stream()
                .map(tmCtx -> (TypeModifier) visit(tmCtx))
                .toList();
        List<AnnotationFieldDefinition> fields = ctx.annotationFieldDefinition().stream()
                .map(afdCtx -> (AnnotationFieldDefinition) visit(afdCtx))
                .toList();
        return new AnnotationTypeDefinition(
                createSpan(ctx),
                typeModifiers,
                fields
        );
    }

    @Override
    public Node visitAnnotationFieldDefinition(JavelinParser.AnnotationFieldDefinitionContext ctx) {
        return new AnnotationFieldDefinition(
                createSpan(ctx),
                (Type) visit(ctx.typeIdentifier()),
                ctx.SymbolIdentifier().getText()
        );
    }

    @Override
    public Node visitEnumTypeDefinition(JavelinParser.EnumTypeDefinitionContext ctx) {
        List<TypeModifier> typeModifiers = ctx.typeModifier().stream()
                .map(tmCtx -> (TypeModifier) visit(tmCtx))
                .toList();
        List<EnumValueDefinition> values = ctx.EnumValueIdentifier().stream()
                .map(evCtx -> new EnumValueDefinition(
                        createSpan(evCtx.getSymbol()),
                        evCtx.getText()
                ))
                .toList();
        return new EnumTypeDefinition(
                createSpan(ctx),
                typeModifiers,
                values
        );
    }

    @Override
    public Node visitErrorNode(ErrorNode node) {
        Span span = createSpan((ParserRuleContext) node.getParent());
        throw new ParserException(span, node.getText());
    }

    private static Span createSpan(ParserRuleContext ctx) {
        return new Span(
                ctx.getStart().getLine(),
                ctx.getStart().getCharPositionInLine(),
                ctx.getStop().getLine(),
                ctx.getStop().getCharPositionInLine() + ctx.getStop().getText().length()
        );
    }

    private static Span createSpan(Token token) {
        return new Span(
                token.getLine(),
                token.getCharPositionInLine(),
                token.getLine(),
                token.getCharPositionInLine() + token.getText().length()
        );
    }

}
