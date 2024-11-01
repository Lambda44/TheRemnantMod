package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.sun.prism.shader.Solid_TextureYV12_Loader;

import static theremnant.RemnantMod.makeID;

public class FreezeSoulPower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("FreezeSoul");
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;
    private boolean justApplied = false;

    public FreezeSoulPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        if (AbstractDungeon.actionManager.turnHasEnded) {
            this.justApplied = true;
        }
    }

    public void atEndOfRound() {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, FreezeSoulPower.POWER_ID, 1));
            if (this.amount <= 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, FreezeSoulPower.POWER_ID));
            }
        }
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new FreezeSoulPower(owner, amount);
    }
}