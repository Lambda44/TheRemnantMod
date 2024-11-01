package theremnant.cards;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theremnant.RemnantMod;
import theremnant.util.CardInfo;
import theremnant.util.TriFunction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.HashMap;
import java.util.Map;

import static theremnant.RemnantMod.makeID;
import static theremnant.util.TextureLoader.getCardTextureString;


public abstract class BaseCard extends CustomCard {
    final private static Map<String, DynamicVariable> customVars = new HashMap<>();
    protected CardStrings cardStrings;

    protected boolean upgradesDescription;

    protected int baseCost;

    protected boolean upgradeCost;
    protected boolean upgradeDamage;
    protected boolean upgradeBlock;
    protected boolean upgradeMagic;

    protected int costUpgrade;
    protected int damageUpgrade;
    protected int blockUpgrade;
    protected int magicUpgrade;

    protected boolean baseExhaust = false;
    protected boolean upgExhaust = false;
    protected boolean baseEthereal = false;
    protected boolean upgEthereal = false;
    protected boolean baseInnate = false;
    protected boolean upgInnate = false;
    protected boolean baseRetain = false;
    protected boolean upgRetain = false;

    final protected Map<String, LocalVarInfo> cardVariables = new HashMap<>();

    public BaseCard(CardInfo cardInfo) {
        this(cardInfo.baseId, cardInfo.baseCost, cardInfo.cardType, cardInfo.cardTarget, cardInfo.cardRarity, cardInfo.cardColor);
    }
    public BaseCard(CardInfo cardInfo, boolean upgradesDescription)
    {
        this(cardInfo.baseId, cardInfo.baseCost, cardInfo.cardType, cardInfo.cardTarget, cardInfo.cardRarity, cardInfo.cardColor, upgradesDescription);
    }

    public BaseCard(String baseID, int cost, CardType cardType, CardTarget target, CardRarity rarity, CardColor color)
    {
        super(makeID(baseID), "", getCardTextureString(baseID, cardType), cost, "", cardType, color, rarity, target);

        loadStrings();

        this.baseCost = cost;

        this.upgradesDescription = cardStrings.UPGRADE_DESCRIPTION != null;
        this.upgradeCost = false;
        this.upgradeDamage = false;
        this.upgradeBlock = false;
        this.upgradeMagic = false;

        this.costUpgrade = cost;
        this.damageUpgrade = 0;
        this.blockUpgrade = 0;
        this.magicUpgrade = 0;

        initializeTitle();
        initializeDescription();
    }

    public BaseCard(String cardName, int cost, CardType cardType, CardTarget target, CardRarity rarity, CardColor color, boolean upgradesDescription)
    {
        super(makeID(cardName), "", getCardTextureString(cardName, cardType), cost, "", cardType, color, rarity, target);

        loadStrings();

        this.baseCost = cost;

        this.upgradesDescription = upgradesDescription;
        this.upgradeCost = false;
        this.upgradeDamage = false;
        this.upgradeBlock = false;
        this.upgradeMagic = false;

        this.costUpgrade = cost;
        this.damageUpgrade = 0;
        this.blockUpgrade = 0;
        this.magicUpgrade = 0;

        initializeTitle();
        initializeDescription();
    }

    private void loadStrings() {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(cardID);

        this.rawDescription = cardStrings.DESCRIPTION;
        this.originalName = cardStrings.NAME;
        this.name = originalName;
    }

    //Methods meant for constructor use
    protected final void setDamage(int damage)
    {
        this.setDamage(damage, 0);
    }
    protected final void setBlock(int block)
    {
        this.setBlock(block, 0);
    }
    protected final void setMagic(int magic)
    {
        this.setMagic(magic, 0);
    }
    protected final void setCostUpgrade(int costUpgrade)
    {
        this.costUpgrade = costUpgrade;
        this.upgradeCost = true;
    }
    protected final void setExhaust(boolean exhaust) { this.setExhaust(exhaust, exhaust); }
    protected final void setEthereal(boolean ethereal) { this.setEthereal(ethereal, ethereal); }
    protected final void setInnate(boolean innate) {this.setInnate(innate, innate); }
    protected final void setSelfRetain(boolean retain) {this.setSelfRetain(retain, retain); }

