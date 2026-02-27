//Membros da Equipe:
//-Vinicius de Brito
//-Pedro Moraes de Carvalho

import DAO.AlunoDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import model.Aluno;
import utils.JavaUtils;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    private static EntityManager entityManager;
    private static AlunoDAO alunoDAO;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        entityManager = JavaUtils.getEntityManager();
        alunoDAO = new AlunoDAO(entityManager);

        int opcao;

        do {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarAluno();
                case 2 -> excluirAluno();
                case 3 -> alterarAluno();
                case 4 -> buscarAluno();
                case 5 -> listarAlunos();
                case 6 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 6);

        entityManager.close();
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n===== CADASTRO DE ALUNOS =====");
        System.out.println("1 - Cadastrar aluno");
        System.out.println("2 - Excluir aluno");
        System.out.println("3 - Alterar aluno");
        System.out.println("4 - Buscar aluno pelo nome");
        System.out.println("5 - Listar alunos (com status)");
        System.out.println("6 - FIM");
        System.out.print("Escolha: ");
    }

    private static void cadastrarAluno() {
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("RA: ");
            String ra = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Nota 1: ");
            BigDecimal n1 = scanner.nextBigDecimal();

            System.out.print("Nota 2: ");
            BigDecimal n2 = scanner.nextBigDecimal();

            System.out.print("Nota 3: ");
            BigDecimal n3 = scanner.nextBigDecimal();
            scanner.nextLine();

            Aluno aluno = new Aluno(nome, ra, email, n1, n2, n3);

            entityManager.getTransaction().begin();
            alunoDAO.cadastrar(aluno);
            entityManager.getTransaction().commit();

            System.out.println("Aluno cadastrado com sucesso!");

        } catch (Exception e) {
            rollback();
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private static void excluirAluno() {
        try {
            System.out.print("Nome do aluno para excluir: ");
            String nome = scanner.nextLine();

            entityManager.getTransaction().begin();
            alunoDAO.remover(nome);
            entityManager.getTransaction().commit();

            System.out.println("Aluno removido!");

        } catch (Exception e) {
            rollback();
            System.out.println("Erro ao excluir: " + e.getMessage());
        }
    }

    private static void alterarAluno() {
        try {
            System.out.print("Nome do aluno para alterar: ");
            String nome = scanner.nextLine();

            if (alunoDAO.buscarPorNome(nome).isEmpty()) {
                System.out.println("Aluno não encontrado. Atualização cancelada.");
                return;
            }

            System.out.print("Novo nome: ");
            String novoNome = scanner.nextLine();

            System.out.print("Novo RA: ");
            String novoRa = scanner.nextLine();

            System.out.print("Novo Email: ");
            String novoEmail = scanner.nextLine();

            System.out.print("Nova Nota 1: ");
            BigDecimal n1 = scanner.nextBigDecimal();

            System.out.print("Nova Nota 2: ");
            BigDecimal n2 = scanner.nextBigDecimal();

            System.out.print("Nova Nota 3: ");
            BigDecimal n3 = scanner.nextBigDecimal();
            scanner.nextLine();

            Aluno alunoAtualizado =
                    new Aluno(novoNome, novoRa, novoEmail, n1, n2, n3);

            entityManager.getTransaction().begin();
            alunoDAO.atualizar(nome, alunoAtualizado);
            entityManager.getTransaction().commit();

            System.out.println("Aluno atualizado!");

        } catch (Exception e) {
            rollback();
            System.out.println("Erro ao alterar: " + e.getMessage());
        }
    }

    private static void buscarAluno() {
        System.out.print("Nome do aluno: ");
        String nome = scanner.nextLine();

        alunoDAO.buscarPorNome(nome)
                .ifPresentOrElse(
                        aluno -> {
                            System.out.println("Nome: " + aluno.getNome());
                            System.out.println("RA: " + aluno.getRa());
                            System.out.println("Email: " + aluno.getEmail());
                            System.out.println("Média: " + aluno.getAvg());
                            System.out.println("Status: " + aluno.getSituation());
                        },
                        () -> System.out.println("Aluno não encontrado.")
                );
    }

    private static void listarAlunos() {
        var lista = alunoDAO.listarTodos();

        if (lista.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }

        lista.forEach(a ->
                System.out.println(
                        "Nome: " + a.getNome() +
                                " | Média: " + a.getAvg() +
                                " | Status: " + a.getSituation()
                )
        );
    }

    private static void rollback() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }
}
