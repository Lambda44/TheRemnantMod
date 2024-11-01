package theremnant.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.map.DungeonMap;
import theremnant.monsters.VoidWarden;
import theremnant.util.TextureLoader;

@SpirePatch(clz = AbstractDungeon.class, method = "setBoss")
public class BossSetPatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractDungeon __instance, String key) {
        if (__instance instanceof TheEnding && AbstractDungeon.player.getCharacterString().NAMES[0].equals("The Remnant")) {
            AbstractDungeon.bossList.clear();

            AbstractDungeon.bossList.add(VoidWarden.ID);
            AbstractDungeon.bossList.add(VoidWarden.ID);
            AbstractDungeon.bossList.add(VoidWarden.ID);

            key = VoidWarden.ID;
            AbstractDungeon.bossKey = key;
        }

        if (key.equals(VoidWarden.ID)) {
            DungeonMap.boss = TextureLoader.getTexture("theremnant/ui/map/icon/void_warden.png");
            DungeonMap.bossOutline = TextureLoader.getTexture("theremnant/ui/map/outline/void_warden.png");
        }
    }
}
