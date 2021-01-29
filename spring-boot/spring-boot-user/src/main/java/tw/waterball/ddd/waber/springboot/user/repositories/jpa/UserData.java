package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

import lombok.*;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Table(name = "user")
@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private Driver.CarType carType;
    private double latitude;
    private double longitude;
    private boolean driver;

    public static UserData toData(User user) {
        return user instanceof Driver ? toData((Driver) user)
                : toData((Passenger) user);
    }

    public static UserData toData(Driver driver) {
        return UserData.builder()
                .id(driver.getId())
                .name(driver.getName())
                .email(driver.getEmail())
                .password(driver.getPassword())
                .carType(driver.getCarType())
                .driver(true)
                .build();
    }

    public static UserData toData(Passenger passenger) {
        return UserData.builder()
                .id(passenger.getId())
                .name(passenger.getName())
                .email(passenger.getEmail())
                .password(passenger.getPassword())
                .driver(false)
                .build();
    }

    public User toEntity() {
        return isDriver() ? toDriver() : toPassenger();
    }

    public Driver toDriver() {
        return new Driver(getId(),
                getName(), getEmail(), getPassword(), getCarType(),
                new Location(getLatitude(), getLongitude()));
    }

    public Passenger toPassenger() {
        return new Passenger(getId(), getName(), getEmail(), getPassword(), new Location(getLatitude(), getLongitude()));
    }

}
