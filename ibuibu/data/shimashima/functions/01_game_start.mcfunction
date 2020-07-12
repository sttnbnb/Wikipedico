# ゲームスタートするよお
#=========================================================================================================================

# ゲーム中に設定
scoreboard players set on_game on_game 1

# 参加者のエフェクトをクリアー
effect clear @a

# タイトル
title @a times 0 40 20
title @a title ["",{"text":"\u22d9 ","bold":true},{"text":"START","bold":true,"color":"yellow"},{"text":" \u22d8","bold":true}]
playsound block.anvil.place master @a ~ ~ ~

# エリトラあげりゅ
replaceitem entity @a[team=!admin] armor.chest elytra
# エリトラ落下死防止
effect give @a[team=!admin] resistance 6 255 true

# 参加者のゲームモードを変更
gamemode survival @a[team=!admin]
gamemode spectator @a[team=admin]

# 参加者のネームタグを隠す
team join nametag @a[team=!admin]

# 上空にTP
tp @a -27663 309 36470 ~ 90


# ワールドボーダーをとりあえず作る
worldborder center -27663 36470 
worldborder add 5000000
# んで狭める中心をランダムで決める
schedule function shimashima:boarder_set 10s
# その他設定
worldborder damage amount 0.5
worldborder damage buffer 0
worldborder warning distance 0
worldborder warning time 10

# ibuibu mode
execute if score ibuibumode ibuibumode matches 1 run effect give iibukuro glowing 1000000 255 true