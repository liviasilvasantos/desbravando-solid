package cotuba.md;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import cotuba.domain.Capitulo;

public class RenderizadorMDParaHTML {

    public List<Capitulo> renderiza(final Path diretorioDosMD) {

        return obtemArquivosMD(diretorioDosMD)
                .stream()
                .map(arquivoMD -> {
                    Capitulo capitulo = new Capitulo();
                    Node document = parseDoMD(arquivoMD, capitulo);
                    renderizaParaHTML(arquivoMD, capitulo, document);
                    return capitulo;
                }).toList();
    }

    private void renderizaParaHTML(final Path arquivoMD, final Capitulo capitulo, final Node document) {
        try {
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String html = renderer.render(document);

            capitulo.setConteudoHTML(html);
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + arquivoMD,
                    ex);
        }
    }

    private Node parseDoMD(final Path arquivoMD, final Capitulo capitulo) {
        Parser parser = Parser.builder().build();
        Node document = null;
        try {
            document = parser.parseReader(Files.newBufferedReader(arquivoMD));
            document.accept(new AbstractVisitor() {
                @Override
                public void visit(Heading heading) {
                    if (heading.getLevel() == 1) {
                        // capítulo
                        String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                        capitulo.setTitulo(tituloDoCapitulo);
                    } else if (heading.getLevel() == 2) {
                        // seção
                    } else if (heading.getLevel() == 3) {
                        // título
                    }
                }

            });
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao fazer parse do arquivo " + arquivoMD, ex);
        }
        return document;
    }

    private List<Path> obtemArquivosMD(final Path diretorioDosMD) {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");

        try (Stream<Path> arquivosMD = Files.list(diretorioDosMD)) {
            return arquivosMD
                    .filter(matcher::matches)
                    .sorted().toList();
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Erro tentando encontrar arquivos .md em " + diretorioDosMD.toAbsolutePath(), ex);
        }
    }
}
