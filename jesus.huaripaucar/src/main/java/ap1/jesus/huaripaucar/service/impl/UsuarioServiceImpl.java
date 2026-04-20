package ap1.jesus.huaripaucar.service.impl;

import ap1.jesus.huaripaucar.model.Usuario;
import ap1.jesus.huaripaucar.repository.UsuarioRepository;
import ap1.jesus.huaripaucar.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Genera el siguiente ID correlativo con formato USR-1, USR-2, etc.
     */
    private Mono<String> generarSiguienteId() {
        return usuarioRepository.findAll()
                .map(u -> {
                    try {
                        return Integer.parseInt(u.getId().replace("USR-", ""));
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .reduce(0, Integer::max)
                .map(max -> "USR-" + (max + 1));
    }

    @Override
    public Flux<Usuario> findAll() {
        log.info("Listando todos los usuarios");
        return usuarioRepository.findAll();
    }

    @Override
    public Flux<Usuario> findActive() {
        log.info("Listando usuarios activos");
        return usuarioRepository.findByEstado("A");
    }

    @Override
    public Flux<Usuario> findInactive() {
        log.info("Listando usuarios inactivos");
        return usuarioRepository.findByEstado("I");
    }

    @Override
    public Mono<Usuario> findById(String id) {
        log.info("Buscando usuario por ID: {}", id);
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id)));
    }

    @Override
    public Mono<Usuario> save(Usuario usuario) {
        log.info("Registrando nuevo usuario: {}", usuario.getNombre());
        return generarSiguienteId()
                .flatMap(nextId -> {
                    usuario.setId(nextId);
                    usuario.setEstado("A");  // Activo por defecto al crear
                    return usuarioRepository.save(usuario);
                });
    }

    @Override
    public Mono<Usuario> update(Usuario usuario) {
        log.info("Actualizando usuario con ID: {}", usuario.getId());
        return usuarioRepository.findById(usuario.getId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + usuario.getId())))
                .flatMap(existing -> {
                    if ("I".equals(existing.getEstado())) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "No se puede actualizar un usuario inactivo (ID: " + usuario.getId() + ")"));
                    }
                    existing.setNombre(usuario.getNombre());
                    existing.setApellido(usuario.getApellido());
                    existing.setCorreo(usuario.getCorreo());
                    existing.setTelefono(usuario.getTelefono());
                    existing.setRol(usuario.getRol());
                    return usuarioRepository.save(existing);
                });
    }

    @Override
    public Mono<Usuario> delete(String id) {
        log.info("Deshabilitando (eliminado lógico) usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id)))
                .flatMap(existing -> {
                    if ("I".equals(existing.getEstado())) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "El usuario ya está inactivo (ID: " + id + ")"));
                    }
                    existing.setEstado("I");
                    return usuarioRepository.save(existing);
                });
    }

    @Override
    public Mono<Usuario> restore(String id) {
        log.info("Restaurando usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id)))
                .flatMap(existing -> {
                    if ("A".equals(existing.getEstado())) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "El usuario ya está activo (ID: " + id + ")"));
                    }
                    existing.setEstado("A");
                    return usuarioRepository.save(existing);
                });
    }
}