package theremnant.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theremnant.character.TheRemnant;
import theremnant.powers.BonesPower;

import static theremnant.RemnantMod.makeID;

public class BearArms extends BaseRelic{
    private static final String NAME = "BearArms";
    public static final String ID = makeID(NAME);

    public BearArms() {
        super(ID, NAME, TheRemnant.Enums.CARD_COLOR, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.counter = -1;
    }

    public void atBattleStart() { //gain 1 STR & DEX on combat start
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
    }
}
