package cotuba;

import java.nio.file.Path;

public class Main {

  public static void main(String[] args) {

    System.out.println("cotuba nova versão");

    Path diretorioDosMD;
    String formato;
    Path arquivoDeSaida;
    boolean modoVerboso = false;

    try {

      var opcoesCLI = new LeitorOpcoesCLI(args);

      diretorioDosMD = opcoesCLI.getDiretorioDosMD();
      formato = opcoesCLI.getFormato();
      arquivoDeSaida = opcoesCLI.getArquivoDeSaida();
      modoVerboso = opcoesCLI.getModoVerboso();

      if ("pdf".equals(formato)) {

        var geradorPDF = new GeradorPDF();
        geradorPDF.gera(diretorioDosMD, arquivoDeSaida);

      } else if ("epub".equals(formato)) {

        var geradorEPUB = new GeradorEPUB();
        geradorEPUB.gera(diretorioDosMD, arquivoDeSaida);

      } else {
        throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
      }

      System.out.println("Arquivo gerado com sucesso: " + arquivoDeSaida);

    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      if (modoVerboso) {
        ex.printStackTrace();
      }
      System.exit(1);
    }
  }

}