    protected final void setDamage(int damage, int damageUpgrade)
    {
        this.baseDamage = this.damage = damage;
        if (damageUpgrade != 0)
        {
            this.upgradeDamage = true;
            this.damageUpgrade = damageUpgrade;
        }
    }
    protected final void setBlock(int block, int blockUpgrade)
    {
        this.baseBlock = this.block = block;
        if (blockUpgrade != 0)
        {
            this.upgradeBlock = true;
            this.blockUpgrade = blockUpgrade;
        }
    }
    protected final void setMagic(int magic, int magicUpgrade)
    {
        this.baseMagicNumber = this.magicNumber = magic;
        if (magicUpgrade != 0)
        {
            this.upgradeMagic = true;
            this.magicUpgrade = magicUpgrade;
        }
    }
    protected final void setExhaust(boolean baseExhaust, boolean upgExhaust)
    {
        this.baseExhaust = baseExhaust;
        this.upgExhaust = upgExhaust;
        this.exhaust = baseExhaust;
    }
    protected final void setEthereal(boolean baseEthereal, boolean upgEthereal)
    {
        this.baseEthereal = baseEthereal;
        this.upgEthereal = upgEthereal;
        this.isEthereal = baseEthereal;
    }
    protected void setInnate(boolean baseInnate, boolean upgInnate)
    {
        this.baseInnate = baseInnate;
        this.upgInnate = upgInnate;
        this.isInnate = baseInnate;
    }
    protected void setSelfRetain(boolean baseRetain, boolean upgRetain)
    {
        this.baseRetain = baseRetain;
        this.upgRetain = upgRetain;
        this.selfRetain = baseRetain;
    }

    protected final void setCustomVar(String key, int base) {
        this.setCustomVar(key, base, 0);
    }
    protected final void setCustomVar(String key, int base, int upgrade) {
        setCustomVarValue(key, base, upgrade);

        if (!customVars.containsKey(key)) {
            QuickDynamicVariable var = new QuickDynamicVariable(key);
            customVars.put(key, var);
            BaseMod.addDynamicVariable(var);
            initializeDescription();
        }
    }

    protected enum VariableType {
        DAMAGE,
        BLOCK,
        MAGIC
    }
    protected final void setCustomVar(String key, VariableType type, int base) {
        setCustomVar(key, type, base, 0);
    }
    protected final void setCustomVar(String key, VariableType type, int base, int upgrade) {
        setCustomVarValue(key, base, upgrade);

        switch (type) {
            case DAMAGE:
                calculateVarAsDamage(key);
                break;
            case BLOCK:
                calculateVarAsBlock(key);
                break;
        }

        if (!customVars.containsKey(key)) {
            QuickDynamicVariable var = new QuickDynamicVariable(key);
            customVars.put(key, var);
            BaseMod.addDynamicVariable(var);
            initializeDescription();
        }
    }
    protected final void setCustomVar(String key, VariableType type, int base, TriFunction<BaseCard, AbstractMonster, Integer, Integer> preCalc) {
        setCustomVar(key, type, base, 0, preCalc);
    }
    protected final void setCustomVar(String key, VariableType type, int base, int upgrade, TriFunction<BaseCard, AbstractMonster, Integer, Integer> preCalc) {
        setCustomVar(key, type, base, upgrade, preCalc, LocalVarInfo::noCalc);
    }
    protected final void setCustomVar(String key, VariableType type, int base, TriFunction<BaseCard, AbstractMonster, Integer, Integer> preCalc, TriFunction<BaseCard, AbstractMonster, Integer, Integer> postCalc) {
        setCustomVar(key, type, base, 0, preCalc, postCalc);
    }
    protected final void setCustomVar(String key, VariableType type, int base, int upgrade, TriFunction<BaseCard, AbstractMonster, Integer, Integer> preCalc, TriFunction<BaseCard, AbstractMonster, Integer, Integer> postCalc) {
        setCustomVarValue(key, base, upgrade);

        switch (type) {
            case DAMAGE:
                setVarCalculation(key, (c, m, baseVal)->{
                    boolean wasMultiDamage = c.isMultiDamage;
                    c.isMultiDamage = false;

                    int origBase = c.baseDamage, origVal = c.damage;

                    c.baseDamage = preCalc.apply(c, m, baseVal);

                    if (m != null)
                        c.calculateCardDamage(m);
                    else
                        c.applyPowers();

                    c.damage = postCalc.apply(c, m, c.damage);

                    c.baseDamage = origBase;
                    c.isMultiDamage = wasMultiDamage;

                    int result = c.damage;
                    c.damage = origVal;

                    return result;
                });
                break;
            case BLOCK:
                setVarCalculation(key, (c, m, baseVal)->{
                    int origBase = c.baseBlock, origVal = c.block;

                    c.baseBlock = preCalc.apply(c, m, baseVal);

                    if (m != null)
                        c.calculateCardDamage(m);
                    else
                        c.applyPowers();

                    c.block = postCalc.apply(c, m, c.block);

                    c.baseBlock = origBase;
                    int result = c.block;
                    c.block = origVal;
                    return result;
                });
                break;
            default:
                setVarCalculation(key, (c, m, baseVal)->{
                    int tmp = baseVal;

                    tmp = preCalc.apply(c, m, tmp);
                    tmp = postCalc.apply(c, m, tmp);

                    return tmp;
                });
                break;
        }

        if (!customVars.containsKey(key)) {
            QuickDynamicVariable var = new QuickDynamicVariable(key);
            customVars.put(key, var);
            BaseMod.addDynamicVariable(var);
            initializeDescription();
        }
    }

