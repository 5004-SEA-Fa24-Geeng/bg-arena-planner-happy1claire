package student;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Class that store the game list that user want to play.
 */
public class GameList implements IGameList {
    /** List of games that user intend to play*/
    private Set<String> listOfGames;
    /** Constant that indicate all game when user type "all".*/
    private final String ADD_ALL = "all";

    /**
     * Constructor for the GameList.
     * The game list that user intend to play is instantiated as empty list.
     */
    public GameList() {
        this.listOfGames = new HashSet<>();
    }

    /**
     * get the games names in the list.
     * @return games names in the list.
     */
    @Override
    public List<String> getGameNames() {
        return List.copyOf(this.listOfGames);
    }

    /**
     * Reset the game list to empty set.
     */
    @Override
    public void clear() {
        this.listOfGames = new HashSet<>();
    }

    /**
     * Get the count of games in the list.
     * @return Count of games in the list.
     */
    @Override
    public int count() {
        return this.listOfGames.size();
    }

    /**
     * Save the game list to csv file.
     * @param filename The name of the file to save the list to.
     */
    @Override
    public void saveGame(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String element : this.listOfGames) {
                writer.write(element);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Add a game to the game list by name, index, or scope of index.
     * @param str      the string to parse and add games to the list.
     * @param filtered the filtered list to use as a basis for adding.
     * @throws IllegalArgumentException the scope of number or a number beyond the index of game or
     * the game name passed in not in the stream.
     */
    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        List<BoardGame> filteredList = filtered.toList();

        if (str.equals(ADD_ALL)) {
            for (BoardGame game: filteredList) {
                listOfGames.add(game.getName());
            }
            return;
        }

        if (containNumberScope(str)) {
            str = str.replaceAll(" ", "");
            String[] numbers = str.split("-");
            int startIndex = Integer.parseInt(numbers[0]) - 1;
            int endIndex = Integer.parseInt(numbers[1]);

            if (numbers.length != 2) {
                throw new IllegalArgumentException("More Than one set of numbers passed in.");
            } else if (startIndex > filteredList.size()
                    || endIndex > filteredList.size()
                    || startIndex < 0
                    || endIndex < 0
                    || startIndex > Integer.parseInt(numbers[1])) {
                throw new IllegalArgumentException("Number out of range.");
            }

            for (int i = startIndex; i < endIndex; i++) {
                BoardGame toAdd = filteredList.get(i);
                listOfGames.add(toAdd.getName());
            }

        } else if (containOneNumber(str)) {
            if (Integer.parseInt(str) < 1 || Integer.parseInt(str) > filteredList.size()) {
                throw new IllegalArgumentException("Number out of range.");
            }
            listOfGames.add(filteredList.get(Integer.parseInt(str) - 1).getName());
        } else if (containStringOnly(str)) {
            for (BoardGame game: filteredList) {
                if (game.getName().equals(str)) {
                    listOfGames.add(game.getName());
                    return;
                }
            }
            throw new IllegalArgumentException("Cannot find the game.");
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    /**
     * Remove a game from the game list by name, index, or scope of index.
     * @param str The string to parse and remove games from the list.
     * @throws IllegalArgumentException the scope of number or a number beyond the index of game or
     * the game name passed in not in the list.
     */
    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        if (str.equals(ADD_ALL)) {
            clear();
            return;
        }

        List<String> gameList = new ArrayList<>(listOfGames);

        if (containNumberScope(str)) {
            str = str.replaceAll(" ", "");
            String[] numbers = str.split("-");
            if (numbers.length != 2) {
                throw new IllegalArgumentException("More than one set of numbers passed in.");
            }

            int startIndex = Integer.parseInt(numbers[0]) - 1;
            int endIndex = Integer.parseInt(numbers[1]);

            if (startIndex < 0 || endIndex > gameList.size() || startIndex >= endIndex) {
                throw new IllegalArgumentException("Number out of range.");
            }

            for (int i = startIndex; i < endIndex; i++) {
                listOfGames.remove(gameList.get(i));
            }

        } else if (containOneNumber(str)) {
            int index = Integer.parseInt(str) - 1;
            if (index < 0 || index >= gameList.size()) {
                throw new IllegalArgumentException("Number out of range.");
            }
            listOfGames.remove(gameList.get(index));

        } else if (containStringOnly(str)) {
            if (!listOfGames.remove(str)) {
                throw new IllegalArgumentException("Game not found.");
            }
        } else {
            throw new IllegalArgumentException("Invalid input.");
        }
    }

    /**
     * Helper function to check if a string contains number scope.
     * @param str string to check.
     * @return whether the string contains number scope.
     */
    private boolean containNumberScope(String str) {
        return str.matches("\\d+-\\d+");
    }

    /**
     * Helper function to check if a string contains only a number.
     * @param str string to check.
     * @return whether the string contains only a number.
     */
    private boolean containOneNumber(String str) {
        return str.matches("\\d+");
    }

    /**
     * Helper function to check if a string contains only alphabets.
     * @param str string to check.
     * @return whether the string contains only alphabets.
     */
    private boolean containStringOnly(String str) {
        return str.matches("\\w+");
    }

}
