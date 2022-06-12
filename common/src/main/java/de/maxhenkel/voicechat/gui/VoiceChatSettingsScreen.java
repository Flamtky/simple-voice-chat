package de.maxhenkel.voicechat.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.gui.audiodevice.SelectMicrophoneScreen;
import de.maxhenkel.voicechat.gui.audiodevice.SelectSpeakerScreen;
import de.maxhenkel.voicechat.gui.widgets.*;
import de.maxhenkel.voicechat.gui.volume.PlayerVolumesScreen;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.client.Denoiser;
import de.maxhenkel.voicechat.voice.client.speaker.AudioType;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collections;

public class VoiceChatSettingsScreen extends VoiceChatScreenBase {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Voicechat.MODID, "textures/gui/gui_voicechat_settings.png");
    private static final Component TITLE = Component.translatable("gui.voicechat.voice_chat_settings.title");
    private static final Component ENABLED = Component.translatable("message.voicechat.enabled");
    private static final Component DISABLED = Component.translatable("message.voicechat.disabled");
    private static final Component ADJUST_VOLUMES = Component.translatable("message.voicechat.adjust_volumes");
    private static final Component SELECT_MICROPHONE = Component.translatable("message.voicechat.select_microphone");
    private static final Component SELECT_SPEAKER = Component.translatable("message.voicechat.select_speaker");
    private static final Component BACK = Component.translatable("message.voicechat.back");
    protected static final Component TOOLTIP_ACTIVATION = Component.translatable("message.voicechat.tooltip_activation");

    @Nullable
    private final Screen parent;

    public VoiceChatSettingsScreen(@Nullable Screen parent) {
        super(TITLE, 248, 219);
        this.parent = parent;
    }

    public VoiceChatSettingsScreen() {
        this(null);
    }

    @Override
    protected void init() {
        super.init();

        int y = guiTop + 20;

        addRenderableWidget(new VoiceSoundSlider(guiLeft + 10, y, xSize - 20, 20));
        y += 21;
        addRenderableWidget(new MicAmplificationSlider(guiLeft + 10, y, xSize - 20, 20));
        y += 21;
        BooleanConfigButton denoiser = addRenderableWidget(new BooleanConfigButton(guiLeft + 10, y, xSize - 20, 20, VoicechatClient.CLIENT_CONFIG.denoiser, enabled -> {
            return Component.translatable("message.voicechat.denoiser", enabled ? ENABLED : DISABLED);
        }));
        if (Denoiser.createDenoiser() == null) {
            denoiser.active = false;
        }
        y += 21;

        VoiceActivationSlider voiceActivationSlider = new VoiceActivationSlider(guiLeft + 10, y + 21, xSize - 20, 20);

        addRenderableWidget(new MicActivationButton(guiLeft + 10, y, xSize - 20, 20, voiceActivationSlider,(button, matrices, mouseX, mouseY) -> {
            renderTooltip(matrices, TOOLTIP_ACTIVATION, mouseX, mouseY);
        }));
        y += 21;

        addRenderableWidget(voiceActivationSlider);
        y += 21;

        MicTestButton micTestButton = new MicTestButton(guiLeft + 10, y, xSize - 20, 20, voiceActivationSlider);
        addRenderableWidget(micTestButton);
        y += 21;

        addRenderableWidget(new EnumButton<>(guiLeft + 10, y, xSize - 20, 20, VoicechatClient.CLIENT_CONFIG.audioType, null) {
            @Override
            protected Component getText(AudioType type) {
                return Component.translatable("message.voicechat.audio_type", type.getText());
            }

            @Override
            protected void onUpdate(AudioType type) {
                ClientVoicechat client = ClientManager.getClient();
                if (client != null) {
                    micTestButton.stop();
                    client.reloadAudio();
                }
            }
        });

        y += 21;
        if (isIngame()) {
            addRenderableWidget(new Button(guiLeft + 10, y, xSize - 20, 20, ADJUST_VOLUMES, button -> {
                minecraft.setScreen(new PlayerVolumesScreen());
            }));
            y += 21;
        }
        addRenderableWidget(new Button(guiLeft + 10, y, xSize / 2 - 15, 20, SELECT_MICROPHONE, button -> {
            minecraft.setScreen(new SelectMicrophoneScreen(this));
        }));
        addRenderableWidget(new Button(guiLeft + xSize / 2 + 6, y, xSize / 2 - 15, 20, SELECT_SPEAKER, button -> {
            minecraft.setScreen(new SelectSpeakerScreen(this));
        }));
        y += 21;
        if (!isIngame() && parent != null) {
            addRenderableWidget(new Button(guiLeft + 10, y, xSize - 20, 20, BACK, button -> {
                minecraft.setScreen(parent);
            }));
        }
    }

    @Override
    public void renderBackground(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        if (isIngame()) {
            blit(poseStack, guiLeft, guiTop, 0, 0, xSize, ySize);
        }
    }

    @Override
    public void renderForeground(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        int titleWidth = font.width(TITLE);
        font.draw(poseStack, TITLE.getVisualOrderText(), (float) (guiLeft + (xSize - titleWidth) / 2), guiTop + 7, getFontColor());
    }
}
