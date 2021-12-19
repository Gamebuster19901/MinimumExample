package com.wildermods.wilderforge.api.modLoadingV1;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static com.wildermods.wilderforge.api.modJsonV1.ModJsonConstants.*;

public class CoremodInfo implements ModContainer {
	
	public final ModContainer coremod;
	
	public CoremodInfo(ModContainer coremod) {

		ModMetadata metadata = coremod.getMetadata();
		
		
		CustomValue HOMEPAGEV = metadata.getCustomValue(HOMEPAGE);
		
		this.coremod = coremod;
	}
	
	protected CoremodInfo() {this.coremod = null;};
	
	private String grammaticallyCorrectAuthorList(Person[] authors) {
		if(authors.length == 0) {
			return "?";
		}
		if(authors.length == 1) {
			return authors[0].getName();
		}
		if(authors.length == 2) {
			return authors[0].getName() + " and " + authors[1].getName();
		}
		if(authors.length > 2) {
			StringBuilder ret = new StringBuilder();
			int i = 0;
			for(; i < authors.length - 1; i++) {
				ret.append(authors[i].getName());
				ret.append(", ");
			}
			ret.append(" and ");
			ret.append(authors.length - 1);
			return ret.toString();
		}
		throw new IllegalArgumentException(Arrays.toString(authors));
	}


	@Override
	public ModMetadata getMetadata() {
		return coremod.getMetadata();
	}


	@Override
	public Path getRootPath() {
		return coremod.getRootPath();
	}
	
	@Override
	public String toString() {
		return coremod.getMetadata().getId();
	}
	
	public ResourceBundle getResourceBundle(String path, Locale locale) {
		try {
			return ResourceBundle.getBundle(path.replace(".properties", ""), locale);
		}
		catch (MissingResourceException e) {
			return null;
		}
	}
	
}
