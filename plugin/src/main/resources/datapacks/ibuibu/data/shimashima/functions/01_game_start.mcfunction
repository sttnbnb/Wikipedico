# ゲームスタートするよお
#=========================================================================================================================

# ゲーム中に設定
scoreboard players set on_game on_game 1
scoreboard players set @a[team=!admin] play 1

# エフェクトをクリアー
effect clear @a

# エリトラあげりゅ
replaceitem entity @a[team=!admin] armor.chest elytra
# エリトラ落下死防止
effect give @a[team=!admin] resistance 10 255 true

# 発光
effect give @a[team=!admin] glowing 10 255 true

# 参加者のゲームモードを変更
gamemode survival @a[team=!admin,team=!dead]
gamemode spectator @a[team=admin]

# ワールドボーダーをとりあえず作る
#TODO:マップによる変動あり
worldborder center -27657 36483
worldborder add 5000000
# んで狭める中心をランダムで決める
schedule function shimashima:boarder_set 15s
# その他設定
worldborder damage amount 0.005
worldborder damage buffer 0
worldborder warning distance 0
worldborder warning time 10

# ibuibu mode
execute if score ibuibumode ibuibumode matches 1 run effect give iibukuro glowing 1000000 255 true