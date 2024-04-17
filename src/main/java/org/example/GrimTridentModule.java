package org.example;

import net.minecraft.world.item.Items;
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
 * Example rusherhack module
 *
 * @author John200410
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
		super("Grim Trident", "Grim trident by coderipper", ModuleCategory.CLIENT);
		
		//register settings
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

	@Subscribe(stage = Stage.PRE)
	public void tick(EventUpdate event) {
		if (tick <= 0) {
			ChatUtils.print("tick");
			if (tridentDelay.getValue() != 0)
				tick = tridentDelay.getValue();

			int tridentSlot = InventoryUtils.findItemHotbar(Items.TRIDENT);
			ChatUtils.print("tridentSlot: " + tridentSlot);
			int oldSlot = mc.player.getInventory().selected;

			if (tridentSlot == -1 || (pauseOnEat.getValue() && mc.player.isUsingItem())) return;
			
			// mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(tridentSlot));
			// mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));

			// if (!grimDisable.get()) {
			// 	float f = mc.player.getYaw();
			// 	float g = mc.player.getPitch();
			// 	float h = -MathHelper.sin(f * (float) (Math.PI / 180.0)) * MathHelper.cos(g * (float) (Math.PI / 180.0));
			// 	float k = -MathHelper.sin(g * (float) (Math.PI / 180.0));
			// 	float l = MathHelper.cos(f * (float) (Math.PI / 180.0)) * MathHelper.cos(g * (float) (Math.PI / 180.0));
			// 	float m = MathHelper.sqrt(h * h + k * k + l * l);
			// 	float n = 3.0F;
			// 	h *= n / m;
			// 	k *= n / m;
			// 	l *= n / m;
			// 	mc.player.addVelocity(h, k, l);
			// 	if (mc.player.isOnGround()) {
			// 		mc.player.move(MovementType.SELF, new Vec3d(0.0, 1.1999999F, 0.0));
			// 	}
			// }

			// mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(oldSlot));

		} else {
			tick--;
		}

	}
}
