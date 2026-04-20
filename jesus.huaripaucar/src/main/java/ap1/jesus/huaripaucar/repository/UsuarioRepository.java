package ap1.jesus.huaripaucar.repository;

import ap1.jesus.huaripaucar.model.Usuario;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioRepository extends ReactiveMongoRepository<Usuario, String> {

    Flux<Usuario> findByEstado(String estado);

    Mono<Usuario> findByCorreo(String correo);
}