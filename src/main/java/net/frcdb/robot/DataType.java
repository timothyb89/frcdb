package net.frcdb.robot;

/**
 *
 * @author tim
 */
public enum DataType {
	
	STRING() {

		@Override
		public String fromString(String input) {
			return input;
		}

		@Override
		public String asString(Object input) {
			return input.toString();
		}
		
		@Override
		public boolean validate(String input) {
			return true;
		}
		
	},
	
	INTEGER() {
		
		@Override
		public String asString(Object input) {
			return String.valueOf(input);
		}

		@Override
		public Integer fromString(String input) {
			return Integer.parseInt(input);
		}

		@Override
		public boolean validate(String input) {
			try {
				Integer.parseInt(input);
				return true;
			} catch (NumberFormatException ex) {
				return false;
			}
		}
		
	};
	
	private DataType() {
		
	}
	
	public abstract String asString(Object input);
	public abstract <T extends Object> T fromString(String input);
	public abstract boolean validate(String input);
	
}
