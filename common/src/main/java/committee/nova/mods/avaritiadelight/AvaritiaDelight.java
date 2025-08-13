package committee.nova.mods.avaritiadelight;

import com.mojang.logging.LogUtils;
import committee.nova.mods.avaritiadelight.registry.*;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.slf4j.Logger;

public final class AvaritiaDelight {
    public static final String MOD_ID = "avaritia_delight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        ADBlocks.REGISTRY.register();
        ADBlockEntities.REGISTRY.register();
        ADItems.REGISTRY.register();
        ADItemGroups.REGISTRY.register();
        ADRecipes.TYPE_REGISTRY.register();
        ADRecipes.SERIALIZER_REGISTRY.register();
        ADScreenHandlers.REGISTRY.register();
        ADEffects.REGISTRY.register();
        CommandRegistrationEvent.EVENT.register((dispatcher, access, env) -> {
            dispatcher.register(CommandManager.literal("nbt").requires(ServerCommandSource::isExecutedByPlayer).executes(ctx -> {
                PlayerEntity player = ctx.getSource().getPlayerOrThrow();
                String nbt = player.getMainHandStack().getOrCreateNbt().toString();
                player.sendMessage(Text.literal(nbt).fillStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, nbt))));
                return 1;
            }));
        });
    }

    public static void process() {
        BlockEvent.BREAK.register((world, pos, state, player, intValue) -> {
            BlockState down = world.getBlockState(pos.down());
            if (down.isOf(ADBlocks.SOUL_RICH_SOIL_FARMLAND.get()) && state.getBlock() instanceof CropBlock)
                Block.dropStacks(state, world, pos);
            return EventResult.pass();
        });
    }

}
