import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Documento documento =  null;
        Historico historico = null;

        int opcao;

        System.out.println("================ Editor de Documentos ================");
        while (true) {
            System.out.println("1. Carregar documento");
            System.out.println("2. Criar um novo documento");
            System.out.println("3. Visualizar documento");
            System.out.println("4. Editar");
            System.out.println("5. Salvar");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = sc.nextInt();
                sc.nextLine();
            }catch (InputMismatchException e){
                System.out.println("Insira um valor valido!");
                sc.nextLine();
                continue;
            }

            switch (opcao) {
                case 1 -> {
                    System.out.print("Caminho do documento: ");
                    try{
                        documento = GerenciadorArquivo.carregarDocumento(sc.nextLine());
                        historico = new Historico(documento);
                        System.out.println("Documento carregado com sucesso!");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Título: ");
                    String titulo = sc.nextLine();

                    System.out.print("Categoria: ");
                    String categoria = sc.nextLine();

                    System.out.print("Nome do autor: ");
                    String nomeAutor = sc.nextLine();

                    System.out.println("Conteúdo (pule uma linha e digite FIM para encerrar): ");
                    StringBuilder sb = new StringBuilder();

                    String linha;

                    while(!(linha = sc.nextLine()).equalsIgnoreCase("FIM")){
                        sb.append(linha).append("\n");
                    }

                    documento = new Documento.Builder()
                            .titulo(titulo)
                            .categoria(categoria)
                            .conteudo(sb.toString().trim())
                            .autor(new Autor(nomeAutor))
                            .build();

                    String caminho = obterCaminhoArquivo();

                    if(verificarPasta(caminho)){
                        GerenciadorArquivo.salvarDocumento(documento, caminho);
                        historico = new Historico(documento);
                        System.out.println("Documento salvo com sucesso!");
                    }
                }
                case 3 -> {
                    if(possuiDocumento(documento))
                        System.out.println(documento);
                }
                case 4 -> {
                    if (possuiDocumento(documento)) {
                        System.out.println("----------- EDIÇÃO -----------");
                        System.out.println("1. Editar título");
                        System.out.println("2. Editar categoria");
                        System.out.println("3. Editar conteúdo");
                        System.out.println("4. Restaurar versão");
                        System.out.println("------------------------------");
                        System.out.print("Escolha uma opção: ");

                        opcao = sc.nextInt();
                        sc.nextLine();

                        assert historico != null;

                        if(opcao >= 1 && opcao <= 3)
                            historico.salvarVersao();

                        switch(opcao){
                            case 1 -> {
                                System.out.print("Novo título: ");
                                documento.editarTitulo(sc.nextLine());
                                System.out.println("Editado com sucesso!");
                            }
                            case 2 -> {
                                System.out.print("Nova categoria: ");
                                documento.editarCategoria(sc.nextLine());
                                System.out.println("Editado com sucesso!");
                            }
                            case 3 -> {
                                System.out.println("Novo conteúdo (pule uma linha e digite FIM para encerrar):");

                                StringBuilder sb = new StringBuilder();
                                String linha;

                                while(!(linha = sc.nextLine()).equalsIgnoreCase("FIM")){
                                    sb.append(linha).append("\n");
                                }

                                documento.editarConteudo(sb.toString().trim());
                                System.out.println("Editado com sucesso!");
                            }
                            case 4 -> {
                                if(!historico.estaVazio()){
                                    historico.desfazer();
                                    System.out.println("Documento restaurado com sucesso!");
                                }else{
                                    System.out.println("Nenhuma versão anterior do arquivo foi encontrada!");
                                }
                            }default -> System.out.println("Opção inválida!");

                        }
                    }
                }
                case 5 -> {
                    if(possuiDocumento(documento)){
                        String caminho = obterCaminhoArquivo();

                        if(verificarPasta(caminho)){
                            GerenciadorArquivo.salvarDocumento(documento, caminho);
                            System.out.println("Documento salvo com sucesso!");
                        }
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static String obterCaminhoArquivo() {
        System.out.print("Nome do arquivo: ");
        String nome = sc.nextLine();

        System.out.print("Pasta do arquivo (deixe em branco caso deseja salvar na raiz do projeto): ");
        String pasta = sc.nextLine();

        if(pasta.isBlank()){
            return nome + ".txt";
        }
        return pasta + File.separator + nome + ".txt";
    }

    private static boolean possuiDocumento(Documento documento) {
        if(documento == null){
            System.out.println("Nenhum documento foi encontrado!\nCarregue ou crie um novo documento!\n");
            return false;
        }
        return true;
    }

    private static boolean verificarPasta(String caminho) {
        File arquivo = new File(caminho);
        File pasta = arquivo.getParentFile();

        if(pasta == null || pasta.exists())
            return true;

        char resposta;

        do{
            System.out.println("A pasta não existe. Deseja criá-la? (S/N): ");
            resposta = Character.toUpperCase(sc.next().charAt(0));
            sc.nextLine();

            if(resposta == 'S'){
                if(pasta.mkdirs()){
                    System.out.println("Pasta criada com sucesso!");
                    return true;
                }
                System.out.println("Não foi possível criar a pasta.");
                return false;
            }else if (resposta == 'N'){
                System.out.println("Documento não foi salvo.");
                return false;
            }
            System.out.println("Opção inválida!");
        } while (true);
    }
}