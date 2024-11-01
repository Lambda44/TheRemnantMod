package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static theremnant.RemnantMod.makeID;

public class ResiliencePower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("Resilience");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static boolean activated = false;

    public ResiliencePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void atStartOfTurn() {
        activated = false;
    }

    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power.ID.equals(ResistancePower.POWER_ID) && !activated && power.amount > 0) {
            this.flash();
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new ResistancePower(this.owner, this.amount), this.amount));
            activated = true;
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ResiliencePower(owner, amount);
    }
}