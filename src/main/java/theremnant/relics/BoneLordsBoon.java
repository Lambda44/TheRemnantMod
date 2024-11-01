package theremnant.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theremnant.character.TheRemnant;
import theremnant.powers.BonesPower;
import theremnant.powers.SoulPower;

import static theremnant.RemnantMod.makeID;

public class BoneLordsBoon extends BaseRelic{
    private static final String NAME = "BoneLordsBoon";
    public static final String ID = makeID(NAME);

    public BoneLordsBoon() {
        super(ID, NAME, TheRemnant.Enums.CARD_COLOR, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.counter = -1;
    }

    public void atBattleStart() { //gain 5 Bones on combat start
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BonesPower(AbstractDungeon.player, 5), 5));
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
    }
}
