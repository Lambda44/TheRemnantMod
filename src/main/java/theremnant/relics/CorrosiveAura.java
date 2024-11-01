package theremnant.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theremnant.character.TheRemnant;
import theremnant.powers.ResistancePower;

import java.util.Iterator;

import static theremnant.RemnantMod.makeID;

public class CorrosiveAura extends BaseRelic{
    private static final String NAME = "CorrosiveAura";
    public static final String ID = makeID(NAME);

    public CorrosiveAura() {
        super(ID, NAME, TheRemnant.Enums.CARD_COLOR, RelicTier.SHOP, LandingSound.MAGICAL);
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
            if (m.hasPower("Artifact")) {
                this.addToTop(new RelicAboveCreatureAction(m, this));
            }
            this.addToTop(new RemoveSpecificPowerAction(m, m, "Artifact"));
        }
        AbstractDungeon.onModifyPower();
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
    }
}
