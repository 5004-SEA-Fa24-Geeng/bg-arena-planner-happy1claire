package student;


import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Planner implements IPlanner {
    Set<BoardGame> games;
    Set<BoardGame> orginalgames;

    public Planner(Set<BoardGame> games) {
        this.games = games;
        this.orginalgames = games;
    }

    @Override
    public Stream<BoardGame> filter(String filter) {
        Stream<BoardGame> result = games.stream();

        String[] separatedFilters = filter.split(",");
        for (String separatedFilter : separatedFilters) {
            separatedFilter = separatedFilter.trim();
            result = Planner.filterSingle(separatedFilter, result);
        }

        Stream<BoardGame> sortedResult = result.sorted((game1, game2) ->
                game1.getName().compareToIgnoreCase(game2.getName()));
        this.games = sortedResult.collect(Collectors.toSet());

        return this.games.stream();
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        Stream<BoardGame> result = games.stream();

        String[] separatedFilters = filter.split(",");
        for (String separatedFilter : separatedFilters) {
            separatedFilter = separatedFilter.trim();
            result = Planner.filterSingle(separatedFilter, result);
        }

        Stream<BoardGame> sortedResult = applySorting(result, sortOn, true);
        this.games = sortedResult.collect(Collectors.toSet());

        return this.games.stream();
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        Stream<BoardGame> result = games.stream();

        String[] separatedFilters = filter.split(",");
        for (String separatedFilter : separatedFilters) {
            separatedFilter = separatedFilter.trim();
            result = Planner.filterSingle(separatedFilter, result);
        }

        Stream<BoardGame> sortedResult = applySorting(result, sortOn, ascending);
        this.games = sortedResult.collect(Collectors.toSet());

        return this.games.stream();
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
            value = parts[1].trim();
        } catch (IllegalArgumentException e) {
            return filteredGames;
        }

        System.out.println(operator);
        System.out.println(column);
        System.out.println(value);

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
        this.games = this.orginalgames;
    }
}
