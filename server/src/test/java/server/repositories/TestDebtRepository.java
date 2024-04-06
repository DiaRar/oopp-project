package server.repositories;

import commons.Debt;
import commons.Expense;
import commons.Participant;
import commons.primary_keys.DebtPK;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.DebtRepository;

import java.util.*;
import java.util.function.Function;

public class TestDebtRepository implements DebtRepository {
    public final List<Expense> expenses = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();
    @Override
    public Collection<Debt> findDebtsByPayerId(UUID id) {
        return null;
    }

    @Override
    public Collection<Debt> findDebtsByPayer(Participant payer) {
        return null;
    }

    @Override
    public Collection<Debt> findDebtsByDebtorId(UUID id) {
        return null;
    }

    @Override
    public Collection<Debt> findDebtsByDebtor(Participant debtor) {
        return null;
    }

    @Override
    public Collection<Debt> findDebtsByEventId(UUID id) {
        return null;
    }

    @Override
    public Integer deleteDebtById(DebtPK id) {
        return null;
    }

    @Override
    public Integer deleteDebtByEventId(UUID eventId) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Debt> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Debt> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Debt> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<DebtPK> debtPKS) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Debt getOne(DebtPK debtPK) {
        return null;
    }

    @Override
    public Debt getById(DebtPK debtPK) {
        return null;
    }

    @Override
    public Debt getReferenceById(DebtPK debtPK) {
        return null;
    }

    @Override
    public <S extends Debt> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Debt> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Debt> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Debt> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Debt> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Debt> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Debt, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Debt> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Debt> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Debt> findById(DebtPK debtPK) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(DebtPK debtPK) {
        return false;
    }

    @Override
    public List<Debt> findAll() {
        return null;
    }

    @Override
    public List<Debt> findAllById(Iterable<DebtPK> debtPKS) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(DebtPK debtPK) {

    }

    @Override
    public void delete(Debt entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends DebtPK> debtPKS) {

    }

    @Override
    public void deleteAll(Iterable<? extends Debt> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Debt> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Debt> findAll(Pageable pageable) {
        return null;
    }
}
