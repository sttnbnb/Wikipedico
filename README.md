# Wikipedico

-Battle Royale in Minecraft-

About : https://wikipedico.studio.site

## Specification

Minecraft Version : Minecraft Java Edition 1.21.4  
Supported Server : Spigot 1.21.4  
Language : Java 25

## Features

- チーム戦バトルロイヤル（最大7チーム）
- 準備カウントダウン → スカイスポーン → エリトラ降下
- ワールドボーダー段階収縮
- キルランキング表示
- サイドバー / アクションバー UI

## Build

```bash
mvn clean package
```

成果物: `target/Wikipedico-<version>.jar`

## Commands

| コマンド | 権限 | 説明 |
|---|---|---|
| `/wiki start` | admin | ゲーム開始（準備フェーズへ） |
| `/wiki end` | admin | ゲーム強制終了 |
| `/wiki join <team> [player]` | user | チームに参加 |
| `/wiki admin <player>` | admin | ADMIN ステータストグル |
| `/wiki revival <player>` | admin | 死亡プレイヤーを復活 |
| `/wiki reload` | admin | config 再読込 |

チーム名: `red` / `blue` / `yellow` / `green` / `orange` / `purple` / `black`

## Configuration

`plugins/Wikipedico/config.yml` で設定します。

```yaml
game:
  teamMaxPlayer: 3       # チームの最大人数
  preparationTime: 30    # 準備時間（秒）
  countDownTime: 5       # カウントダウン開始秒数
  killRankingTimes: 3    # キルランキング表示人数
locations:
  world: world
  lobbySpawn:  { x: 0, y: 64, z: 0 }
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
