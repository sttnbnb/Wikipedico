# Wikipedico v8 — フルスクラッチ Spigot プラグイン化プラン

## Context

Wikipedico は Minecraft 1.16.4/1.16.5 上で動作するバトルロイヤルプラグインで、現在は Skript (`.sk`) + データパック (`.mcfunction`) + Java インストーラーで構成されています。Skript 依存・1.16 系という古い構成、Skript 文法の保守困難さ、複雑なゲームロジックを Skript の動的型変数で表現している脆弱さが課題です。

これを `feature/v8` ブランチで純粋な Java Spigot プラグインへフルスクラッチで再構築します。今回の v8 はゲームロジックの中核と Team 戦モードのみを対象とした MVP とし、その他のモード(Daruma/Yukigassen/Ibuibu/Asure)、ショップ、チェスト機構などは後続フェーズで段階追加します。

過去 `feature/v7` で同様の試みがあり (最終コミット `dd91487 WIP` で未完)、ソースコードは参考にしないものの**プロジェクト設定・ビルド構成・パッケージレイアウトは v7 を踏襲**します。

### 確定済み技術スタック

| 項目 | 選択 |
|---|---|
| Spigot | 26.1.2 (最新版、MC 1.21.x 系) |
| Java | 25 |
| ビルドツール | Maven (maven-shade-plugin 3.2.4 / maven-compiler-plugin 3.8.1) |
| ベースパッケージ | `net.shmn7iii.wikipedico` |
| メインコマンド | `/wikipedico` (alias `/wiki`) |
| 旧 installer 系コマンド | **全削除** (`install/update/loadvaris/backup`) |
| variables.csv / Skript / datapack | **完全廃止** |

## 作業の前提

- 新ブランチ `feature/v8` を `master` から作成。
- 既存の `plugin/` サブディレクトリ配下の Maven プロジェクトは v8 では使わない。`feature/v8` では Maven プロジェクトを**リポジトリ直下** (`pom.xml` をルート) に配置し直す (v7 と同じレイアウト)。古い `plugin/`, `wikisource/`, `errorlog/`, `src/.DS_Store` 配下のレガシーファイルはブランチ上で削除。
- `CLAUDE.md`, `README.md`, `ErrorCode.md` は v8 用に書き直すか削除 (本プランの完了後に別途対応)。

## ディレクトリ・パッケージ構造

```
.
├── pom.xml
└── src/main/
    ├── java/net/shmn7iii/wikipedico/
    │   ├── Wikipedico.java                    # JavaPlugin。各 Manager の DI ルート
    │   ├── config/
    │   │   ├── ConfigManager.java             # config.yml の typed accessor
    │   │   └── BorderStage.java               # ワールドボーダー段階値 (record)
    │   ├── game/
    │   │   ├── GameManager.java               # 状態遷移の単一所有者
    │   │   ├── GameStatus.java                # enum: LOBBY/PREPARING/PLAYING/ENDING
    │   │   ├── GameContext.java               # 1ゲーム分の揮発状態 (kills, 開始時刻 等)
    │   │   └── PreparationCountdown.java      # 準備カウントダウン BukkitRunnable
    │   ├── player/
    │   │   ├── PlayerManager.java             # UUID キーで WPlayer を保持
    │   │   ├── WPlayer.java                   # 1プレイヤーのゲーム状態
    │   │   └── PlayerStatus.java              # enum: ALIVE/DEAD/ADMIN/SPECTATOR
    │   ├── team/
    │   │   ├── TeamManager.java               # チーム登録/参加/上限/勝者判定
    │   │   ├── WTeam.java                     # 1チーム
    │   │   └── TeamColor.java                 # enum: RED/BLUE/YELLOW/GREEN/ORANGE/PURPLE/BLACK
    │   ├── worldborder/
    │   │   └── WorldBorderController.java     # 開始15秒後から段階収縮を Scheduler で予約
    │   ├── scoreboard/
    │   │   ├── ScoreboardService.java         # 5tick タスクで sidebar 更新
    │   │   └── ActionBarService.java          # 同タスクで Adventure ActionBar 送信
    │   ├── command/
    │   │   ├── WikipedicoCommand.java         # CommandExecutor + TabCompleter
    │   │   ├── SubCommand.java                # interface
    │   │   └── sub/
    │   │       ├── StartSubCommand.java
    │   │       ├── EndSubCommand.java
    │   │       ├── JoinSubCommand.java
    │   │       ├── AdminSubCommand.java
    │   │       ├── RevivalSubCommand.java
    │   │       └── ReloadSubCommand.java
    │   ├── listener/
    │   │   ├── PlayerLifecycleListener.java   # Join/Quit
    │   │   └── PlayerCombatListener.java      # Death/Respawn
    │   └── util/
    │       ├── Broadcast.java                 # Adventure Component broadcast ヘルパ
    │       ├── Sounds.java                    # 効果音定数
    │       └── Locations.java                 # config 由来 Location ユーティリティ
    └── resources/
        ├── plugin.yml
        └── config.yml
```

## 主要クラスの責務

