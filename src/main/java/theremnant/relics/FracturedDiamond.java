package theremnant.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theremnant.character.TheRemnant;
import theremnant.powers.BonesPower;
import theremnant.powers.SoulPower;

import static theremnant.RemnantMod.makeID;

public class FracturedDiamond extends BaseRelic{
    private static final String NAME = "FracturedDiamond";
    public static final String ID = makeID(NAME);

    public FracturedDiamond() {
        super(ID, NAME, TheRemnant.Enums.CARD_COLOR, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.counter = -1;
    }

    public void wasHPLost(int damageAmount) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && damageAmount > 0) {
            this.flash();
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BonesPower(AbstractDungeon.player, 2), 2));
        }
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
    }
}
