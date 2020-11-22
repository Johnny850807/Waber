package tw.waterball.ddd.model.user;

import lombok.Getter;
import tw.waterball.ddd.model.AggregateRoot;
import tw.waterball.ddd.model.geo.Location;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
public class User extends AggregateRoot {
    protected int id;

    @Size(min = 1, max = 10)
    protected String name;

    @Email
    protected String email;

    @NotBlank
    protected String password;

    protected Location latestLocation;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setLatestLocation(Location latestLocation) {
        this.latestLocation = latestLocation;
    }
}
