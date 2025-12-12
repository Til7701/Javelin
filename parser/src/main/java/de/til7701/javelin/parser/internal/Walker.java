package de.til7701.javelin.parser.internal;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Script;
import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.expression.*;
import de.til7701.javelin.ast.statement.*;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.parser.ParserException;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.jspecify.annotations.NullMarked;

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
        return new StaticMethodCall(
                createSpan(ctx),
                Optional.of(ctx.typeIdentifier().getText()),
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

}
