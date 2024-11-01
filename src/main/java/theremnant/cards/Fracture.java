package theremnant.cards;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theremnant.RemnantMod;
import theremnant.character.TheRemnant;
import theremnant.powers.FracturePower;
import theremnant.powers.ShatterPower;
import theremnant.util.CardInfo;

import java.util.Collections;
import java.util.List;

import static theremnant.RemnantMod.makeID;

public class Fracture extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Fracture", //Card ID. Will be prefixed with mod id, so the final ID will be "modID:MyCard" with whatever your mod's ID is.
            1, //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            TheRemnant.Enums.CARD_COLOR //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or something similar for a basegame character color.
    );

    //This is theoretically optional, but you'll want it. The ID is how you refer to the card.
    //For example, to add a card to the starting deck, you need to use its ID.
    //With this, you can just use 'MyCard.ID'. Without it, you'd have to type out
    //'yourModID:MyCard' and make sure you don't make any mistakes, and you'd also have to update it
    //if you decided to change the card's ID.
    public static final String ID = makeID(cardInfo.baseId);

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    public Fracture() {
        super(cardInfo); //Pass the cardInfo to the BaseCard constructor. use true as a 2nd argument to change description

        setMagic(MAGIC, UPG_MAGIC);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new ShatterPower(m, 2), 2));
        addToBot(new ApplyPowerAction(m, p, new FracturePower(m, this.magicNumber), this.magicNumber));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        String keywordID = "theremnant:resistance";
        return Collections.singletonList(
                new TooltipInfo(
                        RemnantMod.keywords.get("resistance").PROPER_NAME,
                        RemnantMod.keywords.get("resistance").DESCRIPTION
                )
        );
    }

    @Override
    public AbstractCard makeCopy() {
        return new Fracture();
    }
}