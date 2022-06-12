package de.maxhenkel.voicechat.plugins.impl.events;

import de.maxhenkel.voicechat.api.VoicechatClientApi;
import de.maxhenkel.voicechat.api.events.ClientSoundEvent;

public class ClientSoundEventImpl extends ClientEventImpl implements ClientSoundEvent {

    private short[] rawAudio;
    private boolean whispering;
    private boolean toGroup;

    public ClientSoundEventImpl(VoicechatClientApi api, short[] rawAudio, boolean whispering, boolean toGroup) {
        super(api);
        this.rawAudio = rawAudio;
        this.whispering = whispering;
        this.toGroup = toGroup;
    }

    @Override
    public short[] getRawAudio() {
        return rawAudio;
    }

    @Override
    public void setRawAudio(short[] rawAudio) {
        this.rawAudio = rawAudio;
    }

    @Override
    public boolean isWhispering() {
        return whispering;
    }

    @Override
    public boolean isToGroup() {
        return toGroup;
    }
}
