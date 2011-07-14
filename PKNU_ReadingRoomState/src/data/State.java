package data;

public class State {
	private String name;
	private int total;
	private int use;
	private int remain;
	private float rate;
	
	public State(String name, int total, int use) {
		this.name = name;		
		this.total = total;
		this.use = use;
		
		remain = total - use;
		rate = (float)use / (float)total * 100f;
	}

	public String getStateByString() {
		String stateByString = "¿­¶÷½Ç¸í : " + name
			+ " / ÀüÃ¼ ÁÂ¼®¼ö : " + total
			+ " / »ç¿ë ÁÂ¼®¼ö : " + use
			+ " / ÀÜ¿© ÁÂ¼®¼ö : " + remain
			+ " / ÀÌ¿ë·ü : " + rate + " %";
		
		return stateByString;
	}
	
	public String getName() {
		return name;
	}

	public int getTotal() {
		return total;
	}

	public int getUse() {
		return use;
	}

	public int getRemain() {
		return remain;
	}

	public float getRate() {
		return rate;
	}
}