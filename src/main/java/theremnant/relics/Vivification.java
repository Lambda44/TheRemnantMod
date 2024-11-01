package theremnant.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theremnant.character.TheRemnant;
import theremnant.powers.SoulPower;

import static theremnant.RemnantMod.makeID;

public class Vivification extends BaseRelic{
    private static final String NAME = "Vivification";
    public static final String ID = makeID(NAME);

    public Vivification() {
        super(ID, NAME, TheRemnant.Enums.CARD_COLOR, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.counter = -1;
    }

    public void atBattleStart() { //gain 2 energy and draw 2 extra cards on combat start
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new GainEnergyAction(2));
    }

    public void atTurnStart() { //gain 2 Soul per turn
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulPower(AbstractDungeon.player, 2), 2));
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(Invigoration.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(Invigoration.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(Invigoration.ID);
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
    }
}
