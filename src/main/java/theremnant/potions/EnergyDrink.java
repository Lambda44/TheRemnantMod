package theremnant.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theremnant.RemnantMod;
import theremnant.powers.SoulPower;

import static theremnant.RemnantMod.makeID;

public class EnergyDrink extends BasePotion{
    public static final String ID = makeID("EnergyDrink");
    private static final Color LIQUID_COLOR = CardHelper.getColor(255, 0, 0);
    private static final Color HYBRID_COLOR = CardHelper.getColor(0, 255, 0);
    private static final Color SPOTS_COLOR = null;

    public EnergyDrink() {
        super(ID, 2, PotionRarity.UNCOMMON, PotionSize.BOLT, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.potency), this.potency));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, this.potency), this.potency));
        }
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1] + this.potency + potionStrings.DESCRIPTIONS[2];
    }

    @Override
    public void addAdditionalTips() {
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.STRENGTH.NAMES[0]), GameDictionary.keywords.get(GameDictionary.STRENGTH.NAMES[0])));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.DEXTERITY.NAMES[0]), GameDictionary.keywords.get(GameDictionary.DEXTERITY.NAMES[0])));
    }

}
