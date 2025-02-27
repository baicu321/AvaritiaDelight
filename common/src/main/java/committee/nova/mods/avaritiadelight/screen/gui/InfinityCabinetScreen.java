package committee.nova.mods.avaritiadelight.screen.gui;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.screen.handler.InfinityCabinetScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class InfinityCabinetScreen extends HandledScreen<InfinityCabinetScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(AvaritiaDelight.MOD_ID, "textures/gui/infinity_cabinet.png");

    public InfinityCabinetScreen(InfinityCabinetScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 499;
        this.backgroundHeight = 275;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, 500, 500);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, 7, 6, 4210752, false);
        context.drawText(this.textRenderer, this.playerInventoryTitle, 169, 182, 4210752, false);
    }
}
