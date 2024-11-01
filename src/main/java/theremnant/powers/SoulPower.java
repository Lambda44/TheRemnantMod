package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;
import com.megacrit.cardcrawl.vfx.GlowyFireEyesEffect;
import com.megacrit.cardcrawl.vfx.StaffFireEffect;
import theremnant.effects.SoulGlowingEye;

import static theremnant.RemnantMod.makeID;

public class SoulPower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("Soul");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private float fireTimer = 0.0F;
    private boolean surrounded = false;

    public SoulPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void atStartOfTurn() {
        updateDescription();
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (owner.hasPower(SoulPolarizePower.POWER_ID)) {
            this.amount = this.amount - 1 + owner.getPower(SoulPolarizePower.POWER_ID).amount;
            if (this.owner.hasPower(StabilizePower.POWER_ID) && owner.getPower(SoulPolarizePower.POWER_ID).amount > 1) {
                int blockAmt = this.owner.getPower(StabilizePower.POWER_ID).amount;
                this.addToBot(new GainBlockAction(this.owner, this.owner, blockAmt));
            }
        }
        else if (!(owner.hasPower(PermafrostPower.POWER_ID))) {
            --this.amount;
        }

        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        updateDescription();
    }

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        this.flash();
        AbstractCreature m = AbstractDungeon.getRandomMonster();
        if (this.owner.hasPower(FreezeSoulPower.POWER_ID) && !this.owner.hasPower(PermafrostPower.POWER_ID)) {
            this.owner.getPower(FreezeSoulPower.POWER_ID).flash();
            this.amount /= 2;
        }
        if (m != null) {
            this.addToBot(new DamageAction(m, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    public void updateDescription() {
        String desc = DESCRIPTIONS[0];
        if (this.owner.hasPower(FreezeSoulPower.POWER_ID)) {
            desc += DESCRIPTIONS[1];
        }
        desc += DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
        if (!this.owner.hasPower(SoulPolarizePower.POWER_ID)) {
            desc += DESCRIPTIONS[4];
        } else {
            if (this.owner.getPower(SoulPolarizePower.POWER_ID).amount > 1) {
                int polarize = this.owner.getPower(SoulPolarizePower.POWER_ID).amount - 1;
                desc += DESCRIPTIONS[5] + polarize + DESCRIPTIONS[6];
            }
        }
        this.description = desc;
    }

    public void update(int slot) {
        super.update(slot);
        this.fireTimer -= Gdx.graphics.getDeltaTime();
        if (this.fireTimer < 0.0F) {
            this.fireTimer = 0.07F;
            if (this.owner.flipHorizontal) { //surrounded being true will flip the effects for Soul
                surrounded = true;
            } else {
                surrounded = false;
            }

            if (surrounded == false) {
                if (this.amount < 10) { //small fire in hand
                    AbstractDungeon.effectList.add(new GlowyFireEyesEffect(this.owner.hb.cX - 100.0F * Settings.scale, this.owner.hb.cY + 110.0F * Settings.scale));
                } else { //10+; big fire in hand
                    AbstractDungeon.effectList.add(new StaffFireEffect(this.owner.hb.cX - 95.0F * Settings.scale, this.owner.hb.cY + 130.0F * Settings.scale));
                    if (this.amount >= 20) { //glowing eye
                        AbstractDungeon.effectList.add(new SoulGlowingEye(this.owner.hb.cX + 42.0F * Settings.scale, this.owner.hb.cY + 72.0F * Settings.scale));
                    }
                }
            } else {
                if (this.amount < 10) { //small fire in hand
                    AbstractDungeon.effectList.add(new GlowyFireEyesEffect(this.owner.hb.cX + 65.0F * Settings.scale, this.owner.hb.cY + 110.0F * Settings.scale));
                } else { //10+; big fire in hand
                    AbstractDungeon.effectList.add(new StaffFireEffect(this.owner.hb.cX + 60.0F * Settings.scale, this.owner.hb.cY + 130.0F * Settings.scale));
                    if (this.amount >= 20) { //glowing eye
                        AbstractDungeon.effectList.add(new SoulGlowingEye(this.owner.hb.cX - 81.0F * Settings.scale, this.owner.hb.cY + 72.0F * Settings.scale));
                    }
                }
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new SoulPower(owner, amount);
    }
}