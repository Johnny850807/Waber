package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

import lombok.*;
import org.springframework.lang.Nullable;
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
    private Driver.CarType carType;
    private double latitude;
    private double longitude;
    private boolean driver;
    private String driverStatus;

    public static UserData toData(User user) {
        return user instanceof Driver ? toData((Driver) user)
                : toData((Passenger) user);
    }

    public static UserData toData(Driver driver) {
        return UserData.builder()
                .id(driver.getId())
                .name(driver.getName())
                .email(driver.getEmail())
                .carType(driver.getCarType())
                .driverStatus(driver.getStatus().toString())
                .driver(true)
                .build();
    }

    public static UserData toData(Passenger passenger) {
        return UserData.builder()
                .id(passenger.getId())
                .name(passenger.getName())
                .email(passenger.getEmail())
                .driver(false)
                .build();
    }

    public User toEntity() {
        return isDriver() ? toDriver() : toPassenger();
    }

    public Driver toDriver() {
        return new Driver(getId(),
                getName(), getEmail(), getCarType(),
                new Location(getLatitude(), getLongitude()),
                Driver.Status.valueOf(driverStatus));
    }

    public Passenger toPassenger() {
        return new Passenger(getId(), getName(), getEmail(), new Location(getLatitude(), getLongitude()));
    }

}
