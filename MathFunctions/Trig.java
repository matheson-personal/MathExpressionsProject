
public abstract class Trig extends Function {

	double freq = 1;
	double c = 1;
	double phase = 0;


	public Trig(double freq, double phase, double constant) {
		this.freq = freq;
		this.phase = phase;
		this.c = constant;
	}


	public Trig(double freq, double phase) {
		this.freq = freq;
		this.phase = phase;
	}


	public Trig(double freq) {
		this.freq = freq;
	}


	public Trig() {
	}


	public double getFreq() {
		return this.freq;
	}


	public double getC() {
		return this.c;
	}


	public double getPhase() {
		return this.phase;
	}


	@Override
	public String getFuncSignature() {
		return this.getFuncType();
	}


	@Override
	public String saveString(int depth) {
		StringBuilder wawa = new StringBuilder();

		wawa.append(this.tabs(depth));
		wawa.append(this.getFuncType());

		wawa.append('{');
		wawa.append(this.getFreq());

		wawa.append(", ");
		wawa.append(this.getC());

		wawa.append(", ");
		wawa.append(this.getPhase());

		wawa.append("}");

		return wawa.toString();
	}

}
