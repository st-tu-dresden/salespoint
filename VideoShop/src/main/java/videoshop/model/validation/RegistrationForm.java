package videoshop.model.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegistrationForm {

	@NotNull
	private String name;
	@NotNull
	private String password;
	@NotNull
	
	private String address;
	@NotNull
	@Size(min = 5)
	private String city;
	
}
