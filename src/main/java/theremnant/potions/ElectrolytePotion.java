package theremnant.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theremnant.RemnantMod;
import theremnant.powers.SoulPower;

import static theremnant.RemnantMod.makeID;

public class ElectrolytePotion extends BasePotion{
    public static final String ID = makeID("ElectrolytePotion");
    private static final Color LIQUID_COLOR = CardHelper.getColor(113, 245, 113);
    private static final Color HYBRID_COLOR = null;
    private static final Color SPOTS_COLOR = CardHelper.getColor(0, 0, 0);

    public ElectrolytePotion() {
        super(ID, 15, PotionRarity.COMMON, PotionSize.M, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulPower(AbstractDungeon.player, this.potency), this.potency));
        }
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];
    }

    @Override
    public void addAdditionalTips() {
        this.tips.add(new PowerTip(RemnantMod.keywords.get("soul").PROPER_NAME, RemnantMod.keywords.get("soul").DESCRIPTION));
    }

}
