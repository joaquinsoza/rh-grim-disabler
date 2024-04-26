package org.coderipper;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.util.Mth;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.utils.ChatUtils;
import org.rusherhack.client.api.utils.InventoryUtils;
import org.rusherhack.core.event.stage.Stage;
import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.BooleanSetting;
import org.rusherhack.core.setting.NumberSetting;

/**
 * Grim Trident Module
 *
 * @author coderipper
 */
public class GrimTridentModule extends ToggleableModule {
	
	/**
	 * Settings
	 */

	private final NumberSetting<Integer> tridentDelay = new NumberSetting<>("Trident Delay", 0, 0, 20);
	private final BooleanSetting grimDisable = new BooleanSetting("Disable Grim", "bypass grim movement checks", false);
	private final BooleanSetting pauseOnEat = new BooleanSetting("Pause on Eat", "Pauses when eating", false);
	
	private int tick = 0;

	/**
	 * Constructor
	 */
	public GrimTridentModule() {
		super("Grim Trident", "Grim Trident by coderipper", ModuleCategory.CLIENT);
		
		this.registerSettings(
				this.tridentDelay,
				this.grimDisable,
				this.pauseOnEat
		);
	}
	
	@Override
	public void onEnable() {
		if(mc.level != null) {
			ChatUtils.print("Grim Trident: enabled");
			tick = tridentDelay.getValue();
		}
	}
	
	@Override
	public void onDisable() {
		if(mc.level != null) {
			ChatUtils.print("Grim Trident: disabled");
		}
	}

	@Subscribe(stage = Stage.ON)
	public void tick(EventUpdate event) {
		if (tick <= 0) {
			if (tridentDelay.getValue() != 0)
				tick = tridentDelay.getValue();

			int tridentSlot = InventoryUtils.findItemHotbar(Items.TRIDENT);
			int oldSlot = mc.player.getInventory().selected;

			if (tridentSlot == -1 || (pauseOnEat.getValue() && mc.player.isUsingItem())) {
				ChatUtils.print("There's not a Trident in your hotbar");
				return;
			}
			
			mc.player.connection.send(new ServerboundSetCarriedItemPacket(tridentSlot));
			mc.player.connection.send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 0));
			mc.player.connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN));

			if (!grimDisable.getValue()) {
				float f = mc.player.getYRot();
				float g = mc.player.getXRot();
				float h = -Mth.sin(f * (float) (Math.PI / 180.0)) * Mth.cos(g * (float) (Math.PI / 180.0));
				float k = -Mth.sin(g * (float) (Math.PI / 180.0));
				float l = Mth.cos(f * (float) (Math.PI / 180.0)) * Mth.cos(g * (float) (Math.PI / 180.0));
				float m = Mth.sqrt(h * h + k * k + l * l);
				float n = 3.0F;
				h *= n / m;
				k *= n / m;
				l *= n / m;

				mc.player.addDeltaMovement(new Vec3(h, k, l));
				if (mc.player.onGround()) {
					mc.player.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
				}
			}

			mc.player.connection.send(new ServerboundSetCarriedItemPacket(oldSlot));

		} else {
			tick--;
		}

	}
}
