package net.obmc.OBMetaProducerBungee;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OBMetaEvent implements Listener {

	PrintWriter tracker = null;
	
	public OBMetaEvent() {
	}
	
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
    	// send message to all players that someone has joined OB
    	for ( ProxiedPlayer player : ProxyServer.getInstance().getPlayers() ) {
    		if ( event.getPlayer().getName() != player.getName() ) {
    			player.sendMessage( new TextComponent(event.getPlayer().getName() + " has joined the network.") );
    		}
        }
    }
    
    @EventHandler
    public void onPlayerDisconnect( PlayerDisconnectEvent event ) {
    	// add event to the tracker
   		String disconnectMsg = "PlayerDisconnectEvent#" + event.getPlayer().getName() + "#ob-" + event.getPlayer().getServer().getInfo().getName() + "#" + getTimestamp();
   		logTrackerMsg( disconnectMsg );
    }
    
	@EventHandler
    public void onServerSwitch( ServerSwitchEvent event ) {
    	// add event to the tracker
    	String switchMsg = "ServerSwitchEvent#" + event.getPlayer().getName() + "#ob-" + event.getPlayer().getServer().getInfo().getName() + "#" + getTimestamp();
    	logTrackerMsg( switchMsg );
    }  
    
    private void logTrackerMsg( String msg ) {
    	
    	try ( RandomAccessFile stream = new RandomAccessFile( OBMetaProducer.metafile, "rw" );
    		     FileChannel channel = stream.getChannel()) {

    		    // Use tryLock() or lock() to block until the lock is acquired
    		    FileLock lock = channel.lock();
    		    try {
    		        // Move to the end of the file to append data
    		        channel.position(channel.size());
    		        // Write data
    		        channel.write( ByteBuffer.wrap( ( msg + "\n" ).getBytes() ) );
    		    } finally {
    		        lock.release();
    		    }
    		} catch ( IOException e ) {
    		    e.printStackTrace();
    		}		
	}
    
    private String getTimestamp() {
    	return new SimpleDateFormat( "MM/dd HH:mm:ss.ms" ).format( new Date() );

    }
}
