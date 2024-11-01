package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static theremnant.RemnantMod.makeID;

public class AdeptRitualistPower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("AdeptRitualist");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public AdeptRitualistPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        this.flash();
        int dmg = 0;
        if (this.owner.hasPower(BonesPower.POWER_ID) && this.owner.getPower(BonesPower.POWER_ID).amount > 0) {
            dmg = this.owner.getPower(BonesPower.POWER_ID).amount / 2;
        }

        for(int i = 0; i < amount; ++i) {
            addToBot(new DamageAllEnemiesAction(this.owner, DamageInfo.createDamageMatrix(dmg, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
        }
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
        } else {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new AdeptRitualistPower(owner, amount);
    }
}