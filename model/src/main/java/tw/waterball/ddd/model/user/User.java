package tw.waterball.ddd.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tw.waterball.ddd.model.base.AggregateRoot;
import tw.waterball.ddd.model.geo.Location;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Setter
@Getter
@NoArgsConstructor
public class User extends AggregateRoot<Integer> {

    @Size(min = 1, max = 10)
    protected String name;

    @Email
    protected String email;

    @NotBlank
    protected String password;

    protected Location location;

    public User(int id) {
        this.id = id;
    }

    public User(int id, String name, String email, String password, Location location) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
