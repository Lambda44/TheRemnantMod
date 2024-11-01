package theremnant.character;

import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.BurningBlood;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import theremnant.cards.Defend;
import theremnant.cards.Soulbolt;
import theremnant.cards.SoulShield;
import theremnant.cards.Strike;
import theremnant.relics.Invigoration;

import java.util.ArrayList;

import static theremnant.RemnantMod.characterPath;
import static theremnant.RemnantMod.makeID;

public class TheRemnant extends CustomPlayer {
    //Stats
    public static final int ENERGY_PER_TURN = 3;
    public static final int MAX_HP = 80;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    //Strings
    private static final String ID = makeID("Remnant"); //This should match whatever you have in the CharacterStrings.json file
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    //Image file paths
    private static final String SHOULDER_1 = characterPath("shoulder.png"); //Shoulder 1 and 2 are used at rest sites.
    private static final String SHOULDER_2 = characterPath("shoulder2.png");
    private static final String CORPSE = characterPath("corpse.png"); //Corpse is when you die.

    //Energy Orb file paths
    public static final String[] orbTextures = {
            characterPath("energy/layer1.png"),
            characterPath("energy/layer2.png"),
            characterPath("energy/layer3.png"),
            characterPath("energy/layer4.png"),
            characterPath("energy/layer5.png"),
            characterPath("energy/layer6.png"),
            characterPath("energy/layer1d.png"),
            characterPath("energy/layer2d.png"),
            characterPath("energy/layer3d.png"),
            characterPath("energy/layer4d.png"),
            characterPath("energy/layer5d.png") };

    public static class Enums {
        //These are used to identify your character, as well as your character's card color.
        //Library color is basically the same as card color, but you need both because that's how the game was made.
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_REMNANT;
        @SpireEnum(name = "CHARACTER_SEAFOAM_COLOR") // These two MUST match. Change it to something unique for your character.
        public static AbstractCard.CardColor CARD_COLOR;
        @SpireEnum(name = "CHARACTER_SEAFOAM_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    public TheRemnant() {
        super(NAMES[0], Enums.THE_REMNANT,
                new CustomEnergyOrb(orbTextures, characterPath("energy/vfx.png"), null), //Energy Orb
                new SpriterAnimation(characterPath("animation/remnant.scml"))); //Animation

        initializeClass(null,
                SHOULDER_2,
                SHOULDER_1,
                CORPSE,
                getLoadout(),
                20.0F, -20.0F, 260.0F, 320.0F, //Character hitbox. x y position, then width and height.
                new EnergyManager(ENERGY_PER_TURN));

        //Location for text bubbles. You can adjust it as necessary later. For most characters, these values are fine.
        dialogX = (drawX + 50.0F * Settings.scale);
        dialogY = (drawY + 210.0F * Settings.scale);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        //List of IDs of cards for your starting deck.
        //If you want multiple of the same card, you have to add it multiple times.
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Soulbolt.ID);
        retVal.add(SoulShield.ID);

        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        //IDs of starting relics. You can have multiple, but one is recommended.
        retVal.add(Invigoration.ID);

        return retVal;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        //This card is used for the Gremlin card matching game.
        //It should be a non-strike non-defend starter card, but it doesn't have to be.
        return new Soulbolt();
    }

    /*- Below this is methods that you should *probably* adjust, but don't have to. -*/

    @Override
    public int getAscensionMaxHPLoss() {
        return 5; //Max hp reduction at ascension 14+
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        //These attack effects will be used when you attack the heart.
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    private final Color cardRenderColor = Color.LIGHT_GRAY.cpy(); //Used for some vfx on moving cards (sometimes) (maybe)
    private final Color cardTrailColor = Color.LIGHT_GRAY.cpy(); //Used for card trail vfx during gameplay.
    private final Color slashAttackColor = Color.LIGHT_GRAY.cpy(); //Used for a screen tint effect when you attack the heart.
    @Override
    public Color getCardRenderColor() {
        return cardRenderColor;
    }

    @Override
    public Color getCardTrailColor() {
        return cardTrailColor;
    }

    @Override
    public Color getSlashAttackColor() {
        return slashAttackColor;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        //Font used to display your current energy.
        //energyNumFontRed, Blue, Green, and Purple are used by the basegame characters.
        //It is possible to make your own, but not convenient.
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        //This occurs when you click the character's button in the character select screen.
        //See SoundMaster for a full list of existing sound effects, or look at BaseMod's wiki for adding custom audio.
        CardCrawlGame.sound.playA("ATTACK_FIRE", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        //Similar to doCharSelectScreenSelectEffect, but used for the Custom mode screen. No shaking.
        return "ATTACK_FIRE";
    }

    //Don't adjust these four directly, adjust the contents of the CharacterStrings.json file.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }
    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAMES[1];
    }
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }
    @Override
    public String getVampireText() {
        return TEXT[2]; //Generally, the only difference in this text is how the vampires refer to the player.
    }

    /*- You shouldn't need to edit any of the following methods. -*/

    //This is used to display the character's information on the character selection screen.
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                MAX_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.CARD_COLOR;
    }

    @Override
    public AbstractPlayer newInstance() {
        //Makes a new instance of your character class.
        return new TheRemnant();
    }
}
