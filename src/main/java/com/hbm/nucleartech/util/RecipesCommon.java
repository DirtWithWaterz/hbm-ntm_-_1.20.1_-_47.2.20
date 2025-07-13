package com.hbm.nucleartech.util;

import java.util.*;
import java.util.stream.Collectors;

import com.hbm.nucleartech.lib.Library;
import com.hbm.nucleartech.HBM;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;


public class RecipesCommon {
	
	public static ItemStack[] copyStackArray(ItemStack[] array) {
		
		if(array == null)
			return null;
		
		ItemStack[] clone = new ItemStack[array.length];
		
		for(int i = 0; i < array.length; i++) {
			
			if(array[i] != null)
				clone[i] = array[i].copy();
		}
		
		return clone;
	}
	
	public static ItemStack[] objectToStackArray(Object[] array) {

		if(array == null)
			return null;
		
		ItemStack[] clone = new ItemStack[array.length];
		
		for(int i = 0; i < array.length; i++) {
			
			if(array[i] instanceof ItemStack)
				clone[i] = (ItemStack)array[i];
		}
		
		return clone;
	}
	
	public static abstract class AStack implements Comparable<AStack> {

		protected int stacksize;

		public boolean isApplicable(ItemStack stack) {
			return isApplicable(new NbtComparableStack(stack));
		}

		public AStack singulize(){
			stacksize = 1;
			return this;
		}
		
		public int count(){
			return stacksize;
		}
		
		public void setCount(int c){
			stacksize = c;
		}
		
		/*
		 * Is it unprofessional to pool around in child classes from an abstract superclass? Do I look like I give a shit?
		 */
		public boolean isApplicable(ComparableStack comp) {
			
			if(this instanceof ComparableStack) {
				return ((ComparableStack)this).equals(comp);
			}

			if(this instanceof OreDictStack) {

				List<ItemStack> ores = new ArrayList<>();

				for (Item item : ForgeRegistries.ITEMS.getValues()) {
					if (ForgeRegistries.ITEMS.getHolder(item).orElseThrow().is(((OreDictStack)this).key)) {
						ores.add(new ItemStack(item));
					}
				}

				for(ItemStack stack : ores) {
					if(stack.getItem() == comp.item)
						return true;
				}
			}

			return false;
		}
		
		/**
		 * Whether the supplied itemstack is applicable for a recipe (e.g. anvils). Slightly different from {@code isApplicable}.
		 * @param stack the ItemStack to check
		 * @param ignoreSize whether size should be ignored entirely or if the ItemStack needs to be >at least< the same size as this' size
		 * @return
		 */
		public abstract boolean matchesRecipe(ItemStack stack, boolean ignoreSize);

		public abstract AStack copy();
		public abstract ItemStack getStack();
		public abstract List<ItemStack> getStackList();
		
		@Override
		public String toString() {
			return "AStack: size, " + stacksize;
		}
	}

	public static class ComparableStack extends AStack {

		public Item item;
		
		public ComparableStack(ItemStack stack) {
			this.item = stack.getItem();
			this.stacksize = stack.getCount();
		}
		
		public ComparableStack makeSingular() {
			stacksize = 1;
			return this;
		}
		
		@Override
		public AStack singulize() {
			stacksize = 1;
			return this;
		}
		
		public ComparableStack(Item item) {
			this.item = item;
			this.stacksize = 1;
		}
		
		public ComparableStack(Block item) {
			this.item = item.asItem();
			this.stacksize = 1;
		}
		
		public ComparableStack(Item item, int stacksize) {
			this(item);
			this.stacksize = stacksize;
		}
		
		public ComparableStack(Item item, int stacksize, CompoundTag meta) {
			this(item, stacksize);
		}
		
		public ComparableStack(Block item, int stacksize) {
			this.item = item.asItem();
			this.stacksize = stacksize;
		}
		
		public ItemStack toStack() {
			
			return new ItemStack(item, stacksize);
		}
		
		@Override
		public ItemStack getStack() {
			return toStack();
		}
		
		@Override
		public List<ItemStack> getStackList(){
			return Arrays.asList(getStack());
		}
		
