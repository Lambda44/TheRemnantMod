package theremnant.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.StarBounceEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import theremnant.character.TheRemnant;
import theremnant.powers.BonesPower;
import theremnant.powers.PermafrostPower;
import theremnant.powers.SoulPower;
import theremnant.util.CardInfo;

import static theremnant.RemnantMod.makeID;

public class BoneStrike extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "BoneStrike", //Card ID. Will be prefixed with mod id, so the final ID will be "modID:MyCard" with whatever your mod's ID is.
            0, //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
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
    private static final int DAMAGE = 0;

    private static final int MAGIC = 0;
    private static final int UPG_MAGIC = 5;

    public BoneStrike() {
        super(cardInfo); //Pass the cardInfo to the BaseCard constructor. use true as a 2nd argument to change description

        setDamage(DAMAGE); //Sets the card's damage and how much it increases when upgraded.
        setMagic(MAGIC, UPG_MAGIC);
        setExhaust(true);
        tags.add(CardTags.STRIKE); //This tag marks it as a Strike card for the purposes of Perfected Strike and any similar modded effects
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int dmg = 0;
        if (p.hasPower(BonesPower.POWER_ID)) {
            dmg = p.getPower(BonesPower.POWER_ID).amount;
        }
        dmg += this.magicNumber;
        this.baseDamage = dmg;
        calculateCardDamage((AbstractMonster)null);

        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    public void applyPowers() {
        int boneAmt = 0;
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(BonesPower.POWER_ID)) {
            boneAmt = p.getPower(BonesPower.POWER_ID).amount;
        }

        this.baseDamage = boneAmt;
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new BoneStrike();
    }
}