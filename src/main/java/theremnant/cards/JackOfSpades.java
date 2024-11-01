package theremnant.cards;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theremnant.RemnantMod;
import theremnant.character.TheRemnant;
import theremnant.powers.ShatterPower;
import theremnant.powers.SoulPower;
import theremnant.util.CardInfo;

import java.util.Collections;
import java.util.List;

import static theremnant.RemnantMod.makeID;

public class JackOfSpades extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "JackOfSpades", //Card ID. Will be prefixed with mod id, so the final ID will be "modID:MyCard" with whatever your mod's ID is.
            1, //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
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
    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 2;

    private static final int BLOCK = 4;
    private static final int UPG_BLOCK = 2;

    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 1;

    public JackOfSpades() {
        super(cardInfo); //Pass the cardInfo to the BaseCard constructor. use true as a 2nd argument to change description

        setDamage(DAMAGE, UPG_DAMAGE);
        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if (p.hasPower(SoulPower.POWER_ID)) { //Soulcast
            addToBot(new ApplyPowerAction(m, p, new ShatterPower(m, this.magicNumber), this.magicNumber));
        }
        addToBot(new ApplyPowerAction(p, p, new SoulPower(p, this.magicNumber), this.magicNumber));
    }

    public void triggerOnGlowCheck() {
        if (AbstractDungeon.player.hasPower(SoulPower.POWER_ID)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
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
        return new JackOfSpades();
    }
}