package cn.mmf.slashblade_tic;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeEvent;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;
import cn.mmf.slashblade_tic.compat.tinkertoolleveling.ModBladeLeveling;
import cn.mmf.slashblade_tic.item.RegisterLoader;
import cn.mmf.slashblade_tic.modifiers.ModBladeExtraTrait;
import cn.mmf.slashblade_tic.modifiers.ModProud;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.mantle.util.RecipeMatchRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.modifiers.ModAntiMonsterType;
import slimeknights.tconstruct.tools.modifiers.ModBeheading;
import slimeknights.tconstruct.tools.modifiers.ModCreative;
import slimeknights.tconstruct.tools.modifiers.ModDiamond;
import slimeknights.tconstruct.tools.modifiers.ModEmerald;
import slimeknights.tconstruct.tools.modifiers.ModFiery;
import slimeknights.tconstruct.tools.modifiers.ModKnockback;
import slimeknights.tconstruct.tools.modifiers.ModNecrotic;
import slimeknights.tconstruct.tools.modifiers.ModReinforced;
import slimeknights.tconstruct.tools.modifiers.ModSharpness;
import slimeknights.tconstruct.tools.modifiers.ModSilktouch;
import slimeknights.tconstruct.tools.modifiers.ModSoulbound;
import slimeknights.tconstruct.tools.modifiers.ModWebbed;

public class CommonProxy {
	 public static Modifier modProud;
	 public static List<Modifier> extraTraitMods;
	public void preInit(FMLPreInitializationEvent event)
	{
		new RegisterLoader(event);
		new NetHandler();
	}
	
    public void init(FMLInitializationEvent event)
    { 

    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modBaneOfArthopods);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modBeheading);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modCreative);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modDiamond);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modEmerald);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modFiery);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modNecrotic);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modSmite);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modReinforced);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modSharpness);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modSoulbound);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modWebbed);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modShulking);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modMendingMoss);
    	modProud=new ModProud();
    	modProud.addItem(SlashBlade.findItemStack(SlashBlade.modid, SlashBlade.TrapezohedronBladeSoulStr, 1), 1, 1);
    	TinkerSlashBladeRegistry.registerModifier(modProud);
    }


	public void postInit(FMLPostInitializationEvent event)
    {
        if (Loader.isModLoaded("tinkertoolleveling")) {
            ModBladeLeveling.modLeveling = new ModBladeLeveling();
        }
        registerExtraTraitModifiers();

    }
	
	  private Map<String, ModBladeExtraTrait> extraTraitLookup = new HashMap<>();
	  private void registerExtraTraitModifiers() {
		    TinkerRegistry.getAllMaterials().forEach(this::registerExtraTraitModifiers);
		    extraTraitMods = Lists.newArrayList(extraTraitLookup.values());
		  }

		  private void registerExtraTraitModifiers(Material material) {
		    TinkerSlashBladeRegistry.getTools().forEach(tool -> registerExtraTraitModifiers(material, tool));
		  }

		  private void registerExtraTraitModifiers(Material material, SlashBladeCore tool) {
		    tool.getRequiredComponents().forEach(pmt -> registerExtraTraitModifiers(material, tool, pmt));
		  }

		  private void registerExtraTraitModifiers(Material material, SlashBladeCore tool, PartMaterialType partMaterialType) {
		    partMaterialType.getPossibleParts().forEach(part -> registerExtraTraitModifiers(material, tool, partMaterialType, part));
		  }

		  private <T extends Item & IToolPart> void registerExtraTraitModifiers(Material material, SlashBladeCore tool, PartMaterialType partMaterialType, IToolPart toolPart) {
		    if(toolPart instanceof Item) {
		      Collection<ITrait> traits = partMaterialType.getApplicableTraitsForMaterial(material);
		      if(!traits.isEmpty()) {
		        // we turn it into a set to remove duplicates, reducing the total amount of modifiers created by roughly 25%!
		        final Collection<ITrait> traits2 = ImmutableSet.copyOf(traits);
		        String identifier = ModBladeExtraTrait.generateIdentifier(material, traits2);
		        ModBladeExtraTrait mod = extraTraitLookup.computeIfAbsent(identifier, id -> new ModBladeExtraTrait(material, traits2, identifier));
		        mod.addCombination(tool, (T) toolPart);
		      }
		    }
		  }
}
