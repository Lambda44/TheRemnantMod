package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theremnant.actions.SacrificeAction;

import java.util.Iterator;

import static theremnant.RemnantMod.makeID;

public class GorgePower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("Gorge");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private int sacCost;

    public GorgePower(AbstractCreature owner, int sacCost, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        this.sacCost = sacCost;
    }

    public void atStartOfTurn() {
        this.flash();
        this.addToBot(new SacrificeAction(sacCost));
        this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.sacCost + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new GorgePower(owner, sacCost, amount);
    }
}