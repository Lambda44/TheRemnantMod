package theremnant.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.GlowyFireEyesEffect;
import com.megacrit.cardcrawl.vfx.StaffFireEffect;
import com.megacrit.cardcrawl.vfx.combat.BloodShotEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import theremnant.RemnantMod;
import theremnant.cards.SoulSap;
import theremnant.powers.*;
import java.util.Random;

public class VoidWarden extends AbstractMonster {
    public static final String ID = RemnantMod.makeID("VoidWarden");
    public static final String NAME = "Void Warden";
    public static final String[] MOVES = {};
    public static final String[] DIALOG = {
            "@The@ @Void@ @Fog@ @approaches...@", //Void Fog = 1
            "@YOUR@ @MEANINGLESS@ @RESISTANCE@ @IS@ @OVER!!!@", //Void Collapse
            "~So,~ ~you've~ ~finally~ ~arrived...~", //first attack
            "What makes you think this time will be different?", //atk
            "Your actions are meaningless, you know.", //debuff
            "You'll just fight the same enemies over and over...",//atk
            "Thinking you'll have a chance...",//debuff
            "@...just@ @to@ @die@ @to@ @ME!@",//atk
            "~Why~ ~are~ ~you~ ~even~ ~trying?~",//debuff
            "Just accept your fate and @DIE.@"//atk
    };
    private int say = 2;
    public static final String[] STUNNED_DIALOG = {
            "Rghhh... Not bad...",
            "A cheap blow...",
            "You must be desperate, huh...?",
            "Don't underestimate me...",
            "A god does not fear death!",
            "You're just afraid of my power...",
            "@Is@ @that@ @all@ @you@ @got???@",
            "HA! That's @nothing!@"
    };
    private boolean stunTriggered = false;
    private boolean dyingWords = false;
    private boolean voidFogDialogue = false;

    private static final byte PLATINUM_STAR = 1;
    private static final byte ENCHANT = 2;
    private static final byte SQUASH = 3;
    private static final byte VOID_COLLAPSE = 4;
    private boolean isFirstMove = true;
    private int moveCount = 0;
    private float fireTimer = 0.0F;
    private float voidFogTimer = 0.0F;

    public static int HP = 1350;
    private final int platinumStarAmt;
    private final int voidCollapseAmt;
    private final int sapAmount;
    private final int invincibleAmt;

