package de.til7701.continu_num.core.parser;

import de.holube.continu_num.parser.ContinuNumParser;
import de.holube.continu_num.parser.ContinuNumParserBaseVisitor;
import de.til7701.continu_num.core.ast.*;
import de.til7701.continu_num.core.reflect.Type;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import picocli.CommandLine;

import java.util.List;
import java.util.Optional;

public class Walker extends ContinuNumParserBaseVisitor<Node> {

    @Override
    public ContinuNumFile visitCompilationUnit(ContinuNumParser.CompilationUnitContext ctx) {
        CommandLine.tracer().debug("Visiting CompilationUnit %s", ctx.getText());

        List<Instruction> instructions = ctx.statement().stream()
                .map(statementContext -> (Instruction) visit(statementContext))
                .toList();

        return new ContinuNumFile(
                instructions
        );
    }

    @Override
    public Node visitStatement(ContinuNumParser.StatementContext ctx) {
        CommandLine.tracer().debug("Visiting Statement %s", ctx.getText());

        if (ctx.methodCall() != null) {
            return visit(ctx.methodCall());
        } else if (ctx.symbolDefinition() != null) {
            return visit(ctx.symbolDefinition());
        } else if (ctx.assignment() != null) {
            return visit(ctx.assignment());
        } else {
            throw new ParserException("Unknown statement type: " + ctx.getText());
        }
    }

    @Override
    public Node visitSymbolDefinition(ContinuNumParser.SymbolDefinitionContext ctx) {
        CommandLine.tracer().debug("Visiting SymbolDefinition %s", ctx.getText());

        boolean mutable = ctx.MUT() != null;
        Type type = Type.fromName(ctx.TypeIdentifier().getText());
        String name = ctx.SymbolIdentifier().getText();

        Expression expression = (Expression) visit(ctx.expression());

        return new SymbolInitialization(
                mutable,
                type,
                name,
                expression
        );
    }

    @Override
    public Node visitMethodCall(ContinuNumParser.MethodCallContext ctx) {
        CommandLine.tracer().debug("Visiting MethodCall %s", ctx.getText());

        Optional<String> typeName = Optional.ofNullable(ctx.TypeIdentifier()).map(ParseTree::getText);
        String methodName = ctx.SymbolIdentifier().getText();
        List<Expression> arguments = ctx.expression().stream()
                .map(expressionContext -> (Expression) visit(expressionContext))
                .toList();

        return new MethodCall(typeName, methodName, arguments);
    }

    @Override
    public Node visitExpression(ContinuNumParser.ExpressionContext ctx) {
        CommandLine.tracer().debug("Visiting Expression %s", ctx.getText());

        if (ctx.literalExpression() != null) {
            return visit(ctx.literalExpression());
        } else if (ctx.symbolIdentifierExpression() != null) {
            return visit(ctx.symbolIdentifierExpression());
        } else if (ctx.methodCall() != null) {
            return visit(ctx.methodCall());
        } else if (ctx.expression() != null) {
            if (ctx.expression().size() == 1) {
                return visit(ctx.expression(0));
            } else if (ctx.expression().size() == 2) {
                Expression left = (Expression) visit(ctx.expression(0));
                Expression right = (Expression) visit(ctx.expression(1));
                String operator = ctx.getChild(1).getText();
                return new BinaryExpression(
                        left,
                        BinaryOperator.fromString(operator),
                        right
                );
            } else {
                throw new ParserException("Unsupported expression format: " + ctx.getText());
            }
        } else {
            throw new ParserException("Unknown expression type: " + ctx.getText());
        }
    }

    @Override
    public Node visitLiteralExpression(ContinuNumParser.LiteralExpressionContext ctx) {
        CommandLine.tracer().debug("Visiting LiteralExpression %s", ctx.getText());

        if (ctx.StringLiteral() != null) {
            String value = ctx.StringLiteral().getText();
            value = value.substring(1, value.length() - 1);
            return new StringLiteralExpression(value);
        } else if (ctx.BooleanLiteral() != null) {
            String boolText = ctx.BooleanLiteral().getText();
            boolean value = switch (boolText) {
                case "true" -> true;
                case "false" -> false;
                default -> throw new ParserException("Invalid boolean literal: " + boolText);
            };
            return new BooleanLiteralExpression(value);
        }
        if (ctx.IntegerLiteral() != null) {
            String value = ctx.IntegerLiteral().getText();
            return new IntegerLiteralExpression(value);
        } else {
            throw new ParserException("Unknown literal type: " + ctx.getText());
        }
    }

    @Override
    public Node visitSymbolIdentifierExpression(ContinuNumParser.SymbolIdentifierExpressionContext ctx) {
        CommandLine.tracer().debug("Visiting SymbolIdentifierExpression %s", ctx.getText());

        String name = ctx.SymbolIdentifier().getText();
        return new SymbolExpression(name);
    }

    @Override
    public Node visitAssignment(ContinuNumParser.AssignmentContext ctx) {
        CommandLine.tracer().debug("Visiting Assignment %s", ctx.getText());

        String name = ctx.SymbolIdentifier().getText();
        Expression value = (Expression) visit(ctx.expression());

        return new Assignment(name, value);
    }

    @Override
    public Node visitErrorNode(ErrorNode node) {
        CommandLine.tracer().debug("Visiting ErrorNode %s", node.getText());

        throw new ParserException(node.getText());
    }

}
