package me.befell.hypixelindicators.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private String game;
    public String aquireGame() {
        try {
            String game = EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName());

        } catch (Exception ignored) {
        }
        return game;
    }
    public String getGame(){
        return this.game;
    }
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load e) {
        TimerTask exe = new TimerTask() {
            public void run() {
                game = aquireGame();
            }
        };
        Timer timer = new Timer("WAIDelay");
        timer.schedule(exe, 2000);
    }
}
