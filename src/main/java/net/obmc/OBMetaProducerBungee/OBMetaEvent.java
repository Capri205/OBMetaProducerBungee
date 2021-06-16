package net.obmc.OBMetaProducerBungee;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerConnectRequest;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OBMetaEvent implements Listener {

	PrintWriter tracker = null;
	
	public OBMetaEvent(PrintWriter pw) {
		tracker = pw;
	}
	
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
    	// send message to all players that someone has joined OB
    	for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
    		if (event.getPlayer().getName() != player.getName()) {
    			player.sendMessage(new TextComponent(event.getPlayer().getName() + " has joined the network."));
    		}
        }
    }
    
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
    	// add event to the tracker
    	try {
    		tracker.print("PlayerDisconnectEvent#" + event.getPlayer().getName() + "#ob-" + event.getPlayer().getServer().getInfo().getName() + "#" + getTimestamp() + "\n");
    		tracker.flush();
    	} catch (NullPointerException e) {
    		// catches timeout events
    	}
    }
    
    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
    	// add event to the tracker
    	tracker.print("ServerSwitchEvent#" + event.getPlayer().getName() + "#ob-" + event.getPlayer().getServer().getInfo().getName() + "#" + getTimestamp() + "\n");
    	tracker.flush();
    }  
    
    /*
    @EventHandler
    public void ServerConnectedEvent(ServerConnectedEvent event) {
    	// add event to the tracker
    	tracker.print("ServerConnectedEvent:" + event.getPlayer().getName() + ":ob-" + event.getServer().getInfo().getName() + ":" + getTimestamp() + "\n");
    	tracker.flush();
    }
    */
    
    private String getTimestamp() {
    	return new SimpleDateFormat("MM/dd HH:mm:ss.ms").format(new Date());

    }
}
