package student;

/**
 * Class to filter games.
 */
public final class Filters {
    /** private constructor */
    private Filters() { }

    /**
     * function to indicate whether a game match the conditions.
     * @param game board game to be checked.
     * @param column column that will be filtered.
     * @param operations operation to be applied on filter.
     * @param value value to be applied on filter.
     * @return whether a game match the conditions.
     */
    public static boolean filter(BoardGame game,
                                 GameData column,
                                 Operations operations,
                                 String value) {
        switch (column) {
            case NAME:
                // filter name
                return filterString(game.getName().toLowerCase(), operations, value);
            case MAX_PLAYERS:
                return filterNum(game.getMaxPlayers(), operations, value);
            case MIN_PLAYERS:
                return filterNum(game.getMinPlayers(), operations, value);
            case MAX_TIME:
                return filterNum(game.getMaxPlayTime(), operations, value);
            case MIN_TIME:
                return filterNum(game.getMinPlayTime(), operations, value);
            case RANK:
                return filterNum(game.getRank(), operations, value);
            case RATING:
                return filterDouble(game.getRating(), operations, value);
            case DIFFICULTY:
                return filterDouble(game.getDifficulty(), operations, value);
            case YEAR:
                return filterNum(game.getYearPublished(), operations, value);
            default:
                return false;
        }
    }

    /**
     * Helper function to apply conditions on column that is string type.
     * @param gameData The string type data to apply conditions on.
     * @param operations operation to be applied on filter.
     * @param value value to be applied on filter.
     * @return whether a game data match the conditions.
     */
    public static boolean filterString(String gameData, Operations operations, String value) {
        switch (operations) {
            case EQUALS:
                return gameData.equals(value);
            case NOT_EQUALS:
                return !gameData.equals(value);
            case CONTAINS:
                return gameData.contains(value);
            case GREATER_THAN:
                return gameData.compareTo(value) > 0;
            case LESS_THAN:
                return gameData.compareTo(value) < 0;
            case GREATER_THAN_EQUALS:
                return gameData.compareTo(value) >= 0;
            case LESS_THAN_EQUALS:
                return gameData.compareTo(value) <= 0;
            default:
                return false;
        }
    }

    /**
     * Helper function to apply conditions on column that is integer type.
     * @param gameData The integer type data to apply conditions on.
     * @param operations operation to be applied on filter.
     * @param value value to be applied on filter.
     * @return whether a game data match the conditions.
     */

    public static boolean filterNum(int gameData, Operations operations, String value) {
        int intValue = Integer.parseInt(value);
        switch (operations) {
            case EQUALS:
                return gameData == intValue;
            case NOT_EQUALS:
                return gameData != intValue;
            case GREATER_THAN:
                return gameData > intValue;
            case LESS_THAN:
                return gameData < intValue;
            case GREATER_THAN_EQUALS:
                return gameData >= intValue;
            case LESS_THAN_EQUALS:
                return gameData <= intValue;
            default:
                return false;
        }
    }

    /**
     * Helper function to apply conditions on column that is double type.
     * @param gameData The double type data to apply conditions on.
     * @param operations operation to be applied on filter
     * @param value value to be applied on filter.
     * @return whether a game data match the conditions.
     */
    public static boolean filterDouble(double gameData, Operations operations, String value) {
        double doubleValue = Double.parseDouble(value);
        switch (operations) {
            case EQUALS:
                return gameData == doubleValue;
            case NOT_EQUALS:
                return gameData != doubleValue;
            case GREATER_THAN:
                return gameData > doubleValue;
            case LESS_THAN:
                return gameData < doubleValue;
            case GREATER_THAN_EQUALS:
                return gameData >= doubleValue;
            case LESS_THAN_EQUALS:
                return gameData <= doubleValue;
            default:
                return false;
        }
    }
}
