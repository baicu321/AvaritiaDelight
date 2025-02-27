package committee.nova.mods.avaritiadelight.screen.gui;

import committee.nova.mods.avaritiadelight.AvaritiaDelight;
import committee.nova.mods.avaritiadelight.screen.handler.ExtremeCookingPotScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class ExtremeCookingPotScreen extends HandledScreen<ExtremeCookingPotScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(AvaritiaDelight.MOD_ID, "textures/gui/extreme_cooking_pot_gui.png");

    public ExtremeCookingPotScreen(ExtremeCookingPotScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 234;
        this.backgroundHeight = 277;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        return mouseX < (double) left || mouseY < (double) top || mouseX >= (double) (left + this.backgroundWidth) || mouseY >= (double) (top + this.backgroundHeight);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, 512, 512);
        int l = this.handler.getCookProgressionScaled();
        context.drawTexture(TEXTURE, this.x + 172, this.y + 90, 234, 15, l + 1, 17, 512, 512);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean heated = this.handler.isHeated();
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        if (heated)
            context.drawTexture(TEXTURE, this.x + 177, this.y + 66, 234, 0, 17, 15, 512, 512);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
        if (this.isPointWithinBounds(174, 63, 21, 21, mouseX, mouseY)) {
            String key = "container.cooking_pot." + (heated ? "heated" : "not_heated");
            context.drawTooltip(this.textRenderer, TextUtils.getTranslation(key, this.handler), mouseX, mouseY);
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, 7, 6, 4210752, false);
        context.drawText(this.textRenderer, this.playerInventoryTitle, 37, 184, 4210752, false);
    }
}
