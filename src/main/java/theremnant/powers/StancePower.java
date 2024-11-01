package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import theremnant.cards.Hexbreaker;

import static theremnant.RemnantMod.makeID;

public class StancePower extends BasePower implements CloneablePowerInterface, HealthBarRenderPower {
    public static final String POWER_ID = makeID("Stance");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static final int hpLoss = 100;
    private int maxAmt;
    private static boolean canBreak;

    public StancePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        this.maxAmt = amount;
        canBreak = true;
        this.canGoNegative = true;
    }

    public void atStartOfTurn() {
        if (!this.owner.hasPower(StunMonsterPower.POWER_ID)) {
            this.amount = maxAmt;
            canBreak = true; //i think this should fix the Stun auto-Stance break bug?
        }
        updateDescription();
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (canBreak && info.type != DamageInfo.DamageType.HP_LOSS) {
            this.amount -= damageAmount;
            if (this.amount <= 0) {
                this.amount = 0;
                this.flash();
                this.owner.damage(new DamageInfo(this.owner, hpLoss, DamageInfo.DamageType.HP_LOSS));
                this.addToBot(new VFXAction(new ViolentAttackEffect(this.owner.hb.cX, this.owner.hb.cY, Color.GREEN)));
                if (this.owner.hasPower(HexedPower.POWER_ID)) {
                    addToBot(new MakeTempCardInHandAction(new Hexbreaker(), 1));
                    this.owner.getPower(HexedPower.POWER_ID).amount--;
                    this.owner.getPower(HexedPower.POWER_ID).updateDescription();
                    if (this.owner.getPower(HexedPower.POWER_ID).amount <= 0) {
                        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, HexedPower.POWER_ID));
                    }
                }
                canBreak = false;
            }
        }
        this.updateDescription();
        return damageAmount;
    }

    public void updateDescription() {
        if (this.amount <= 0) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        }
    }

    @Override
    public int getHealthBarAmount() {
        return this.amount;
    }

    @Override
    public Color getColor() {
        return CardHelper.getColor(255.0f, 255.0f, 255.0f);
    }

    @Override
    public AbstractPower makeCopy() {
        return new StancePower(owner, amount);
    }
}