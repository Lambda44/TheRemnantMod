package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.StarBounceEffect;

import static theremnant.RemnantMod.makeID;

public class ShatterPower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("Shatter");
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;

    public ShatterPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void atStartOfTurn() {
        if (!(this.owner.hasPower(FracturePower.POWER_ID))) {
            this.flash();
            this.amount /= 2;
        }
        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        updateDescription();
    }

    public void onInitialApplication() {
        AbstractPlayer p = AbstractDungeon.player;
        int resistance = 0;
        if (this.owner.hasPower(ResistancePower.POWER_ID)) {
            resistance = this.owner.getPower(ResistancePower.POWER_ID).amount;
        }
        if (this.amount >= (10 + resistance)) {
            AbstractMonster mo = (AbstractMonster) this.owner;
            for (int i = 0; i < 20; ++i) {
                AbstractDungeon.effectsQueue.add(new StarBounceEffect(mo.hb.cX, mo.hb.cY));
            }
            addToBot(new StunMonsterAction(mo, AbstractDungeon.player, 1));
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            addToBot(new ApplyPowerAction(this.owner, this.owner, new ResistancePower(this.owner, 2), 2));
        }
        updateDescription();

    }

    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        AbstractPlayer p = AbstractDungeon.player;
        int resistance = 0;
        if (this.owner.hasPower(ResistancePower.POWER_ID)) {
            resistance = this.owner.getPower(ResistancePower.POWER_ID).amount;
        }
        if (this.amount >= (10 + resistance)) {
            AbstractMonster mo = (AbstractMonster) this.owner;
            for (int i = 0; i < 20; ++i) {
                AbstractDungeon.effectsQueue.add(new StarBounceEffect(mo.hb.cX, mo.hb.cY));
            }
            addToBot(new StunMonsterAction(mo, AbstractDungeon.player, 1));
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            addToBot(new ApplyPowerAction(this.owner, this.owner, new ResistancePower(this.owner, 2), 2));
        }
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        updateDescription();

    }

    public void updateDescription() {
        int resistance = 0;
        if (this.owner.hasPower(ResistancePower.POWER_ID)) {
            resistance = this.owner.getPower(ResistancePower.POWER_ID).amount;
        }
        int stunReq = 10 + resistance;
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + stunReq + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ShatterPower(owner, amount);
    }
}