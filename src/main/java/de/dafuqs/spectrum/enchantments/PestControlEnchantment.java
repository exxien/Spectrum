package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class PestControlEnchantment extends SpectrumEnchantment {
	
	public PestControlEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
	}
	
	@Override
	public int getMinPower(int level) {
		return 10;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 20;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != SpectrumEnchantments.RESONANCE;
	}
	
}