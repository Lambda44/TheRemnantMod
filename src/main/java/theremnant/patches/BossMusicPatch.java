package theremnant.patches;

import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theremnant.RemnantMod;

import java.util.Objects;

@SpirePatch(clz = TempMusic.class, method = "getSong")
public class BossMusicPatch {
    @SpirePrefixPatch
    public static SpireReturn<Music> NotPrefix(TempMusic __instance, String key) {
        if (Objects.equals(key, "BOSS_VOID_WARDEN") && AbstractDungeon.player.getCharacterString().NAMES[0].equals("The Remnant") && RemnantMod.customBossBGM) {
            return SpireReturn.Return(MainMusic.newMusic("theremnant/music/CallOfTheVoid.mp3"));
        }

        return SpireReturn.Continue();
    }
}
