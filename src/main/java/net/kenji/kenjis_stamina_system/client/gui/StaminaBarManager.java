package net.kenji.kenjis_stamina_system.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.kenjis_stamina_system.KenjisStaminaSystem;
import net.kenji.kenjis_stamina_system.config.KenjisStaminaSystemCommon;
import net.kenji.kenjis_stamina_system.stamina.StaminaManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL30.glGenerateMipmap;

@Mod.EventBusSubscriber(modid = KenjisStaminaSystem.MODID, value = Dist.CLIENT)
public class StaminaBarManager {

    public static final ResourceLocation BG = new ResourceLocation(KenjisStaminaSystem.MODID, "textures/gui/stamina_bar_background.png");
    public static final ResourceLocation BAR_RED = new ResourceLocation(KenjisStaminaSystem.MODID, "textures/gui/stamina_bar_overlay_red.png");
    public static final ResourceLocation BAR_GREEN = new ResourceLocation(KenjisStaminaSystem.MODID, "textures/gui/stamina_bar_overlay.png");



    private static final int TEXTURE_WIDTH = 1024;
    private static final int TEXTURE_HEIGHT = 1368;
    public static float prevStamina = -1;
    private static float redBarStamina = -1;
    private static StaminaManager stamina;
    private static class IntPair {
        public int x;
        public int y;
    }
    private static class ResourcePair{
        public ResourceLocation background;
        public ResourceLocation overlay;
    }


    private static IntPair GetUVCoords(){
        Minecraft minecraft = Minecraft.getInstance();
        IntPair uvCoords = new IntPair();
        if (minecraft.options.getCameraType().isFirstPerson()){
            uvCoords.x = 265;
            uvCoords.y = 112;
        }
        else{
            uvCoords.x = 285;
            uvCoords.y = 105;
        }

        return uvCoords;
    }
    public static int currentTextSizeX = 54;
    public static int currentTextSizeY = 34;

    private static IntPair GetTextureSize(){
        Minecraft minecraft = Minecraft.getInstance();
        IntPair uvCoords = new IntPair();
        if (minecraft.options.getCameraType().isFirstPerson()){
            // Original: 42x68 (ratio 0.618)
            // Corrected to match 256:342 ratio:
            uvCoords.x = currentTextSizeX;  // 256 * 0.199 ≈ 51
            uvCoords.y = currentTextSizeY;  // 342 * 0.199 ≈ 68
        }
        else{
            // Original: 28x42 (ratio 0.667)
            // Corrected to match 256:342 ratio:
            uvCoords.x = 51;  // 256 * 0.199 ≈ 51
            uvCoords.y = 68;  // 342 * 0.199 ≈ 68
        }

        return uvCoords;
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null && KenjisStaminaSystemCommon.ENABLE_UI.get()) {
            if(stamina == null) {
                stamina = StaminaManager.get(player);
            }
            if(prevStamina == -1 || redBarStamina == -1) {
                prevStamina = stamina.currentMaxStamina;
                redBarStamina = stamina.currentMaxStamina;
            }

            int DISPLAY_WIDTH = GetTextureSize().x;
            int DISPLAY_HEIGHT = GetTextureSize().y;

            float currentStamina =stamina.currentStamina;
            float maxStamina = stamina.currentMaxStamina;

            ResourceLocation CurrentMainBar = !stamina.isPuffed ? BAR_GREEN : BAR_RED;
            if (currentStamina < maxStamina) {

                GuiGraphics gfx = event.getGuiGraphics();
                float pt = event.getPartialTick();

                // 1. Smooth value

                int x = GetUVCoords().x;
                int y = GetUVCoords().y;

                float scaleX = (float) DISPLAY_WIDTH / TEXTURE_WIDTH;
                float scaleY = (float) DISPLAY_HEIGHT / TEXTURE_HEIGHT;

                float smoothStamina = Mth.lerp(pt, prevStamina, currentStamina);


                PoseStack pose = gfx.pose();
                pose.pushPose();
                pose.translate(x, y, 0);
                pose.scale(scaleX, scaleY, 1f);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

// D            raw background with transparency
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.075f);

                gfx.blit(
                        BG,
                        0, 0,
                        0, 0,
                        TEXTURE_WIDTH, TEXTURE_HEIGHT,
                        TEXTURE_WIDTH, TEXTURE_HEIGHT
                );

                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);


                // Smooth delay effect using lerp
                float catchUpSpeed = 0.0005f; // Lower = slower catch up (try values between 0.01-0.1)

                if (redBarStamina > currentStamina) {
                    redBarStamina = Mth.lerp(catchUpSpeed, redBarStamina, currentStamina);
                    if (Math.abs(redBarStamina - currentStamina) < 0.1f) {
                        redBarStamina = currentStamina; // Snap when close enough
                    }
                } else {
                    redBarStamina = currentStamina;
                }
// Draw red bar
                float redFillPercent = Mth.clamp(redBarStamina / maxStamina, 0f, 1f);
                int redFilledTexHeight = (int)(TEXTURE_HEIGHT * redFillPercent);
                int redTexYOffset = TEXTURE_HEIGHT - redFilledTexHeight;
                int redScreenYOffset = TEXTURE_HEIGHT - redFilledTexHeight;

                gfx.blit(
                        BAR_RED,
                        0, redScreenYOffset,
                        0, redTexYOffset,
                        TEXTURE_WIDTH, redFilledTexHeight,
                        TEXTURE_WIDTH, TEXTURE_HEIGHT
                );

// Draw main bar
                float fillPercent = Mth.clamp(smoothStamina / maxStamina, 0f, 1f);
                int filledTexHeight = (int) (TEXTURE_HEIGHT * fillPercent);
                int texYOffset = TEXTURE_HEIGHT - filledTexHeight;
                int screenYOffset = TEXTURE_HEIGHT - filledTexHeight;


                gfx.blit(
                        CurrentMainBar,
                        0, screenYOffset,
                        0, texYOffset,
                        TEXTURE_WIDTH, filledTexHeight,
                        TEXTURE_WIDTH, TEXTURE_HEIGHT
                );

                pose.popPose();

                prevStamina = currentStamina;
            }
        }
    }

}