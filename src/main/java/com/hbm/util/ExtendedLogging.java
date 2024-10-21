package com.hbm.util;

public class ExtendedLogging {
    /*final AxisAlignedBB aabb;
    final BlockPos pos;

    public ExtendedLogging(BlockPos pos) {
        this.aabb = new AxisAlignedBB(pos).grow(50);
        this.pos = pos;
    }

    public void missileLog(World world, String missile, int targetX, int targetY) {
        if (GeneralConfig.enableExtendedLogging) {
            try { sendGlobalMessage(world.getMinecraftServer().getPlayerList(), missile + " launched from " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + " to " + targetX + " / " + targetY); } catch (NullPointerException e) {}
            sendDiscordMsg("Missile Launch", missile + " Tried to launch missile at " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + " to " + targetX + " / " + targetY + "! Players near launch site under 20 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString() + " and nearest player within 100 blocks " + world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 100, false).toString());
            MainRegistry.logger.log(Level.INFO, "[HBM] " + missile + " Tried to launch missile at " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + " to " + targetX + " / " + targetY + "! Players near launch site under 20 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString() + " and nearest player within 100 blocks " + world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 100, false).toString());
        }
    }

    public void soyuzLog(World world, String soyuz, boolean payload, int targetX, int targetZ) {
        if (GeneralConfig.enableExtendedLogging) {
            if (!payload) {
                sendDiscordMsg("Soyuz Launch", soyuz + " Launched from " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + "! Players near launch site under 50 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString() + " and nearest player within 100 blocks " + world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 100, false).toString());
                MainRegistry.logger.log(Level.INFO, "[HBM] " + soyuz + " Launched from " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + "! Players near launch site under 50 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString() + " and nearest player within 100 blocks " + world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 100, false).toString());
            } else {
                sendDiscordMsg("Soyuz Launch", soyuz + " (Targeted) Launched from " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + " to target " + targetX + " / " + targetZ + "! Players near launch site under 50 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString() + " and nearest player within 100 blocks " + world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 100, false).toString());
                MainRegistry.logger.log(Level.INFO, "[HBM] " + soyuz + " (Targeted) Launched from " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + " to target " + targetX + " / " + targetZ + "! Players near launch site under 50 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString() + " and nearest player within 100 blocks " + world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 100, false).toString());
            }
        }
    }

    public void nukeLog(World world, String nuke) {
        if (GeneralConfig.enableExtendedLogging) {
            try { sendGlobalMessage(world.getMinecraftServer().getPlayerList(), nuke + " set off at " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ()); } catch (NullPointerException e) {}
            sendDiscordMsg("Nuke Log", nuke + "set off at " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + "! Players near launch site under 20 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString());
            MainRegistry.logger.log(Level.INFO, "[HBM] " + nuke + " set off at " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + "! Players near launch site under 20 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString());
        }
    }

    public void detonatorLog(World world, String detonator, int targetX, int targetY, int targetZ, String playerName) {
        final AxisAlignedBB aabb2 = new AxisAlignedBB(new BlockPos(targetX, targetY, targetZ)).grow(50);
        if (GeneralConfig.enableExtendedLogging) {
            sendDiscordMsg("Detonator Log", playerName + " tried to use detonator from " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + " targeting " + targetX + " / " + targetY + " / " + targetZ + "! Players near detonator " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString() + " and players near target in 50 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb2).toString());
            MainRegistry.logger.log(Level.INFO, "[HBM] " + playerName + " tried to use detonator from " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + " targeting " + targetX + " / " + targetY + " / " + targetZ + "! Players near detonator " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString() + " and players near target in 50 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb2).toString());
        }
    }

    public void dangerousDrop(World world, String itemName, String playerName) {
        if (GeneralConfig.enableExtendedLogging) {
            sendDiscordMsg("Dangerous Drop Log", playerName + " dropped " + itemName + " At " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + "! Players near dropped site " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString());
            MainRegistry.logger.log(Level.INFO, "[HBM] " + playerName + " dropped " + itemName + " At " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + "! Players near dropped site " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString());
        }
    }

    public void extraInfoLog(String log) {
        if (GeneralConfig.enableExtendedLogging) {
            sendDiscordMsg("Extra Info Log", log);
            MainRegistry.logger.log(Level.INFO, log);
        }
    }

    public void designatorLog(World world, String designator, int x, int z, String playerName) {
        if (GeneralConfig.enableExtendedLogging) {
            sendDiscordMsg("Designator Log", playerName + " set designator at " + x + " / " + z);
            MainRegistry.logger.log(Level.INFO, "[HBM] " + playerName + " set designator at " + x + " / " + z);
        }
    }

    public void railGunLog(World world, int targetX, int targetZ) {
        if (GeneralConfig.enableExtendedLogging) {
            sendDiscordMsg("RailGun Log", "Tried to fire railgun from " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + " targeting " + targetX + " / " + targetZ + "! Players near railgun " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString());
            MainRegistry.logger.log(Level.INFO, "[HBM] Tried to fire railgun from " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + " targeting " + targetX + " / " + targetZ + "! Players near railgun " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString());
        }
    }

    public void explosionLog(World world, BlockPos pos, String explosive) {
        if (GeneralConfig.enableExtendedLogging) {
            sendDiscordMsg("Explosive Log", "Set off " + explosive + " from " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + "! Players near explosion under 20 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString());
            MainRegistry.logger.log(Level.INFO, "[HBM] Set off " + explosive + " from " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + "! Players near explosion under 20 blocks " + world.getEntitiesWithinAABB(EntityPlayer.class, aabb).toString());
        }
    }

    private void sendDiscordMsg(String logType, String details) {
        if(GeneralConfig.discordWebhook != "" && GeneralConfig.discordWebhook != null) {
            try {
                DiscordWebhook webhook = new DiscordWebhook(GeneralConfig.discordWebhook);
                webhook.setUsername("(NTM) Extended Logging");
                webhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setTitle("**"+logType+"**")
                        .setDescription(details)
                        .setColor(Color.RED)
                        .setFooter((new Date()).toString(), null));
                webhook.execute(); //Handle exception
            } catch(IOException e) {
                MainRegistry.logger.log(Level.INFO, "Exception when trying to send discord message through webhook, might be because of invalid webhook url!");
                e.printStackTrace();
            }
        }
    }

    private void sendGlobalMessage(PlayerList playerList, String details) {
        playerList.getPlayers().forEach(player -> player.sendMessage(new TextComponentString(details)));
    }
*/}