    public VoidWarden() {
        super(NAME, ID, HP, 30.0F, -30.0F, 476.0F, 410.0F, RemnantMod.resourcePath("monsters/VoidWarden.png"));
        //^^constructor can have 2 additional parameters, which are for offsetX and offsetY
        //if Void Warden ever gets animated, put that stuff there
        this.loadAnimation(RemnantMod.resourcePath("monsters/VoidWarden/skeleton.atlas"), RemnantMod.resourcePath("monsters/VoidWarden/skeleton.json"), 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        this.dialogX = -90.0F * Settings.scale;
        this.dialogY = 20.0F * Settings.scale;

        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(1500);
        } else {
            this.setHp(1350);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.damage.add(0, new DamageInfo(this, 30)); //squash damage
            this.platinumStarAmt = 11; //amount of hits
        } else {
            this.damage.add(0, new DamageInfo(this, 25)); //squash damage
            this.platinumStarAmt = 9; //amount of hits
        }
        this.damage.add(1, new DamageInfo(this, 3)); //platinum star damage
        this.damage.add(2, new DamageInfo(this, 40)); //void collapse damage
        this.voidCollapseAmt = 99; //amount of void collapse hits

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.sapAmount = 3;
            this.invincibleAmt = 300;
        } else {
            this.sapAmount = 2;
            this.invincibleAmt = 400;
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        if (RemnantMod.customBossBGM) {
            AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_VOID_WARDEN");
        } else {
            AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_ENDING");
        }
        int voidFogAmt = 13;
        int hexedAmt = 4;
        int stanceAmt = 60;
        if (AbstractDungeon.ascensionLevel >= 19) {
            voidFogAmt--;
            hexedAmt--;
            stanceAmt += 15;
        }

        //apply all the powers
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, invincibleAmt), invincibleAmt));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StancePower(this, stanceAmt), stanceAmt));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HexedPower(this, hexedAmt), hexedAmt));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VoidFogPower(this, voidFogAmt), voidFogAmt));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ResiliencePower(this, 1), 1));
    }

    @Override
    public void takeTurn() {
        if (!this.hasPower(VoidFogPower.POWER_ID) && !voidFogDialogue) { //void collapse dialogue
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 3.0F, 3.0F));
            voidFogDialogue = true;
        } else if (this.hasPower(VoidFogPower.POWER_ID) && this.getPower(VoidFogPower.POWER_ID).amount == 1) { //1 turn from void collapse dialogue
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 3.0F, 3.0F));
        } else { //normal dialogue
            if (say < DIALOG.length) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[say], 3.0F, 3.0F));
                ++say;
            }
        }

        label37:
        switch(this.nextMove) {
            case 1: //Platinum Star
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.platinumStarAmt), 0.25F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.platinumStarAmt), 0.6F));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FreezeBonesPower(AbstractDungeon.player, 2), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FreezeSoulPower(AbstractDungeon.player, 2), 2));
                AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(new Color(1.0F, 0.1F, 1.0F, 1.0F)));

                int i = 0;
                while(true) {
                    if (i >= this.platinumStarAmt) {
                        break label37;
                    }
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                    ++i;
                }
            case 2: //Enchant
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new InflameEffect(this), 1.0F));
                int additionalAmount = 0;
                if (this.hasPower("Strength") && this.getPower("Strength").amount < 0) {
                    additionalAmount = -this.getPower("Strength").amount;
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, additionalAmount + 2), additionalAmount + 2));
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, GainStrengthPower.POWER_ID));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ConstrictedPower(AbstractDungeon.player, this, 2), 2));
                AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(new Color(1.0F, 0.8F, 0.1F, 1.0F)));
                break;
            case 3: //Squash
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ViceCrushEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 1, true), 1));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 1, true), 1));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new SoulSap(), sapAmount, true, true, false));
                AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(new Color(1.0F, 0.1F, 1.0F, 1.0F)));
                break;
            case 4: //Void Collapse
                AbstractDungeon.actionManager.addToTop(new TalkAction(this, "PERISH."));
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.voidCollapseAmt), 0.25F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.voidCollapseAmt), 0.6F));
                }

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GainStrengthPower(this, 10), 10));
                int j = 0;
                while(true) {
                    if (j >= this.voidCollapseAmt) {
                        break label37;
                    }
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                    ++j;
                }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (this.isFirstMove) {
            this.setMove((byte) 3, Intent.ATTACK_DEBUFF, ((DamageInfo) this.damage.get(0)).base); //Squash
            this.isFirstMove = false;
        } else if (this.hasPower(VoidFogPower.POWER_ID)) {
            switch (this.moveCount % 2) {
                case 0:
                    this.setMove((byte) 1, Intent.ATTACK_DEBUFF, ((DamageInfo) this.damage.get(1)).base, this.platinumStarAmt, true); //Platinum Star
                    break;
                case 1:
                    this.setMove((byte) 2, Intent.DEBUFF); //Enchant
            }
            this.moveCount++;
        } else {
            this.setMove((byte) 4, Intent.ATTACK_BUFF, ((DamageInfo) this.damage.get(2)).base, this.voidCollapseAmt, true); //Void Collapse
        }
        stunTriggered = false;
    }

    public void update() {
        super.update();
        if (!this.isDying) {
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            this.voidFogTimer -= Gdx.graphics.getDeltaTime();
            if (this.fireTimer < 0.0F) {
                this.fireTimer = 0.07F;
                AbstractDungeon.effectList.add(new GlowyFireEyesEffect(this.skeleton.getX() + this.skeleton.findBone("lefteyefireslot").getX(), this.skeleton.getY() + this.skeleton.findBone("lefteyefireslot").getY() + 140.0F * Settings.scale));
                AbstractDungeon.effectList.add(new GlowyFireEyesEffect(this.skeleton.getX() + this.skeleton.findBone("righteyefireslot").getX(), this.skeleton.getY() + this.skeleton.findBone("righteyefireslot").getY() + 140.0F * Settings.scale));
                //AbstractDungeon.effectList.add(new StaffFireEffect(this.skeleton.getX() + this.skeleton.findBone("fireslot").getX() - 110.0F * Settings.scale, this.skeleton.getY() + this.skeleton.findBone("fireslot").getY() + 310.0F * Settings.scale));
            }
            if (this.voidFogTimer < 0.0F && !this.hasPower(VoidFogPower.POWER_ID)) {
                this.voidFogTimer = 0.5F;
                AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.BLACK.cpy(), 0.8F));
            }

            if (this.hasPower(StunMonsterPower.POWER_ID) && stunTriggered == false) {
                stunTriggered = true;
                Random rand = new Random();
                int randTalk = rand.nextInt(STUNNED_DIALOG.length);
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, STUNNED_DIALOG[randTalk], 3.0F, 3.0F));
            }
        } else if (this.isDying && !dyingWords) {
            dyingWords = true;
            AbstractDungeon.actionManager.addToTop(new TalkAction(this, "@IMPOSSIBLE!!!!!@"));
        }
    }

    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();
            CardCrawlGame.stopClock = true;
        }
    }
}
