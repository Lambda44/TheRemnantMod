package theremnant.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theremnant.character.TheRemnant;
import theremnant.powers.BonesPower;
import theremnant.powers.SoulPower;

import static theremnant.RemnantMod.makeID;

public class Mitigation extends BaseRelic{
    private static final String NAME = "Mitigation";
    public static final String ID = makeID(NAME);
    private static final int MAGIC = 2;

    public Mitigation() {
        super(ID, NAME, TheRemnant.Enums.CARD_COLOR, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void atTurnStart() {
        this.counter = 0;
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            ++this.counter;
            if (this.counter % 3 == 0) {
                this.flash();
                this.counter = 0;
                this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BonesPower(AbstractDungeon.player, MAGIC), MAGIC));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulPower(AbstractDungeon.player, MAGIC), MAGIC));
            }
        }
    }

    public void onVictory() {
        this.counter = -1;
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
    }
}
