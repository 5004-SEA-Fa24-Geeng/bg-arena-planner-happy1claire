package student;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Planner implements IPlanner {
    Set<BoardGame> games;
    Set<BoardGame> filteredGames;

    public Planner(Set<BoardGame> games) {
        this.games = games;
        this.filteredGames = games;
    }

    @Override
    public Stream<BoardGame> filter(String filter) {
        Stream<BoardGame> stream = this.filteredGames.stream();

        String[] separatedFilters = filter.split(",");
        for (String separatedFilter : separatedFilters) {
            separatedFilter = separatedFilter.trim();
            stream = Planner.filterSingle(separatedFilter, stream);
        }

        this.filteredGames = stream.collect(Collectors.toSet());

        return this.filteredGames.stream().sorted(Comparator.comparing(BoardGame::getName, String.CASE_INSENSITIVE_ORDER));
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {

        Stream<BoardGame> stream = this.filteredGames.stream();

        // Separating multiple filters
        String[] separatedFilters = filter.split(",");
        for (String separatedFilter : separatedFilters) {
            separatedFilter = separatedFilter.trim();
            stream = Planner.filterSingle(separatedFilter, stream);
        }

        this.filteredGames = stream.collect(Collectors.toSet());
        return applySorting(this.filteredGames.stream(), sortOn, true);
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        // TODO Auto-generated method stub

        Stream<BoardGame> stream = this.filteredGames.stream();

        String[] separatedFilters = filter.split(",");
        for (String separatedFilter : separatedFilters) {
            separatedFilter = separatedFilter.trim();
            stream = Planner.filterSingle(separatedFilter, stream);
        }

        this.filteredGames = stream.collect(Collectors.toSet());

        return applySorting(this.filteredGames.stream(), sortOn, ascending);
    }

    // Helper function to be call on single filter that can be used in filter functions.
    private static Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filteredGames) {

        // Get the operator if exists, otherwise, get the filteredGames stream. e.g. Operation.GREATER_THAN_EQUALS.
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null) {
            return filteredGames;
        }

        // remove spaces
        filter = filter.replaceAll(" ", "");

        String[] parts = filter.split(operator.getOperator());
        if (parts.length != 2) {
            return filteredGames;
        }

        GameData column;
        try {
            column = GameData.fromString(parts[0]);
        } catch (IllegalArgumentException e) {
            return filteredGames;
        }

        String value;
        try {
            value = parts[1].trim().toLowerCase();
        } catch (IllegalArgumentException e) {
            return filteredGames;
        }

        // Call Comparator in filter which is built in Filter class
        Stream<BoardGame> filteredGamesList = filteredGames
                .filter(game -> Filters.filter(game, column, operator, value));

        return filteredGamesList;
    }

    // Helper function to get comparator
    private Stream<BoardGame> applySorting(Stream<BoardGame> gameStream, GameData sortOn, boolean ascending) {
        Comparator<BoardGame> comparator = switch (sortOn) {
            case NAME -> Comparator.comparing(game -> game.getName().toLowerCase(), String.CASE_INSENSITIVE_ORDER);
            case MAX_PLAYERS -> Comparator.comparingInt(BoardGame::getMaxPlayers);
            case MIN_PLAYERS -> Comparator.comparingInt(BoardGame::getMinPlayers);
            case MAX_TIME -> Comparator.comparingInt(BoardGame::getMaxPlayTime);
            case MIN_TIME -> Comparator.comparingInt(BoardGame::getMinPlayTime);
            case RANK -> Comparator.comparingInt(BoardGame::getRank);
            case DIFFICULTY -> Comparator.comparingDouble(BoardGame::getDifficulty);
            case YEAR -> Comparator.comparingInt(BoardGame::getYearPublished);
            default -> null;
        };

        if (comparator != null) {
            if (!ascending) {
                comparator = comparator.reversed();
                return gameStream.sorted(comparator);
            }
            return gameStream.sorted(comparator);
        }

        return gameStream;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        this.filteredGames = this.games;
    }
}