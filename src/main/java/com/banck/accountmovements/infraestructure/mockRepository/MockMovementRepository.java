package com.banck.accountmovements.infraestructure.mockRepository;

import com.banck.accountmovements.domain.Movement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.banck.accountmovements.aplication.model.MovementRepository;

/**
 *
 * @author jonavcar
 */
@Component
public class MockMovementRepository implements MovementRepository {

    @Override
    public Mono<Movement> get(String credito) {
        Movement c = new Movement();
        c.setAccount("34984545");
        c.setCustomer("CTP");
        return Mono.just(c);
    }

    @Override
    public Flux<Movement> list() {
        List<Movement> lc = new ArrayList<>();
        Movement c = new Movement();
        c.setAccount("34984545");
        c.setCustomer("CTP");
        lc.add(c);
        return Flux.fromIterable(lc);
    }

    @Override
    public Mono<Movement> create(Movement c) {
        return Mono.just(c);
    }

    @Override
    public Mono<Movement> update(String credito, Movement c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String dniRuc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Flux<Movement> listByCustomer(String customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Flux<Movement> listByCustomerAndAccount(String customer, String account) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Flux<Movement> listByCustomerAndAccountAndAccountType(String customer, String account, String accountType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Flux<Movement> listByAccount(String account) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
