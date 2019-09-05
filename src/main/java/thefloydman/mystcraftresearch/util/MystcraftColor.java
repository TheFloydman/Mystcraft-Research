package thefloydman.mystcraftresearch.util;

import net.minecraft.util.ResourceLocation;

public enum MystcraftColor {
	BLACK(0.0F, 0.0F, 0.0F, "black"),
	BLUE(0.0F, 0.0F, 1.0F, "blue"),
	CYAN(0.0F, 1.0F, 1.0F, "cyan"),
	DARK_GREEN(0.0F, 0.5F, 0.0F, "darkgreen"),
	GREEN(0.0F, 1.0F, 0.0F, "green"),
	GREY(0.5F, 0.5F, 0.5F, "grey"),
	MAGENTA(1.0F, 0.0F, 1.0F, "magenta"),
	MAROON(0.5F, 0.0F, 0.0F, "maroon"),
	NAVY(0.0F, 0.0F, 0.5F, "navy"),
	OLIVE(0.5F, 0.5F, 0.0F, "olive"),
	PURPLE(0.5F, 0.0F, 0.5F, "purple"),
	RED(1.0F, 0.0F, 0.0F, "red"),
	SILVER(0.75F, 0.75F, 0.75F, "silver"),
	TEAL(0.0F, 0.5F, 0.5F, "teal"),
	WHITE(1.0F, 1.0F, 1.0F, "white"),
	YELLOW(1.0F, 1.0F, 0.0F, "yellow");
	
	public float red;
	public float green;
	public float blue;
	public ResourceLocation resourceLocation;
	
	MystcraftColor(float r, float g, float b, String name) {
		this.red = r;
		this.green = g;
		this.blue = b;
		this.resourceLocation = new ResourceLocation("mystcraft", "modcolor" + name);
	}
}
