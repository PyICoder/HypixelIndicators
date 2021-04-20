package me.befell.hypixelindicators;

import co.uk.isxander.xanderlib.XanderLib;
import me.befell.hypixelindicators.modules.wcib.WhatCanIBuy;
import me.befell.hypixelindicators.modules.wkaiu.WhatKitAmIUsing;
import me.befell.hypixelindicators.utils.Game;
import me.befell.hypixelindicators.utils.ThreadPool;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;

@Mod(modid = HypixelIndicators.MODID, version = HypixelIndicators.VERSION)
public class HypixelIndicators {
    public static final String MODID = "hypixelindicators";
    public static final String VERSION = "1.0";
    final private ThreadPool threadPool = new ThreadPool();
    final private Game gameUtil = new Game();

    @EventHandler
    public void init(FMLInitializationEvent event) {
        XanderLib.getInstance().initPhase();
        final EventBus bus = MinecraftForge.EVENT_BUS;

        bus.register(new WhatCanIBuy(this));
        bus.register(new WhatKitAmIUsing());
        bus.register(gameUtil);
    }
    public ThreadPool getPool(){
        return this.threadPool;
    }
    public Game gameUtil(){
        return this.gameUtil;
    }
}
