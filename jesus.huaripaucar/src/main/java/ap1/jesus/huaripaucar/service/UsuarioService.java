package ap1.jesus.huaripaucar.service;

import ap1.jesus.huaripaucar.model.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioService {

    Flux<Usuario> findAll();

    Flux<Usuario> findActive();

    Flux<Usuario> findInactive();

    Mono<Usuario> findById(String id);

    Mono<Usuario> save(Usuario usuario);

    Mono<Usuario> update(Usuario usuario);

    Mono<Usuario> delete(String id);

    Mono<Usuario> restore(String id);
}