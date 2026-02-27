package DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import model.Aluno;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlunoDAO {
    private EntityManager entity;

    public AlunoDAO(EntityManager entity) {
        this.entity = entity;
    }

    public void cadastrar(Aluno aluno) {

        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo.");
        }

        if (buscarPorNome(aluno.getNome()).isPresent()) {
            throw new IllegalStateException("Já existe um aluno cadastrado com esse nome.");
        }

        entity.persist(aluno);
    }

    public void remover(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }

        String jpql = "SELECT a FROM Aluno a WHERE a.nome = :n";

        try {
            Aluno aluno = entity.createQuery(jpql, Aluno.class)
                    .setParameter("n", nome)
                    .getSingleResult();

            entity.remove(aluno);

        } catch (NoResultException e) {
            throw new RuntimeException("Aluno não encontrado no banco.");
        }
    }

    public void atualizar(String nome, Aluno alunoAtualizado) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }

        if (alunoAtualizado == null) {
            throw new IllegalArgumentException("Dados para atualização não podem ser nulos.");
        }

        String query = "SELECT a FROM Aluno a WHERE a.nome = :n";

        Aluno aluno;

        try {
            aluno = entity.createQuery(query, Aluno.class)
                    .setParameter("n", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new IllegalStateException("Aluno não encontrado. Atualização cancelada.");
        }

        aluno.setNome(alunoAtualizado.getNome());
        aluno.setRa(alunoAtualizado.getRa());
        aluno.setEmail(alunoAtualizado.getEmail());
        aluno.setNota1(alunoAtualizado.getNota1());
        aluno.setNota2(alunoAtualizado.getNota2());
        aluno.setNota3(alunoAtualizado.getNota3());
    }

    public Optional<Aluno> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }

        String query = "SELECT a FROM Aluno a WHERE a.nome = :n";

        try {
            Aluno aluno = entity.createQuery(query, Aluno.class)
                    .setParameter("n", nome)
                    .getSingleResult();

            return Optional.of(aluno);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Aluno> listarTodos() {
        return entity.createQuery("SELECT a FROM Aluno a", Aluno.class)
                .getResultList();
    }
}
