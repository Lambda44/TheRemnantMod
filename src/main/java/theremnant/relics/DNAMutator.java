package theremnant.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theremnant.character.TheRemnant;
import theremnant.powers.ResistancePower;
import theremnant.powers.SoulPower;

import java.util.Iterator;

import static theremnant.RemnantMod.makeID;

public class DNAMutator extends BaseRelic{
    private static final String NAME = "DNAMutator";
    public static final String ID = makeID(NAME);

    public DNAMutator() {
        super(ID, NAME, TheRemnant.Enums.CARD_COLOR, RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.counter = -1;
    }

    public void updateDescription(AbstractPlayer.PlayerClass c) {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    public void atBattleStart() {
        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            this.addToBot(new RelicAboveCreatureAction(m, this));
            this.addToBot(new ApplyPowerAction(m, m, new ResistancePower(m, -2), -2));
        }

        AbstractDungeon.onModifyPower();
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
    }
}
