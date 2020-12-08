package tw.waterball.ddd.waber.springboot.user.repositories.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Integer id;
    public String name;
    public String email;
    public String password;
    public Driver.CarType carType;
    public double latitude;
    public double longitude;
    public boolean isDriver;

    public static Driver toDriver(UserData data) {
        return new Driver(data.id,
                data.name, data.email, data.password, data.carType);
    }

    public static Passenger toPassenger(UserData data) {
        return new Passenger(data.id, data.name, data.email, data.password);
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
}