- **Wikipedico (JavaPlugin)** — `onEnable` で `ConfigManager → TeamManager → PlayerManager → GameManager → WorldBorderController → ScoreboardService → ActionBarService` の順に初期化、リスナーとコマンドを登録。`onDisable` で全 BukkitTask キャンセル + 状態を LOBBY に戻す。
- **GameManager** — `startGame()` (LOBBY→PREPARING)、`onPreparationFinished()` (PREPARING→PLAYING、TP/耐性/エリトラ付与/WB 起動)、`onDeath(victim, killer)`、`endGame()` (→ENDING、ランキング)、`resetToLobby()` (→LOBBY)。`getStatus()` は volatile な enum を返す。
- **TeamManager** — 起動時に 7 チームを enum 由来で登録。`join(player, color)` は LOBBY 限定 + `teamMaxPlayer` チェック。`aliveTeams()` で残存チーム集合を返し、勝者判定で使う。
- **ConfigManager** — `int teamMaxPlayer()`, `int preparationTime()`, `int countDownTime()`, `int killRankingTimes()`, `Location lobbySpawn()`, `Location deathSpawn()`, `Location skySpawn()`, `int borderStartDelaySeconds()`, `List<BorderStage> borderStages()`。`reload()` で再読込。
- **WorldBorderController** — `start()` で開始時刻+15秒に最初の段階を予約。各段階で `world.getWorldBorder().setSize(range, timeSeconds)` を呼び、次段階を `runTaskLater` で予約。`stop()` で全 task をキャンセル + border リセット。
- **ScoreboardService / ActionBarService** — `runTaskTimer(plugin, 0L, 5L)` 共有 1 本で `GameStatus` 別表示を切替 (PREPARING: 残り秒、PLAYING: サバイバー数 / 残チーム数、ENDING: 勝利チーム + キルランキング上位 N)。

## コマンド一覧

| コマンド | 権限 | 動作 |
|---|---|---|
| `/wiki start` | `wikipedico.admin` | LOBBY→PREPARING 遷移 |
| `/wiki end` | `wikipedico.admin` | PREPARING/PLAYING→ENDING 遷移 |
| `/wiki join <team> [player]` | `wikipedico.user` | LOBBY 中のチーム参加 (player 指定は admin のみ) |
| `/wiki admin <player>` | `wikipedico.admin` | ADMIN status トグル |
| `/wiki revival <player>` | `wikipedico.admin` | DEAD→ALIVE、TP locaSky |
| `/wiki reload` | `wikipedico.admin` | config 再読込 |

`WikipedicoCommand` は `Map<String, SubCommand>` で dispatch。`onTabComplete` も同 Map から候補生成。

## ゲーム状態遷移

```
LOBBY ──/wiki start──▶ PREPARING ──準備カウント0──▶ PLAYING
  ▲                       │                           │
  │                  /wiki end                  最後の1チーム
  │                       │                           ▼
  └─────── 10秒後 ◀──── ENDING ◀─── /wiki end ────────┘
```

各遷移は `GameManager` のメソッド内で `GameStatus` を切替後、関係する Service (border/scoreboard/actionbar/preparationCountdown) に通知。

## イベントリスナー

- **PlayerLifecycleListener**
  - `PlayerJoinEvent` → `PlayerManager.register(uuid)` (status=SPECTATOR、TP locaLobbySpawn)
  - `PlayerQuitEvent` → 参加中ならチーム離脱、PLAYING 中なら `TeamManager.checkVictory()` 再評価
- **PlayerCombatListener**
  - `PlayerDeathEvent` → PLAYING 限定で `GameManager.onDeath(victim, killer)`、kill count 加算 → broadcast → status=DEAD
  - `PlayerRespawnEvent` → DEAD なら respawnLocation を locaDeathSpawn に書き換え + Spectator gamemode 切替

## config.yml 構造

```yaml
game:
  teamMaxPlayer: 3
  preparationTime: 30
  countDownTime: 5
  killRankingTimes: 3
locations:
  world: world
  lobbySpawn:  { x: 0, y: 64, z: 0, yaw: 0, pitch: 0 }
  deathSpawn:  { x: 0, y: 200, z: 0 }
  skySpawn:    { x: 0, y: 250, z: 0 }
worldBorder:
  startDelaySeconds: 15
  stages:
    - { range: 1000, time: 60 }
    - { range: 500,  time: 90 }
    - { range: 200,  time: 120 }
    - { range: 100,  time: 60 }
```

## plugin.yml 構造

```yaml
name: Wikipedico
version: '${project.version}'
main: net.shmn7iii.wikipedico.Wikipedico
api-version: '1.21'
authors: [shmn7iii]
commands:
  wikipedico:
    description: Wikipedico battle royale commands
    aliases: [wiki]
    usage: /wiki <start|end|join|admin|revival|reload>
permissions:
  wikipedico.admin:
    description: Game master commands
    default: op
  wikipedico.user:
    description: Player commands
    default: true
```

## pom.xml の主要設定

