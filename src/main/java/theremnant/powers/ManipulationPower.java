package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

import static theremnant.RemnantMod.makeID;

public class ManipulationPower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("Manipulation");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public ManipulationPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        int amt = 0;
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster mo = (AbstractMonster)var1.next();
            if (mo.hasPower(ShatterPower.POWER_ID)) {
                if (mo.getPower(ShatterPower.POWER_ID).amount > amt) {
                    amt = mo.getPower(ShatterPower.POWER_ID).amount;
                }
            }
        }

        if (amt > 0) {
            this.flash();
            for (int i = 0; i < amount; ++i) {
                this.addToBot(new GainBlockAction(this.owner, this.owner, amt));
            }
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
        return new ManipulationPower(owner, amount);
    }
}