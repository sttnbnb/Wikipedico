# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Wikipedico はMinecraft Java Edition 1.16.4/1.16.5向けのバトルロイヤルプラグインです。Spigot サーバー上で動作し、Skript (2.5.1) + skQuery (4.1.4) でゲームロジックを実装しています。

Java プラグイン（`plugin/`）は**インストーラー兼デプロイヤー**として機能し、JAR 内に同梱したスクリプトとデータパックをサーバーの所定ディレクトリへコピーします。

## ビルド

```bash
cd plugin
mvn package
```

成果物: `plugin/target/Wikipedico-<version>.jar`

Java 1.8 / maven-shade-plugin でシェードJARを生成します。

## アーキテクチャ

### Java プラグイン (`plugin/src/main/java/`)

| ファイル | 役割 |
|---|---|
| `Wikipedico.java` | プラグイン本体。`onEnable` 時に `config.yml` を読み込み、`config.sk` 内の変数値を直接書き換えて反映する (`wikiConfigReload`) |
| `command.java` | `/install` `/update` `/loadvaris` `/backup` コマンドの実装 |

**コマンド動作:**
- `/install` — `config.yml` 削除 → `variables.csv` ロード → スクリプト・データパックを全展開 → サーバーリロード
- `/update` — スクリプト・データパックのみ再展開 → サーバーリロード
- `/loadvaris` — `variables.csv` をバックアップしてリロード
- `/backup` — `variables.csv` を `plugins/Wikipedico/backup/` にタイムスタンプ付きで保存

### 設定の流れ

`plugins/Wikipedico/config.yml` → `Wikipedico.java#wikiConfigReload()` が各行を文字列マッチして書き換え → `plugins/Skript/scripts/config.sk` に反映。設定変更後はサーバーリロードが必要。

### Skript ファイル (`plugin/src/main/resources/scripts/`)

| ファイル | 役割 |
|---|---|
| `config.sk` | 全設定変数の初期化（Java によって値が注入される） |
| `function.sk` | ゲーム共通関数（`gameStart()` `deathPlayer()` `resetGameVariables()` 等） |
| `welcome.sk` | プレイヤーの参加・退出・ロードイベント処理 |
| `system/system_main.sk` | メインゲームループ（5tickごと）、死亡処理、ブロック破壊処理 |
| `system/system_team.sk` | チーム戦モード |
| `system/system_daruma.sk` | だるまさんが転んだモード |
| `system/system_yukigassen.sk` | 雪合戦モード |
| `system/system_asure.sk` | アスレチックモード |
| `command/command_dev.sk` | 開発者コマンド |
| `command/command_gen.sk` | 一般プレイヤーコマンド |
| `command/command_gm.sk` | ゲームマスターコマンド |
| `shop/shop_buy.sk` | ショップ購入処理 |
| `shop/shop_sell.sk` | ショップ売却処理 |

### データパック (`plugin/src/main/resources/datapacks/ibuibu/`)

`shimashima/functions/` 配下の `.mcfunction` ファイルで毎tick処理とゲーム開始・終了時処理を実装。`wb_range/` と `wb_time/` はワールドボーダー設定用（100〜1000 の段階値）。

### 主要な Skript 変数

| 変数 | 値 |
|---|---|
| `{gameStatus}` | 0=ロビー、1=ゲーム中、5=準備中 |
| `{playerStatus.%player%}` | `"alive"` / `"death"` |
| `{playerJoined.%player%}` | チームへの参加状態（boolean） |
| `{admin.%player%}` | 管理者フラグ |
| `{mode.team}` / `{mode.daruma}` 等 | ゲームモードフラグ |
| `{debugMode}` | デバッグモード |

## エラーコード

`ErrorCode.md` にエラーコード一覧があります。主な既知エラー:
- `CODE-D01` — `{playerStatus}` が未定義または `"death"` のプレイヤーが死亡イベントを発生させた
- `CODE-T01/T02/T03` — チームモードでの `playerJoined` 未定義エラー
