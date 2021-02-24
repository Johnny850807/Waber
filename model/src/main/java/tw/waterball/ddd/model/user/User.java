package tw.waterball.ddd.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tw.waterball.ddd.model.base.AggregateRoot;
import tw.waterball.ddd.model.geo.Location;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
@NoArgsConstructor
public abstract class User extends AggregateRoot<Integer> {

    protected String type;

    @Size(min = 1, max = 10)
    protected String name;

    @Email
    protected String email;

    @NotBlank
    protected String password;

    protected Location location;

    public User(String type, int id) {
        this.type = type;
        this.id = id;
    }

    public User(String type, int id, String name, String email, String password, Location location) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
    }

    public User(String type, String name, String email, String password) {
        this.type = type;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
