package theremnant.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theremnant.character.TheRemnant;
import theremnant.powers.SoulPower;

import static theremnant.RemnantMod.makeID;

public class RagingSpirit extends BaseRelic{
    private static final String NAME = "RagingSpirit";
    public static final String ID = makeID(NAME);

    public RagingSpirit() {
        super(ID, NAME, TheRemnant.Enums.CARD_COLOR, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.counter = -1;
    }

    public void atTurnStart() { //at turn start, if you have 8+ Soul, gain 2 STR
        if (AbstractDungeon.player.hasPower(SoulPower.POWER_ID) && AbstractDungeon.player.getPower(SoulPower.POWER_ID).amount >= 8) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 2), 2));
        }
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
    }
}