- `<java.version>25</java.version>`
- `<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>`
- `<artifactId>Wikipedico</artifactId>`, `<version>8.0.0-SNAPSHOT</version>`
- `spigot-api 1.21.x-R0.1-SNAPSHOT` (provided)、`commons-io 2.6`、`commons-lang 2.6`
- `maven-shade-plugin 3.2.4`、`maven-compiler-plugin 3.8.1` (source/target = 25)
- リソース filtering: true (`${project.version}` を plugin.yml に注入)
- repositories: spigotmc-repo, sonatype

> Note: Spigot 26.1.2 はサーバー本体のバージョンで、利用する **API 依存** は対応する `spigot-api` の `1.21.x-R0.1-SNAPSHOT` (実際のバージョンは Spigot 26.1.2 のリリース時の Minecraft バージョンに合わせる)。`mvn -U` 時に最新スナップショットを引く形で進める。

## 実装フェーズ

1. **Phase 1 — リポジトリ初期化**
   - `feature/v8` ブランチ作成、master 由来のレガシー (`plugin/`, `wikisource/`, `errorlog/`, `target/`, root の `.DS_Store`) を削除。
   - `pom.xml`, `plugin.yml`, `config.yml`, 空 `Wikipedico` クラスを配置し `mvn package` 成功・JAR ロード確認。
2. **Phase 2 — 状態とコマンド枠**
   - `GameStatus`, `PlayerStatus`, `ConfigManager`, `WikipedicoCommand` + `SubCommand` interface + 全サブコマンドの "not implemented" スタブ + tab completion。
3. **Phase 3 — Player/Team 基盤**
   - `WPlayer`, `PlayerManager`, `WTeam`, `TeamColor`, `TeamManager`, `PlayerLifecycleListener`, `JoinSubCommand` 実装。LOBBY でのチーム参加・退出が動作。
4. **Phase 4 — ゲームライフサイクル**
   - `GameManager`, `GameContext`, `PreparationCountdown`, `StartSubCommand`/`EndSubCommand`, `PlayerCombatListener`, kill count、勝者判定、TP/耐性/エリトラ付与。
5. **Phase 5 — ワールドボーダー**
   - `BorderStage` record、`WorldBorderController` 段階収縮、開始 15 秒遅延、`endGame` でリセット。
6. **Phase 6 — UI**
   - `ScoreboardService`, `ActionBarService`, `Broadcast` ヘルパ。Adventure Component で死亡通知・勝利通知・キルランキング。
7. **Phase 7 — 仕上げ**
   - `ReloadSubCommand`, `AdminSubCommand`, `RevivalSubCommand`、エッジケース (PLAYING 中 quit、最後の 1 人切断、再戦時の状態リセット)、ロギング、CLAUDE.md / README 更新。

## 作成・修正対象ファイル (Phase 1 で作成する基盤分)

- `pom.xml` (リポジトリルート、新規作成)
- `src/main/resources/plugin.yml` (新規作成)
- `src/main/resources/config.yml` (新規作成)
- `src/main/java/net/shmn7iii/wikipedico/Wikipedico.java` (新規作成)
- `.gitignore` (Maven 出力 `target/` を含めて更新)

以降の Phase で追加されるクラス群は「ディレクトリ・パッケージ構造」セクションを参照。

## 参考にする既存資産

- 旧 Skript 設定値の意味 → `plugin/src/main/resources/scripts/config.sk` と `plugin/src/main/resources/config.yml` を**仕様書として**読み、新 `config.yml` のキー構造へ翻訳。
- 旧 datapack の `boarder_set.mcfunction` および `wb_range/`, `wb_time/` 配下 → `BorderStage` リストへ翻訳。
- `feature/v7` の `pom.xml` / `plugin.yml` / `.gitignore` の文面 → ベースに Java 25 / Spigot 26.1.2 へ更新。

## 検証方法

1. `mvn clean package` → `target/Wikipedico-8.0.0-SNAPSHOT.jar` が生成されること。
2. ローカル Spigot 26.1.2 サーバーの `plugins/` に配置 → 初回起動で `plugins/Wikipedico/config.yml` が生成されること。
3. プレイヤー2名でログイン → `/wiki join red`, `/wiki join blue` でそれぞれ参加 → `/wiki start` → 30 秒準備 → 5 秒カウント (title + ping 効果音) → locaSky へ TP・耐性 II・エリトラ付与を確認。
4. PvP で1名死亡 → broadcast 通知、status=DEAD、Spectator gamemode 切替 → `/wiki revival <player>` で復活 + locaSky TP を確認。
5. 開始 15 秒後にワールドボーダー第1段階収縮開始、`/worldborder get` で範囲・時間が config の各段階値どおりに推移することを確認。
6. 片チーム全滅で ENDING へ自動遷移、勝利チームと上位 3 名のキルランキングが画面表示、10 秒後 LOBBY へ復帰。
7. `/wiki reload` で config 値変更 (例: `teamMaxPlayer: 3 → 4`) が次ゲームから反映されることを確認。
8. `/wiki end` で PLAYING 中ゲームを強制終了 → ENDING → LOBBY 遷移を確認。
