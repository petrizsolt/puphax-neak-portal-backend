package hu.neak.puphax.syncronizer.config;

import org.joda.time.LocalDate;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SimplePasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String rightPass = getActualPassword(encodedPassword);
		return encode(rawPassword).equals(rightPass);
	}
	
	
	private String getActualPassword(String encodedPassword) {
		LocalDate nowDate = LocalDate.now();
		String monthCorr = getTwoSizeStr(String.valueOf(nowDate.getMonthOfYear()));
		String dayCorr = getTwoSizeStr(String.valueOf(nowDate.getDayOfMonth()));
		String dateStr = String.valueOf(nowDate.getYear()).concat(monthCorr)
				.concat(dayCorr);
		return encodedPassword.replace("{date}", dateStr);
	}
	
	String getTwoSizeStr(String str) {
		String zero = "0";
		if(str.length() < 2) {
			return zero.concat(str);
		}
		return str;
	}
}
