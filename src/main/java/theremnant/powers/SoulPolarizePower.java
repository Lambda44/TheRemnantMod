package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static theremnant.RemnantMod.makeID;

public class SoulPolarizePower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("SoulPolarize");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public SoulPolarizePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if ((!this.owner.hasPower(SoulPower.POWER_ID)) && this.amount > 1) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new SoulPower(this.owner, this.amount - 1), this.amount - 1));
        }
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + (this.amount - 1) + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new SoulPolarizePower(owner, amount);
    }
}