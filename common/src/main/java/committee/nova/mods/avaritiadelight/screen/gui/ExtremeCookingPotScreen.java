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
        int x = (this.width - 256) / 2;
        int y = (this.height - 256) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, 512, 512);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int x = (this.width - 256) / 2;
        int y = (this.height - 256) / 2;
        boolean heated = this.handler.isHeated();
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        if (heated)
            context.drawTexture(TEXTURE, x + 177, y + 66, 234, 0, 17, 15, 512, 512);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
        if (this.isPointWithinBounds(164, 73, 21, 21, mouseX, mouseY)) {
            String key = "container.cooking_pot." + (heated ? "heated" : "not_heated");
            context.drawTooltip(this.textRenderer, TextUtils.getTranslation(key, this.handler), mouseX, mouseY);
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title,  -4,  16, 4210752,false);
        context.drawText(this.textRenderer, this.playerInventoryTitle,  26,  194, 4210752,false);
    }
}
