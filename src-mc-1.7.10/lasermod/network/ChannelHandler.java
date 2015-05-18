package lasermod.network;

import lasermod.LaserMod;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;

/**
 * @author ProPercivalalb
 */
public class ChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket>{
	
	public FMLProxyPacket packet;
	
    public ChannelHandler() {
        for (int i = 0; i < PacketType.values().length; i++)
            addDiscriminator(i, PacketType.values()[i].packetClass);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf bytes) throws Exception {
    	PacketBuffer packetbuffer = new PacketBuffer(bytes);

    	msg.write(packetbuffer);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf bytes, IPacket msg)  {
		try {
			PacketBuffer packetbuffer = new PacketBuffer(bytes);
			
			msg.read(packetbuffer);
			EntityPlayer player = null;
			
			INetHandler handler = this.packet.getOrigin().getNetHandler();
			if(handler instanceof NetHandlerPlayServer) {
				player = ((NetHandlerPlayServer)handler).playerEntity;
			}
			
			else {
				player = LaserMod.proxy.getClientPlayer();
			}
			
			msg.execute(player);
		} 
    	catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    @Override
    protected void testMessageValidity(FMLProxyPacket msg) {
    	this.packet = msg;
    }
}
