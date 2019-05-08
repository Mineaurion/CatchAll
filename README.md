## Spigot
### Permissions *(commands permission below)*
**Maintenance.bypass** - Give possibility to be connected while server is in **maintenance** mode
### Commands
**catchall:** <br>
*description:* Show all commands callable by CatchAll <br>
*permission:* catchall.help <br>
**catchall reload:** <br>
*description:* Reload the plugin <br>
*permission:* catchall.reload <br>
*usage:* /command <br>
**lastlogin:** <br>
*description:* Write a file with players offline between MIN and MAX months ago (default: 1 and 6 months) <br>
*permission:* catchall.lastlogin <br>
*usage:* /command [minMonth] [maxMonth] <br>
**webhook:** <br>
*description:* Send message under discord webhook <br>
*permission:* catchall.webhook <br>
*usage:* /command [channel] [message] <br>
**maintenance:** <br>
*description:* Enable/Disable the maintenance server status <br>
*permission:* catchall.maintenance <br>
*usage:* /command <br>
**donateur:** <br>
*description:* Enable/Disable the donator server status <br>
*permission:* catchall.donator <br>
*usage:* /command <br>
**m_chat:** <br>
*description:* Send custom message<br>
*permission:* catchall.mchat <br>
*usage:* /mchat [to] [message] <br>
### Events
**PlayerLogin** : Check the state (maintenance/donateur) server and check permissions user
**PlayerJoin** : Print message "+NAME"
**PlayerQuit** : Print message "-NAME"
## Sponge (5 and 7)