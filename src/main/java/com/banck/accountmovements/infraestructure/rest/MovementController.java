package com.banck.accountmovements.infraestructure.rest;

import com.banck.accountmovements.aplication.AccountOperations;
import com.banck.accountmovements.domain.Movement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import com.banck.accountmovements.aplication.MovementOperations;
import com.banck.accountmovements.utils.AccountType;
import com.banck.accountmovements.utils.Concept;
import com.banck.accountmovements.utils.MovementType;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author jonavcar
 */
@RestController
@RequestMapping("/account-movement")
@RequiredArgsConstructor
public class MovementController {

    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("America/Bogota"));
    private final MovementOperations operations;
    private final AccountOperations accountOperations;

    @GetMapping
    public Flux<Movement> listAll() {
        return operations.list();
    }

    @GetMapping("/{id}")
    public Mono<Movement> get(@PathVariable("id") String id) {
        return operations.get(id);
    }

    @GetMapping("/customer/{id}/list")
    public Flux<Movement> listByCustomer(@PathVariable("id") String id) {
        return operations.listByCustomer(id);
    }

    @GetMapping("/account/{id}/list")
    public Flux<Movement> listByAccount(@PathVariable("id") String id) {
        return operations.listByAccount(id);
    }

    @GetMapping("/customer-account/{customer}/{account}/list")
    public Flux<Movement> listByCustomerAndAccount(@PathVariable("customer") String customer, @PathVariable("account") String account) {
        return operations.listByCustomerAndAccount(customer, account);
    }

    @PostMapping
    public Mono<ResponseEntity> create(@RequestBody Movement c) {
        c.setMovement(c.getCustomer() + "-" + getRandomNumberString());
        c.setDate(dateTime.format(formatDate));
        c.setTime(dateTime.format(formatTime));
        c.setCorrect(true);
        return Mono.just(c).flatMap(o -> {

            return operations.listByCustomerAndAccount(c.getCustomer(), c.getAccount()).collect(Collectors.summingDouble(Movement::getAmount)).flatMap(r -> {

                boolean isMovementType = false;
                for (MovementType tc : MovementType.values()) {
                    if (c.getMovementType().equals(tc.value)) {
                        isMovementType = true;
                    }
                }

                if (!isMovementType) {
                    return Mono.just(ResponseEntity.ok("El codigo de Tipo Movimiento (" + c.getMovementType() + "), no existe!"));
                }

                if ((r + c.getAmount()) < 0) {
                    return Mono.just(ResponseEntity.ok("El movimiento a efectuar sobrepasa el saldo disponible."));
                } else {
                    return operations.create(c).flatMap(i -> {
                        return Mono.just(ResponseEntity.ok(i));
                    });
                }
            });
        });
    }

    @PostMapping("/transfer/other-account")
    public Mono<ResponseEntity> transferOtherAccounts(@RequestBody Movement rqMovement) {
        rqMovement.setMovement(rqMovement.getCustomer() + "-" + getRandomNumberString());
        rqMovement.setDate(dateTime.format(formatDate));
        rqMovement.setTime(dateTime.format(formatTime));
        rqMovement.setCorrect(true);
        return Mono.just(rqMovement).flatMap(movement -> {

            if (Optional.ofNullable(movement.getCustomer()).isEmpty()) {
                return Mono.just(ResponseEntity.ok("Debe ingresar su Identificacion, Ejemplo: { \"customer\": \"78345212\" }"));
            }

            if (Optional.ofNullable(movement.getAccount()).isEmpty()) {
                return Mono.just(ResponseEntity.ok("Debe ingresar la cuenta de CARGO, Ejemplo: { \"account\": \"78345212-653\" }"));
            }

            if (Optional.ofNullable(movement.getTransferAccount()).isEmpty()) {
                return Mono.just(ResponseEntity.ok("Debe ingresar la cuenta de ABONO, Ejemplo: { \"transferAccount\": \"78345212-653\" }"));
            }

            if (Optional.ofNullable(movement.getTransferCustomer()).isEmpty()) {
                return Mono.just(ResponseEntity.ok("Debe ingresar Identificacion Beneficiario, Ejemplo: { \"transferCustomer\": \"78345212\" }"));
            }

            if (Optional.ofNullable(movement.getAmount()).isEmpty() || movement.getAmount() == 0) {
                return Mono.just(ResponseEntity.ok("Debe ingresar el monto diferente de cero, Ejemplo: { \"amount\": \"300.50\" }"));
            }

            if (movement.getAmount() > 0) {
                movement.setAmount(-1 * movement.getAmount());
            }
            movement.setObservations("Tranferncia bancaria otras cuentas");

            return operations.listByAccount(movement.getAccount()).collect(Collectors.summingDouble(ui -> ui.getAmount())).flatMap(balance -> {
                if ((balance + movement.getAmount()) < 0) {
                    return Mono.just(ResponseEntity.ok("El movimiento a efectuar sobrepasa el saldo disponible."));
                } else {
                    movement.setMovementType(MovementType.CHARGE.value);
                    movement.setConcept(Concept.TRANSFER.value);
                    return operations.create(movement).flatMap(mCG -> {
                        movement.setMovementType(MovementType.PAYMENT.value);
                        movement.setConcept(Concept.TRANSFER.value);
                        movement.setAccount(movement.getTransferAccount());
                        movement.setCustomer(movement.getTransferCustomer());
                        movement.setTransferAccount(movement.getAccount());
                        movement.setTransferCustomer(movement.getCustomer());
                        if (movement.getAmount() < 0) {
                            movement.setAmount(-1 * movement.getAmount());
                        }
                        return operations.create(movement).flatMap(mAB -> {
                            return Mono.just(ResponseEntity.ok(mCG));
                        });
                    });
                }
            });
        });
    }

    @PostMapping("/transfer/my-account")
    public Mono<ResponseEntity> transferMyAccounts(@RequestBody Movement rqMovement) {
        rqMovement.setMovement(rqMovement.getCustomer() + "-" + getRandomNumberString());
        rqMovement.setDate(dateTime.format(formatDate));
        rqMovement.setTime(dateTime.format(formatTime));
        rqMovement.setCorrect(true);
        return Mono.just(rqMovement).flatMap(movement -> {

            if (Optional.ofNullable(movement.getCustomer()).isEmpty()) {
                return Mono.just(ResponseEntity.ok("Debe ingresar su Identificacion, Ejemplo: { \"customer\": \"78345212\" }"));
            }

            if (Optional.ofNullable(movement.getAccount()).isEmpty()) {
                return Mono.just(ResponseEntity.ok("Debe ingresar la cuenta de CARGO, Ejemplo: { \"account\": \"78345212-653\" }"));
            }

            if (Optional.ofNullable(movement.getTransferAccount()).isEmpty()) {
                return Mono.just(ResponseEntity.ok("Debe ingresar la cuenta de ABONO, Ejemplo: { \"transferAccount\": \"78345212-653\" }"));
            }
            if (Optional.ofNullable(movement.getAmount()).isEmpty() || movement.getAmount() == 0) {
                return Mono.just(ResponseEntity.ok("Debe ingresar el monto diferente de cero, Ejemplo: { \"amount\": \"300.50\" }"));
            }

            if (movement.getAmount() > 0) {
                movement.setAmount(-1 * movement.getAmount());
            }
            movement.setObservations("Tranferncia bancaria mis cuentas");

            return operations.listByAccount(movement.getAccount()).collect(Collectors.summingDouble(ui -> ui.getAmount())).flatMap(balance -> {
                if ((balance + movement.getAmount()) < 0) {
                    return Mono.just(ResponseEntity.ok("El movimiento a efectuar sobrepasa el saldo disponible."));
                } else {
                    movement.setMovementType(MovementType.CHARGE.value);
                    movement.setConcept(Concept.TRANSFER.value);
                    return operations.create(movement).flatMap(mCG -> {
                        movement.setMovementType(MovementType.PAYMENT.value);
                        movement.setConcept(Concept.TRANSFER.value);
                        movement.setTransferCustomer(movement.getCustomer());
                        if (movement.getAmount() < 0) {
                            movement.setAmount(-1 * movement.getAmount());
                        }
                        return operations.create(movement).flatMap(mAB -> {
                            return Mono.just(ResponseEntity.ok(mCG));
                        });
                    });
                }
            });
        });
    }

    @PutMapping("/{id}")
    public Mono<Movement> update(@PathVariable("id") String id, @RequestBody Movement movement) {
        return operations.update(id, movement);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        operations.delete(id);
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
