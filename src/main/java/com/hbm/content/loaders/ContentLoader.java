package com.hbm.content.loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hbm.lib.RefStrings;
import com.hbm.main.MainRegistry;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public abstract class ContentLoader {

	protected static final Gson gson = new GsonBuilder().create();
	private static final List<ContentLoader> contentHandlers = new ArrayList<>();
	protected static File contentRoot;

	public boolean modified = false;

	public static void registerDefaultHandlers() {
		registerHandler(new ArmorLoader());
	}

	public static void registerHandler(ContentLoader loader) {
		contentHandlers.add(loader);
	}

	public static void initialize() {
		contentRoot = new File(MainRegistry.configDir.getAbsolutePath() + File.separatorChar + "content");

		if(!contentRoot.exists()) {
			if(!contentRoot.mkdir()) {
				throw new IllegalStateException("Unable to make content directory " + contentRoot.getAbsolutePath());
			}
		}

		MainRegistry.logger.info("Loading custom content!");

		for(ContentLoader loader : contentHandlers) {

			File contentFolder = new File(contentRoot.getAbsolutePath() + File.separatorChar + loader.getFolderName());

            if (!contentFolder.exists() && !contentFolder.mkdirs()) {
                MainRegistry.logger.info("Failed to create content directory for " + loader.getName().toLowerCase() + " loader");
                continue;
            }

			File[] contentFiles = contentFolder.listFiles((dir, name) -> name.endsWith(".json"));
			if(contentFiles == null || contentFiles.length == 0) {
				MainRegistry.logger.info("No content was loaded for " + loader.getName().toLowerCase() + " loader");
				continue;
			}

			for(File contentFile : contentFiles) {
				if(!loader.loadContentFile(contentFile)) {
					MainRegistry.logger.warn("Couldn't load "+contentFile.getName());
				}
			}

			// can be loaded after the content since its not going to be loaded due to _template being true
			File template = new File(contentFolder.getAbsolutePath() + File.separatorChar + loader.getTemplateName() + ".txt");
			if(!template.exists() && !template.mkdirs()) {
				MainRegistry.logger.info("Failed to make template file for " + loader.getName());
			} else if(template.exists()) {
				if(!copyFileFromAssets(
					loader,
					"templates" + File.separatorChar + loader.getFolderName() + File.separatorChar + loader.getTemplateName() + ".json",
					contentRoot.getAbsolutePath()+File.separatorChar+loader.getFolderName())
				) {
					MainRegistry.logger.warn("Failed to load template for "+loader.getName().toLowerCase()+" loader");
				}
			}
		}

		MainRegistry.logger.info("Finished loading content!");
	}

	public abstract String getFolderName();

	public abstract String getTemplateName();

	public abstract String getName();

	public abstract boolean loadContent(String contentId, JsonObject content);

	public static boolean copyFileFromAssets(ContentLoader loader, String internalPath, String targetPath) {
		InputStream input = loader.getClass().getResourceAsStream(internalPath + File.separatorChar + "assets" + File.separatorChar + RefStrings.MODID + File.separatorChar + internalPath);

		if (input == null) {
			MainRegistry.logger.warn("File not found inside JAR: " + internalPath);
			return false;
		}

		File targetFile = new File(targetPath);

		if(!targetFile.exists()) {
			MainRegistry.logger.warn("Target file not found: " + targetPath);
			return false;
		}

		try (OutputStream output = Files.newOutputStream(targetFile.toPath())) {

			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}

			MainRegistry.logger.info("File successfully copied to: " + targetPath);

			input.close();

		} catch (Exception e) {
			MainRegistry.logger.error(e);
			return false;
		}
		return true;
	}

	public boolean loadContentFile(File file) {

		try {
			JsonObject json = gson.fromJson(new FileReader(file), JsonObject.class);
			if(!this.loadContent(file.getName(), json)) return false;
		} catch(FileNotFoundException ex) {
			return false;
		}
		return true;

	}
}
