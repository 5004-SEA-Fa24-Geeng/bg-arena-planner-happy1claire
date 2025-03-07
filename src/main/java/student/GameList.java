package student;

import jdk.jshell.spi.SPIResolutionException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
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

//    @Override
//    public void removeFromList(String str) throws IllegalArgumentException {
//        if (containStringOnly(str)) {
//            this.listOfGames.remove(str);
//        } else if (containNumberScope(str)) {
//            str = str.replaceAll(" ", "");
//            String[] numbers = str.split("-");
//            int startIndex = Integer.parseInt(numbers[0]) - 1;
//            int endIndex = Integer.parseInt(numbers[1]);
//
//            if (numbers.length != 2) {
//                throw new IllegalArgumentException("More Than one set of numbers passed in.");
//            } else if (startIndex > this.listOfGames.size()
//                    || endIndex > this.listOfGames.size()
//                    || startIndex < 0
//                    || endIndex < 0
//                    || startIndex > Integer.parseInt(numbers[1])) {
//                throw new IllegalArgumentException("Number out of range.");
//            }
//
//            Iterator<String> iterator = listOfGames.iterator();
//            int count = 0;
//            while (iterator.hasNext()) {
//                iterator.next();
//                if (count >= startIndex && count < endIndex) {
//                    iterator.remove();
//                }
//                count++;
//            }
//        } else if (str.equals(ADD_ALL)) {
//            clear();
//        } else if (containOneNumber(str)) {
//            int targetIndex = Integer.parseInt(str) - 1;
//            if (targetIndex < 0 || targetIndex > listOfGames.size()) {
//                throw new IllegalArgumentException("Number out of range.");
//            }
//
//            Iterator<String> iterator = listOfGames.iterator();
//            int count = 0;
//            while (iterator.hasNext()) {
//                iterator.next();
//                if (count == targetIndex) {
//                    iterator.remove();
//                    return;
//                }
//                count++;
//            }
//        } else {
//            throw new IllegalArgumentException("The game doesn't exist.");
//        }
//    }

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

    private boolean containNumberScope(String str) {
        return str.matches("\\d+-\\d+");
    }

    private boolean containOneNumber(String str) {
        return str.matches("\\d+");
    }

    private boolean containStringOnly(String str) {
        return str.matches("\\w+");
    }

}
