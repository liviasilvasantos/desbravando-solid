package cotuba.application;

import java.nio.file.Path;
import java.util.List;

import cotuba.domain.Capitulo;
import cotuba.domain.Ebook;
import cotuba.epub.GeradorEPUB;
import cotuba.md.RenderizadorMDParaHTML;
import cotuba.pdf.GeradorPDF;

public class Cotuba {

    public void executa(final String formato, final Path diretorioDosMD,
            final Path arquivoDeSaida) {
        var renderizador = new RenderizadorMDParaHTML();
        final List<Capitulo> capitulos = renderizador.renderiza(diretorioDosMD);

        final Ebook ebook = new Ebook();
        ebook.setFormato(formato);
        ebook.setArquivoDeSaida(arquivoDeSaida);
        ebook.setCapitulos(capitulos);

        if ("pdf".equals(formato)) {

            var geradorPDF = new GeradorPDF();
            geradorPDF.gera(ebook);

        } else if ("epub".equals(formato)) {

            var geradorEPUB = new GeradorEPUB();
            geradorEPUB.gera(ebook);

        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
        }
    }
}
