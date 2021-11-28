package com.banck.accountmovements.aplication;

import com.banck.accountmovements.domain.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jonavcar
 */
public interface MovementOperations {

    public Flux<Movement> list();

    public Flux<Movement> listByCustomer(String customer);

    public Flux<Movement> listByAccount(String customer);

    public Flux<Movement> listByCustomerAndAccount(String customer, String account);

    public Flux<Movement> listByCustomerAndAccountAndAccountType(String customer, String account, String accountType);

    public Mono<Movement> get(String movement);

    public Mono<Movement> create(Movement movement);

    public Mono<Movement> update(String id, Movement movement);

    public void delete(String id);

}
