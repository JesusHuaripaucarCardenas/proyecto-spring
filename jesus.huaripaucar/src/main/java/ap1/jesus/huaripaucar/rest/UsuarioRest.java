package ap1.jesus.huaripaucar.rest;

import ap1.jesus.huaripaucar.model.Usuario;
import ap1.jesus.huaripaucar.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/api/usuario")
public class UsuarioRest {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioRest(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public Flux<Usuario> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/active")
    public Flux<Usuario> findActive() {
        return usuarioService.findActive();
    }

    @GetMapping("/inactive")
    public Flux<Usuario> findInactive() {
        return usuarioService.findInactive();
    }

    @GetMapping("/{id}")
    public Mono<Usuario> findById(@PathVariable String id) {
        return usuarioService.findById(id);
    }

    @PostMapping("/save")
    public Mono<Usuario> save(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @PutMapping("/update")
    public Mono<Usuario> update(@RequestBody Usuario usuario) {
        return usuarioService.update(usuario);
    }

    @PatchMapping("/delete/{id}")
    public Mono<Usuario> delete(@PathVariable String id) {
        return usuarioService.delete(id);
    }

    @PatchMapping("/restore/{id}")
    public Mono<Usuario> restore(@PathVariable String id) {
        return usuarioService.restore(id);
    }
}