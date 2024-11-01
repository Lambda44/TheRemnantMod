package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theremnant.actions.SacrificeAction;

import static theremnant.RemnantMod.makeID;

public class SacrificeNextTurnPower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("SacrificeNextTurn");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public SacrificeNextTurnPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void atStartOfTurn() {
        this.flash();
        this.addToBot(new SacrificeAction(this.amount));
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SacrificeNextTurnPower(owner, amount);
    }
}