    private void setCustomVarValue(String key, int base, int upg) {
        cardVariables.compute(key, (k, old)->{
            if (old == null) {
                return new LocalVarInfo(base, upg);
            }
            else {
                old.base = base;
                old.upgrade = upg;
                return old;
            }
        });
    }

    protected final void colorCustomVar(String key, Color normalColor) {
        colorCustomVar(key, normalColor, Settings.GREEN_TEXT_COLOR, Settings.RED_TEXT_COLOR, Settings.GREEN_TEXT_COLOR);
    }
    protected final void colorCustomVar(String key, Color normalColor, Color increasedColor, Color decreasedColor) {
        colorCustomVar(key, normalColor, increasedColor, decreasedColor, increasedColor);
    }
    protected final void colorCustomVar(String key, Color normalColor, Color increasedColor, Color decreasedColor, Color upgradedColor) {
        LocalVarInfo var = getCustomVar(key);
        if (var == null) {
            throw new IllegalArgumentException("Attempted to set color of variable that hasn't been registered.");
        }

        var.normalColor = normalColor;
        var.increasedColor = increasedColor;
        var.decreasedColor = decreasedColor;
        var.upgradedColor = upgradedColor;
    }


    private LocalVarInfo getCustomVar(String key) {
        return cardVariables.get(key);
    }

    protected void calculateVarAsDamage(String key) {
        setVarCalculation(key, (c, m, base)->{
            boolean wasMultiDamage = c.isMultiDamage;
            c.isMultiDamage = false;

            int origBase = c.baseDamage, origVal = c.damage;

            c.baseDamage = base;
            if (m != null)
                c.calculateCardDamage(m);
            else
                c.applyPowers();

            c.baseDamage = origBase;
            c.isMultiDamage = wasMultiDamage;

            int result = c.damage;
            c.damage = origVal;

            return result;
        });
    }
    protected void calculateVarAsBlock(String key) {
        setVarCalculation(key, (c, m, base)->{
            int origBase = c.baseBlock, origVal = c.block;

            c.baseBlock = base;
            if (m != null)
                c.calculateCardDamage(m);
            else
                c.applyPowers();

            c.baseBlock = origBase;
            int result = c.block;
            c.block = origVal;
            return result;
        });
    }
    protected void setVarCalculation(String key, TriFunction<BaseCard, AbstractMonster, Integer, Integer> calculation) {
        cardVariables.get(key).calculation = calculation;
    }

    public int customVarBase(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return -1;
        return var.base;
    }
    public int customVar(String key) {
        LocalVarInfo var = cardVariables == null ? null : cardVariables.get(key); //Prevents crashing when used with dynamic text
        if (var == null)
            return -1;
        return var.value;
    }
    public int[] customVarMulti(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return null;
        return var.aoeValue;
    }
    public boolean isCustomVarModified(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return false;
        return var.isModified();
    }
    public boolean customVarUpgraded(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return false;
        return var.upgraded;
    }



    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();

        if (card instanceof BaseCard)
        {
            card.rawDescription = this.rawDescription;
            ((BaseCard) card).upgradesDescription = this.upgradesDescription;

            ((BaseCard) card).baseCost = this.baseCost;

            ((BaseCard) card).upgradeCost = this.upgradeCost;
            ((BaseCard) card).upgradeDamage = this.upgradeDamage;
            ((BaseCard) card).upgradeBlock = this.upgradeBlock;
            ((BaseCard) card).upgradeMagic = this.upgradeMagic;

            ((BaseCard) card).costUpgrade = this.costUpgrade;
            ((BaseCard) card).damageUpgrade = this.damageUpgrade;
            ((BaseCard) card).blockUpgrade = this.blockUpgrade;
            ((BaseCard) card).magicUpgrade = this.magicUpgrade;

            ((BaseCard) card).baseExhaust = this.baseExhaust;
            ((BaseCard) card).upgExhaust = this.upgExhaust;
            ((BaseCard) card).baseEthereal = this.baseEthereal;
            ((BaseCard) card).upgEthereal = this.upgEthereal;
            ((BaseCard) card).baseInnate = this.baseInnate;
            ((BaseCard) card).upgInnate = this.upgInnate;
            ((BaseCard) card).baseRetain = this.baseRetain;
            ((BaseCard) card).upgRetain = this.upgRetain;
        }

