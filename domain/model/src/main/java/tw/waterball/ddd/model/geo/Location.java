package tw.waterball.ddd.model.geo;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private double latitude;
    private double longitude;

    public double distance(Location location) {
//        double theta = this.longitude - location.getLongitude();
//        double dist = sin(degreeToRadian(this.latitude)) * sin(degreeToRadian(location.latitude)) +
//                cos(degreeToRadian(this.latitude)) * cos(degreeToRadian(location.latitude)) * cos(degreeToRadian(theta));
//        dist = Math.acos(dist);
//        dist = radianToDegree(dist);
//        dist = dist * 60 * 1.1515;  // Miles
//        return Double.isNaN(dist) ? 0 : dist;

        // TODO: this is euclidean space, rather than using the coordinate system
        return sqrt(pow(abs(this.latitude - location.getLatitude()), 2) +
                pow(abs(this.longitude - location.getLongitude()), 2));
    }

    /**
     * Converts decimal degrees to radians
     */
    private double degreeToRadian(double degree) {
        return (degree * Math.PI / 180.0);
    }

    /**
     * Converts radians to decimal degrees
     */
    private double radianToDegree(double radian) {
        return (radian * 180.0 / Math.PI);
    }


    public void moveToward(Location destination, double mile) {
        // TODO: this is euclidean space, rather than using the coordinate system
        float angle = (float) Math.toDegrees(atan2(destination.longitude - longitude, destination.latitude - latitude));


        double radians = toRadians(angle);  // turn the axis for -90,
        double offsetX = cos(radians) * mile;
        double offsetY = sin(radians) * mile;
        this.latitude += offsetX;
        this.longitude += offsetY;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", latitude, longitude);
    }

    public static void main(String[] args) {
        Location location = new Location(-100, -10);
        Location destination = new Location(35, 7);
        while (location.distance(destination) > 1) {
            location.moveToward(destination, 1);
        }
    }
}
