package theremnant.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theremnant.RemnantMod;
import theremnant.powers.ShatterPower;

import static theremnant.RemnantMod.makeID;

public class DebilitatingBrew extends BasePotion{
    public static final String ID = makeID("DebilitatingBrew");
    private static final Color LIQUID_COLOR = CardHelper.getColor(105, 27, 207);
    private static final Color HYBRID_COLOR = null;
    private static final Color SPOTS_COLOR = CardHelper.getColor(255, 255, 0);

    public DebilitatingBrew() {
        super(ID, 5, PotionRarity.RARE, PotionSize.H, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        isThrown = true;
        targetRequired = true;
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new ShatterPower(target, this.potency), this.potency));
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target, this.potency, false), this.potency));
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new WeakPower(target, this.potency, false), this.potency));
        }
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1]
                + this.potency + potionStrings.DESCRIPTIONS[2]
                + this.potency + potionStrings.DESCRIPTIONS[3];
    }

    @Override
    public void addAdditionalTips() {
        this.tips.add(new PowerTip(RemnantMod.keywords.get("shatter").PROPER_NAME, RemnantMod.keywords.get("shatter").DESCRIPTION));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.VULNERABLE.NAMES[0]), (String)GameDictionary.keywords.get(GameDictionary.VULNERABLE.NAMES[0])));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.WEAK.NAMES[0]), (String)GameDictionary.keywords.get(GameDictionary.WEAK.NAMES[0])));
    }

}
