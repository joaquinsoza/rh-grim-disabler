package org.coderipper;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

/**
 * Example rusherhack plugin
 *
 * @author John200410
 */
public class GrimTridentPlugin extends Plugin {
	
	@Override
	public void onLoad() {
		
		//creating and registering a new module
		final GrimTridentModule grimTridentModule = new GrimTridentModule();
		RusherHackAPI.getModuleManager().registerFeature(grimTridentModule);

	}
	
	@Override
	public void onUnload() {
		this.getLogger().info("Grim Trident Plugin unloaded!");
	}
	
}