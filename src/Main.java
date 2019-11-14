import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class Main {

    private final static boolean DEBUG = false;

    private final static Map<String, String> mainMenuChoices = new HashMap<>();
    private final static Scanner inputScanner = new Scanner(System.in);
    private final static HashSet<City> cities = new HashSet<>();
    private static int index = 0;


    public static void main(String[] args) {
        // Load menus
        initDatas();

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

    private static void initDatas() {
        loadCitiesFromFile(Path.of("resources\\Communes.csv"));

        if (DEBUG)
            System.out.println("Importation réussie !");
    }

    private static void initMenus() {
        mainMenuChoices.put("1", "Calculer la distance d'une commune à un point quelconque repéré par ses latitude et longitude");
        mainMenuChoices.put("2", "Calculer la distance d'une commune à une autre commune");
        mainMenuChoices.put("3", "Trouver la commune la plus proche d'une commune, parmi un ensemble quelconque de communes");
        mainMenuChoices.put("4", "Trouver les communes qui sont présentes dans un rayon donné autour d'un point quelconque repéré par ses latitude et longitude");
        mainMenuChoices.put("5", "Trouver les communes qui sont simultanément dans un rayon donnée d'un ensemble de points quelconques repérés par leur latitude et longitude");
        mainMenuChoices.put("6", "Quitter le programme");
    }

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

    private static String getUserChoice(List<String> possibleValues) {
        String userChoice;
        do {
            System.out.print("Entrez votre choix : ");
            userChoice = inputScanner.nextLine();
        } while (!possibleValues.contains(userChoice));

        return userChoice;
    }

    private static void displayDistanceWithPoint() {
        City city = getCityChoice("la ville ");
        Float latitude = getCoordChoice("Latitude");
        Float longitude = getCoordChoice("Longitude");

        System.out.println("La distance entre " + city.getName() + " et le point " + latitude + "," + longitude + " est de :" + city.getDistance(latitude, longitude) + " kms");
    }

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

    private static void displayCityNearestOfList() {
        // Cities of test
        City cityChosen = getCityChoice("la ville à partir de laquelle la recherche se fera ");

        // list of cities
        // ToDo récupérer une liste de villes saisies par l'utilisateur
        City cityAdded = null;
        HashSet<City> citiesAdded = new HashSet<>();

        for(int ind = 1; (cityAdded = getCityChoice("la ville " + ind + " à tester ")) != null; ++ind){
            citiesAdded.add(cityAdded);
        }

        City cityNearest = cityChosen.getNearestCity(citiesAdded);
//        City cityNearest = cityChosen.getNearestCity(cities);

        System.out.println("La ville la plus proche de " + cityChosen.getName() + " est : " + cityNearest.getName());
    }

    private static void displayCitiesInRadius() {
        Float latitude = getCoordChoice("Latitude ");
        Float longitude = getCoordChoice("Longitude ");
        Float radius = getRadiusChoice("rayon ");

        HashSet<City> citiesInRadius = getCitiesInCircle(radius, cities, latitude, longitude);
        System.out.println("Les villes dans un rayon de "+ 10 + " km du point " + latitude + "," + longitude + " sont :");
        for (City city : citiesInRadius) {
            System.out.println(city.getName());
        }
    }

    private static void displayCitiesInIntersect() {
        int numberCircle = 0;

        do {
            System.out.println("Combien de cercle voulez-vous définir ?");
            numberCircle = inputScanner.nextInt();
        } while (numberCircle < 1);

        HashSet<City> intersection = null;

        for (int i = 0; i != numberCircle; i++) {
            Float latitude = getCoordChoice("Latitude du centre " + (i + 1) + " ");
            Float longitude = getCoordChoice("Longitude du centre " + (i + 1) + " ");
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

    private static HashSet<City> getCitiesInCircle(float rayon, HashSet<City> cities, double pointLatitude, double pointLongitude) {
        HashSet<City> citiesInCircle = new HashSet<>();

        for (City city : cities) {
            if (city.getDistance(pointLatitude, pointLongitude) <= rayon) {
                citiesInCircle.add(city);
            }
        }

        return citiesInCircle;
    }

    private static Float getCoordChoice(String typeCoord) {
        Float coordDeg;
        inputScanner.useLocale(Locale.US);
        do {
            System.out.println("Veuillez entrer la " + typeCoord.toLowerCase() + "(en degré) : ");
            coordDeg = inputScanner.nextFloat();
        } while (coordDeg < 0.0f && coordDeg > 360.0f);
        return coordDeg;
    }

    private static Float getRadiusChoice(String message) {
        Float inputValue;
        inputScanner.useLocale(Locale.US);
        do {
            System.out.println("Veuillez entrer " + message.toLowerCase() + "(en km) : ");
            inputValue = inputScanner.nextFloat();
        } while (inputValue < 0.0f);
        return inputValue;
    }

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
                        cityChosen = citiesNamed.get(indexCity);
                    } while (indexCity < 0 || indexCity >= citiesNamed.size());
                }
            }
        } while ( !nameCity.equals("exit") && citiesNamed.size() == 0 );


        return cityChosen;
    }

    private static void loadCitiesFromFile(Path filePath) {
        // ToDo Chalons-sur-Saône doublons

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
            BufferedWriter writer = new BufferedWriter(new FileWriter(Path.of("resources\\Extract.csv").toFile()));
            String line;

            do {
                line = reader.readLine();
                try {
                    String[] datas = line.split(";");
                    String cityName = datas[0];
                    float cityLatitude = Float.parseFloat(datas[1]);
                    float cityLongitude = Float.parseFloat(datas[2]);
                    addCity(new City(cityName, cityLatitude, cityLongitude));
                    if (DEBUG)
                        writer.write(line + "\n");
                } catch (Exception ignored) {
                    if (DEBUG)
                        System.out.println("Problème sur la ligne : " + line);
                }

            } while (line != null);

        } catch (IOException e) {
            System.err.println("Une erreur est survenue lors de la lecture du fichier : " + filePath);
        }
        if (DEBUG)
            System.out.println("Importation de " + index + " lignes");
    }

    private static void addCity(City city) {
        if (cities.add(city))
            index++;
    }

    private static ArrayList<City> getListOfCitiesNamed(String name) {
        ArrayList<City> citiesNamed = new ArrayList<>();
        for (City city : cities) {
            if (name.equals(city.getName()) && !citiesNamed.contains(city))
                citiesNamed.add(city);
        }
        return citiesNamed;
    }


}
