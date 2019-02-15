package cn.mmf.slashblade_tic.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;

import org.apache.commons.io.IOUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;

import cn.mmf.slashblade_tic.client.MixTexture;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureMixer {

	   public BufferedImage TextureMix(List<ResourceLocationRaw> list,float alpha) throws IOException {
	        // 获取List并读取
		   	IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
		   	List<BufferedImage> imgs = new ArrayList<BufferedImage>();
	        BufferedImage buffImg = new BufferedImage(64, 128, BufferedImage.TYPE_4BYTE_ABGR);
	        InputStream imageStream;
	        for(int i = 0; i<list.size();i++){
	        	imageStream = manager.getResource(list.get(i)).getInputStream();
	        	try
	            {
	        		imgs.add(ImageIO.read(imageStream));
	            }
	            finally
	            {
	                IOUtils.closeQuietly(imageStream);
	        	}
	        }
	        
	        // 创建Graphics2D对象，用在底图对象上绘图
            Graphics2D g2d = buffImg.createGraphics();  
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, alpha));
	        g2d.drawImage(imgs.get(0), 0, 0, imgs.get(0).getWidth(), imgs.get(0).getHeight(), null);
	        g2d.drawImage(imgs.get(1), 0, 64, imgs.get(1).getWidth(), imgs.get(1).getHeight(), null);
	        g2d.drawImage(imgs.get(2), 0, 96, imgs.get(2).getWidth(), imgs.get(2).getHeight(), null);
	        g2d.dispose();
	        g2d.finalize();
	        return buffImg;
	    }

		private final Cache<BufferedImage, ResourceLocation> texCache = CacheBuilder.newBuilder()
				.maximumSize(500L)
				.expireAfterWrite(10L, TimeUnit.MINUTES)
				.build();
	 private final Map<String, Integer> mapTextureCounters = Maps.<String, Integer>newHashMap();

	   public ResourceLocationRaw  generateTexture(BufferedImage buffImg) throws ExecutionException {
		   ResourceLocation res = texCache.get(buffImg,()-> getMixTextureLocation("blade_texture", new MixTexture(buffImg)));
		   ResourceLocationRaw res1 = new ResourceLocationRaw(res.getResourceDomain(), res.getResourcePath());
		return res1;
	    }
	   public TextureMixer() {
		// TODO Auto-generated constructor stub
	   }
	   
	   public ResourceLocation getMixTextureLocation(String name, MixTexture texture)
	    {
	        Integer integer = this.mapTextureCounters.get(name);

	        if (integer == null)
	        {
	            integer = Integer.valueOf(1);
	        }
	        else
	        {
	            integer = integer.intValue() + 1;
	        }

	        this.mapTextureCounters.put(name, integer);
	        ResourceLocation resourcelocation = new ResourceLocation(String.format("mix_texture/%s_%d", name, integer));
	        Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, texture);
	        return resourcelocation;
	    }
}