package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static theremnant.RemnantMod.makeID;

public class ResistancePower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("Resistance");
    //private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public ResistancePower(AbstractCreature owner, int amount) {
        super(POWER_ID, null, TURN_BASED, owner, amount);
        this.canGoNegative = true;
        if (this.amount < 0) {
            this.type = PowerType.DEBUFF;
        } else {
            this.type = PowerType.BUFF;
        }
    }

    public void onInitialApplication() {
        if (this.amount < 0) {
            this.type = PowerType.DEBUFF;
        } else {
            this.type = PowerType.BUFF;
        }

        if (this.owner.hasPower(ShatterPower.POWER_ID)) {
            this.owner.getPower(ShatterPower.POWER_ID).updateDescription();
        }
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }

        if (this.amount < 0) {
            this.type = PowerType.DEBUFF;
        } else {
            this.type = PowerType.BUFF;
        }

        if (this.owner.hasPower(ShatterPower.POWER_ID)) {
            this.owner.getPower(ShatterPower.POWER_ID).updateDescription();
        }
    }

    public void updateDescription() {
        if (this.amount > 0) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
            this.type = PowerType.BUFF;
        } else {
            int tmp = -this.amount;
            this.description = DESCRIPTIONS[0] + tmp + DESCRIPTIONS[2];
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ResistancePower(owner, amount);
    }
}