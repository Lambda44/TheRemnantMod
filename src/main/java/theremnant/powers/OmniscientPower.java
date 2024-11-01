package theremnant.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static theremnant.RemnantMod.makeID;

public class OmniscientPower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("Omniscient");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public OmniscientPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        loadRegion("swivel");
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && this.amount > 0) {
            this.flash();
            --this.amount;
            if (this.amount == 0) {
                this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
        }

    }

    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    public static boolean isActive(){
        if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null &&
                (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player.hasPower(OmniscientPower.POWER_ID)) {
            return true;
        }
        return false;
    }

    @SpirePatch2(clz=AbstractCard.class, method="freeToPlay", paramtypez={})
    public static class freeToPlayPatch{
        @SpirePostfixPatch
        public static boolean Postfix(AbstractCard __instance, boolean __result){
            if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null &&
                    (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT &&
                    AbstractDungeon.player.hasPower(OmniscientPower.POWER_ID)) {
                __result = true;
            }
            return __result;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new OmniscientPower(owner, amount);
    }
}