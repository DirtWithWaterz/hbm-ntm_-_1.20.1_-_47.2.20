package com.hbm.nucleartech.util;


import net.minecraft.client.resources.language.I18n;

public class I18nUtil {

	public static String resolveKey(String s, Object... args) {
		return I18n.get(s, args);
	}

	public static String[] resolveKeyArray(String s, Object... args) {
		return resolveKey(s, args).split("\\$");
	}
}
