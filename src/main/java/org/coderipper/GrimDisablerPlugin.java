package org.coderipper;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

public class GrimDisablerPlugin extends Plugin {
	
	@Override
	public void onLoad() {
		
		final GrimDisablerModule grimDisablerModule = new GrimDisablerModule();
		RusherHackAPI.getModuleManager().registerFeature(grimDisablerModule);

	}
	
	@Override
	public void onUnload() {
		this.getLogger().info("Grim Disabler Plugin unloaded!");
	}
	
}