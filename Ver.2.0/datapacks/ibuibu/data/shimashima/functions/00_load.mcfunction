#minecraft/tags/functions/loadに書く用
#データパックが読み込まれた時に一度だけ実行
#=========================================================================================================================
#=========================================================================================================================


#=========================================================================================================================
#ゲームルール設定

    #コマンドブロックの実行結果出力無効
        gamerule commandBlockOutput false
    #モブの自然湧き無効
        gamerule doMobSpawning false
    #延焼の無効
        gamerule doFireTick false
    #モブによる破壊・アイテム取得の無効化
        gamerule mobGriefing false
    #ランダムチックスピード＜設定値：０＝成長はしない＞
        gamerule randomTickSpeed 0
    #デバッグ情報の簡素化
        gamerule reducedDebugInfo true
    #手打ちコマンド実行結果表示の無効
        gamerule sendCommandFeedback false
    #初期スポーン範囲の設定＜設定値：１＞
        gamerule spawnRadius 1
    #デスメオフ
        gamerule showDeathMessages false
    #時刻の固定・設定＜設定値：6000(太陽がほぼ真上)＞
        gamerule doDaylightCycle false
        time set 6000
    #天気の固定・設定＜設定値：晴れ＞
        gamerule doWeatherCycle false
        weather clear

    #難易度
        difficulty hard


#=========================================================================================================================
#チームの追加・編集

# Admin
    team add admin
    team modify adimn color dark_red
    team modify adimn friendlyFire false
    team modify adimn nametagVisibility hideForOtherTeams
    team modify adimn collisionRule never
    team modify adimn displayName "Adimn"
    team modify adimn prefix "[Adimn]"
    scoreboard objectives add adimn dummy
    scoreboard objectives modify adimn displayname "Adimn"
    scoreboard objectives setdisplay sidebar.team.dark_red admin

# red
    team add red
    team modify red color red
    team modify red friendlyFire false
    team modify red nametagVisibility hideForOtherTeams
    team modify red collisionRule never
    team modify red displayName "Red Team"
    scoreboard objectives add red dummy
    scoreboard objectives modify red displayname "Red Team"
    scoreboard objectives setdisplay sidebar.team.red red

# blue
    team add blue
    team modify blue color blue
    team modify blue friendlyFire false
    team modify blue nametagVisibility hideForOtherTeams
    team modify blue collisionRule never
    team modify blue displayName "Blue Team"
    scoreboard objectives add blue dummy
    scoreboard objectives modify blue displayname "Blue Team"
    scoreboard objectives setdisplay sidebar.team.blue blue

# yellow
    team add yellow
    team modify yellow color yellow
    team modify yellow friendlyFire false
    team modify yellow nametagVisibility hideForOtherTeams
    team modify yellow collisionRule never
    team modify yellow displayName "Yellow Team"
    scoreboard objectives add yellow dummy
    scoreboard objectives modify yellow displayname "Yellow Team"
    scoreboard objectives setdisplay sidebar.team.yellow yellow

# green
    team add green
    team modify green color green
    team modify green friendlyFire false
    team modify green nametagVisibility hideForOtherTeams
    team modify green collisionRule never
    team modify green displayName "Green Team"
    scoreboard objectives add green dummy
    scoreboard objectives modify green displayname "Green Team"
    scoreboard objectives setdisplay sidebar.team.green green

# orange
    team add orange
    team modify orange color gold
    team modify orange friendlyFire false
    team modify orange nametagVisibility hideForOtherTeams
    team modify orange collisionRule never
    team modify orange displayName "Orange Team"
    scoreboard objectives add orange dummy
    scoreboard objectives modify orange displayname "Orange Team"
    scoreboard objectives setdisplay sidebar.team.gold orange

# purple
    team add purple
    team modify purple color light_purple
    team modify purple friendlyFire false
    team modify purple nametagVisibility hideForOtherTeams
    team modify purple collisionRule never
    team modify purple displayName "Purple Team"
    scoreboard objectives add purple dummy
    scoreboard objectives modify purple displayname "Purple Team"
    scoreboard objectives setdisplay sidebar.team.light_purple purple

# gray
    team add gray
    team modify gray color gray
    team modify gray friendlyFire false
    team modify gray nametagVisibility hideForOtherTeams
    team modify gray collisionRule never
    team modify gray displayName "Gray Team"
    scoreboard objectives add gray dummy
    scoreboard objectives modify gray displayname "Gray Team"
    scoreboard objectives setdisplay sidebar.team.gray gray



# spread
    team add spread

# players
    team add players
    team modify players nametagVisibility always
    team modify players collisionRule never

# dead
    team add dead

#=========================================================================================================================
#スコアボードの追加

# ゲーム中/外を判定(ゲーム中＝1/ゲーム外＝0｜初期値0)
    scoreboard objectives add on_game dummy
    scoreboard players set on_game on_game 0

# プレイヤーの参加/非参加を判定(参加＝1/非参加＝0｜初期値0)
    scoreboard objectives add play dummy
    scoreboard players set @a play 0

# 死亡判定(死亡＝1/生存＝0｜初期値0)
    scoreboard objectives add death deathCount
    scoreboard players set @a death 0

# エリトラ使用検知
    scoreboard objectives add fly custom:aviate_one_cm
    scoreboard objectives add flied dummy

# エリトラ使用後に歩いたこと検知
    scoreboard objectives add walk custom:walk_one_cm

# ワールドボーダーの範囲決定用のスコアぼ
    scoreboard objectives add wb_range dummy
    scoreboard players set wb_range wb_range 0

# ワールドボーダーの時間用のスコアぼ
    scoreboard objectives add wb_time dummy
    scoreboard players set wb_time wb_time 0

# ibuibu mode
    scoreboard objectives add ibuibumode dummy
    scoreboard players set ibuibumode ibuibumode 0

# 雪玉爆弾
    scoreboard objectives add sb used:snowball
    scoreboard players set @a sb 0

# なんかリストに表示されよるからぐっばい
    scoreboard objectives setdisplay list

#
    scoreboard objectives remove Info
    scoreboard objectives add Info dummy
    scoreboard objectives setdisplay sidebar Info
    scoreboard players set 状態：準備中 Info 0


#=========================================================================================================================
#