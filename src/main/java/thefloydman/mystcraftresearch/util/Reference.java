package thefloydman.mystcraftresearch.util;

import net.minecraft.util.ResourceLocation;

public class Reference {

	public static final String MOD_ID = "mystcraftresearch";
	public static final String NAME = "Mystcraft Research";
	public static final String VERSION = "1.0.0";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";
	public static final String CLIENT_PROXY_CLASS = "thefloydman.mystcraftresearch.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "thefloydman.mystcraftresearch.proxy.ServerProxy";
	public static final String DEPENDENCIES = "required-after:mystcraft";

	public static ResourceLocation makeRL(String input) {
		return new ResourceLocation(MOD_ID, input);
	}

	public class BlockNames {

		public static final String SYMBOL_RESEARCH_DESK = MOD_ID + ":symbol_research_desk";

	}

	public class ItemNames {

		public static final String JOURNAL_OF_THE_ART = MOD_ID + ":journal_of_the_art";

	}
	
	public enum Message {
		LEARN_SYMBOL("research_learn"),
		KNOWN_SYMBOL("research_known"),
		FORGET_SYMBOL("research_forget"),
		FORBIDDEN_SYMBOL("research_forbidden");

		public String key;

		Message(String str) {
			this.key = "message." + MOD_ID + "." + str + ".desc";
		}
	}

}
