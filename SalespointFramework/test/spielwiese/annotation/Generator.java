package spielwiese.annotation;

import java.util.Vector;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.internal.databaseaccess.Accessor;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.sequencing.Sequence;
import org.eclipse.persistence.sessions.Session;

@SuppressWarnings("serial")
public class Generator extends Sequence implements SessionCustomizer {

	public Generator() {
		super();
	}
	
	public Generator(String name) {
		super(name);
	}
	
	@Override
	public void customize(Session session) throws Exception {
		System.out.println("Setting Salespoint-Generator");
		Generator g = new Generator("SalespointId");
		session.getLogin().addSequence(g);
	}

	@Override
	public Object getGeneratedValue(Accessor arg0, AbstractSession arg1,
			String arg2) {
		System.out.println();
		System.out.println("arg2: " + arg2);
		System.out.println();
		return new Identifier();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Vector getGeneratedVector(Accessor arg0, AbstractSession arg1,
			String arg2, int arg3) {
		Identifier id;
		Vector v = new Vector();
		for(int i = 0; i < arg3; i++) {
			id = new Identifier();
			System.out.println(id);
			v.add(id);
		}
		System.out.println();
		System.out.println("Getting vector " + arg2 + " " + arg3);
		System.out.println();
		return v;
	}

	@Override
	public void onConnect() {
	}

	@Override
	public void onDisconnect() {
	}

	@Override
	public boolean shouldAcquireValueAfterInsert() {
		System.out.println("shouldAcquireValueAfterIsert");
		return false;
	}

	@Override
	public boolean shouldUseTransaction() {
		System.out.println("ShouldUseTransaction");
		return true;
	}

}
