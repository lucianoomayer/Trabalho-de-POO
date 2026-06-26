import java.io.Serializable;
import java.time.LocalDateTime;

public class Documento {
    private String titulo;
    private String conteudo;
    private String categoria;

    private Documento(Builder builder) {
        this.titulo = builder.titulo;
        this.conteudo = builder.conteudo;
        this.categoria = builder.categoria;
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

    public void editarTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void editarConteudo(String novoConteudo) {
        this.conteudo = novoConteudo;
    }

    public void editarCategoria(String categoria) {
        this.categoria = categoria;
    }

    public DocumentoMemento criarMemento() {
        return new DocumentoMemento(
                titulo,
                conteudo,
                categoria);
    }

    public void restaurarMemento(DocumentoMemento memento) {
        this.titulo = memento.getTitulo();
        this.conteudo = memento.getConteudo();
        this.categoria = memento.getCategoria();
    }

    private String formatarTexto(String texto, int largura) {
        StringBuilder resultado = new StringBuilder();

        String[] palavras = texto.split(" ");

        int tamanhoLinha = 0;

        for (String palavra : palavras) {

            if (tamanhoLinha + palavra.length() + 1 > largura) {
                resultado.append("\n");
                tamanhoLinha = 0;
            }

            resultado.append(palavra).append(" ");
            tamanhoLinha += palavra.length() + 1;
        }
        return resultado.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("==================================================\n");
        sb.append("                    DOCUMENTO\n");
        sb.append("==================================================\n");

        sb.append(String.format("Título:    %s%n", titulo));
        sb.append(String.format("Categoria: %s%n", categoria));

        sb.append("--------------------------------------------------\n");
        sb.append("Conteúdo:\n");
        sb.append(formatarTexto(conteudo, 50));
        sb.append("\n");
        sb.append("==================================================\n");

        return sb.toString();
    }

    public static class Builder {
        private String titulo;
        private String conteudo;
        private String categoria;

        public Builder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public Builder conteudo(String conteudo) {
            this.conteudo = conteudo;
            return this;
        }

        public Builder categoria(String categoria) {
            this.categoria = categoria;
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
        private final String conteudo;
        private final String categoria;
        private final LocalDateTime dataCriacao;

        private DocumentoMemento(String titulo, String conteudo, String categoria) {
            this.titulo = titulo;
            this.conteudo = conteudo;
            this.categoria = categoria;
            this.dataCriacao = LocalDateTime.now();
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

        public LocalDateTime getDataCriacao() { return dataCriacao; }

        @Override
        public String toString() {
            return "Título: " + titulo +
                    "\nCategoria: " + categoria +
                    "\nConteúdo: " + conteudo +
                    "\nData: " + dataCriacao;
        }
    }
}