		public String[] getDictKeys() {

			Set<ResourceLocation> tags = ForgeRegistries.ITEMS.getHolder(toStack().getItem())
					.stream()
					.flatMap(holder -> holder.tags().map(TagKey::location))
					.collect(Collectors.toSet());


			if(tags.isEmpty())
				return new String[0];
			
			List<String> entries = new ArrayList<>();

			for(ResourceLocation loc : tags) {

				entries.add(loc.toString());
			}

            return entries.toArray(new String[0]);
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			if(item == null) {
				HBM.LOGGER.error("ComparableStack has a null item! This is a serious issue!");
				Thread.dumpStack();
				item = Items.STICK;
			}

			ResourceLocation name = ForgeRegistries.ITEMS.getKey(item);
			
			if(name == null) {
				HBM.LOGGER.error("ComparableStack holds an item that does not seem to be registered. How does that even happen?");
				Thread.dumpStack();
				item = Items.STICK; //we know sticks have a name, so sure, why not
			}

			if(name != null)
				result = prime * result + ForgeRegistries.ITEMS.getKey(item).hashCode(); //using the int ID will cause fucky-wuckys if IDs are scrambled
			result = prime * result;
			result = prime * result + stacksize;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ComparableStack))
				return false;
			ComparableStack other = (ComparableStack) obj;
			if (item == null) {
				if (other.item != null)
					return false;
			} else if (!item.equals(other.item))
				return false;
			if (stacksize != other.stacksize)
				return false;
			return true;
		}

		@Override
		public int compareTo(AStack stack) {

			if(stack instanceof ComparableStack) {

				ComparableStack comp = (ComparableStack) stack;

				int thisID = Item.getId(item);
				int thatID = Item.getId(comp.item);

				if(thisID > thatID)
					return 1;
				if(thatID > thisID)
					return -1;

				return 0;
			}

			//if compared with an ODStack, the CStack will take priority
			if(stack instanceof OreDictStack)
				return 1;

			return 0;
		}
		
		@Override
		public boolean matchesRecipe(ItemStack stack, boolean ignoreSize) {
			
			if(stack == null)
				return false;
			
			if(stack.getItem() != this.item)
				return false;
			
			if(!ignoreSize && stack.getCount() < this.stacksize)
				return false;
			
			return true;
		}
		
		@Override
		public AStack copy() {
			return new ComparableStack(item, stacksize);
		}
		
		@Override
		public String toString() {
			return "ComparableStack: { "+stacksize+" x "+item.getDescriptionId()+"}";
		}
	}
	
	public static class NbtComparableStack extends ComparableStack {
		ItemStack stack;
		public NbtComparableStack(ItemStack stack) {
			super(stack);
			this.stack = stack.copy();
		}
		
		@Override
		public ComparableStack makeSingular() {
			ItemStack st = stack.copy();
			st.setCount(1);
			return new NbtComparableStack(st);
		}
		
		@Override
		public AStack singulize() {
			stack.setCount(1);
			this.stacksize = 1;
			return this;
		}
		
		@Override
		public ItemStack toStack() {
			return stack.copy();
		}
		
		@Override
		public ItemStack getStack() {
			return toStack();
		}
		
		@Override
		public int hashCode() {
			if(!stack.hasTag())
				return super.hashCode();
			else
				return super.hashCode() * 31 + stack.getTag().hashCode();
		}
		
		@Override
		public AStack copy() {
			return new NbtComparableStack(stack);
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!stack.hasTag() || !(obj instanceof NbtComparableStack)) {
				return super.equals(obj);
			} else {
				return super.equals(obj) && Library.tagContainsOther(stack.getTag(), ((NbtComparableStack)obj).stack.getTag());
			}
		}
		
		@Override
		public boolean matchesRecipe(ItemStack stack, boolean ignoreSize){
			return super.matchesRecipe(stack, ignoreSize) && Library.tagContainsOther(this.stack.getTag(), stack.getTag());
		}
		
		@Override
		public String toString() {
			return "NbtComparableStack: " + stack.toString();
		}
		
	}
	
	public static class OreDictStack extends AStack {

		public TagKey<Item> key;

		public OreDictStack(TagKey<Item> key) {
			this.key = key;
			this.stacksize = 1;
		}

		public OreDictStack(TagKey<Item> key, int stacksize) {
			this(key);
			this.stacksize = stacksize;
		}

		public List<ItemStack> toStacks() {

			List<ItemStack> stacks = new ArrayList<>();

			for (Item item : ForgeRegistries.ITEMS) {

				if (ForgeRegistries.ITEMS.getHolder(item).map(h -> h.is(key)).orElse(false)) {

					stacks.add(new ItemStack(item));
				}
			}

			return stacks;
		}
		
		@Override
		public ItemStack getStack() {

			ItemStack stack = toStacks().get(0);

			return new ItemStack(stack.getItem(), stacksize, stack.getTag());
		}
		
		@Override
		public List<ItemStack> getStackList(){

			List<ItemStack> list = Library.copyItemStackList(toStacks());

			for(ItemStack stack : list){

				stack.setCount(this.stacksize);
			}
			return list;
		}

		@Override
		public int hashCode() {

			return Objects.hash(key.location(), stacksize);
		}
		
		@Override
		public AStack singulize() {

			stacksize = 1;

			return this;
		}

		@Override
		public int compareTo(AStack stack) {

			if(stack instanceof OreDictStack comp) {

                return key.location().compareTo(comp.key.location());
			}

			//if compared with a CStack, the ODStack will yield
			if(stack instanceof ComparableStack)
				return -1;

			return 0;
		}

		@Override
		public boolean matchesRecipe(ItemStack stack, boolean ignoreSize) {
			if (stack == null || stack.isEmpty()) return false;
			if (!ignoreSize && stack.getCount() < this.stacksize) return false;

			return stack.is(this.key);
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof OreDictStack other))
				return false;
            if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			if (stacksize != other.stacksize)
				return false;
			return true;
		}

		@Override
		public AStack copy() {
			return new OreDictStack(key, stacksize);
		}
		
		@Override
		public String toString() {
			return "OreDictStack: name, " + key.location() + ", stacksize, " + stacksize;
		}
	}
}
