package util;

import model.Category;
import model.Dice;

import java.util.Arrays;

import static model.Dice.*;

public class YatzyUtility {

    private YatzyUtility() {
    }

    public static int getScore(Dice[] dicesRolled, Category category) {
        int[] dices = Arrays.stream(dicesRolled).mapToInt(Dice::getValue).toArray();

        switch (category) {
            case YAHTZEE:
                return yatzy(dices);
            case ONES:
                return sumSameNumberCategory(dices, ONE);
            case TWOS:
                return sumSameNumberCategory(dices, TWO);
            case THREES:
                return sumSameNumberCategory(dices, THREE);
            case FOURS:
                return sumSameNumberCategory(dices, FOUR);
            case FIVES:
                return sumSameNumberCategory(dices, FIVE);
            case SIXES:
                return sumSameNumberCategory(dices, SIX);
            case ONE_PAIR:
                return onePair(dices);
            case TWO_PAIR:
                return twoPair(dices);
            case THREE_OF_A_KIND:
                return fourOrThreeOfAKind(dices, THREE);
            case FOUR_OF_A_KIND:
                return fourOrThreeOfAKind(dices, FOUR);
            case SMALL_STRAIGHT:
                return smallStraight(dices);
            case LARGE_STRAIGHT:
                return largeStraight(dices);
            case FULL_HOUSE:
                return fullHouse(dices);
            default:
                return chance(dices);
        }
    }

    private static int chance(int[] dices) {
        return Arrays.stream(dices).sum();
    }

    private static int yatzy(int[] dices) {
        return Arrays.stream(dices).distinct().count() == 1 ? 50 : 0;
    }

    private static int sumSameNumberCategory(int[] dices, Dice target) {
        return Arrays.stream(dices).filter(d -> d == target.getValue()).sum();
    }


    private static int onePair(int[] dices) {
        int[] diceOccurrences = getOccurenceTable(dices);
        for (int i = 0; i < 6; i++)
            if (diceOccurrences[5 - i] >= 2)
                return (6 - i) * 2;
        return 0;
    }

    private static int twoPair(int[] dices) {
        int[] diceOccurrences = getOccurenceTable(dices);
        int n = 0;
        int score = 0;
        for (int i = 0; i < 6; i += 1)
            if (diceOccurrences[5 - i] >= 2) {
                n++;
                score += 6 - i;
            }
        return n == 2 ? score * 2 : 0;
    }

    private static int fourOrThreeOfAKind(int[] dices, Dice target) {
        int[] diceOccurrences = getOccurenceTable(dices);

        for (int i = 0; i < 6; i++)
            if (diceOccurrences[i] >= target.getValue())
                return (i + 1) * target.getValue();
        return 0;
    }


    private static int smallStraight(int[] dices) {
        return Arrays.stream(getOccurenceTable(dices)).limit(5).filter(d -> d == 1).count() == 5 ? 15 : 0;

    }

    private static int largeStraight(int[] dices) {
        return Arrays.stream(getOccurenceTable(dices)).skip(1).filter(d -> d == 1).count() == 5 ? 20 : 0;
    }

    private static int fullHouse(int[] dices) {
        int[] diceOccurrences = getOccurenceTable(dices);
        boolean isPairFound = false;
        int pairDiceValue = 0;
        boolean isThreeFound = false;
        int threeKindDiceValue = 0;


        for (int i = 0; i < 6; i++) {
            if (diceOccurrences[i] == 2) {
                isPairFound = true;
                pairDiceValue = i + 1;
            }

            if (diceOccurrences[i] == 3) {
                isThreeFound = true;
                threeKindDiceValue = i + 1;
            }

            if (isPairFound && isThreeFound) break;
        }

        return (isPairFound && isThreeFound) ? pairDiceValue * 2 + threeKindDiceValue * 3 : 0;
    }

    private static int[] getOccurenceTable(int[] dices) {
        int[] counts = new int[6];
        for (int dice : dices) {
            counts[dice - 1]++;
        }
        return counts;
    }
}