package student;


import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class to handle the filter that user enters.
 */
public class Planner implements IPlanner {
    /** Set contains all board games*/
    private Set<BoardGame> games;
    /** Set contains filtered board games.*/
    private Set<BoardGame> filteredGames;

    /**
     * Constructor for planners
     * @param games Set contains all board games.
     */
    public Planner(Set<BoardGame> games) {
        this.games = games;
        this.filteredGames = games;
    }

    /**
     * Filtered out the board games based on the filter that is passed in.
     * Sorted on the games name with ascending order.
     * @param filter The filter to apply to the board games.
     * @return filtered board games in stream with ascending order on game names.
     */
    @Override
    public Stream<BoardGame> filter(String filter) {
        Stream<BoardGame> stream = this.filteredGames.stream();

        String[] separatedFilters = filter.split(",");
        for (String separatedFilter : separatedFilters) {
            separatedFilter = separatedFilter.trim();
            stream = Planner.filterSingle(separatedFilter, stream);
        }

        this.filteredGames = stream.collect(Collectors.toSet());

        return this.filteredGames.stream()
                .sorted(Comparator.comparing(BoardGame::getName,
                        String.CASE_INSENSITIVE_ORDER));
    }

    /**
     * Filtered out the board games based on the filter that is passed in.
     * Sorted on the targeted columns with ascending order.
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @return filtered board games in stream with ascending order on target column.
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {

        Stream<BoardGame> stream = this.filteredGames.stream();

        String[] separatedFilters = filter.split(",");
        for (String separatedFilter : separatedFilters) {
            separatedFilter = separatedFilter.trim();
            stream = Planner.filterSingle(separatedFilter, stream);
        }

        this.filteredGames = stream.collect(Collectors.toSet());
        return applySorting(this.filteredGames.stream(), sortOn, true);
    }

    /**
     * Filtered out the board games based on the filter that is passed in.
     * Sorted on the targeted columns with order that user choose.
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @param ascending sort with ascending or descending order.
     * @return filtered board games in stream with order and column passed in.
     */
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

    /**
     * Helper function to process single filter on filteredGames stream.
     * @param filter string that contain only one filter.
     * @param filteredGames stream of Board Games.
     * @return processed stream with single filter passed in.
     */
    private static Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filteredGames) {

        // Get the operator if exists, otherwise, get the filteredGames stream. e.g. Operation.GREATER_THAN_EQUALS.
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null) {
            return filteredGames;
        }

        filter = filter.trim();
        String[] parts = filter.split(operator.getOperator());
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

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

        Stream<BoardGame> filteredGamesList = filteredGames
                .filter(game -> Filters.filter(game, column, operator, value));

        return filteredGamesList;
    }

    /**
     * Helper function to apply sorting on filtered stream.
     * @param filteredGames filtered game stream.
     * @param sortOn columns to sort on.
     * @param ascending whether sort by ascending or descending order.
     * @return filtered and ordered game stream.
     */
    private Stream<BoardGame> applySorting(Stream<BoardGame> filteredGames, GameData sortOn, boolean ascending) {
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
                return filteredGames.sorted(comparator);
            }
            return filteredGames.sorted(comparator);
        }

        return filteredGames;
    }

    /**
     * reset the filtered games to entire games set.
     */
    @Override
    public void reset() {
        this.filteredGames = this.games;
    }
}
