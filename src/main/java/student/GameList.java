package student;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class GameList implements IGameList {
    private Set<String> listOfGames;
    private final String ADD_ALL = "all";

    /**
     * Constructor for the GameList.
     */
    public GameList() {
        this.listOfGames = new HashSet<>();
    }

    @Override
    public List<String> getGameNames() {
        return List.copyOf(this.listOfGames);
    }

    @Override
    public void clear() {
        this.listOfGames = new HashSet<>();
    }

    @Override
    public int count() {
        return this.listOfGames.size();
    }

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

    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        List<BoardGame> filteredList = filtered.toList();

        if (str.equals(ADD_ALL)) {
            for (BoardGame game: filteredList) {
                listOfGames.add(game.getName());
            }
            return;
        }

        if (str.matches("\\d+-\\d+")) {
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

        } else if (str.matches("\\d+")) {
            listOfGames.add(filteredList.get(Integer.parseInt(str) - 1).getName());

        } else if (str.matches("\\w+")) {
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

    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        if (this.listOfGames.contains(str)) {
            listOfGames.remove(str);
        } else {
            throw new IllegalArgumentException("The game doesn't exist.");
        }
    }
}
