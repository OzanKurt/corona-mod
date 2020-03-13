package com.ozankurt.corona.interfaces;

import com.ozankurt.corona.mixin.player.PlayerEntityMixin;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface PlayerMoveCallback
{
    Event<PlayerMoveCallback> EVENT = EventFactory.createArrayBacked(PlayerMoveCallback.class,
        (listeners) -> (player) -> {
            for (PlayerMoveCallback event : listeners) {
                ActionResult result = event.move(player);
                if(result != ActionResult.PASS) {
                    return result;
                }
            }
 
        return ActionResult.PASS;
    });
 
    ActionResult move(PlayerEntityMixin player);
}