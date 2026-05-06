package net.shmn7iii.wikipedico.game.mode;

import net.shmn7iii.wikipedico.Wikipedico;
import net.shmn7iii.wikipedico.game.GameContext;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface GameMode {
    String getId();
    String getDisplayName();

    /** 死亡時にキル集計・ブロードキャストを行う */
    void onDeath(Player victim, Player killer, Wikipedico plugin, GameContext context);

    /** 勝利条件を判定する。ゲーム続行なら empty、決着なら勝利メッセージを返す */
    Optional<String> checkVictory(Wikipedico plugin);
}
