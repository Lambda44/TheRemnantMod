package theremnant.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theremnant.character.TheRemnant;
import theremnant.powers.SoulPower;

import static theremnant.RemnantMod.makeID;

public class Invigoration extends BaseRelic{
    private static final String NAME = "Invigoration";
    public static final String ID = makeID(NAME);

    public Invigoration() {
        super(ID, NAME, TheRemnant.Enums.CARD_COLOR, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.counter = -1;
    }

    public void atBattleStart() { //gain 1 energy and 3 Soul on combat start
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new GainEnergyAction(1));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulPower(AbstractDungeon.player, 3), 3));
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
    }
}