        return card;
    }

    @Override
    public void upgrade()
    {
        if (!upgraded)
        {
            this.upgradeName();

            if (this.upgradesDescription)
            {
                if (cardStrings.UPGRADE_DESCRIPTION == null)
                {
                    RemnantMod.logger.error("Card " + cardID + " upgrades description and has null upgrade description.");
                }
                else
                {
                    this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
                }
            }

            if (upgradeCost)
            {
                if (isCostModified && this.cost < this.baseCost && this.cost >= 0) {
                    int diff = this.costUpgrade - this.baseCost; //how the upgrade alters cost
                    this.upgradeBaseCost(this.cost + diff);
                    if (this.cost < 0)
                        this.cost = 0;
                }
                else {
                    upgradeBaseCost(costUpgrade);
                }
            }

            if (upgradeDamage)
                this.upgradeDamage(damageUpgrade);

            if (upgradeBlock)
                this.upgradeBlock(blockUpgrade);

            if (upgradeMagic)
                this.upgradeMagicNumber(magicUpgrade);

            if (baseExhaust ^ upgExhaust)
                this.exhaust = upgExhaust;

            if (baseInnate ^ upgInnate)
                this.isInnate = upgInnate;

            if (baseEthereal ^ upgEthereal)
                this.isEthereal = upgEthereal;

            if (baseRetain ^ upgRetain)
                this.selfRetain = upgRetain;


            this.initializeDescription();
        }
    }

    private static class QuickDynamicVariable extends DynamicVariable {
        final String localKey, key;

        private BaseCard current = null;

        public QuickDynamicVariable(String key) {
            this.localKey = key;
            this.key = makeID(key);
        }

        @Override
        public String key() {
            return key;
        }

        @Override
        public void setIsModified(AbstractCard c, boolean v) {
            if (c instanceof BaseCard) {
                LocalVarInfo var = ((BaseCard) c).getCustomVar(localKey);
                if (var != null)
                    var.forceModified = v;
            }
        }

        @Override
        public boolean isModified(AbstractCard c) {
            return c instanceof BaseCard && (current = (BaseCard) c).isCustomVarModified(localKey);
        }

        @Override
        public int value(AbstractCard c) {
            return c instanceof BaseCard ? ((BaseCard) c).customVar(localKey) : 0;
        }

        @Override
        public int baseValue(AbstractCard c) {
            return c instanceof BaseCard ? ((BaseCard) c).customVarBase(localKey) : 0;
        }

        @Override
        public boolean upgraded(AbstractCard c) {
            return c instanceof BaseCard && ((BaseCard) c).customVarUpgraded(localKey);
        }

        public Color getNormalColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.CREAM_COLOR;

            return var.normalColor;
        }

        public Color getUpgradedColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.GREEN_TEXT_COLOR;

            return var.upgradedColor;
        }

        public Color getIncreasedValueColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.GREEN_TEXT_COLOR;

            return var.increasedColor;
        }

        public Color getDecreasedValueColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.RED_TEXT_COLOR;

            return var.decreasedColor;
        }
    }

    protected static class LocalVarInfo {
        int base, value, upgrade;
        int[] aoeValue = null;
        boolean upgraded = false;
        boolean forceModified = false;
        Color normalColor = Settings.CREAM_COLOR;
        Color upgradedColor = Settings.GREEN_TEXT_COLOR;
        Color increasedColor = Settings.GREEN_TEXT_COLOR;
        Color decreasedColor = Settings.RED_TEXT_COLOR;

        TriFunction<BaseCard, AbstractMonster, Integer, Integer> calculation = LocalVarInfo::noCalc;

        public LocalVarInfo(int base, int upgrade) {
            this.base = this.value = base;
            this.upgrade = upgrade;
        }

        private static int noCalc(BaseCard c, AbstractMonster m, int base) {
            return base;
        }

        public boolean isModified() {
            return forceModified || base != value;
        }
    }
}