package io.github.andriyko69.vitalxp.gameplay;

import io.github.andriyko69.vitalxp.data.HeartProgress;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgressionLogicTest {
    @Test
    void largeJumpAwardsEveryCrossedMilestoneOnce() {
        var result = ProgressionLogic.reconcile(HeartProgress.EMPTY, 17, 5);

        assertEquals(new HeartProgress(3, 17), result.progress());
        assertEquals(3, result.gainedTiers());
        assertEquals(0, ProgressionLogic.reconcile(result.progress(), 17, 5).gainedTiers());
    }

    @Test
    void repeatedSmallGainsMatchOneLargeGain() {
        HeartProgress progress = HeartProgress.EMPTY;
        int gained = 0;
        for (int level = 1; level <= 17; level++) {
            var result = ProgressionLogic.reconcile(progress, level, 5);
            progress = result.progress();
            gained += result.gainedTiers();
        }

        assertEquals(new HeartProgress(3, 17), progress);
        assertEquals(3, gained);
    }

    @Test
    void ordinaryMilestoneSequenceIsCorrect() {
        HeartProgress progress = HeartProgress.EMPTY;

        progress = assertTransition(progress, 5, 1, 1);
        progress = assertTransition(progress, 7, 0, 1);
        progress = assertTransition(progress, 11, 1, 2);
        progress = assertTransition(progress, 14, 0, 2);
        assertTransition(progress, 17, 1, 3);
    }

    @Test
    void spendingAndRegainingLevelsDoesNotFarmTiers() {
        HeartProgress atTwenty = ProgressionLogic.reconcile(HeartProgress.EMPTY, 20, 5).progress();

        var spent = ProgressionLogic.reconcile(atTwenty, 0, 5);
        var regained = ProgressionLogic.reconcile(spent.progress(), 20, 5);

        assertEquals(new HeartProgress(4, 20), spent.progress());
        assertEquals(0, spent.gainedTiers());
        assertEquals(new HeartProgress(4, 20), regained.progress());
        assertEquals(0, regained.gainedTiers());
    }

    @Test
    void intervalChangeOnlyAffectsFutureHighs() {
        HeartProgress progress = ProgressionLogic.reconcile(HeartProgress.EMPTY, 17, 5).progress();

        var levelEighteen = ProgressionLogic.reconcile(progress, 18, 10);
        var levelTwenty = ProgressionLogic.reconcile(levelEighteen.progress(), 20, 10);

        assertEquals(new HeartProgress(3, 18), levelEighteen.progress());
        assertEquals(0, levelEighteen.gainedTiers());
        assertEquals(new HeartProgress(4, 20), levelTwenty.progress());
        assertEquals(1, levelTwenty.gainedTiers());
    }

    @Test
    void tierCountSaturatesWithoutRepeatedPhantomRewards() {
        HeartProgress almostFull = new HeartProgress(Integer.MAX_VALUE - 1, 0);

        var saturated = ProgressionLogic.reconcile(almostFull, Integer.MAX_VALUE, 1);
        var repeated = ProgressionLogic.reconcile(saturated.progress(), Integer.MAX_VALUE, 1);

        assertEquals(Integer.MAX_VALUE, saturated.progress().earnedTiers());
        assertEquals(1, saturated.gainedTiers());
        assertEquals(0, repeated.gainedTiers());
    }

    @Test
    void migrationUsesGreaterOfVisibleModifierAndCurrentLevel() {
        assertEquals(3, ProgressionLogic.inferLegacyTiers(6.0, 20));
        assertEquals(8, ProgressionLogic.inferLegacyTiers(15.0, 20));
        assertEquals(0, ProgressionLogic.inferLegacyTiers(Double.NaN, 20));

        assertEquals(new HeartProgress(3, 17), ProgressionLogic.initialProgress(17, 5, 1));
        assertEquals(new HeartProgress(8, 40), ProgressionLogic.initialProgress(4, 5, 8));
    }

    private static HeartProgress assertTransition(HeartProgress progress, int level, int gained, int totalTiers) {
        var result = ProgressionLogic.reconcile(progress, level, 5);
        assertEquals(gained, result.gainedTiers());
        assertEquals(totalTiers, result.progress().earnedTiers());
        return result.progress();
    }
}
