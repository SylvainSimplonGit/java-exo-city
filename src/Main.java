import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class Main {

    private final static boolean DEBUG = true;

    private final static Map<String, String> mainMenuChoices = new HashMap<>();
    private final static Scanner inputScanner = new Scanner(System.in);
    private final static HashSet<City> cities = new HashSet<>();
//    private static int index = 0;


    /**
     * Entry point of program
     * @param args file containing the list of common CSV format
     */
    public static void main(String[] args) {

        // Load menus
        initDatas(args[0]);

        // Load menus
        initMenus();

        String mainChoice;
        do {
            System.out.println();
            mainChoice = showMenu(mainMenuChoices);

            switch (mainChoice) {
                case "1":
                    displayDistanceWithPoint();
                    break;
                case "2":
                    displayDistanceBetweenTwoCities();
                    break;
                case "3":
                    displayCityNearestOfList();
                    break;
                case "4":
                    displayCitiesInRadius();
                    break;
                case "5":
                    displayCitiesInIntersect();
                    break;
                default:
                    break;
            }
        } while (!mainChoice.equals("6"));

        // Close input scanner
        inputScanner.close();
    }

    /**
     * Initialise data needed in program
     * @param filePath file path of csv file
     */
    private static void initDatas(String filePath) {
        loadCitiesFromFile(Path.of(filePath));

        if (DEBUG)
            System.out.println("Importation réussie !");
    }

    /**
     * Load menus in a menu choices HashMap
     */
    private static void initMenus() {
        mainMenuChoices.put("1", "Calculer la distance d'une commune à un point quelconque repéré par ses latitude et longitude");
        mainMenuChoices.put("2", "Calculer la distance d'une commune à une autre commune");
        mainMenuChoices.put("3", "Trouver la commune la plus proche d'une commune, parmi un ensemble quelconque de communes");
        mainMenuChoices.put("4", "Trouver les communes qui sont présentes dans un rayon donné autour d'un point quelconque repéré par ses latitude et longitude");
        mainMenuChoices.put("5", "Trouver les communes qui sont simultanément dans un rayon donnée d'un ensemble de points quelconques repérés par leur latitude et longitude");
        mainMenuChoices.put("6", "Quitter le programme");
    }

    /**
     * Shows a menu based on a list of choices given as parameter in a map and gets the user choice.
     * @param menuPossibleChoiceMap the list of possible choices.
     * @return the actual choice made by the user.
     */
    private static String showMenu(Map<String, String> menuPossibleChoiceMap) {
        ArrayList<String> menuPossibleChoiceList = new ArrayList<>();
        System.out.println("----------- Statistiques sur les villes ----------");
        for (Map.Entry<String, String> menuChoice : menuPossibleChoiceMap.entrySet()) {
            System.out.print(menuChoice.getKey());
            System.out.print(". --> ");
            System.out.println(menuChoice.getValue());

            menuPossibleChoiceList.add(menuChoice.getKey());
        }
        System.out.println("----------- ########################### ----------");

        return getUserChoice(menuPossibleChoiceList);
    }

    /**
     * Gets a user choice based on a list of possible choices.
     * @param possibleValues the possible values to check against.
     * @return the user choice.
     */
    private static String getUserChoice(List<String> possibleValues) {
        String userChoice;
        do {
            System.out.print("Entrez votre choix : ");
            userChoice = inputScanner.nextLine();
        } while (!possibleValues.contains(userChoice));

        return userChoice;
    }

    /**
     * Display the distance between a city and a point.
     */
    private static void displayDistanceWithPoint() {
        City city = getCityChoice("la ville ");
        Float latitude = getCoordinateChoice("Latitude");
        Float longitude = getCoordinateChoice("Longitude");

        System.out.println("La distance entre " + city.getName() + " et le point " + latitude + "," + longitude + " est de :" + city.getDistance(latitude, longitude) + " kms");
    }

    /**
     * Display the distance between two cities.
     */
    private static void displayDistanceBetweenTwoCities() {
        City city1 = getCityChoice("la 1ère ville ");
        if (city1 != null) {
            City city2 = getCityChoice("la 2nde ville ");
            if (city2 != null) {
                System.out.println("Ville 1 : " + city1.toString());
                System.out.println("Ville 2 : " + city2.toString());

                System.out.println("La distance entre " + city1.getName() + " et " + city2.getName() + " est de : " + city1.getDistanceBetweenTwoCity(city2) + " kms");
            }
        }
    }

    /**
     * Display the city nearest of a list of cities.
     */
    private static void displayCityNearestOfList() {
        // Cities of test
        City cityChosen = getCityChoice("la ville à partir de laquelle la recherche se fera ");

        // list of cities
        City cityAdded = null;
        HashSet<City> citiesAdded = new HashSet<>();

        for(int ind = 1; (cityAdded = getCityChoice("la ville " + ind + " à tester ")) != null; ++ind){
            citiesAdded.add(cityAdded);
        }

        City cityNearest = cityChosen.getNearestCity(citiesAdded);
//        City cityNearest = cityChosen.getNearestCity(cities);

        System.out.println("La ville la plus proche de " + cityChosen.getName() + " est : " + cityNearest.getName());
    }

    /**
     * Display the cities in a circle defined by a point (latitude, longitude)
     * and a radius.
     */
    private static void displayCitiesInRadius() {
        Float latitude = getCoordinateChoice("Latitude ");
        Float longitude = getCoordinateChoice("Longitude ");
        Float radius = getRadiusChoice("rayon ");

        HashSet<City> citiesInRadius = getCitiesInCircle(radius, cities, latitude, longitude);
        System.out.println("Les villes dans un rayon de "+ 10 + " km du point " + latitude + "," + longitude + " sont :");
        for (City city : citiesInRadius) {
            System.out.println(city.getName());
        }
    }

    /**
     * Display cities in the intersection of several circles defined
     * by a point (latitude, longitude) and a radius.
     */
    private static void displayCitiesInIntersect() {
        int numberCircle = 0;

        do {
            System.out.println("Combien de cercle voulez-vous définir ?");
            numberCircle = inputScanner.nextInt();
        } while (numberCircle < 1);

        HashSet<City> intersection = null;

        for (int i = 0; i != numberCircle; i++) {
            Float latitude = getCoordinateChoice("Latitude du centre " + (i + 1) + " ");
            Float longitude = getCoordinateChoice("Longitude du centre " + (i + 1) + " ");
            Float radius = getRadiusChoice("rayon du centre " + (i + 1) + " ");

            HashSet<City> currentCities = getCitiesInCircle(radius, cities, latitude, longitude);

            if (i == 0) {
                intersection = new HashSet<>(currentCities);
            } else {
                intersection.retainAll(currentCities);
            }
        }

        System.out.println("Les villes dans l'intersection des " + numberCircle + " cercles sont :");
        for (City city : intersection) {
            System.out.println(city.getName());
        }
    }

    /**
     * Gets cities in a circle defined by a point (latitude, longitude) and a radius.
     * @param radius radius of the research circle.
     * @param cities hash set list of cities to check.
     * @param pointLatitude latitude of circle center in degrees.
     * @param pointLongitude longitude of circle center in degrees.
     * @return a hash set of cities whose distance to the center is less than the radius
     */
    private static HashSet<City> getCitiesInCircle(float radius, HashSet<City> cities, double pointLatitude, double pointLongitude) {
        HashSet<City> citiesInCircle = new HashSet<>();

        for (City city : cities) {
            if (city.getDistance(pointLatitude, pointLongitude) <= radius) {
                citiesInCircle.add(city);
            }
        }

        return citiesInCircle;
    }

    /**
     * Gets and checks a coordinate in degrees.
     * @param message display message to clarify the requested entry.
     * @return a coordinate checked.
     */
    private static Float getCoordinateChoice(String message) {
        Float coordinateDeg;
        inputScanner.useLocale(Locale.US);
        do {
            System.out.println("Veuillez entrer la " + message.toLowerCase() + "(en degré) : ");
            coordinateDeg = inputScanner.nextFloat();
        } while (coordinateDeg < 0.0f && coordinateDeg > 360.0f);
        return coordinateDeg;
    }

    /**
     * Gets and checks a radius positive.
     * @param message display message to clarify the requested entry.
     * @return a raduis checked.
     */
    private static Float getRadiusChoice(String message) {
        Float inputValue;
        inputScanner.useLocale(Locale.US);
        do {
            System.out.println("Veuillez entrer " + message.toLowerCase() + "(en km) : ");
            inputValue = inputScanner.nextFloat();
        } while (inputValue < 0.0f);
        return inputValue;
    }

    /**
     * Gets and checked a city in hash set of cities.
     * @param message display message to clarify the requested entry.
     * @return a city checked
     */
    private static City getCityChoice(String message) {
        String nameCity = "";
        ArrayList<City> citiesNamed;
        City cityChosen = null;

        do {
            System.out.println("Taper 'exit' pour sortir");
            System.out.print("Veuillez entrer " + message + ": ");
            nameCity = inputScanner.nextLine();
            citiesNamed = getListOfCitiesNamed(nameCity);
            if (!nameCity.equals("exit")) {
                if (citiesNamed.size() == 0) {
                    System.out.println("Aucune ville avec ce nom : " + nameCity);
                } else if (citiesNamed.size() == 1) {
                    cityChosen = citiesNamed.get(0);
                } else {
                    int indexCity;
                    do {
                        int index = 0;
                        // Afficher la liste des villes
                        for (City city : citiesNamed) {
                            System.out.println("" + index + " --> " + city.toString());
                            index++;
                        }
                        System.out.println("Quelle ville choisissez-vous : ");
                        indexCity = inputScanner.nextInt();
                        inputScanner.nextLine();
                        if (indexCity < citiesNamed.size()) {
                            cityChosen = citiesNamed.get(indexCity);
                        } else {
                            System.out.println("le choix " + indexCity + " n'est pas possible !");
                        }
                    } while (indexCity < 0 || indexCity >= citiesNamed.size());
                }
            }
        } while ( !nameCity.equals("exit") && citiesNamed.size() == 0 );


        return cityChosen;
    }

    /**
     * Loads data from a CSV file in hash set, excluding duplicate and non-compliant data.
     * @param filePath Path to the CSV file contains datas.
     */
    private static void loadCitiesFromFile(Path filePath) {
        int index = 0;
        int indexNbLine = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
            BufferedWriter writerOK = null;
            BufferedWriter writerKO = null;
            if (DEBUG) {
                writerOK = new BufferedWriter(new FileWriter(Path.of("resources\\Extract.csv").toFile()));
                writerKO = new BufferedWriter(new FileWriter(Path.of("resources\\Extract-ko.csv").toFile()));
            }

            String line;

            do {
                line = reader.readLine();
                indexNbLine++;
                try {
                    String[] dataCSV = line.split(";");
                    String cityName = dataCSV[0];
                    float cityLatitude = Float.parseFloat(dataCSV[1]);
                    float cityLongitude = Float.parseFloat(dataCSV[2]);
                    if (addCity(new City(cityName, cityLatitude, cityLongitude)) && DEBUG) {
                        index++;
                        writerOK.write(line + "\n");
                    } else {
                        assert writerKO != null;
                        writerKO.write("Doublon;" + line + "\n");
                    }

                } catch (Exception ignored) {
                    if (DEBUG) {
                        writerKO.write("Format;"+ line + "\n");
//                        System.out.println("Problème sur la ligne : " + line);
                    }
                }

            } while (line != null);

        } catch (IOException e) {
            System.err.println("Une erreur est survenue lors de la lecture du fichier : " + filePath);
        }
        if (DEBUG)
            System.out.println("Lecture de " + index + " lignes");
            System.out.println("Importation de " + index + " lignes");
    }

    /**
     * Adds city in hash set cities.
     * @param city a city object to add.
     * @return a list of cities which have the same name.
     */
    private static boolean addCity(City city) {
        return cities.add(city);
    }

    /**
     * Gets a list of cities which have the same name but different coordinates.
     * @param name the city name to search.
     * @return a list of cities which have the same name.
     */
    private static ArrayList<City> getListOfCitiesNamed(String name) {
        ArrayList<City> citiesNamed = new ArrayList<>();
        for (City city : cities) {
            if (name.equals(city.getName()) && !citiesNamed.contains(city))
                citiesNamed.add(city);
        }
        return citiesNamed;
    }


}
