package tw.waterball.ddd.waber.passenger.usecases;

import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.passenger.repositories.PassengerRepository;

import javax.inject.Named;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
public class CancelMatching extends PassengerUsaCase {

    public CancelMatching(PassengerRepository passengerRepository) {
        super(passengerRepository);
    }

    public void execute(int passengerId) {
        Passenger passenger = getPassengerById(passengerId);
        passenger.cancelMatching();
        passengerRepository.save(passenger);
    }

}
