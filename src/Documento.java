import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Documento implements Editavel{
    private String titulo;
    private Autor autor;
    private String conteudo;
    private String categoria;
    private LocalDateTime dataModificacao;

    private Documento(Builder builder) {
        this.titulo = builder.titulo;
        this.conteudo = builder.conteudo;
        this.categoria = builder.categoria;
        this.dataModificacao = builder.dataModificacao != null ? builder.dataModificacao : LocalDateTime.now();
        this.autor = builder.autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDateTime getDataModificacao() {
        return dataModificacao;
    }

    public String getDataFormatada() {
        return dataModificacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public Autor getAutor() {
        return autor;
    }

    public void editarTitulo(String titulo) {
        this.titulo = titulo;
        atualizarDataModificacao();
    }

    public void editarConteudo(String novoConteudo) {
        this.conteudo = novoConteudo;
        atualizarDataModificacao();
    }

    public void editarCategoria(String categoria) {
        this.categoria = categoria;
        atualizarDataModificacao();
    }

    public DocumentoMemento criarMemento() {
        return new DocumentoMemento(
                titulo,
                autor,
                conteudo,
                categoria,
                dataModificacao);
    }

    public void restaurarMemento(DocumentoMemento memento) {
        this.titulo = memento.getTitulo();
        this.autor = memento.getAutor();
        this.conteudo = memento.getConteudo();
        this.categoria = memento.getCategoria();
        this.dataModificacao = memento.getDataModificacao();
    }

    private String formatarTexto(String texto, int largura) {
        StringBuilder resultado = new StringBuilder();
        String[] linhas = texto.split("\n");

        for (String linha : linhas) {
            String[] palavras = linha.split(" ");
            int tamanhoLinha = 0;

            for (String palavra : palavras) {
                if (tamanhoLinha + palavra.length() + 1 > largura) {
                    resultado.append("\n");
                    tamanhoLinha = 0;
                }
                resultado.append(palavra).append(" ");
                tamanhoLinha += palavra.length() + 1;
            }
            resultado.append("\n");
        }
        return resultado.toString().trim();
    }

    private void atualizarDataModificacao() {
        this.dataModificacao = LocalDateTime.now();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("==================================================\n");
        sb.append("                    DOCUMENTO\n");
        sb.append("==================================================\n");

        sb.append(String.format("Título:    %s%n", getTitulo()));
        sb.append(String.format("Categoria: %s%n", getCategoria()));
        sb.append(String.format("Autor:     %s%n", getAutor().getNome()));
        sb.append(String.format("Data:      %s%n", getDataFormatada()));

        sb.append("--------------------------------------------------\n");
        sb.append("Conteúdo:\n");
        sb.append(formatarTexto(getConteudo(), 50));
        sb.append("\n");
        sb.append("==================================================\n");

        return sb.toString();
    }

    public static class Builder {
        private String titulo;
        private String conteudo;
        private String categoria;
        private Autor autor;
        private LocalDateTime dataModificacao;

        public Builder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public Builder conteudo(String conteudo) {
            if (conteudo == null)
                conteudo = "";

            this.conteudo = conteudo;
            return this;
        }

        public Builder categoria(String categoria) {
            if (categoria == null)
                categoria = "";

            this.categoria = categoria;
            return this;
        }

        public Builder autor(Autor autor) {
            this.autor = autor;
            return this;
        }

        public Builder dataModificacao(LocalDateTime dataModificacao) {
            this.dataModificacao = dataModificacao;
            return this;
        }

        public Documento build() {
            if(titulo == null || titulo.isBlank())
                throw new IllegalArgumentException("Título obrigatório");
            return new Documento(this);
        }
    }

    public static class DocumentoMemento implements Serializable {
        private final String titulo;
        private final Autor autor;
        private final String conteudo;
        private final String categoria;
        private final LocalDateTime dataModificacao;

        private DocumentoMemento(String titulo, Autor autor, String conteudo, String categoria, LocalDateTime dataModificacao) {
            this.titulo = titulo;
            this.autor = autor;
            this.conteudo = conteudo;
            this.categoria = categoria;
            this.dataModificacao = dataModificacao;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getConteudo() {
            return conteudo;
        }

        public String getCategoria() {
            return categoria;
        }

        public LocalDateTime getDataModificacao() { return dataModificacao; }

        public Autor getAutor() { return autor; }

        @Override
        public String toString() {
            return "Título: " + getTitulo() +
                    "\nCategoria: " + getCategoria() +
                    "\nAutor: " + getAutor() +
                    "\nData: " + getDataModificacao() +
                    "\nConteúdo: " + getConteudo();

        }
    }
}
