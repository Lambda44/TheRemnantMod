package theremnant.relics;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theremnant.character.TheRemnant;

import java.util.Iterator;

import static theremnant.RemnantMod.makeID;

public class Magnitude9 extends BaseRelic{
    private static final String NAME = "Magnitude9";
    public static final String ID = makeID(NAME);

    public Magnitude9() {
        super(ID, NAME, TheRemnant.Enums.CARD_COLOR, RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.counter = -1;
    }

    public void atBattleStart() {
        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            this.addToTop(new RelicAboveCreatureAction(m, this));
            this.addToTop(new StunMonsterAction(m, AbstractDungeon.player, 1));
        }
        AbstractDungeon.onModifyPower();

        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VulnerablePower(AbstractDungeon.player, 2, false), 2));
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
    }
}
