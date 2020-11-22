package tw.waterball.ddd.waber.passenger.usecases;

import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.passenger.repositories.PassengerRepository;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class PassengerUsaCase {
    protected PassengerRepository passengerRepository;

    public PassengerUsaCase(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public Passenger getPassengerById(int passengerId) {
        return passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Passenger Not Found."));
    }
}
