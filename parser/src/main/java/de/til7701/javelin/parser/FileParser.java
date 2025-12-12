package de.til7701.javelin.parser;

import de.til7701.javelin.ast.Ast;
import de.til7701.javelin.ast.Node;
import de.til7701.javelin.parser.internal.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.io.IOException;

@NullMarked
public class FileParser {

    public Ast parse(File file) throws IOException {
        CharStream charStream = CharStreams.fromFileName(file.getAbsolutePath());
        JavelinLexer lexer = new JavelinLexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);

        JavelinParser parser = new JavelinParser(commonTokenStream);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(new ErrorListener());

        ParseTree cst = parser.compilationUnit();
        JavelinParserBaseVisitor<Node> listener = new Walker();
        return (Ast) listener.visit(cst);
    }

}
