package de.til7701.continu_num.core.parser;

import de.holube.continu_num.parser.ContinuNumLexer;
import de.holube.continu_num.parser.ContinuNumParser;
import de.holube.continu_num.parser.ContinuNumParserBaseVisitor;
import de.til7701.continu_num.core.ast.ContinuNumFile;
import de.til7701.continu_num.core.ast.Node;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.IOException;

public class FileParser {

    public ContinuNumFile parse(File file) throws IOException {
        CharStream charStream = CharStreams.fromFileName(file.getAbsolutePath());
        ContinuNumLexer lexer = new ContinuNumLexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);

        ContinuNumParser parser = new ContinuNumParser(commonTokenStream);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(new ErrorListener());

        ParseTree cst = parser.compilationUnit();
        ContinuNumParserBaseVisitor<Node> listener = new Walker();
        return (ContinuNumFile) listener.visit(cst);
    }

}
