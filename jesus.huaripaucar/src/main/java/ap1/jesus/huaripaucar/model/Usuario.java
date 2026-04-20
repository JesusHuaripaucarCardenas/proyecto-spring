package ap1.jesus.huaripaucar.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "usuario")
public class Usuario {

    @Id
    @Field("_id")
    private String id;          // Formato: USR-1, USR-2, USR-3...

    private String nombre;      // Nombre del usuario
    private String apellido;    // Apellido del usuario
    private String correo;      // Correo electrónico
    private String telefono;    // Número de teléfono
    private String rol;         // Rol: ADMIN, USER, MODERATOR
    private String estado;      // A = Activo, I = Inactivo
}