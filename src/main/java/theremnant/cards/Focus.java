package theremnant.cards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.GrandFinalEffect;
import org.apache.logging.log4j.Level;
import theremnant.RemnantMod;
import theremnant.actions.UltimateFocusAction;
import theremnant.character.TheRemnant;
import theremnant.powers.SoulPower;
import theremnant.util.CardInfo;
import theremnant.util.TextureLoader;

import static theremnant.RemnantMod.makeID;
import static theremnant.RemnantMod.resourcePath;

public class Focus extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Focus", //Card ID. Will be prefixed with mod id, so the final ID will be "modID:MyCard" with whatever your mod's ID is.
            1, //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
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
    private static final int MAGIC = 8;

    public Focus() {
        this(false);
    }

    public Focus(boolean transcend) {
        super(cardInfo); //Pass the cardInfo to the BaseCard constructor. use true as a 2nd argument to change description
        setMagic(MAGIC);
        setCustomVar("upgrades", VariableType.MAGIC, 4); //can be upgraded X times

        if (!transcend) {
            AbstractCard preview = new Focus(true);
            for (int i = 0; i < customVar("upgrades"); ++i) {
                preview.upgrade();
            }
            this.cardsToPreview = preview;
        }

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[1] + cardStrings.EXTENDED_DESCRIPTION[2];
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.timesUpgraded >= customVar("upgrades") && p.hasPower(SoulPower.POWER_ID)) { //Soulcast on final upgrade only
            addToBot(new UltimateFocusAction());
            if (Settings.FAST_MODE) {
                this.addToBot(new VFXAction(new GrandFinalEffect(), 0.7F));
            } else {
                this.addToBot(new VFXAction(new GrandFinalEffect(), 1.0F));
            }
        }
        addToBot(new ApplyPowerAction(p, p, new SoulPower(p, this.magicNumber), this.magicNumber));
    }

    public void triggerOnGlowCheck() {
        if (this.timesUpgraded >= customVar("upgrades") && AbstractDungeon.player.hasPower(SoulPower.POWER_ID)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void upgrade() {
        if (this.timesUpgraded < customVar("upgrades")) {
            this.upgradeMagicNumber(3);
            ++this.timesUpgraded;
        }
        if (this.timesUpgraded >= customVar("upgrades")) {
            name = cardStrings.EXTENDED_DESCRIPTION[0];
            this.initializeTitle();
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[3] + cardStrings.DESCRIPTION;
            this.initializeDescription();
            this.cardsToPreview = null;
            loadCardImage(resourcePath("cards/skill/Transcendence.png"));
        } else {
            this.name = cardStrings.NAME + "+" + this.timesUpgraded;
            this.initializeTitle();
        }
        this.upgraded = true;
    }

    @Override
    public boolean canUpgrade() {
        if (this.timesUpgraded < customVar("upgrades")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected Texture getPortraitImage() {
        if (this.timesUpgraded >= customVar("upgrades")) {
            String path = TextureLoader.getCardTextureString("Transcendence_p", CardType.SKILL);
            return TextureLoader.getTexture(path);
        } else {
            return super.getPortraitImage();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Focus();
    }
}