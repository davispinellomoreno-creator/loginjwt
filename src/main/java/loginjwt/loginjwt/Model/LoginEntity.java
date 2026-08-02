package loginjwt.loginjwt.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import javax.annotation.processing.Generated;

@Entity
@Table(name = "db_usuarios")
public class LoginEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;


    @NotNull
@Column(unique = true)
    private String password;
}
