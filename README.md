commands:
  catchall:
    description: "Show all commands callable by CatchAll"
    permission: catchall.cmd.help
  catchall reload:
    description: "Reload the plugin"
    permission: catchall.cmd.reload
    usage: "Usage : /<command>"
  lastlogin:
    description: "Write a file with players offline between MIN and MAX months ago (default: 1 and 6 months)"
    permission: catchall.cmd.lastlogin
    usage: "Usage : /<command> [minMonth] [maxMonth]"
  webhook:
    description: "Send message under discord webhook"
    permission: catchall.cmd.webhook
    usage: "Usage : /<command> [channel] [message]"
  maintenance:
    description: "Enable/Disable the maintenance server status"
    permission: catchall.cmd.maintenance
    usage: "Usage : /<command>"
  donateur:
    description: "Enable/Disable the donator server status"
    permission: catchall.cmd.donator
    usage: "Usage : /<command>"
  mchat:
    description: "Send custom message"
    permission: catchall.cmd.mchat
    usage: "Usage : /<command> [to] [message]"
  glist:
    description: "glist"
    permission: catchall.cmd.glist
    usage: "Usage : /<command>"
  hand: #Sponge
    description: "show item in your hand"
    permission: catchall.cmd.hand
    usage: "Usage: /<command>"
