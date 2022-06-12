package de.maxhenkel.voicechat.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.configbuilder.ConfigEntry;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class EnumButton<T extends Enum<T>> extends AbstractButton {

    protected ConfigEntry<T> entry;
    protected EnumButton.TooltipSupplier tooltipSupplier;

    public EnumButton(int xIn, int yIn, int widthIn, int heightIn, ConfigEntry<T> entry, EnumButton.TooltipSupplier tooltipSupplier) {
        super(xIn, yIn, widthIn, heightIn, Component.empty());
        this.entry = entry;
        this.tooltipSupplier = tooltipSupplier;
        updateText();
    }

    protected void updateText() {
        setMessage(getText(entry.get()));
    }

    protected abstract Component getText(T type);

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);

        if (isHovered) {
            renderToolTip(matrices, mouseX, mouseY);
        }
    }

    public void renderToolTip(PoseStack matrices, int mouseX, int mouseY) {
        if (tooltipSupplier != null) {
            tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
        }
    }

    protected void onUpdate(T type) {

    }

    @Override
    public void onPress() {
        T e = entry.get();
        Enum<T>[] values = e.getClass().getEnumConstants();
        T type = (T) values[(e.ordinal() + 1) % values.length];
        entry.set(type).save();
        updateText();
        onUpdate(type);
    }

    public interface TooltipSupplier {
        void onTooltip(EnumButton<?> button, PoseStack matrices, int mouseX, int mouseY);
    }

    @Override
    public void updateNarration(NarrationElementOutput narration) {
        defaultButtonNarrationText(narration);
    }
}
