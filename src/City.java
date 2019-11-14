import java.util.Collection;
import java.util.HashSet;

public class City {
    private final int RADIUS_EARTH = 6371;

    private final String name;
    private final float latitude;
    private final float longitude;

    City(String name, float latitude, float longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return this.name;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    @Override
    public String toString() {
        return this.getName() + " ( l : " + this.getLatitude() + ", L : " + this.getLongitude() + " )";
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof City){
            City otherCity = (City) other;
            return name.equals(otherCity.name) && latitude == otherCity.latitude && longitude == otherCity.longitude;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() ^ Float.valueOf(this.latitude).hashCode() ^ Float.valueOf(this.longitude).hashCode();
    }

    double getDistance(double latitudeDeg, double longitudeDeg) {
        double latitudePointRad = Math.toRadians(latitudeDeg);
        double longitudePointRad = Math.toRadians(longitudeDeg);

        double latitudeCityRad = Math.toRadians(this.latitude);
        double longitudeCityRad = Math.toRadians(this.longitude);

        return RADIUS_EARTH * Math.acos(Math.cos(latitudePointRad) * Math.cos(latitudeCityRad) * Math.cos(longitudeCityRad - longitudePointRad) + Math.sin(latitudePointRad) * Math.sin(latitudeCityRad));
    }

    // ToDo surcharger la méthode getDistance
    double getDistanceBetweenTwoCity(City otherCity) {
        return getDistance(otherCity.latitude, otherCity.longitude);
    }

    // ToDo voir Javadoc Optional<City>
    // Optional<City> getNearestCity(Collection<City> cities)
    City getNearestCity(Collection<City> cities) {
        City nearestCity = null;
        double distOfNearestCity = 0;

        if (!cities.isEmpty()) {
            for(City city : cities) {
                double distCity = getDistanceBetweenTwoCity(city);
                if (distOfNearestCity == 0 || distCity < distOfNearestCity) {
                    nearestCity = city;
                    distOfNearestCity = distCity;
                }
            }
        }

        return nearestCity;
    }

}
