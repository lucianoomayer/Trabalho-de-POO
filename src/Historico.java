// Historico.java
import java.util.Stack;

public class Historico {
    private final Documento documento;
    private Stack<Documento.DocumentoMemento> historico = new Stack<>();

    public Historico(Documento Documento) {
        this.documento = Documento;
    }

    public void salvarVersao(){
        historico.push(documento.criarMemento());
    }

    public void desfazer(){
        if(!historico.isEmpty())
            documento.restaurarMemento(historico.pop());
    }

    public boolean estaVazio(){
        return historico.isEmpty();
    }
}
