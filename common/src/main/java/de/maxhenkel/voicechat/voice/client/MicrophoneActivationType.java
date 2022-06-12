package de.maxhenkel.voicechat.voice.client;

import net.minecraft.network.chat.Component;

public enum MicrophoneActivationType {

    PTT(Component.translatable("message.voicechat.activation_type.ptt")),
    VOICE(Component.translatable("message.voicechat.activation_type.voice")),
    GROUP_PTT(Component.translatable("message.voicechat.activation_type.group_ppt"));

    private final Component component;

    MicrophoneActivationType(Component component) {
        this.component = component;
    }

    public Component getText() {
        return component;
    }
}
