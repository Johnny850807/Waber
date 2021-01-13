package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

import lombok.*;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;

import javax.persistence.*;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private Driver.CarType carType;
    private double latitude;
    private double longitude;
    private boolean isDriver;

    public static UserData fromEntity(User user) {
        return user instanceof Driver ? fromEntity((Driver) user)
                : fromEntity((Passenger) user);
    }

    public static UserData fromEntity(Driver driver) {
        return UserData.builder()
                .id(driver.getId())
                .name(driver.getName())
                .email(driver.getEmail())
                .password(driver.getPassword())
                .carType(driver.getCarType())
                .isDriver(true)
                .build();
    }

    public static UserData fromEntity(Passenger passenger) {
        return UserData.builder()
                .id(passenger.getId())
                .name(passenger.getName())
                .email(passenger.getEmail())
                .password(passenger.getPassword())
                .isDriver(false)
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
