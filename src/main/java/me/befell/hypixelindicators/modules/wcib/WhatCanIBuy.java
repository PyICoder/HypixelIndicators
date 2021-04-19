package me.befell.hypixelindicators.modules.wcib;

import me.befell.hypixelindicators.HypixelIndicators;
import me.befell.hypixelindicators.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhatCanIBuy {
    final List<String> correct_names = Arrays.asList("Quick Buy", "Blocks", "Melee", "Armor", "Tools", "Ranged", "Potions", "Utility", "Upgrades & Traps", "Queue a trap");
    final private Pattern costPattern = Pattern.compile("\u00a75\u00a7o\u00a77.+: \u00a7(?<type>.)(?<cost>\\d+)");
    private int ironCount;
    private int goldCount;
    private int emeraldCount;
    private int diamondCount;
    private HypixelIndicators mod;

    public  WhatCanIBuy(HypixelIndicators mod){
        this.mod = mod;
    }

    @SubscribeEvent
    public void onRenderGuiBackground(final GuiScreenEvent.DrawScreenEvent.Pre e) {
        if (e.gui instanceof GuiChest) {
            GuiChest guiChest = (GuiChest) e.gui;

            Container inventorySlots = guiChest.inventorySlots;
            IInventory inventory = inventorySlots.getSlot(0).inventory;
            if (correct_names.contains(inventory.getName())) {
                this.mod.getPool().start(() ->{
                    int ironCount = 0;
                    int goldCount = 0;
                    int emeraldCount = 0;
                    int diamondCount = 0;
                    InventoryPlayer playerItems = Minecraft.getMinecraft().thePlayer.inventory;
                    for (int i = 0; i < playerItems.getSizeInventory(); i++) {
                        ItemStack slotItem = playerItems.getStackInSlot(i);
                    if (slotItem != null) {
                        if (slotItem.getItem().equals(Items.iron_ingot)) {
                            ironCount += slotItem.stackSize;
                        } else if (slotItem.getItem().equals(Items.gold_ingot)) {
                            goldCount += slotItem.stackSize;
                        } else if (slotItem.getItem().equals(Items.emerald)) {
                            emeraldCount += slotItem.stackSize;
                        } else if (slotItem.getItem().equals(Items.diamond)) {
                            diamondCount += slotItem.stackSize;
                        }

                    }
                    this.ironCount = ironCount;
                    this.goldCount = goldCount;
                    this.emeraldCount = emeraldCount;
                    this.diamondCount = diamondCount;
                }
                });
                int guiLeft = (guiChest.width - 176) / 2;
                int inventoryRows = inventory.getSizeInventory() / 9;
                int ySize = 222 - 108 + inventoryRows * 18;
                int guiTop = (guiChest.height - ySize) / 2;
                GlStateManager.pushMatrix();
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.translate(0, 0, 1);
                for (Slot inventorySlot : inventorySlots.inventorySlots) {
                    int slotRow = inventorySlot.slotNumber / 9;
                    int slotColumn = inventorySlot.slotNumber % 9;
                    int slotX = guiLeft + inventorySlot.xDisplayPosition;
                    int slotY = guiTop + inventorySlot.yDisplayPosition;
                    int maxRow = inventoryRows - 1;
                    int maxRow2 = inventoryRows - 3;
                    // check if slot is one of the middle slots with items
                    if ((inventory.getName().equals("Queue a trap") && slotRow > 0 && slotRow < maxRow && slotColumn > 0 && slotColumn < 8)
                            || (inventory.getName().equals("Upgrades & Traps") && slotRow > 0 && slotRow < maxRow2 && slotColumn > 0 && slotColumn < 8)
                            || slotRow > 1 && slotRow < maxRow && slotColumn > 0 && slotColumn < 8) {
                        renderIndicator(inventorySlot.getStack(), slotX, slotY, (inventory.getName().equals("Upgrades & Traps") || inventory.getName().equals("Queue a trap")));
                    }
                }

                GlStateManager.disableBlend();
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();


            }

        }

    }

    public void renderIndicator(ItemStack item, int x, int y, boolean upgrades) {
        try {
            if (EnumChatFormatting.getTextWithoutFormattingCodes(item.getDisplayName()).equals("Buy a trap")) {
                return;
            }
            List<String> tooltip = item.getTooltip(Minecraft.getMinecraft().thePlayer, false);
            if (tooltip.size() < 3) {
                return;
            }


//            tooltip.remove(0);
//            tooltip.forEach(line -> {
//                if (line.c4)
//            });
            Matcher costMatcher = costPattern.matcher(tooltip.get(1));
            boolean GreenRed = false;
            System.out.println(tooltip.get(1));
            System.out.println(costMatcher.matches());
            if (!upgrades && costMatcher.matches()) {
                System.out.println(costMatcher.group("cost"));
                String type = costMatcher.group("type");

                switch (type) {
                    case "f":
                        if (this.ironCount >= Integer.parseInt(costMatcher.group("cost"))) {
                            GreenRed = true;
                        }
                        break;
                    case "6":
                        if (this.goldCount >= Integer.parseInt(costMatcher.group("cost"))) {
                            GreenRed = true;
                        }
                        break;
                    case "2":
                        if (this.emeraldCount >= Integer.parseInt(costMatcher.group("cost"))) {
                            GreenRed = true;
                        }
                        break;
                }

                GuiUtils.drawRect(x, y, x + 16, y + 16, (GreenRed ? -16537085 : -7207160));
            } else if (upgrades) {
                String BuyLine = EnumChatFormatting.getTextWithoutFormattingCodes(tooltip.get(tooltip.size() - 1));
                if (BuyLine.startsWith("Click")) {
                    GuiUtils.drawRect(x, y, x + 16, y + 16, -16537085);
                } else if (BuyLine.startsWith("You")) {
                    GuiUtils.drawRect(x, y, x + 16, y + 16, -7207160);
                }
            }

        } catch (NullPointerException | NumberFormatException ignored) {
        }
    }


}
