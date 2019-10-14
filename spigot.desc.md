[B]What does it do:[/B]

This plugin will show players the number of items of a chest on a Wall-Sign which is attached to a chest. Upon right-clicking the sign it'll update with the amount of items inside the chest its attached to.

[U]Note:[/U]
If you have many items of different type in one chest, the maximum amount will most likely not be correct. The reason behind is, that items may have a different maximum stack size.
If you have only stone in one chest, it'll work just fine. But putting stone together with snowballs, will not properly work. (Will just show the maximum amount of the first item slot in the chest)

It will also most likely not work with any plugin correctly which will alter the items max stack size. 

It might also not work with plugins which add custom items to the game. This has not been tested against such plugins and therefore the outcome is unknown.

[B]Installation:[/B]

Put the .jar file into "plugins" folder, restart or reload the server. (Restart is recommended)

[B]Inside the world:[/B]

Place any wall-sign (excluding Birch Sign) onto a chest on any direction you like, and then Type [B][Counter][/B] in the first line of that sign.

Rightclick the sign to update its values.

It will also add the position of the signs into the [B]config.yml[/B] file of this plugin and will automatically update the values without any player interaction. (This happens every 5 minutes currently)

[B]Special Wall Sign
[/B]
The "Birch Wall Sign" is special because it'll count the signs up to 3 blocks beneath it and add it up, like shown in the screenshot. 

[B]Screenshots:[/B]

[IMG]https://i.imgur.com/vIdoEbV.png[/IMG]

[B]TODO:
[/B]
[LIST]
[*]Make it configurable which signs to use where, ie. not hardcode the special "Birch sign"
[*]Make it configurable whether or not you want the plugin to auto-update the signs.
[*]Maybe: Make it the line "[Counter]" configurable
[/LIST]