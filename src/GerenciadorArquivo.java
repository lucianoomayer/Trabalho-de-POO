import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GerenciadorArquivo {
    public static Documento carregarDocumento(String caminho){
        try(BufferedReader br = new BufferedReader(new FileReader(caminho))){
            String titulo = br.readLine();
            String categoria = br.readLine();
            String autor = br.readLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime dataModificacao = LocalDateTime.parse(br.readLine(), formatter);

            StringBuilder conteudo = new StringBuilder();

            String linha;

            while((linha = br.readLine()) != null){
                conteudo.append(linha).append("\n");
            }

            return new Documento.Builder()
                    .titulo(titulo)
                    .categoria(categoria)
                    .conteudo(conteudo.toString().trim())
                    .autor(new Autor(autor))
                    .dataModificacao(dataModificacao)
                    .build();
        }catch (FileNotFoundException e){
            throw new RuntimeException("Erro ao carregar o documento: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro: " + e.getMessage());
        }
    }

    public static void salvarDocumento(Documento documento, String caminho) {
        if (documento == null) {
            throw new IllegalArgumentException("Documento não pode ser nulo.");
        }

        try {
            File arquivo = new File(caminho);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
                bw.write(documento.getTitulo());
                bw.newLine();

                bw.write(documento.getCategoria());
                bw.newLine();

                bw.write(documento.getAutor().getNome());
                bw.newLine();

                bw.write(documento.getDataFormatada());
                bw.newLine();

                bw.write(documento.getConteudo());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar documento: " + e.getMessage());
        }
    }
}
