package theremnant.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import com.megacrit.cardcrawl.vfx.combat.WaterDropEffect;
import org.apache.logging.log4j.Level;
import theremnant.RemnantMod;
import theremnant.powers.BoneBlockadePower;
import theremnant.powers.BonesPower;
import theremnant.powers.ScapegoatPower;

import java.util.Iterator;

public class SacrificeAction extends AbstractGameAction {
    private AbstractPlayer player;
    private int sacCost;

    public SacrificeAction(int sacCost) {
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.sacCost = sacCost;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(new Color(1.0F, 0.1F, 0.1F, 1.0F)));
            AbstractDungeon.effectsQueue.add(new WaterDropEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY + 250.0F * Settings.scale));
            AbstractDungeon.effectsQueue.add(new WaterDropEffect(AbstractDungeon.player.hb.cX + 150.0F * Settings.scale, AbstractDungeon.player.hb.cY - 80.0F * Settings.scale));
            AbstractDungeon.effectsQueue.add(new WaterDropEffect(AbstractDungeon.player.hb.cX - 200.0F * Settings.scale, AbstractDungeon.player.hb.cY + 50.0F * Settings.scale));

            if (player.hasPower(BoneBlockadePower.POWER_ID)) {
                int blockAmt = player.getPower(BoneBlockadePower.POWER_ID).amount;
                addToTop(new GainBlockAction(player, player, blockAmt));
            }

            if (player.hasPower(ScapegoatPower.POWER_ID)) {
                addToTop(new ReducePowerAction(player, player, ScapegoatPower.POWER_ID, 1));
            } else {
                addToTop(new ApplyPowerAction(player, player, new BonesPower(player, -sacCost), -sacCost));
            }
        }
        this.tickDuration();
    }

}
