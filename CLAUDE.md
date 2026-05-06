# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Wikipedico は Minecraft Java Edition 1.21.4 向けのバトルロイヤルプラグインです。  
純粋な Java Spigot プラグインとして実装されています（Skript・datapack は廃止）。

## ビルド

```bash
mvn clean package
```

成果物: `target/Wikipedico-<version>.jar`

Java 25 / maven-shade-plugin でシェード JAR を生成します。

## アーキテクチャ

`Wikipedico.java` (JavaPlugin) が全 Manager の DI ルートです。

| パッケージ | 役割 |
|---|---|
| `config/` | `ConfigManager` — config.yml の typed accessor。`reload()` で再読込。`BorderStage` record |
| `game/` | `GameManager` — LOBBY/PREPARING/PLAYING/ENDING の状態遷移を一元管理。`PreparationCountdown` (BukkitRunnable)、`GameContext` (ゲーム内 kill 集計) |
| `player/` | `PlayerManager` (UUID → WPlayer map)、`WPlayer` (status/teamId/kills)、`PlayerStatus` enum |
| `team/` | `TeamManager` (join/leave/aliveTeams)、`WTeam`、`TeamColor` enum (7色) |
| `worldborder/` | `WorldBorderController` — ゲーム開始 N 秒後から段階的にボーダーを収縮 |
| `scoreboard/` | `ScoreboardService` (5tick sidebar 更新)、`ActionBarService` (5tick action bar) |
| `command/` | `WikipedicoCommand` (Map dispatch) + `SubCommand` interface。`sub/` 配下に各サブコマンド |
| `listener/` | `PlayerLifecycleListener` (Join/Quit)、`PlayerCombatListener` (Death/Respawn) |
| `util/` | `Broadcast` — `Bukkit.broadcastMessage` のラッパー |

## ゲーム状態遷移

```
LOBBY ──/wiki start──▶ PREPARING ──カウント0──▶ PLAYING
  ▲                       │                        │
  │                  /wiki end              最後の1チーム
  │                       │                        ▼
  └────── 10秒後 ◀──── ENDING ◀──── /wiki end ─────┘
```

## コマンド

| コマンド | 権限 |
|---|---|
| `/wiki start` | `wikipedico.admin` |
| `/wiki end` | `wikipedico.admin` |
| `/wiki join <team> [player]` | `wikipedico.user` |
| `/wiki admin <player>` | `wikipedico.admin` |
| `/wiki revival <player>` | `wikipedico.admin` |
| `/wiki reload` | `wikipedico.admin` |

## Adventure API について

`sendActionBar` は Spigot の `Player.spigot().sendMessage(ChatMessageType.ACTION_BAR, ...)` を使用しています（Spigot 経由では Adventure の `sendActionBar(Component)` が直接呼べないため）。  
Adventure Component を使う場合は Spigot の API サーフェスを確認してください。
