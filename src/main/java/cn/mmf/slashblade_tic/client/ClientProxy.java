package cn.mmf.slashblade_tic.client;

import org.lwjgl.input.Keyboard;

import cn.mmf.slashblade_tic.CommonProxy;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistryClient;
import cn.mmf.slashblade_tic.client.book.BookBladeSmith;
import cn.mmf.slashblade_tic.client.gui.SlashBladeBuildGuiInfo;
import cn.mmf.slashblade_tic.item.RegisterLoader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.book.TinkerBook;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.tools.melee.TinkerMeleeWeapons;

public class ClientProxy extends CommonProxy {
	public static final ModelResourceLocation modelLoc = new ModelResourceLocation("flammpfeil.slashblade:model/named/blade.obj","inventory");

		@Override
	    public void preInit(FMLPreInitializationEvent event)
	    {
	        super.preInit(event);
	        BookBladeSmith.init();
	    }


	    @Override
	    public void init(FMLInitializationEvent event)
	    {
	        super.init(event);

	        registerToolBuildInfo();
	    }

	    @Override
	    public void postInit(FMLPostInitializationEvent event)
	    {
	        super.postInit(event);
	        BookBladeSmith.INSTANCE.fontRenderer = TinkerBook.INSTANCE.fontRenderer;
	    }
	    private void registerToolBuildInfo() {
	        SlashBladeBuildGuiInfo info;

	        info = new SlashBladeBuildGuiInfo(RegisterLoader.sb);
	        info.addSlotPosition(33 - 20 - 1, 42 + 20); 
	        info.addSlotPosition(33 + 20 - 5, 42 - 20 + 4); 
	        info.addSlotPosition(33 - 2 - 1, 42 + 2); 
	        TinkerSlashBladeRegistryClient.addToolBuilding(info);
	        SlashBladeBuildGuiInfo info2;
	        info2 = new SlashBladeBuildGuiInfo(RegisterLoader.sb_white);
	        info2.addSlotPosition(33 - 20 - 1, 42 + 20); 
	        info2.addSlotPosition(33 + 20 - 5, 42 - 20 + 4); 
	        info2.addSlotPosition(33 - 2 - 1, 42 + 2); 
	        TinkerSlashBladeRegistryClient.addToolBuilding(info2);
	        
	    }
       
}
