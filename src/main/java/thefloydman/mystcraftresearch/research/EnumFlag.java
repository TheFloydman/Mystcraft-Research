package thefloydman.mystcraftresearch.research;

public enum EnumFlag {

	TOOK_NOTES("took_notes"),
	LEARNED_BLUE("learned_blue"),
	LEARNED_GREEN("learned_green"),
	LEARNED_BLACK("learned_black"),
	LEARNED_GREY("learned_grey"),
	LEARNED_RED("learned_red");

	public String name;

	EnumFlag(String str) {
		this.name = str;
	}

}
