package theremnant.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import com.megacrit.cardcrawl.vfx.combat.WaterDropEffect;

import static theremnant.RemnantMod.makeID;

public class BonesPower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("Bones");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public BonesPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        this.canGoNegative = true;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    public void atStartOfTurn() {
        updateDescription();
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (this.owner.hasPower(FreezeBonesPower.POWER_ID) && this.amount < 0) {
            CardCrawlGame.sound.playA("BELL", MathUtils.random(0.2F, 0.3F));
            this.addToBot(new DamageAction(this.owner, new DamageInfo(this.source, -1 * this.amount, DamageInfo.DamageType.THORNS)));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    public void onVictory() {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.amount < 0) {
            CardCrawlGame.sound.playA("BELL", MathUtils.random(0.2F, 0.3F));
            p.maxHealth -= (-this.amount);
            if (p.currentHealth > p.maxHealth) {
                p.currentHealth = p.maxHealth;
            }
        }
    }

    public void updateDescription() {
        String desc = DESCRIPTIONS[0];
        if (this.amount < 0) {
            if (this.owner.hasPower(FreezeBonesPower.POWER_ID)) {
                desc += DESCRIPTIONS[3] + (-this.amount) + DESCRIPTIONS[4];
            } else {
                desc += DESCRIPTIONS[1] + (-this.amount) + DESCRIPTIONS[2];
            }
        }
        this.description = desc;
    }

    @Override
    public AbstractPower makeCopy() {
        return new BonesPower(owner, amount);
    }
}