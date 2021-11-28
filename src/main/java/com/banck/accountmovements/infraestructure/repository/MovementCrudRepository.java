package com.banck.accountmovements.infraestructure.repository;

import com.banck.accountmovements.domain.Movement;
import com.banck.accountmovements.infraestructure.model.dao.MovementDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.banck.accountmovements.aplication.model.MovementRepository;

/**
 *
 * @author jonavcar
 */
@Component
public class MovementCrudRepository implements MovementRepository {

    @Autowired
    IMovementCrudRepository movementRepository;

    @Override
    public Mono<Movement> get(String movement) {
        return movementRepository.findById(movement).map(this::MovementDaoToMovement);
    }

    @Override
    public Flux<Movement> list() {
        return movementRepository.findAll().map(this::MovementDaoToMovement);
    }

    @Override
    public Mono<Movement> create(Movement movement) {
        //Flux<MovementDao> fmd = movementRepository.findAllByCustomer(movement.getCustomer());
        return movementRepository.save(MovementToMovementDao(movement)).map(this::MovementDaoToMovement);
    }

    @Override
    public Mono<Movement> update(String movement, Movement c) {
        c.setMovement(movement);
        return movementRepository.save(MovementToMovementDao(c)).map(this::MovementDaoToMovement);
    }

    @Override
    public void delete(String movement) {
        movementRepository.deleteById(movement).subscribe();
    }

    public Movement MovementDaoToMovement(MovementDao md) {
        Movement m = new Movement();
        m.setMovement(md.getMovement());
        m.setMovementType(md.getMovementType());
        m.setAccount(md.getAccount());
        m.setTransferAccount(md.getTransferAccount());
        m.setTransferCustomer(md.getTransferCustomer());
        m.setCustomer(md.getCustomer());
        m.setAmount(md.getAmount());
        m.setConcept(md.getConcept());
        m.setObservations(md.getObservations());
        m.setDate(md.getDate());
        m.setTime(md.getTime());
        m.setCorrect(md.isCorrect());
        return m;
    }

    public MovementDao MovementToMovementDao(Movement m) {
        MovementDao md = new MovementDao();
        md.setMovement(m.getMovement());
        md.setMovementType(m.getMovementType());
        md.setAccount(m.getAccount());
        md.setTransferAccount(m.getTransferAccount());
        md.setTransferCustomer(m.getTransferCustomer());
        md.setCustomer(m.getCustomer());
        md.setAmount(m.getAmount());
        md.setConcept(m.getConcept());
        md.setObservations(m.getObservations());
        md.setDate(m.getDate());
        md.setTime(m.getTime());
        md.setCorrect(m.isCorrect());
        return md;
    }

    @Override
    public Flux<Movement> listByCustomer(String customer) {
        return movementRepository.findAllByCustomer(customer).map(this::MovementDaoToMovement);
    }

    @Override
    public Flux<Movement> listByCustomerAndAccount(String customer, String account) {
        return movementRepository.findAllByCustomerAndAccount(customer, account).map(this::MovementDaoToMovement);
    }

    @Override
    public Flux<Movement> listByCustomerAndAccountAndAccountType(String customer, String account, String accountType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Flux<Movement> listByAccount(String account) {
        return movementRepository.findAllByAccount(account).map(this::MovementDaoToMovement);
    }

}
